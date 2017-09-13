package im.xgs.net.channel;

public class ChannelInstance {

	private ChannelInstance(){}
	public static ChannelManager getChannelManager(){
		return MapChannel.instance();
	}
}
