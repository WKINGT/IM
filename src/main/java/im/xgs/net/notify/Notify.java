package im.xgs.net.notify;

import im.xgs.net.channel.ChannelInstance;
import im.xgs.net.channel.ChannelManager;
import io.netty.channel.Channel;

public interface Notify {
	ChannelManager cm = ChannelInstance.getChannelManager();
	public void exec(boolean isWeb,int client,Channel channel,String msg);
	public void exec(boolean isWeb,String client,Channel channel,String msg);
}
