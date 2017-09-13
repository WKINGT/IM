package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.Config;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.ExitGroupReq;
import im.xgs.net.protocol.entity.resp.group.ExitGroupNotify;
import im.xgs.net.protocol.entity.resp.group.ExitGroupResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;

import com.alibaba.fastjson.JSON;
/**
 * 退出群组
 * 给群主发送退群消息，如果群主不在线，发离线消息
 * 给除群主外的其他群成员发送退群消息，如果成员不在线，则不通知
 * 只给在线群成员发送，客户端收到消息后，执行群成员的减量更新
 * @author TianW
 *
 */
public class RecHandler21 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.EXIT;
		//给退群者的响应实体
		ExitGroupResp exitGroup = new ExitGroupResp();
		ExitGroupReq entity = null;
		try {
			entity = JSON.parseObject(msg, ExitGroupReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			String groupName = entity.getGroupName();
			String userId = cm.getUserId(channel);
//			String userName = cm.getUserName(channel);
			
			groupService.quit(groupId, userId);
			exitGroup.setCode(0);
			//给群成员和其他自己的客户端发退群消息
			new Thread(new Sync(userId, groupId, groupName, channel)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.exitGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, exitGroup);
		return rMsg;
	}
	private class Sync implements Runnable{
		private String userId;
//		private String userName;
		private String groupId;
		private String groupName;
		private Channel ch;
		public Sync (String userId,String groupId, String groupName, Channel ch){
			this.userId = userId;
//			this.userName = userName;
			this.groupId = groupId;
			this.groupName = groupName;
			this.ch = ch;
		}

		public void run() {
			String version = Protocol.VERSION;
			String uuid = UUIDHexGenerator.getId();
			short cmd = Protocol.GROUPC;
			short opera = Protocol.Group.EXIT;
			//给群成员通知退群的实体
			ExitGroupNotify exitGroupNotify = new ExitGroupNotify();
			exitGroupNotify.setCode(0);
			exitGroupNotify.setFromUserId(userId);
			exitGroupNotify.setToType(1);
			exitGroupNotify.setToId(groupId);
//			exitGroupNotify.setText(userName + "退出群组");
			exitGroupNotify.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			exitGroupNotify.setCmd(cmd);
			exitGroupNotify.setOpera(opera);
			exitGroupNotify.setGroupName(groupName);
			
			String msgBody = JSON.toJSONString(exitGroupNotify);
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, exitGroupNotify);
			//获得群主的id
			String groupOwnnerId = groupService.getGroupOwnnerId(groupId);
			//给除群主外的其他群成员发送退群消息(如果不在线，则不通知，只给在线群成员发送)
			SendMsg.quitGroupMsg(userId, groupId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, ch, false, groupOwnnerId);
			//给群主发送退群消息(如果不在线，发离线 消息)
			SendMsg.toOrgUser(groupOwnnerId, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, true);
			imservice.saveSysMsg(groupOwnnerId, msgBody);
//			String groupOwnnerId = groupService.getGroupOwnnerId(groupId);
//			ExitGroupNotify exitGroupNotify = new ExitGroupNotify();	
//			SendMsg.ToFriend(groupOwnnerId, groupId, 1, msgBody, rOwnnerMsg, true);
		}
	}
}
