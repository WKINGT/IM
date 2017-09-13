package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.channel.OrgMembers;
import im.xgs.net.channel.OrgUser;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.OrgInfoResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;
/**
 * 请求组织好友信息
 * @author TianW
 *
 */
public class RecHandler11 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		OrgInfoResp orgInfoResp = new OrgInfoResp();
		Map<String, OrgMembers> map = OrgUser.instance().getAll();
		orgInfoResp.setCode(0);
		orgInfoResp.setOrgMembers(map);
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.ORG_INFO;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, orgInfoResp);
		
		return rMsg;
	}
}
