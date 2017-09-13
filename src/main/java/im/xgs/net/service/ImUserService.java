package im.xgs.net.service;


import im.xgs.net.Constants;
import im.xgs.net.exception.ImException;
import im.xgs.net.model.RecentContacter;
import im.xgs.net.model.SysMessage;
import im.xgs.net.model.SysOfflineMessage;
import im.xgs.net.model.SysOrgUser;
import im.xgs.net.model.SysOrgUserMessage;
import im.xgs.net.model.SysUser;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.PinyinUtil;
import im.xgs.net.util.UUIDHexGenerator;
import im.xgs.net.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;



public class ImUserService extends BaseService{
	
	/**
	 * 登入验证
	 * @param loginAccount
	 * @param loginPwd
	 * @return
	 */
	public SysOrgUser login(String loginAccount,String loginPwd){
		String sql = " select * from sys_org_user sou where sou.login_account = ? ";
		SysOrgUser orgUser = SysOrgUser.dao.findFirst(sql,loginAccount);
		if ( orgUser == null) return null;
//		if(Utils.md5(loginPwd + user.getLoginSalt()).equals(user.getLoginPwd())){
		if(Utils.md5(loginPwd).equals(orgUser.getLoginPwd())){
			return orgUser;
		}
		return null;
	}
	/**
	 * 查找好友或群组
	 * @param name
	 * @return List<Record> Record字段：name,header,id,flag
	 */
	public List<Record> findFriendOrGroup(String name){
		String fullPinYin = PinyinUtil.cn2Spell(name);
		String briefPinYin = PinyinUtil.cn2FirstSpell(name);
		String sql = " ( select sg.group_name name,sg.header,sg.id,'1' flag "
					+ " from sys_group sg "
					+ " where "
					+ " sg.group_name like ? "
					+ " or sg.full_pinyin like ? "
					+ " or sg.brief_pinyin like ? "
					+ " limit 5 ) "
					+ " union "
					+ " ( select su.nickname name,su.header,su.user_id id,'0' flag "
					+ " from sys_user su "
					+ " left join sys_user_friend suf on su.user_id = suf.friend_id "
					+ " where "
					+ " su.nickname like ? "
					+ " or su.full_pinyin like ? "
					+ " or su.brief_pinyin like ? "
					+ " limit 5 ) ";
		return Db.find(sql, name,fullPinYin,briefPinYin,name,fullPinYin,briefPinYin);
	}
	/**
	 * 查找组织成员或群组
	 * @param name
	 * @return
	 */
	public List<Record> findMemberOrGroup(String name){
		String fullPinYin = PinyinUtil.cn2Spell(name);
		String briefPinYin = PinyinUtil.cn2FirstSpell(name);
		String sql = " ( select sg.group_name name,sg.header,sg.id,'1' flag "
					+ " from sys_group sg "
					+ " where "
					+ " sg.group_name like ? "
					+ " or sg.full_pinyin like ? "
					+ " or sg.brief_pinyin like ? "
					+ " limit 5 ) "
					+ " union "
					+ " ( select sou.name,sou.id header,sou.id id,'0' flag "
					+ " from sys_org_user sou "
					+ " where "
					+ " sou.name like ? "
					+ " limit 5 ) ";
		return Db.find(sql, name,fullPinYin,briefPinYin,name);
	}
			
	/**
	 * 更改用户个性签名
	 * @param userId
	 * @param sign
	 */
	public void updateSign(String userId,String sign){

		String update = " update sys_user set sign = ? where user_id = ? ";
		Db.update(update, sign,userId);
	}
	/**
	 *  更改登录密码
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 */
	public void changePwd(String userId,String oldPwd, String newPwd){
		SysUser user = SysUser.dao.findById(userId);
		if(user == null){
			throw new ImException(Constants.errorCode.getInt("error.wrongPassword"));
		}
		if(!user.getLoginPwd().equals(Utils.md5(oldPwd+user.getLoginSalt()))){
			throw new ImException(Constants.errorCode.getInt("error.wrongPassword"));
		}
		String updataPwd = " updata sys_user set login_pwd = ? where user_id = ? ";
		Db.update(updataPwd, Utils.md5(newPwd+user.getLoginSalt()),userId);

	}
	
	@Before(Tx.class)
	public void saveOfflines(List<Map<String,Object>> list){
		for(Map<String,Object> map :list){
			this.saveOffline((String)map.get("toUserId"), (String)map.get("fromId"), (Integer)map.get("toType"), (String)map.get("msg"));
		}
	}
	
	public void saveOffline(String toUserId,String fromId,int toType, String msg){
		String sendTime = DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS);
		String sql = " select id,to_user,to_type,from_id,number from sys_offline_message where to_type=?  and from_id = ? and to_user = ? ";
		SysOfflineMessage som = SysOfflineMessage.dao.findFirst(sql,toType,fromId,toUserId);
		if(som == null){
			// save the message
			som = new SysOfflineMessage();
			som.setFromId(fromId);
			som.setId(UUIDHexGenerator.getId());
			som.setMessage(msg);
			som.setNumber(1);
			som.setSendTime(sendTime);
			som.setToType(toType);
			som.setToUser(toUserId);
			som.save();
		}else{
			// update the message
			som.setMessage(msg);
			som.setNumber(som.getNumber()+1);
			som.setSendTime(sendTime);
			som.update();
		}
	}
	
//	/**
//	 * 批量保存离线消息
//	 * @param paras
//	 */
//	@Before(Tx.class)
//	public void saveOfflineMsgs(List<List<Object>> paras){
//		Long startime = System.currentTimeMillis();
//		Object[][] params = new Object[paras.size()][];
//		for(int i=0;i<paras.size();i++){
//			params[i] = paras.get(i).toArray();
//		}
//		
//		String sql = " insert into sys_offline_message (id,message,to_user,from_id,to_type,send_time) "
//				+ "values(?,?,?,?,?,?)";
//		//Db.batch(sql, params, 500);
//		Long endtime = System.currentTimeMillis();
//		logger.debug("batch insert sys_offline_message time :{},length:{}",endtime-startime,params.length);
//	}
//	/**
//	 * 保存离线消息
//	 * @param toUserId
//	 * @param fromId
//	 * @param toType
//	 * @param msg
//	 */
//	public void saveOfflineMsg(String toUserId,String fromId,int toType, String msg){
////		byte[] req = new byte[msg.readableBytes()];
////		msg.readBytes(req);
//		SysOfflineMessage oMsg = new SysOfflineMessage();
//		oMsg.setId(UUIDHexGenerator.getId());
//		oMsg.setMessage(msg);
//		oMsg.setToUser(toUserId);
//		oMsg.setFromId(fromId);
//		oMsg.setToType(toType);
//		oMsg.setSendTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
//		//oMsg.save();
//	}
	/**
	 * 获取离线信息
	 * @param userId
	 * @param fromId
	 * @param toType
	 * @return List<Record> Record字段:message
	 */
	public List<Record> findOfflineMsg(String userId, String fromId, int toType){//id,from_id fromId, sendTime ,send_time 
		String sql = " select message from sys_offline_message where to_type = ? and to_user = ? and from_id = ? order by send_time desc limit 10 ";
		return Db.find(sql, toType, userId, fromId);
	}
	/**
	 * 获取离线消息条数
	 * @param userId
	 * @return List<Record> Record字段:count,fromId,toType
	 */
	public List<Record> coutOfflineMsg(String userId){
		String sql = " select number count ,from_id fromId,to_type toType, message from sys_offline_message where to_user = ? group by from_id  ";
		return Db.find(sql, userId);
	}
	/**
	 * 删除已读的离线消息
	 * @param userId
	 */
	public void deleteOfflineMsg(String fromId){
		String deleSql = " delete from sys_offline_message where from_id= ? ";
		Db.update(deleSql, fromId);
	}

	/**
	 * 给群主发消息
	 * @param userId
	 * @param msgs
	 */
	@Before(Tx.class)
	public void saveSysMsg(String userId,List<String> msgs){
		String sendTime = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM_DD_HH_MM_SS );
		String searchYm = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM );
		for(String msg : msgs){
			this.saveSysMsg(userId, msg, sendTime, searchYm);
		}
	}
	/**
	 * 保存系统消息
	 * @param userIds 要发送系统消息的所有用户的id
	 * @param Msg
	 */
	@Before(Tx.class)
	public void saveSysMsg(List<String> userIds,String msg){
		String sendTime = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM_DD_HH_MM_SS );
		String searchYm = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM );
		for(String userId : userIds){
			this.saveSysMsg(userId, msg, sendTime, searchYm);
		}
	}

	public void saveSysMsg(String userId,String msg){
		String sendTime = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM_DD_HH_MM_SS );
		String searchYm = DateUtil.DateToString(new Date(),DateStyle.YYYY_MM );
		this.saveSysMsg(userId, msg, sendTime, searchYm);
	}
	public void saveSysMsg(String userId,String msg,String sendTime,String searchYm){
		SysMessage sMsg = new SysMessage();
		sMsg.setId(UUIDHexGenerator.getId());
		sMsg.setToUser(userId);
		sMsg.setMessage(msg);
		sMsg.setSearchYm(searchYm);
		sMsg.setSendTime(sendTime);
		sMsg.save();
	}
	/**
	 * 查看系统消息
	 * @param userId
	 * @param searchYm
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> findSysMsg(String userId,String searchYm, int pageNumber,int pageSize){
		String select = " select id,message,send_time ";
		String sqlExceptSelect = " from sys_message where search_ym = ? and to_user = ? order by send_time desc ";
		return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, searchYm,userId);
	}
	/**
	 * 添加最近联系人，我们规定，最近联系人最多存20，包括好友和群组
	 * @param userId
	 * @param friendId
	 * @param groupId
	 * @return
	 */
	public String saveRecentContacter(String userId,String friendId,String groupId){
		int recentMax = PropKit.use("cnf.txt").getInt("recent.contacter.max");
		long count = Db.queryLong("select count(*) from recent_contacter where user_id = ? ",userId);
		if(count >= recentMax){
			String sql = " select create_time from recent_contacter where user_id = ? order by create_time asc limit 1";
			RecentContacter rc = RecentContacter.dao.findFirst(sql, userId);
			if(rc != null){
				rc.delete();
			}
		}
		RecentContacter rc = new RecentContacter();
		rc.setCreateTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		rc.setFriendId(friendId);
		rc.setGroupId(groupId);
		rc.setUserId(userId);
		String id = UUIDHexGenerator.getId();
		rc.setId(id);
		rc.save();
		return id;
	}
	/**
	 * 根据id删除最近联系人
	 * @param id
	 */
	public void deleteRecentContacter(String id){
		String sql = " delete from recent_contacter where id = ? ";
		Db.update(sql, id);
	}
	/**
	 * 查找出所有的最近联系人
	 * @param userId
	 * @return List<Record> Record字段：id,contacterId,header,name,flag
	 */
	public List<Record> findRecentContacter(String userId){
		String sql = "  (select rc.id,sou.id contacterId,sou.id header,sou.name,'0' flag from recent_contacter rc  "
					+" inner join sys_org_user sou on rc.friend_id = sou.id "
					+" where rc.user_id = ? ) "
					+" union "
					+" (select rc.id,sg.id contacterId,sg.header,sg.group_name , '1' flag from recent_contacter rc  "
					+" inner join sys_group sg on rc.group_id = sg.id "
					+" where rc.user_id = ? )  " ;
		return Db.find(sql, userId,userId);
	}
	/**
	 * 保存用户和其他组织用户聊天消息
	 * @param fromUserId
	 * @param toUserId
	 * @param msg
	 */
	public void saveOrgUserMsg(String fromUserId,String toUserId,String msg){
		String hashId = Utils.hashId(fromUserId, toUserId);
		SysOrgUserMessage sFMsg = new SysOrgUserMessage();
		sFMsg.setId(UUIDHexGenerator.getId());
		sFMsg.setFromUser(fromUserId);
		sFMsg.setToUser(toUserId);
		sFMsg.setHashId(hashId);
		sFMsg.setMessage(msg);
		sFMsg.setSendTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		sFMsg.setSearchYm(DateUtil.DateToString(new Date(), DateUtil.DateToString(new Date(), DateStyle.YYYY_MM)));
		sFMsg.save();
		
		
	}
	/**
	 * 用户和其他组织用户聊天记录
	 * @param pageNumber
	 * @param pageSize
	 * @param userId
	 * @param friendId
	 * @return Page<Record> Record字段：toUser,fromUser,message,sendTime
	 */
	public Page<Record> findOrgUserMsg(int pageNumber,int pageSize,String userId,String friendId,String searchYm){
		String hashId = Utils.hashId(userId, friendId);
		String sql = " select to_user toUser,from_user fromUser,message,send_time sendTime ";
		String sqlExceptSelect = " from sys_org_user_message where hash_id = ? and search_ym = ?  order by send_time desc";
		return Db.paginate(pageNumber, pageSize, sql, sqlExceptSelect,hashId,searchYm);
		
	}
	
}
