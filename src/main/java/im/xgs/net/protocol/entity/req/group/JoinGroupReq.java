package im.xgs.net.protocol.entity.req.group;


/**
 * 加入群组的请求实体
 * @author TianW
 *
 */
public class JoinGroupReq{
	private String GroupId;
	private String nickname;

	public String getGroupId() {
		return GroupId;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
//	public static void main(String[] args){
//		JoinGroupReq jo = new JoinGroupReq();
//		jo.setGroupId(null);
//		jo.setNickname(null);
//		System.out.println(JsonKit.toJson(jo));	
//	}
}