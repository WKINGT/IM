package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.Config;
import im.xgs.net.channel.ChannelInstance;
import im.xgs.net.channel.GroupInstance;
import im.xgs.net.channel.Groups;
import im.xgs.net.channel.MapChannel;
import im.xgs.net.channel.Session;
import im.xgs.net.exception.ImException;
import im.xgs.net.model.SysGroup;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.CreateGroupReq;
import im.xgs.net.protocol.entity.resp.group.CreateGroupResp;
import im.xgs.net.protocol.entity.resp.group.InGroupNotify;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
/**
 * 创建群组 
 * 新创建群组时，群成员的信息不向各群成员发送，只发送创建群的信息
 * 如果群成员在线，该群成员则拉取群成员数据
 * 否则，向该群成员发送离线消息，群成员在登录后，拉取所有群成员信息，在登录时只拉取一次，以保证性能
 * 
 * 
 * @author TianW
 *
 */
public class RecHandler10 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		ByteBuf rMsg = null;
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.CREATE;
		CreateGroupResp createGroup = new CreateGroupResp();
		CreateGroupReq entity = null;
		try {
			entity = JSON.parseObject(msg, CreateGroupReq.class);	
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try{
			String userId = cm.getUserId(channel);
			String groupName = entity.getGroupName();
			String description = entity.getDescription();
			String nickName = entity.getNickname();
			String header = entity.getHeader();
			Map<String,String> members = entity.getMembers();
			SysGroup group = groupService.create(groupName, description, header, userId, nickName, members);
			String groupId = group.getId();
			/**创建群后，加入到群数据中 begin**************/
			Groups g = new Groups();
			g.put(userId, "");
			GroupInstance.init().put(groupId, g);
			Session session = MapChannel.instance().get(userId);
			session.getGroups().put(groupId, "");
			/**创建群后，加入到群数据中 end**************/
			
			// 将这些人加入到群数据里，然后通知这些人被拉进群，并更改这些在线的群成员session里的group数据
			Groups g1 = GroupInstance.init().get(groupId);
			for(String id : members.keySet()){
				g1.put(id, "");
			}
			
			createGroup.setCode(0);
			createGroup.setGroupId(groupId);
			createGroup.setGroupName(groupName);
			
			rMsg = Message.Packing(version, uuid, cmd, opera, createGroup);
			//给自己发送创建群消息
			SendMsg.toSelf(userId, rMsg.copy(), channel);
			//异步给被拉进群的成员发送消息
			new Thread(new Sync(userId, groupId,groupName, members)).start();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			int code = prop.getInt("error.creatGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		
		return rMsg;
	}
	private class Sync implements Runnable {
		private String userId;
		private String groupId;
		private String groupName;
//		private String text;
		Map<String,String> members;
		public Sync(String userId,String groupId,String groupName,Map<String,String> members){
			this.userId = userId;
			this.groupId = groupId;
			this.groupName = groupName;
			this.members = members;
//			this.text = "您被拉入" + groupName + "群组";
		}
		
		public void run() {
			String version = Protocol.VERSION;
			String uuid = UUIDHexGenerator.getId();
			short cmd = Protocol.GROUPC;
			short opera = Protocol.Group.INVITE_TO_GROUP;
			//给被拉进群的人发送消息的实体
			InGroupNotify notify = new InGroupNotify();
			notify.setCode(0);
			notify.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			notify.setFromUserId(userId);
			notify.setToType(MsgToType.TOGROUP.ordinal());
			notify.setToId(groupId);
			notify.setGroupName(groupName);
			notify.setCmd(cmd);
			notify.setOpera(opera);
			String msgBody = JSON.toJSONString(notify);
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, notify);
			for(String id : members.keySet()){
				//给被拉进群的人发送被拉进群消息(除了创建群的用户)
				if(!id.equals(userId)){
					SendMsg.toOrgUser(id, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, true);
				}
				
//				SendMsg.ToGroup(id, groupId, 1, msgBody, rMsg, true,);给群成员发送进群消息rMsg里的text内容需要修改
				
				//将群组加入到被拉进群的成员的session里
				Session session = ChannelInstance.getChannelManager().get(id);
				if(session!=null){
					session.getGroups().put(groupId, "");
				}
			}
			
		}
	}
}
