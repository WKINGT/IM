package im.xgs.net.protocol.rec.cmd20;


import im.xgs.net.channel.Session;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.OnlineStatusResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请求所有用户在线状态
 * @author TianW
 *
 */
public class RecHandler13 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		//请求其他用户的在线状态
		String userId = cm.getUserId(channel);
		OnlineStatusResp resp = new OnlineStatusResp();
		
		try {
			Map<String, Session> map = cm.getAll();
			List<String> idList = new ArrayList<String>();
			for(String id : map.keySet()){
				if(!userId.equals(id)){
					idList.add(id);
				}
			}
			resp.setCode(0);
			resp.setOnlineIds(idList);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.onLineStatus");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.ONLINE_STATUS;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, resp);
		return rMsg;
	}

}
