package im.xgs.net.service;

import im.xgs.net.model.SysFriendMessage;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import im.xgs.net.util.Utils;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ImFriendService extends BaseService {
	/**
	 * 查找用户好友列表
	 * @param userId
	 * @return List<Record> Record字段：friendId,nickname,sign
	 */
	public List<Record> findFriends(String userId){
		String sql = " select suf.friend_id friendId,suf.nickname,su.sign from sys_user_friend suf " 
					+" left join sys_user su on suf.friend_id = su.user_id "
					+" where suf.user_id = ? ";
		return Db.find(sql,userId);
	}
	
	/**
	 * 好友详细信息
	 * @param userId
	 * @return Record Record字段：userId,nickname,header,sign
	 */
	public Record getDetailInfo(String userId){
		String sql = " select user_id userId, nickname,header,sign from sys_user where sys_user.user_id = ? limit 1 ";
		return Db.findFirst(sql, userId);
	}
	/**
	 * 保存用户和好友聊天消息
	 * @param fromUserId
	 * @param toUserId
	 * @param msg
	 */
	public void saveMsg(String fromUserId,String toUserId,String msg){
		String hashId = Utils.hashId(fromUserId, toUserId);
		SysFriendMessage sFMsg = new SysFriendMessage();
		sFMsg.setId(UUIDHexGenerator.getId());
		sFMsg.setFromUser(fromUserId);
		sFMsg.setToUser(toUserId);
		sFMsg.setHashId(hashId);
		sFMsg.setMessage(msg);
		sFMsg.setSendTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		sFMsg.save();
		
		
	}
	/**
	 * 查询好友聊天记录
	 * @param pageNumber
	 * @param pageSize
	 * @param userId
	 * @param friendId
	 * @return Page<Record> Record字段：toUser,fromUser,message,sendTime
	 */
	public Page<Record> findMsg(int pageNumber,int pageSize,String userId,String friendId){
		String hashId = Utils.hashId(userId, friendId);
		String sql = " select to_user toUser,from_user fromUser,message,send_time sendTime ";
		String sqlExceptSelect = " from sys_friend_message where hash_id = ? order by send_time desc";
		return Db.paginate(pageNumber, pageSize, sql, sqlExceptSelect,hashId);
		
	}
	//TODO 添加好友
	//TODO 删除好友
	//lazy loading
	
}
