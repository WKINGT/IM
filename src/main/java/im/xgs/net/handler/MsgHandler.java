package im.xgs.net.handler;


import im.xgs.net.channel.MapChannel;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.HeaderBody;
import im.xgs.net.msg.ImMsgType;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.ResMessage;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.Codec;
import im.xgs.net.util.UUIDHexGenerator;
import im.xgs.net.util.Utility;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.PropKit;

public class MsgHandler {
//	private ChannelManager cm = ChannelInstance.getChannelManager();
//	private SelfUpDown sud = Enhancer.enhance(SelfUpDown.class);

	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 该方法是记录一个socket的连接已断开，
	 * 断开时，如果是已登陆用户，应通知该用户的其它设备，还有其好友，
	 * 群组（如果有组织机构时，则还应通知其所在的组织机构）
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void handlerRemoved(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		logger.debug("离线=============="+ctx.channel().remoteAddress());
		Channel channel = ctx.channel();
		//将给用户的session管道里相关删除
		MapChannel.instance().destroy(channel);
		channel.close();
//		String key = cm.getKey(channel.id());
//		if(key != null){
//			Map<String, Channel> channels = cm.gets(key);
//			String client = ChannelType.WEB.getName();
//			for(String type : channels.keySet()){
//				if(channels.get(type) == channel){
//					client  = type;
//					break;
//				}
//			}
//			//通知自己
//			//通知好友
//			//通知群组
//			sud.exec(isWeb, client, channel, "您在"+client+"已经离开");
//			cm.remove(key, client);
//		}
	}


	public void channelRead0(boolean isWeb, ChannelHandlerContext ctx,
			byte[] req) throws Exception {
		
		Channel incoming = ctx.channel();
		
		try {
			int len = req.length;
			logger.debug("req length:{}",req.length);
			//read length
//			byte[] len_b = new byte[4];
//			System.arraycopy(req, 0, len_b, 0, 4);
//			int len = Utility.byte2Int(len_b);
//			logger.debug("data length:{}",req.length);
			
			byte[] src=new byte[req.length];
			System.arraycopy(req, 0, src, 0, req.length);
			//解码
			byte[] dest = Codec.Decoder(src);
			byte[] cbyte = new byte[]{dest[0]};
			String client = new String(cbyte);
			
			byte[] version_b = new byte[3];
			System.arraycopy(dest, 1, version_b, 0, 3);
			String version = new String(version_b);
			
			byte[] uuid_b = new byte[32];
			System.arraycopy(dest, 4, uuid_b, 0, 32);
			String uuid = new String(uuid_b);
			
			byte[] cmd_b = new byte[2];
			System.arraycopy(dest, 36, cmd_b, 0, 2);
			short cmd = Utility.byte2Short(cmd_b);
			
			byte[] opera_b = new byte[2];
			System.arraycopy(dest, 38, opera_b, 0, 2);
			short opera = Utility.byte2Short(opera_b);
			
			byte[] msg_b = new byte[dest.length-40];
			System.arraycopy(dest, 40, msg_b, 0, dest.length-40);
			String  msg = new String(msg_b);
			
			if(PropKit.use("cnf.txt").getBoolean("isprint", false)){
				HeaderBody entity = new HeaderBody(len, client, version, uuid, cmd, opera, msg);

				logger.debug("%n=={},报文内容：{}" ,ctx.channel().remoteAddress(), JSON.toJSONString(entity));
			}
			
			
//			if("".equals(msg)){
//				return;
//			}
			Object objMsg;
			try {
				if(PropKit.use("cnf.txt").getBoolean("isprint", false)){
					logger.debug("%n=={},报体：{}" ,ctx.channel().remoteAddress(), msg);
				}
				objMsg = JSON.parseObject(msg);
			} catch (Exception e) {
				throw new ImException(PropKit.use("errcode.txt").getInt("error.format"), "消息格式错误");
//				throw new ImException(ExceptionCode.msg_code_error,"消息格式错误");
			}
			RecHandler rech = (RecHandler)Enhancer.enhance(Class.forName("im.xgs.net.protocol.rec.cmd"+cmd+".RecHandler"+opera));
			
			Object obj = rech.exec(uuid, msg, client, incoming);
			if(obj != null){
				incoming.write(obj);
			}
			incoming.flush();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//FIXME 有待修改出 错时的信息
			Map<String,Object> errorMsg = new HashMap<String,Object>();
			if(e instanceof ImException){
				errorMsg.put("code", ((ImException)e).getCode());
				errorMsg.put("msg",  ((ImException)e).getMessage());
				ByteBuf rMsg = Message.Packing(Protocol.VERSION, UUIDHexGenerator.getId(), (short)80, (short)10, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}else{
				errorMsg.put("code", 8001);
				errorMsg.put("msg",  e.getMessage());
				ByteBuf rMsg = Message.Packing(Protocol.VERSION, UUIDHexGenerator.getId(), (short)80, (short)20, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}
		}
		incoming.flush();
	}

	/**
	 * 该用户在线
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void channelActive(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()
				+ "在线\n在线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	/**
	 * 该用户下线
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void channelInactive(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()
				+ "掉线\n掉线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//FIXME session remove
	}

	/**
	 * 在处理消息时或其它地方的异常，这里根据情况进行处理，或关闭该
	 * channel，或发消息通知,etc...
	 * @param isWeb
	 * @param ctx
	 * @param cause
	 */
	public void exceptionCaught(boolean isWeb, ChannelHandlerContext ctx,
			Throwable cause) {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()+ "异常，异常原因："+cause.getMessage());
		if(cause.getClass().equals(ImException.class)){
			ImException ie = (ImException)cause;
			Object obj = new ResMessage(isWeb).setMsg(ie.getMsg()).setType(ImMsgType.msg).setMtype(ImMsgType.msgc.text).msg();
			incoming.write(obj);
			incoming.flush();
		}else{
			ctx.close();
		}
	}
}
