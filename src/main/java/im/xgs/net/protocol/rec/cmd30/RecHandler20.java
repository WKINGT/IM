package im.xgs.net.protocol.rec.cmd30;

import java.util.Date;

import im.xgs.net.Config;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.JoinGroupReq;
import im.xgs.net.protocol.entity.resp.group.JoinGroupNotify;
import im.xgs.net.protocol.entity.resp.group.JoinGroupResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
/**
 * 加入群组
 * @author TianW
 *
 */
public class RecHandler20 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		//FIXME 加群，群主同意
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.JOIN;
		
		JoinGroupResp joinGroup = new JoinGroupResp();
		JoinGroupReq entity = null;
		try {
			entity = JSON.parseObject(msg, JoinGroupReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = cm.getUserId(channel);
			String groupId = entity.getGroupId();
			String nickname = entity.getNickname();
			groupService.add(userId, groupId, nickname);
			joinGroup.setCode(0);
			
			//异步给群组好友和自己的其他客户端发送加群信息
			new Thread(new Sync(userId,nickname,groupId,channel)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.joinGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, joinGroup);
		
		return rMsg;
	}
	private class Sync implements Runnable {
		private String userId;
		private String nickname;
		private String groupId;
		private String text;
		private Channel ch;
		public Sync(String userId,String nickname,String groupId,Channel ch){
			this.userId = userId;
			this.nickname = nickname;
			this.groupId = groupId;
			this.ch = ch;
			this.text = nickname + "加入群组";
		}
		
		public void run() {
			String version = Protocol.VERSION;
			String uuid = UUIDHexGenerator.getId();
			short cmd = Protocol.GROUPC;
			short opera = Protocol.Group.JOIN;
			JoinGroupNotify joinGroupNotify = new JoinGroupNotify();
			joinGroupNotify.setCode(0);
			joinGroupNotify.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			joinGroupNotify.setFromUserId(userId);
			joinGroupNotify.setToType(1);
			joinGroupNotify.setToId(groupId);
			joinGroupNotify.setNickname(nickname);
			joinGroupNotify.setText(text);
			joinGroupNotify.setCmd(cmd);
			joinGroupNotify.setOpera(opera);
			String msgBody = JSON.toJSONString(joinGroupNotify);
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, joinGroupNotify);
			//获取群主Id
			String groupOwnnerId = groupService.getGroupOwnnerId(groupId);
			//给除群主外的其他群成员发送加群消息
			SendMsg.addGroupMsg(userId, groupId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, ch, false, groupOwnnerId);
			//给群主发送加群消息
			SendMsg.toOrgUser(groupOwnnerId, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg, true);
		}
	}

}
