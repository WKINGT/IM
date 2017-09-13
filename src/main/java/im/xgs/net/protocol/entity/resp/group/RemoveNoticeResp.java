package im.xgs.net.protocol.entity.resp.group;
/**
 * 删除公告的响应实体
 * @author TianW
 *
 */
public class RemoveNoticeResp{
	private int code;
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}