package im.xgs.net.channel;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 内存的实现
 * @author hasee
 *
 */
public final class MapChannel implements ChannelManager{

	private MapChannel(){}
	
	private static MapChannel mc = new MapChannel();
	
	public static MapChannel instance(){
		return mc;
	}
	
	private Map<String,Session> map = new ConcurrentHashMap<String,Session>();
	private Map<String,String> socketUser = new ConcurrentHashMap<String,String>();
	
	public Map<String,Session> getAll(){
		return this.map;
	}
	/**
	 * 移除管道和用户id的映射
	 * @param channel
	 */
	public void remove(Channel channel){
		socketUser.remove(channel.id().asShortText());
	}
	public void put(String userId, Session session,Channel channel) {
		map.put(userId, session);
		socketUser.put(channel.id().asShortText(), userId);
	}

	public Session get(String userId) {
		return map.get(userId);
	}
	
	public String getUserId(Channel channel){
		return socketUser.get(channel.id().asShortText());
	}
	//通过管道得到用户的用户名
	public String getUserName(Channel channel){
		return map.get(socketUser.get(channel.id().asShortText())).getInfo().getName();
	}

	public Session get(Channel channel) {
		return get(getUserId(channel));
	}
	public void destroy(Channel channel){
		String userId = socketUser.get(channel.id().asShortText());
		if(userId == null) return;
		Session session = get(userId);
		socketUser.remove(channel.id().asShortText());
		String tid = null;
		if(session != null){
			for(String key : session.getTerminal().keySet()){
				Channel terChannel = session.getTerminal().get(key);
				if(channel.equals(terChannel)){
					tid = key;
					break;
				}
			}
			if(tid != null){
				session.getTerminal().remove(tid);
			}
			if(session.getTerminal().size() == 0){
				map.remove(userId);
				//状态改为下线
				OrgUser.instance().get(userId).setOnline(false);
			}
//			channel.disconnect();
		}
	}
	public int getHighestPriorityClient(String userId, String client){
		int a = -1;
		Session session = map.get(userId);
		if(session!=null){
			Map<String, Channel> termials = session.getTerminal();
			for(String ter : termials.keySet()){
				a = Integer.parseInt(ter)>a ? Integer.parseInt(ter) : a;
			}
		}
		
		return a;
	}
	public String getSecondPriorityClient(String userId,String client){
		int a = -1;
		Session session = map.get(userId);
		if(session!=null){
			Map<String, Channel> termials = session.getTerminal();
			for(String ter : termials.keySet()){
				if (Integer.parseInt(ter)>a && !ter.equals(client)){
					a = Integer.parseInt(ter);
				}
			}
		}
		if(a!=-1) {
			return String.valueOf(a);
		}
		return null;//返回null表示用户只有client这一个客户端在线，没有第二个在线的客户端
	}
}
