package im.xgs.net.protocol.rec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import im.xgs.net.channel.ChannelInstance;
import im.xgs.net.channel.ChannelManager;
import im.xgs.net.exception.ImException;
import im.xgs.net.notify.SelfUpDown;
import im.xgs.net.service.ImFriendService;
import im.xgs.net.service.ImGroupService;
import im.xgs.net.service.ImUserService;
import io.netty.channel.Channel;

public abstract class RecHandler {

	protected ImUserService imservice = Enhancer.enhance(ImUserService.class);
	protected ImFriendService friendService = Enhancer.enhance(ImFriendService.class);
	protected ImGroupService groupService = Enhancer.enhance(ImGroupService.class);
	protected ChannelManager cm = ChannelInstance.getChannelManager();
	protected SelfUpDown sud = Enhancer.enhance(SelfUpDown.class);
	protected Prop prop = PropKit.use("errcode.txt");
	
	public final static String emptyStr = "";
	
	protected Logger logger = LoggerFactory.getLogger("im.xgs.net.protocol.rec.RecHandler");

	
	public abstract Object exec(String uuid,String msg,boolean isWeb,String client,Channel channel) throws ImException;
	
	public Object exec(String uuid,String msg,String client,Channel channel) throws ImException{
		return this.exec(uuid, msg, false, client, channel);
	}

}
