package im.xgs.net.protocol.entity.req.group;

import java.util.Map;

public class InviteToGroupReq {
	private String groupId;
	private String groupName;
	private Map<String,String> members;
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
	public Map<String, String> getMembers() {
		return members;
	}
	public void setMembers(Map<String, String> members) {
		this.members = members;
	}
	
}
