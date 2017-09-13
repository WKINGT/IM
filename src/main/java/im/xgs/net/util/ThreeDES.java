package im.xgs.net.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class ThreeDES {
	private static final String IV = "1234567-";
	public static final String KEY = "uatspdbcccgame2014061800";
    /**
     * DESCBC加密
     *
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
	public static byte[] encryptDESCBC(final byte[] src,final String key) throws Exception{
		// --- 生成key ,同时制定是DES还是DESede，两者的key长度要求不同
		final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		// --- 加密向量
		final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
		
		// --- 通过Cipher执行加密得到的是一个byte的数组，Cipher.getInstance("DES")就是采用ECB模式，
		// cipher.init(Cihper.ENCRYPT_MODE,secretKey)就可以了
		final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(src);
	}
    /**
     * DESCBC解密
     *
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
	public static byte[] decryptDESCBC(final byte[] src,final String key) throws Exception{
		// --- 解密的key
		final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		
		// --- 向量
		final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
		
		// --- Cipher对象解密 Cipher.getInstance("DES/CBC/PKCS5Padding")就是采用ECB模式，
		// cipher.init(Cipher.DECRYPT_MODE,secretKey)就可以了
		final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey,iv);
		return cipher.doFinal(src);
	}
	
	/**
	 * 3DESECB 加密,
	 * @param src
	 * @param key key必须是长度大于等于 3*8 = 24 位
	 * @return
	 * @throws Exception
	 */
    public static byte[] encryptThreeDESECB(final byte[] src, final String key) throws Exception {
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        return cipher.doFinal(src);

    }

    /**
     *  3DESECB解密
     * @param src
     * @param key key必须是长度大于等于 3*8 = 24 位
     * @return
     * @throws Exception
     */
    public static byte[] decryptThreeDESECB(final byte[] src, final String key) throws Exception {
        // --解密的key
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        // --Chipher对象解密
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }

    public static void main(String[] args) throws Exception {
        final String key = "D07D6882A1E5ADB5F65544C9";
        // 加密流程
        String telePhone = "13100000000";
        byte[] telePhone_encrypt = ThreeDES.encryptThreeDESECB(telePhone.getBytes(), key);
        System.out.println(new String(telePhone_encrypt));
        System.out.println(telePhone_encrypt);// nWRVeJuoCrs8a+Ajn/3S8g==

        // 解密流程
        byte[] tele_decrypt = ThreeDES.decryptThreeDESECB(telePhone_encrypt, key);
        System.out.println("模拟代码解密:" + new String(tele_decrypt));
    }

}
