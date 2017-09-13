package im.xgs.net.protocol.entity.resp.chat;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public class FindHistoryMsgResp {
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
//	public static void main(String[] args){
//		FindHistoryResp fh = new FindHistoryResp();
//		fh.setSucc(true);
//		Record record = new Record();
//		record.set("to_user", null);
//		record.set("from_user", null);
//		record.set("message", null);
//		record.set("send_time", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		Page<Record> page = new Page<Record>(list, 1, 10, 1, 1);
//		fh.setMessages(page);
//		System.out.println(JsonKit.toJson(fh));	
//	}
	
}
