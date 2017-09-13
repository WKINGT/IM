package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class GroupListResp{
	private int code;
	private List<Record> groups;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Record> getGroups() {
		return groups;
	}

	public void setGroups(List<Record> groups) {
		this.groups = groups;
	}
//	public static void main(String[] args){
//		GroupListResp glr = new GroupListResp();
//		Record record = new Record();
//		record.set("id", null);
//		record.set("group_name", null);
//		record.set("header", null);
//		record.set("decription", null);
//		Record record1 = new Record();
//		record1.set("id", null);
//		record1.set("group_name", null);
//		record1.set("header", null);
//		record1.set("decription", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		list.add(record1);
//		glr.setGroups(list);
//		System.out.println(JsonKit.toJson(glr));	
//	}
}