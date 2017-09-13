package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class CountOfflineMsgResp {
	private int code;
	private List<Record> countOfflineMsg;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<Record> getCountOfflineMsgs() {
		return countOfflineMsg;
	}
	public void setCountOfflineMsgs(List<Record> countOfflineMsg) {
		this.countOfflineMsg = countOfflineMsg;
	}
//	public static void main(String[] args){
//		CountOfflineMsgResp cout = new CountOfflineMsgResp();//count ,from_id fromId,to_type toType
//		Record record = new Record();
//		record.set("count", null);
//		record.set("fromId", null);
//		record.set("toType", 0);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		cout.setCode(0);
//		cout.setCountOfflineMsgs(list);
//		System.out.println(JsonKit.toJson(cout));	
//	}

}
