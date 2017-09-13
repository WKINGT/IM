package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
/**
 * 查找好友或群组的响应实体
 * @author TianW
 *
 */
public class SearchResp{
	private int code;
	private List<Record> frinedsOrgroups;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Record> getFrinedsOrgroups() {
		return frinedsOrgroups;
	}

	public void setFrinedsOrgroups(List<Record> frinedsOrgroups) {
		this.frinedsOrgroups = frinedsOrgroups;
	}
//	public static void main(String[] args){
//		SearchResp sr = new SearchResp();
//		Record record = new Record();
//		record.set("name", null);
//		record.set("header", null);
//		record.set("id", null);
//		record.set("flag", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		sr.setFrinedsOrgroups(list);
//		System.out.println(JsonKit.toJson(sr));	
//	}
}