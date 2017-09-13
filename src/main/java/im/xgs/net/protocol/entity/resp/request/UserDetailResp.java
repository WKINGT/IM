package im.xgs.net.protocol.entity.resp.request;
import com.jfinal.plugin.activerecord.Record;

public class UserDetailResp{
	private int code;
	private Record userDetail;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Record getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(Record userDetail) {
		this.userDetail = userDetail;
	}
//	public static void main(String[] args){
//		UserDetailResp udr = new UserDetailResp();
//		Record record = new Record();
//		record.set("nickname", null);
//		record.set("header", null);
//		record.set("sign", null);
//		udr.setUserDetail(record);
//		System.out.println(JsonKit.toJson(udr));	
//	}
}