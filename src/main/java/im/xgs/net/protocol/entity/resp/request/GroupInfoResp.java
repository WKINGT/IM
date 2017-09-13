package im.xgs.net.protocol.entity.resp.request;

import com.jfinal.plugin.activerecord.Record;
/**
 * 群组详细信的响应实体
 * @author TianW
 *
 */
public class GroupInfoResp{
	private Record groupInfo;
	private int code;
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Record getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(Record groupInfo) {
		this.groupInfo = groupInfo;
	}
//	public static void main(String[] args){
//		GroupInfoResp gir = new GroupInfoResp();
//		Record record = new Record();
//		record.set("group_name", null);
//		record.set("header", null);
//		record.set("decription", null);
//		record.set("owner", null);
//		gir.setGroupInfo(record);
//		System.out.println(JsonKit.toJson(gir));	
//	}
}