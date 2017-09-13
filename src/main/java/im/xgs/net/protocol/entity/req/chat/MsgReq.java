package im.xgs.net.protocol.entity.req.chat;

import im.xgs.net.msg.MsgToType;

public class MsgReq {

	private String to;
	private MsgToType toType;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getToType() {
		return toType.ordinal();
	}
	public void setToType(int code) {
		this.toType = MsgToType.get(code);
	}	
}
