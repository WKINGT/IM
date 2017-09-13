package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.channel.GroupInstance;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.DisbandGroupReq;
import im.xgs.net.protocol.entity.resp.group.DisbandGroupNotify;
import im.xgs.net.protocol.entity.resp.group.DisbandGroupResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;

import com.alibaba.fastjson.JSON;
/**
 * 解散群组
 * 给所有群成员发送解散群的消息，客户端收到这个消息后，将该群从群列表里删除
 * 如果该成员不在线，则发送离线消息
 * 
 * @author TianW
 *
 */
public class RecHandler11 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.DISBAND;
		DisbandGroupResp disbandGroup = new DisbandGroupResp();
		DisbandGroupReq entity = null;
		try {
			entity = JSON.parseObject(msg, DisbandGroupReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			String groupName = entity.getGroupName();
			String userId = cm.getUserId(channel);
			groupService.quit(groupId, userId);
			disbandGroup.setCode(0);
			
			//异步给群组成员和自己的其他客户端发送解散群消息
			new Thread(new Sync(userId, groupId, groupName, channel)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.disbandGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, disbandGroup);
		
		return rMsg;
	}
	private class Sync implements Runnable{
		private String userId;
		private String groupId;
		private String groupName;
		private Channel ch;
		public Sync(String userId,String groupId,String groupName,Channel ch){
			this.userId = userId;
			this.groupId = groupId;
			this.groupName = groupName;
			this.ch = ch;
		}
		public void run() {
			String version = Protocol.VERSION;
			String uuid = UUIDHexGenerator.getId();
			short cmd = Protocol.GROUPC;
			short opera = Protocol.Group.DISBAND;
			//给群成员发送群解散消息的实体
			DisbandGroupNotify disbandGroupNotify = new DisbandGroupNotify();
			disbandGroupNotify.setCode(0);
			disbandGroupNotify.setFromUserId(userId);
			disbandGroupNotify.setToType(MsgToType.TOGROUP.ordinal());
			disbandGroupNotify.setToId(groupId);
//			disbandGroupNotify.setText(groupName + "群已解散");
			disbandGroupNotify.setCmd(cmd);
			disbandGroupNotify.setOpera(opera);
			disbandGroupNotify.setGroupName(groupName);
			disbandGroupNotify.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));

			String msgBody = JSON.toJSONString(disbandGroupNotify);
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, disbandGroupNotify);
			//给群组的所有成员发送解散群消息
			SendMsg.disbandGroupMsg(userId, groupId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, ch, true);
			/***解散群后，在群里删除该群数据 ***/
			GroupInstance.init().remove(groupId);
		}
	}
}
