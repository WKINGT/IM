package im.xgs.net.channel;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

	public enum ter{
		PC,WEB,MOBILE;
	}
	
	private Map<String,Channel> terminal= new ConcurrentHashMap<String, Channel>();
	private Map<String,String> friends = new ConcurrentHashMap<String, String>();
	private Map<String,String>  groups = new ConcurrentHashMap<String, String>();
	private Info info = new Info();
	
	
	
	public Map<String, Channel> getTerminal() {
		return terminal;
	}
	public void setTerminal(Map<String, Channel> terminal) {
		this.terminal = terminal;
	}
	public Map<String, String> getFriends() {
		return friends;
	}
	public void setFriends(Map<String, String> friends) {
		this.friends = friends;
	}
	public Map<String, String> getGroups() {
		return groups;
	}
	public void setGroups(Map<String, String> groups) {
		this.groups = groups;
	}
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	
	
}
