package im.xgs.net.channel;

import java.util.Map;

import io.netty.channel.Channel;


/**
 * 已登陆的【tcp管道】存放与管理
 * @author hasee
 *
 */
public interface ChannelManager{
	
	public void put(String userId,Session session,Channel channel);
	
	public Session get(String userId);
	
	public Session get(Channel channel);
	
	public void destroy(Channel channel);
	
	public String getUserId(Channel channel);
	
	public String getUserName(Channel channel);
	
	public Map<String,Session> getAll();
	
	public int getHighestPriorityClient(String userId, String client);
	
	public String getSecondPriorityClient(String userId,String client);
}
