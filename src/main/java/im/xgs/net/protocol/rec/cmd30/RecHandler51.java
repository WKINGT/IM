package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.ViewGroupFileReq;
import im.xgs.net.protocol.entity.resp.group.ViewGroupFileResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 查看群文件
 * @author TianW
 *
 */
public class RecHandler51 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {

		ViewGroupFileResp resp = new ViewGroupFileResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.VIEW_FILE;
		ViewGroupFileReq entity = null;
		try {
			entity = JSON.parseObject(msg, ViewGroupFileReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			int pageSize = entity.getPageSize();
			int pageNumber = entity.getPageNumber();
			Page<Record> groupFiles = groupService.viewFile(pageNumber, pageSize, groupId);
			resp.setCode(0);
			resp.setGroupFiles(groupFiles);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.viewGroupFile");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, resp);
		return rMsg;
	}

}
