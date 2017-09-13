package im.xgs.net.msg;

import im.xgs.net.exception.ImException;
import im.xgs.net.util.Codec;
import im.xgs.net.util.Utility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
/**
 * 报文的解包与打包
 * @author TianW
 *
 */
public class Message {
	
	public static ByteBuf Packing(String version,String uuid,short cmd,short opera,Object body){
		try {
			byte[] version_b = version.getBytes();
			byte[] uuid_b = uuid.getBytes();
			byte[] cmd_b = Utility.short2Byte(cmd);
			byte[] opera_b = Utility.short2Byte(opera);
			byte[] body_b = JsonKit.toJson(body).getBytes();
			
			int length = version_b.length+uuid_b.length+cmd_b.length+opera_b.length+body_b.length;
			
			byte[] all = new byte[length];
			System.arraycopy(version_b, 0, all, 0, version_b.length);
			System.arraycopy(uuid_b, 0, all, version_b.length, uuid_b.length);
			System.arraycopy(cmd_b, 0, all, version_b.length+uuid_b.length, cmd_b.length);
			System.arraycopy(opera_b, 0, all, version_b.length+uuid_b.length+cmd_b.length, opera_b.length);
			System.arraycopy(body_b, 0, all, version_b.length+uuid_b.length+cmd_b.length+opera_b.length, body_b.length);
			//加密
			byte[] dest;
				dest = Codec.Encoder(all);
			
			int len=dest.length;
			byte[] len_b =  Utility.int2Byte(len);
			byte[] rMsg = new byte[len+4];
			System.arraycopy(len_b, 0, rMsg, 0, 4);
			System.arraycopy(dest, 0, rMsg, 4, len);
			
			return Unpooled.copiedBuffer(rMsg);
		} catch (Exception e) {
			throw new ImException(PropKit.use("errcode.txt").getInt("error.encoding"));
		}
	}
	
//	public static HeaderBody Unpacking(byte[] req){
//		try {
//
//			HeaderBody h = new HeaderBody();
//			byte[] len_b = new byte[4];
//			System.arraycopy(req, 0, len_b, 0, 4);
//			
//			h.setLen(Utility.byte2Int(len_b));
//			
//			byte[] src=new byte[req.length-4];
//			System.arraycopy(req, 4, src, 0, req.length-4);
//			//解码
//			byte[] dest = Codec.Decoder(src);
//			
//			h.setClient(new String(new byte[] {dest[0]}));
//			
//			byte[] version_b = new byte[3];
//			System.arraycopy(dest, 1, version_b, 0, 3);
//			
//			h.setVersion(new String(version_b));
//			
//			byte[] uuid_b = new byte[32];
//			System.arraycopy(dest, 4, uuid_b, 0, 32);
//			
//			h.setUuid(new String(uuid_b));
//			
//			byte[] cmd_b = new byte[2];
//			System.arraycopy(dest, 36, cmd_b, 0, 2);
//			
//			h.setCmd(Utility.byte2Short(cmd_b));
//			
//			byte[] opera_b = new byte[2];
//			System.arraycopy(dest, 38, opera_b, 0, 2);
//			
//			h.setOpera(Utility.byte2Short(opera_b));
//			
//			byte[] msg_b = new byte[dest.length-40];
//			System.arraycopy(dest, 40, msg_b, 0, dest.length-40);
//			
//			h.setMsg(new String(msg_b));
//			
//			return h;
//		} catch (Exception e) {
//			throw new ImException(PropKit.use("errcode.txt").getInt("error.decoding"));
//		}
//	}
}
