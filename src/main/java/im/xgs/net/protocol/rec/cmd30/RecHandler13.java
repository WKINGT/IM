package im.xgs.net.protocol.rec.cmd30;

import com.alibaba.fastjson.JSON;

import im.xgs.net.Config;
import im.xgs.net.channel.GroupInstance;
import im.xgs.net.channel.MapChannel;
import im.xgs.net.channel.Session;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.KickOutGroupReq;
import im.xgs.net.protocol.entity.resp.group.KickOutGroupResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 踢出群，只有群主才可以踢掉其他人
 * @author TianW
 *
 */
public class RecHandler13 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.KICK_OUT_GROUP;
		KickOutGroupReq entity = null;
		KickOutGroupResp resp = null;
		ByteBuf rMsg = null;
		try {
			entity = JSON.parseObject(msg, KickOutGroupReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupOwner = cm.getUserId(channel);
			String groupId = entity.getGroupId();
			String groupName = entity.getGroupName();
			String userId = entity.getUserId();
			groupService.kickOutGroup(groupId, userId, groupOwner);
			
			resp = new KickOutGroupResp();
			resp.setCode(0);
			resp.setGroupId(groupId);
			resp.setGroupName(groupName);
			resp.setGroupOwner(groupOwner);
			resp.setUserId(userId);
			String msgBody = JSON.toJSONString(resp);
			rMsg = Message.Packing(version, uuid, cmd, opera, resp);
			//异步通知其他群组成员和被踢出群者
			new Thread(new Sync(userId, groupOwner, msgBody, rMsg.copy(), groupId)).start();
			//给群主的其他客户端通知
			SendMsg.toSelf(groupOwner, rMsg.copy(), channel);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.kickOutgroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		
		
		return rMsg;
	}

	private class Sync implements Runnable {
		private ByteBuf rMsg;
		private String msgBody;
		private String userId;
		private String groupId;
		private String ownerId;
		public Sync(String userId, String ownerId, String msgBody, ByteBuf rMsg, String groupId) {
			this.msgBody = msgBody;
			this.ownerId = ownerId;
			this.rMsg = rMsg;
			this.userId = userId;
			this.groupId = groupId;
		}

		public void run() {
			//给群组其他群成员通知(除了群主)
			SendMsg.toGroupExceptOwner(userId, groupId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg.copy(), false, ownerId);
			//给被踢出群者通知
			SendMsg.toOrgUser(userId, Config.systemId, MsgToType.TOSYSTEM.ordinal(), msgBody, rMsg.copy(), true);
			//在群实例里删除被踢出群者
			GroupInstance.init().get(groupId).remove(userId);
			
			Session session = MapChannel.instance().get(userId);
			if(session!=null){
				//在被踢出群的人的session里删掉推掉的群组
				session.getGroups().remove(groupId);
			}
		}
	}
}
