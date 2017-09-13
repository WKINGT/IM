package im.xgs.net.protocol.rec.cmd30;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.DeleteGroupFileReq;
import im.xgs.net.protocol.entity.resp.group.DeleteGroupFileResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 删除群文件
 * @author TianW
 *
 */
public class RecHandler52 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		DeleteGroupFileResp resp = new DeleteGroupFileResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.DELETE_FILE;
		DeleteGroupFileReq entity = null;
		try {
			entity = JSON.parseObject(msg, DeleteGroupFileReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			String fileId = entity.getFileId();
			groupService.deleteFile(fileId);
			
			resp.setCode(0);
			resp.setGroupId(groupId);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.deleteGroupFile");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, resp);
		return rMsg;
	}

}
