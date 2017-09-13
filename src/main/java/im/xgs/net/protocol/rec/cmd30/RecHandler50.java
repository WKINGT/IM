package im.xgs.net.protocol.rec.cmd30;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.UploadGroupFileReq;
import im.xgs.net.protocol.entity.resp.group.UploadGroupFileResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 上传群文件
 * @author TianW
 *
 */
public class RecHandler50 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {

		UploadGroupFileResp resp = new UploadGroupFileResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.UPLOAD_FILE;
		UploadGroupFileReq entity = null;
		try {
			entity = JSON.parseObject(msg, UploadGroupFileReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = cm.getUserId(channel);
			String groupId = entity.getGroupId();
			String fileId = entity.getFileId();
			groupService.uploadFile(fileId, groupId, userId);
			
			resp.setCode(0);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.uploadGroupFile");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, resp);
		return rMsg;
	}

}
