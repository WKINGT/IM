package im.xgs.net.protocol.entity.req.group;

import java.util.Map;

/**
 * 创建群组的请求实体
 * @author TianW
 *
 */
public class CreateGroupReq{
	private String groupName;
	private String description;
	private String header;
	private String nickname;
	private Map<String,String> members;
	
	public Map<String, String> getMembers() {
		return members;
	}
	public void setMembers(Map<String, String> members) {
		this.members = members;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
//	public static void main(String[] args){
//		CreateGroupReq cgr = new CreateGroupReq();
//		cgr.setGroupName(null);
//		cgr.setDescription(null);
//		cgr.setHeader(null);
//		cgr.setNickname(null);
//		
//		System.out.println(JsonKit.toJson(cgr));	
//	}
}