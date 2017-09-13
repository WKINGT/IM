package im.xgs.net.msg;

import im.xgs.net.Config;
import im.xgs.net.channel.GroupInstance;
import im.xgs.net.channel.Groups;
import im.xgs.net.channel.MapChannel;
import im.xgs.net.channel.Session;
import im.xgs.net.service.ImUserService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.aop.Enhancer;

public class SendMsg {

	static ImUserService imservice = Enhancer.enhance(ImUserService.class);
	/**
	 * 广播
	 * @param toType
	 * @param msgBody
	 * @param rMsg
	 * @param isSendOffline
	 */
	public static void broadCast(int toType, String msgBody, ByteBuf rMsg,boolean isSendOffline){
		String sysId = Config.systemId;
		Map<String,Session> map =  MapChannel.instance().getAll();
		for(String userId : map.keySet()){
			SendMsg.toOrgUser(userId, sysId, toType, msgBody ,rMsg ,isSendOffline);
		}
	}
	
	/**
	 * 给自己发消息
	 * @param userId
	 * @param rMsg
	 * @param channel
	 */
	public static void toSelf(String userId, ByteBuf rMsg, Channel channel){

		Session session = MapChannel.instance().get(userId);
		if(session == null) return;
		if(channel != null){
			for(String ter : session.getTerminal().keySet()){
				if(channel != session.getTerminal().get(ter)){
					session.getTerminal().get(ter).writeAndFlush(rMsg.copy());
				}
			}
		} else {
			for(String ter : session.getTerminal().keySet()){
				session.getTerminal().get(ter).writeAndFlush(rMsg.copy());
			}	
		}
	}
	/**
	 * 给除群主外的群成员和自己的其他客户端发送加群信息(不离线)
	 * @param ch
	 * @param formUserId
	 * @param gid
	 * @param rMsg
	 * @param toType
	 * @param msgBody
	 * @param isSendOffline
	 */
	public static void addGroupMsg(String fromUserId,String gid,int toType, String msgBody, ByteBuf rMsg, Channel ch,boolean isSendOffline,String ownerId){
		toGroupExceptOwner(fromUserId, Config.systemId, toType, msgBody, rMsg, isSendOffline, ownerId);
		Groups group = GroupInstance.init().get(gid);
		//把自己加到群里
		group.put(fromUserId, "");
		Session session = MapChannel.instance().get(fromUserId);
		if(session!=null){
			//加载群到自己的session
			session.getGroups().put(gid, "");
		}
		SendMsg.toSelf(fromUserId,rMsg,ch);
	}
	/**
	 * 给群组除群主外其他成员和自己的其他客户端发送退群信息(不离线)
	 * @param fromUserId
	 * @param gid
	 * @param toType
	 * @param msgBody
	 * @param rMsg
	 * @param ch
	 * @param isSendOffline
	 * @param ownerId
	 */
	public static void quitGroupMsg(String fromUserId,String gid,int toType, String msgBody, ByteBuf rMsg, Channel ch,boolean isSendOffline, String ownerId){
		//从群数据里删掉退群者
		GroupInstance.init().get(gid).remove(fromUserId);
		Session session = MapChannel.instance().get(fromUserId);
		if(session!=null){
			//从退群者的session里删掉推掉的群组
			session.getGroups().remove(fromUserId);
		}
		toGroupExceptOwner(fromUserId, gid, toType, msgBody, rMsg, isSendOffline, ownerId);
		SendMsg.toSelf(fromUserId,rMsg,ch);
	}
	/**
	 * 解散群通知
	 * @param formUserId
	 * @param gid
	 * @param toType
	 * @param msgBody
	 * @param rMsg
	 * @param ch
	 * @param isSendOffline
	 */
	public static void disbandGroupMsg(String fromUserId,String gid,int toType, String msgBody, ByteBuf rMsg, Channel ch,boolean isSendOffline){
		List<String> userIds = new ArrayList<String>();
		/***解散群后，在群里删除用户数据,在用户的session里删除群组 ***/
		for(String uId : GroupInstance.init().get(gid).keySet()){
			userIds.add(uId);
			if(!uId.equals(fromUserId)){
				SendMsg.toOrgUser(uId, Config.systemId, toType, msgBody, rMsg, true);
				Session session = MapChannel.instance().get(uId);
				if(session!=null){
					//在其他群成员的session里删掉解散的群
					session.getGroups().remove(gid);
				}
			}
		}
		SendMsg.toSelf(fromUserId,rMsg,ch);
		imservice.saveSysMsg(userIds, msgBody);
		Session s = MapChannel.instance().get(fromUserId);
		if(s!=null){
			//在群主的session里删除解散的群
			s.getGroups().remove(gid);
		}
	}
	
	/**
	 * 给群组发消息
	 * @param fromUserId
	 * @param gid
	 * @param toType
	 * @param msgBody
	 * @param rMsg
	 * @param isSendOffline
	 */
	public static void toGroup(String fromUserId,String gid,int toType, String msgBody, ByteBuf rMsg,boolean isSendOffline){
		List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
		Groups group = GroupInstance.init().get(gid);
		if(group==null) return;
		Set<String> sets = group.keySet();
		for(String uid : sets){
			if (!uid.equals(fromUserId)){
				Map<String,Object> p = SendMsg.toGroupOrgUser(uid, gid, toType, msgBody, rMsg.copy(), isSendOffline);
				if(p!=null && p.size() != 0){
					params.add(p);
				}
			}
		}
		if(params!=null && params.size() != 0){
			ThreadPool.instance().batchExecOfflines(params);
		}
	}
	
	
	
	/**
	 * 给除群主外的其他群成员发送信息
	 * @param fromUserId
	 * @param gid
	 * @param toType
	 * @param msgBody
	 * @param rMsg
	 * @param isSendOffline
	 * @param ownerId
	 */
	public static void toGroupExceptOwner(String fromUserId,String gid,int toType, String msgBody, ByteBuf rMsg,boolean isSendOffline,String ownerId){
		Groups group = GroupInstance.init().get(gid);
		for(String uid : group.keySet()){
			if (!uid.equals(fromUserId)&&!uid.equals(ownerId)){
				SendMsg.toOrgUser(uid, Config.systemId, toType, msgBody, rMsg, isSendOffline);
			}
		}
	}
	
	
	/**
	 * 给所有群组发消息
	 * @param userId 发消息人的Id
	 * @param toType 0给个人，1给群组发消息
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
	public static void toAllGroup(String userId,int toType,String msgBody, ByteBuf rMsg,boolean isSendOffline){
		Session session = MapChannel.instance().get(userId);
		for(String gid : session.getGroups().keySet()){
			SendMsg.toGroup(userId, gid, toType, msgBody, rMsg.copy(), isSendOffline);
		}
	}
	/**
	 * 给群组中的成员发送消息
	 * @param userId 给谁发
	 * @param fromId 消息来源Id(如果是好友聊天，则为userId,如果是群组聊天，则为groupId)
	 * @param toType 0好友聊天，1群组聊天
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
	public static Map<String,Object> toGroupOrgUser(String userId,String fromId,int toType, String msgBody, ByteBuf rMsg, boolean isSendOffline){
		try {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			Session session = MapChannel.instance().get(userId);
			if(session == null && isSendOffline){
				//(String)map.get("toUserId"), (String)map.get("fromId"), (Integer)map.get("toType"), (String)map.get("msg")
				map.put("toUserId",userId);
				map.put("fromId",fromId);
				map.put("toType",toType);
				map.put("msg",msgBody);
				//ThreadPool.instance().exec(userId, fromId, toType, msgBody);
				//imservice.saveOfflineMsg(userId, fromId, toType, msgBody);
			}
			if(session != null){
				for(String ter : session.getTerminal().keySet()){
					session.getTerminal().get(ter).writeAndFlush(rMsg.copy());
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 给组织中的成员发送消息
	 * @param userId 给谁发
	 * @param fromId 消息来源Id(如果是好友聊天，则为userId,如果是群组聊天，则为groupId，系统通知，则为system)
	 * @param toType 0好友聊天，1群组聊天， 2系统通知
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
	public static void toOrgUser(String userId,String fromId,int toType, String msgBody, ByteBuf rMsg, boolean isSendOffline){
		try {
			Session session = MapChannel.instance().get(userId);
			if(session == null && isSendOffline){
//				ThreadPool.instance().exec(userId, fromId, toType, msgBody);
				ThreadPool.instance().ExecOfflines(userId, fromId, toType, msgBody);
//				imservice.saveOffline(userId, fromId, toType, msgBody);
			}
			if(session != null){
				for(String ter : session.getTerminal().keySet()){
					session.getTerminal().get(ter).writeAndFlush(rMsg.copy());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 给所有组织成员发消息
	 * @param userId 发消息人Id
	 * @param toType 0给个人，1给群组发消息
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
	public static void toAllOrgUser(String userId,int toType,String msgBody, ByteBuf rMsg,boolean isSendOffline){
		/*Session session = MapChannel.instance().get(userId);
		if(session==null) return;
		for(String uid : session.getFriends().keySet()){
			SendMsg.toOrgUser(uid,userId,toType, msgBody,rMsg,isSendOffline);
		}*/
		Map<String, Session> map = MapChannel.instance().getAll();
		for (String id : map.keySet()) {
			SendMsg.toOrgUser(id,userId,toType, msgBody,rMsg,isSendOffline);
		}
	}
	
	
	
	/**
	 * 给好友发送消息
	 * @param userId 给谁发
	 * @param fromId 消息来源Id(如果是好友聊天，则为userId,如果是群组聊天，则为groupId)
	 * @param toType 0好友聊天，1群组聊天
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
//	public static void ToFriend(String userId,String fromId,int toType, String msgBody, ByteBuf rMsg, boolean isSendOffline){
//		try {
//			Session session = MapChannel.instance().get(userId);
//			if(session == null && isSendOffline){
//				//TODO send offLine message;
//				ImUserService imservice = Enhancer.enhance(ImUserService.class);
//				imservice.saveOfflineMsg(userId, fromId, toType, msgBody);
//			}
//			if(session != null){
//				for(String ter : session.getTerminal().keySet()){
//					session.getTerminal().get(ter).writeAndFlush(rMsg.copy());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * 给所有好友发消息
	 * @param userId 发消息人Id
	 * @param toType 0给个人，1给群组发消息
	 * @param msgBody 消息体
	 * @param rMsg 报文
	 * @param isSendOffline 是否离线发送
	 */
//	public static void ToAllFriend(String userId,int toType,String msgBody, ByteBuf rMsg,boolean isSendOffline){
//		Session session = MapChannel.instance().get(userId);
//		for(String uid : session.getFriends().keySet()){
//			SendMsg.ToFriend(uid,userId,toType, msgBody,rMsg,isSendOffline);
//		}
//	}
}
