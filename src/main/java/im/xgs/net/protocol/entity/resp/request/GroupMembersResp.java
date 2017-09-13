package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class GroupMembersResp {
	private int code;
	private List<Record> members;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<Record> getMembers() {
		return members;
	}
	public void setMembers(List<Record> members) {
		this.members = members;
	}
//	public static void main(String[] args){
//	GroupMembersResp gm = new GroupMembersResp();
//	Record record = new Record();//group_id,user_id,nickname,user_identity
//	record.set("group_id", null);
//	record.set("user_id", null);
//	record.set("nickname", null);
//	record.set("user_identity", null);
//	List<Record> list = new ArrayList<Record>();
//	list.add(record);
//	gm.setMembers(list);
//	System.out.println(JsonKit.toJson(gm));	
//	}
}
