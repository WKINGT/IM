package im.xgs.net.protocol.entity.req.group;


/**
 * 解散群组的请求实体
 * @author TianW
 *
 */
public class DisbandGroupReq{
	private String groupId;
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
//	public static void main(String[] args){
//		DisbandGroupReq d = new DisbandGroupReq();
//		d.setGroupId(null);
//		d.setGroupName(null);
//		System.out.println(JsonKit.toJson(d));	
//	}
}