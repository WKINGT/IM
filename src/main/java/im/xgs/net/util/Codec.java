package im.xgs.net.util;

import im.xgs.net.exception.ImException;

import com.jfinal.kit.PropKit;

/**
 * 报文加密解密
 * @author TianW
 *
 */

public class Codec {
	
	public static byte[] Encoder(byte[] src) throws ImException {
		try {
			return ThreeDES.encryptThreeDESECB(src, PropKit.use("cnf.txt").get("3des.key"));
		} catch (Exception e) {
			throw new ImException(PropKit.use("errcode.txt").getInt("error.encoding"));
		}
	}
	
	public static byte[] Decoder(byte[] src) throws ImException {
		try {
			return ThreeDES.decryptThreeDESECB(src, PropKit.use("cnf.txt").get("3des.key"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ImException(PropKit.use("errcode.txt").getInt("error.decoding"));
		}
	}
}
