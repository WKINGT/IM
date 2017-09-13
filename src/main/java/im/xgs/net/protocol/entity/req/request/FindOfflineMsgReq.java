package im.xgs.net.protocol.entity.req.request;

import im.xgs.net.msg.MsgToType;

public class FindOfflineMsgReq {
	private String fromId;
	private MsgToType toType;
	
	public int getToType() {
		return toType.ordinal();
	}
	public void setToType(int code) {
		this.toType = MsgToType.get(code);
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
}
