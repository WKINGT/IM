package im.xgs.net.protocol.rec.cmd20;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.GroupMembersReq;
import im.xgs.net.protocol.entity.resp.request.GroupMembersResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 查看群组的成员
 * 客户端登录后，点击群组时，请求一次群组成员信息
 * @author TianW
 *
 */
public class RecHandler22 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		GroupMembersResp groupMembersResp = null;
		GroupMembersReq entity = null;
		try {
			entity = JSON.parseObject(msg, GroupMembersReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			List<Record> members = groupService.findMember(groupId);
			groupMembersResp = new GroupMembersResp();
			groupMembersResp.setCode(0);
			groupMembersResp.setMembers(members);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getGroupMembers");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.GROUP_MEMBERS;
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, groupMembersResp);
		
		return rMsg;
		
	}
	

}
