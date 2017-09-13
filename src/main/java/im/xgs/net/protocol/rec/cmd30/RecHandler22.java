package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.Config;
import im.xgs.net.channel.ChannelInstance;
import im.xgs.net.channel.GroupInstance;
import im.xgs.net.channel.Groups;
import im.xgs.net.channel.Session;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.InviteToGroupReq;
import im.xgs.net.protocol.entity.resp.group.InGroupNotify;
import im.xgs.net.protocol.entity.resp.group.InviteToGroupResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
/**
 * 拉好友进群
 * 如果该群成员在线，怎向该群成员发送加群消息，客户端收到消息后，执行群成员的增量更新
 * 如果该群成员不在线，则不发送离线消息，该群成员下次登录后，执行拉取群所有成员
 * 如果该群成员身份为群主，则要发送离线消息
 * @author TianW
 *
 */
public class RecHandler22 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.INVITE_TO_GROUP;
		InviteToGroupResp inviteToGroupResp = new InviteToGroupResp();
		InviteToGroupReq entity = null;
		try {
			entity = JSON.parseObject(msg, InviteToGroupReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = cm.getUserId(channel);
			String groupId = entity.getGroupId();
			String groupName = entity.getGroupName();
			Map<String, String> members = entity.getMembers();
			//将这些人拉进群组
			groupService.bitchAdd(groupId, members);
			//将这些被拉进群的人加入到群数据里
			Groups g1 = GroupInstance.init().get(groupId);
			for(String id : members.keySet()){
				g1.put(id, "");
			}
			//异步通知被拉进群的人被拉进群，和给其他的群成员发送被拉的人进群的消息
			new Thread(new Sync(userId, groupId, groupName, members)).start();
			
			inviteToGroupResp.setCode(0);
			inviteToGroupResp.setGroupId(groupId);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.inviteToGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, inviteToGroupResp);
		return rMsg;
	}
	
	
	private class Sync implements Runnable {
		private String userId;
		private String groupId;
		private String groupName;
//		private String text;
		Map<String,String> members;
		public Sync(String userId,String groupId, String groupName, Map<String,String> members){
			this.userId = userId;
			this.groupId = groupId;
			this.members = members;
			this.groupName = groupName;
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
//			notify.setText(text);
			notify.setCmd(cmd);
			notify.setOpera(opera);
			notify.setGroupName(groupName);
			
			String msgBody = JSON.toJSONString(notify);
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, notify);
			
			//给其他群成员发送被拉的人进群的消息实体
			InGroupNotify notify1 = new InGroupNotify();
			notify1.setCode(0);
			notify1.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			notify1.setToType(MsgToType.TOGROUP.ordinal());
			notify1.setToId(groupId);
			notify1.setCmd(cmd);
			notify1.setOpera(Protocol.Group.JOIN);
			notify1.setGroupName(groupName);
			
			List<String> userIds = new ArrayList<String>();
			List<String> msgs = new ArrayList<String>();
			String groupOwnnerId = groupService.getGroupOwnnerId(groupId);
			for(String id : members.keySet()){
				userIds.add(id);
				//通知该成员被拉进群
				SendMsg.toOrgUser(id, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, true);
				//给其他群成员通知时，消息的来源者是被拉进群的人
				notify1.setFromUserId(id);
				String msgBody1 = JSON.toJSONString(notify1);
				ByteBuf rMsg1 = Message.Packing(version, uuid, cmd, Protocol.Group.JOIN, notify1);
				
				//给除群主外的其他群成员发送加群消息
				SendMsg.toGroupExceptOwner(id, groupId, MsgToType.TOSYSTEM.ordinal(), msgBody1, rMsg1, false, groupOwnnerId);
				msgs.add(msgBody1);
				//给群主发送加群消息
				SendMsg.toOrgUser(groupOwnnerId, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody1, rMsg1, true);
				//如果这个成员在线，则将群组加入到被拉进群的成员的session里
				Session session = ChannelInstance.getChannelManager().get(id);
				if(session!=null){
					session.getGroups().put(groupId, "");
				}
			}
			//给被拉进群的人发的消息进系统通知
			imservice.saveSysMsg(userIds, msgBody);
			//给群主发的消息进系统通知
			imservice.saveSysMsg(groupOwnnerId, msgs);
		}
	}
}
