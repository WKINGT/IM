package im.xgs.net.protocol.entity.req.group;


/**
 * 删除公告的请求实体
 * @author TianW
 *
 */
public class RemoveNoticeReq{
	private String groupId;
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	private String groupNoticeId;

	public String getGroupNoticeId() {
		return groupNoticeId;
	}

	public void setGroupNoticeId(String groupNoticeId) {
		this.groupNoticeId = groupNoticeId;
	}
//	public static void main(String[] args){
//		RemoveNoticeReq re = new RemoveNoticeReq();
//		re.setGroupNoticeId(null);
//		System.out.println(JsonKit.toJson(re));	
//	}
}