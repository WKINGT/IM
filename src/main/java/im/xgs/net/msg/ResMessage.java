package im.xgs.net.msg;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import com.alibaba.fastjson.JSON;

public class ResMessage {

	private int type;
	private int mtype;
	private Object msg;
	
	private boolean isWeb;
	
	public ResMessage(boolean isWeb){
		this.isWeb = isWeb;
	}
	
	public int getType() {
		return type;
	}
	public ResMessage setType(int type) {
		this.type = type;
		return this;
	}
	public int getMtype() {
		return mtype;
	}
	public ResMessage setMtype(int mtype) {
		this.mtype = mtype;
		return this;
	}
	public Object getMsg() {
		return msg;
	}
	public ResMessage setMsg(Object msg) {
		this.msg = msg;
		return this;
	}
	
	public Object msg(){
		String s = this.toString();
		System.out.println(s);
		if (isWeb) {
			return new TextWebSocketFrame(s);
		} else {
			return Unpooled.copiedBuffer(s.getBytes());
		}
	}
	
	public String toString(){
		if(type == 0 || mtype == 0){
			 return null;
		}
		return Integer.toHexString(this.type)+Integer.toHexString(this.mtype)+JSON.toJSONString(this.msg);
	}
}
