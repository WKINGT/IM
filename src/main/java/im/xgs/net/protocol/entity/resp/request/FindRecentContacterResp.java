package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
/**
 * 最近联系人的响应实体
 * @author TianW
 *
 */
public class FindRecentContacterResp{
	private int code;
	private List<Record> recentContacters;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Record> getRecentContacters() {
		return recentContacters;
	}

	public void setRecentContacters(List<Record> recentContacters) {
		this.recentContacters = recentContacters;
	}
//	public static void main(String[] args){
//		RecentContacterResp rcr= new RecentContacterResp();
//		Record record = new Record();
//		record.set("header", null);
//		record.set("name", null);
//		record.set("id", null);
//		record.set("flag", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		rcr.setRecentContacters(list);
//		System.out.println(JsonKit.toJson(rcr));	
//	}
	
}