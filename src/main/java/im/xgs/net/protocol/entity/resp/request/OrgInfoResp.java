package im.xgs.net.protocol.entity.resp.request;

import im.xgs.net.channel.OrgMembers;

import java.util.Map;

public class OrgInfoResp {
	private int code;
	private Map<String, OrgMembers> orgMembers;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Map<String, OrgMembers> getOrgMembers() {
		return orgMembers;
	}
	public void setOrgMembers(Map<String, OrgMembers> orgMembers) {
		this.orgMembers = orgMembers;
	}
//	public static void main(String[] args){
//		OrgInfoResp org = new OrgInfoResp();
//		Map<String, OrgMembers> orgMeb = new ConcurrentHashMap<String, OrgMembers>();
//		OrgMembers meb = new OrgMembers();
//		meb.setId("id值");
//		meb.setParentId(null);
//		meb.setName(null);
//		meb.setLeaf(true);
//		meb.setOnline(true);
//		orgMeb.put(meb.getId(), meb);
//		org.setCode(0);
//		org.setOrgMembers(orgMeb);
//		System.out.println(JsonKit.toJson(org));	
//	}
}
