package im.xgs.net.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupInstance {
	private Map<String,Groups>  groups = new ConcurrentHashMap<String, Groups>();
	
	private static GroupInstance instance = new GroupInstance();
	
	private GroupInstance(){}
	
	public static GroupInstance init(){
		return instance;
	}
	
	public void put(String groupId, Groups g){
		groups.put(groupId, g);
	}
	
	public Groups get(String groupId){
		return groups.get(groupId);
	}
	/**
	 * 解散群
	 * @param groupId
	 */
	public void remove(String groupId){
		groups.get(groupId).clear();
		groups.remove(groupId);
	}
}
