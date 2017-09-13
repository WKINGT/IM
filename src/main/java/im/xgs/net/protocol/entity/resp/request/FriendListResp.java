package im.xgs.net.protocol.entity.resp.request;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class FriendListResp{
	private int code;
	private List<Record> friends;
		
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Record> getFriends() {
		return friends;
	}

	public void setFriends(List<Record> friends) {
		this.friends = friends;
	}
//	public static void main(String[] args){
//		FriendListResp flr = new FriendListResp();
//		Record record = new Record();
//		record.set("friend_id", null);
//		record.set("nickname", null);
//		record.set("sign", null);
//		List<Record> list = new ArrayList<Record>();
//		list.add(record);
//		flr.setFriends(list);
//		System.out.println(JsonKit.toJson(flr));
//	}
}