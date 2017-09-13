package im.xgs.net.notify;

import im.xgs.net.channel.ChannelType;
import im.xgs.net.msg.ImMsgType;
import im.xgs.net.msg.ResMessage;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 通知自己上线
 * @author hasee
 *
 */
public class SelfUpDown implements Notify {
	
	public void exec(boolean isWeb, int client, Channel channel, String msg) {
		String name = ChannelType.get(client).getName();
		this.exec(isWeb, name, channel, msg);
	}

	public void exec(boolean isWeb, String client, Channel channel, String msg) {
//		String uid = cm.getKey(channel.id());
//		Map<String, Channel> map = cm.gets(uid);
//		if (map != null) {
//			for (String t : map.keySet()) {
//				if(client.equals(t)){
//					continue;
//				}
//				ResMessage rmsg = new ResMessage(t.equals(ChannelType.WEB.getName()));
//				rmsg.setType(ImMsgType.ntf).setMtype(ImMsgType.ntfc.rep.user_up).setMsg(msg);
//				Object obj = rmsg.msg();
//				if(obj != null){
//					map.get(t).writeAndFlush(obj);
//				}
//			}
//		}
	}

}
