package im.xgs.net.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrgUser {

	private Map<String,OrgMembers> members = new ConcurrentHashMap<String,OrgMembers>();
	
	private static OrgUser orguser = new OrgUser();
	
	private OrgUser(){}
	
	public static OrgUser instance(){
		return orguser;
	}
	
	public void put(OrgMembers m){
		members.put(m.getId(), m);
	}
	
	public OrgMembers get(String id){
		return members.get(id);
	}
	
	public void remove(String id){
		members.remove(id);
	}
	
	public Map<String,OrgMembers> getAll(){
		return members;
	}
}
