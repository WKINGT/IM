package im.xgs.net.protocol.entity.req.group;


/**
 * 发布公告的请求实体
 * @author TianW
 *
 */
public class PublishNoticeReq{
	private String groupId;
	private String notice;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
//	public static void main(String[] args){
//		PublishNoticeReq pu = new PublishNoticeReq();
//		pu.setGroupId(null);
//		pu.setNotice(null);
//		System.out.println(JsonKit.toJson(pu));	
//	}
}