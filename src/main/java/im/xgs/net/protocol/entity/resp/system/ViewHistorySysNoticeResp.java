package im.xgs.net.protocol.entity.resp.system;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ViewHistorySysNoticeResp {
	private int code;
	private Page<Record> messages;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Page<Record> getMessages() {
		return messages;
	}
	public void setMessages(Page<Record> messages) {
		this.messages = messages;
	}
}
