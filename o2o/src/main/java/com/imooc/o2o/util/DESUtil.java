package com.imooc.o2o.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同秘钥的算法
 * @author Administrator
 *
 */
public class DESUtil {
	private static Key key;
	//设置密钥key
	private static String KEY_STR = "mykey";
	private static String CHARSETNAME = "UTF-8";
	private static String ALGORITHM = "DES";
	//静态代码块来生成我们算法的实例
	static{
		try {
			//生成DES算法对象
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			//运用SHA1安全策略
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			//设置上密钥种子
			secureRandom.setSeed(KEY_STR.getBytes());
			//初始化基于SHA1的算法对象
			generator.init(secureRandom);
			//生成秘钥对象
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 获取加密后的信息
	 * @param str
	 * @return
	 */
	public static String getEncryptString(String str){
		//基于BASE64编码，接收byte[]，并转换成String
		BASE64Encoder base64Encoder = new BASE64Encoder();
		try {
			//按UTF-8编码
			byte[] bytes = str.getBytes(CHARSETNAME);
			//获取加密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//初始化密码信息
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//加密
			byte[] doFinal = cipher.doFinal(bytes);
			//byte[] to encode 好的String 并返回
			return base64Encoder.encode(doFinal);	
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 获取解密后的信息
	 * @param str
	 * @return
	 */
	public static String getDecryptString(String str){
		//基于BASE64编码，接收byte[]，并成功转换String
		BASE64Decoder base64Decoder = new BASE64Decoder();
		try {
			//将字符串decode转换成byte[]
			byte[] bytes = base64Decoder.decodeBuffer(str);
			//获取解密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//初始化解密信息
			cipher.init(Cipher.DECRYPT_MODE, key);
			//解密
			byte[] doFinal = cipher.doFinal(bytes);
			//返回解密之后的信息
			return new String(doFinal, CHARSETNAME);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public static void main(String[] args){
		System.out.println(getEncryptString("root"));
		System.out.println(getEncryptString("123456"));
	}
}
