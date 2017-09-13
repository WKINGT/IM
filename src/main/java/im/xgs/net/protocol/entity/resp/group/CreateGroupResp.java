package im.xgs.net.protocol.entity.resp.group;


/**
 * 创建群组的响应实体
 * @author TianW
 *
 */
public class CreateGroupResp{
	private int code;
	private String groupId;
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}