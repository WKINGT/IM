package im.xgs.net.protocol.entity.resp.group;

import im.xgs.net.msg.MsgToType;

public class NoticeNotify {
	private String time;
	private int code;
	private String fromUserId;
	private MsgToType toType;
	private String toId;
	private String notice;
	private int cmd;
	private int opera;
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public int getOpera() {
		return opera;
	}
	public void setOpera(int opera) {
		this.opera = opera;
	}
	
	public int getToType() {
		return toType.ordinal();
	}
	public void setToType(int code) {
		this.toType = MsgToType.get(code);
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
}
