package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class FindOfflineMsgResp {
//	private String id;
//	private String fromId;
//	private byte[] message;
//	private String sendTime;
	private int code;
	private List<Record> offlineMsgs;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<Record> getOfflineMsgs() {
		return offlineMsgs;
	}

	public void setOfflineMsgs(List<Record> offlineMsgs) {
		this.offlineMsgs = offlineMsgs;
	}

//	public static void main(String[] args){
//		FindOfflineMsgResp f = new FindOfflineMsgResp();
//		Record record = new Record();
////		record.set("id", null);
////		record.set("fromId", null);
//		record.set("message", null);
////		record.set("sendTime", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		f.setCode(0);
//		f.setOfflineMsgs(list);
//		System.out.println(JsonKit.toJson(f));	
//	}
}
