package im.xgs.net.protocol.entity.req.request;


/**
 * 群组详细信的请求实体
 * @author TianW
 *
 */
public class GroupInfoReq{
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
//	public static void main(String[] args){
//		GroupInfoReq fir = new GroupInfoReq();
//		fir.setGroupId(null);
//		System.out.println(JsonKit.toJson(fir));	
//	}
}