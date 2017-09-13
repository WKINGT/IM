package im.xgs.net;

import im.xgs.net.channel.GroupInstance;
import im.xgs.net.channel.Groups;
import im.xgs.net.channel.OrgMembers;
import im.xgs.net.channel.OrgUser;
import im.xgs.net.chat.AbstractChatServer;
import im.xgs.net.chat.socket.ChatServer;
import im.xgs.net.model.SysGroup;
import im.xgs.net.model.SysGroupMembers;
import im.xgs.net.model.SysOrgUser;
import im.xgs.net.model._MappingKit;

import java.util.List;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;


public class WChatServer {

	
	protected static DruidPlugin dp;
    protected static ActiveRecordPlugin activeRecord;
	public static void main(String[] args) throws Exception {
		WChatServer chat = new WChatServer();
		chat.start();
	}
	
	public void start(){

    	Prop prop = PropKit.use("jdbc_app_config.txt");
		dp = new DruidPlugin(prop.get("jdbcUrl"),prop.get("user"), prop.get("password"));
		dp.start();
        activeRecord = new ActiveRecordPlugin(dp);
        activeRecord.setShowSql(prop.getBoolean("show_sql",false));
        activeRecord.setDialect(new MysqlDialect());
        _MappingKit.mapping(activeRecord);
        activeRecord.start();
        System.out.println("数据库已启动");
        System.out.println("group init started ...");
        List<SysGroup> list = SysGroup.dao.find(" select id from sys_group ");
        GroupInstance inst = GroupInstance.init();
        for(SysGroup group : list){
        	List<SysGroupMembers> members = SysGroupMembers.dao.find("select user_id from sys_group_members where group_id = ? ",group.getId());
        	Groups g = new Groups();
        	for(SysGroupMembers m : members){
        		g.put(m.getUserId(), "");
        	}
        	inst.put(group.getId(), g);
        }
        System.out.println("group init finished !");
        
        System.out.println("orguser init started !");
        List<SysOrgUser> userList = SysOrgUser.dao.find(" select * from sys_org_user ");
        OrgUser orgUser = OrgUser.instance();
        for(SysOrgUser u : userList){
        	OrgMembers member = new OrgMembers();
        	member.setId(u.getId());
        	member.setParentId(u.getParentId());
        	member.setName(u.getName());
        	member.setLeaf(u.getLeaf());
        	member.setOnline(false);
        	orgUser.put(member);
        }
        System.out.println("orguser init finished !");
        
//		new Thread(new chatStart(new WebsocketChatServer(8888))).start();
		new Thread(new chatStart(new ChatServer(8122))).start();
	}
	
	class chatStart implements Runnable{
		private AbstractChatServer server;
		public chatStart(AbstractChatServer server){
			this.server = server;
		}
		public void run() {
			try {
				this.server.run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
