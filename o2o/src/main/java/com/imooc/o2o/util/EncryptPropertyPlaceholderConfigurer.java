package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	//需要加密的字段数组
	private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
	
	/**
	 * 对关键的属性进行转换
	 */
	protected String convertProperty(String propertyName, String propertyValue){
		if(isEncryptProp(propertyName)){
			//对已经加密的字段进行解密工作
			String decryptValue = DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		}else {
			return propertyValue;
		}
	}

	/**
	 * 该属性是否已经加密
	 * @param propertyName
	 * @return
	 */
	private boolean isEncryptProp(String propertyName) {
		//遍历字符串数组，若等于需要加密的filed，则已进行加密
		for(String encryptpropertyNameString : encryptPropNames){
			if(encryptpropertyNameString.equals(propertyName)){
				return true;
			}
		}
		return false;
	}
}
