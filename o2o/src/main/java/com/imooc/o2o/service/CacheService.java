package com.imooc.o2o.service;


public interface CacheService {
	
	/**
	 * 依据key的前缀删除匹配该模式下的所有的key-value，如传入：shopCategory,则shopCategory_allfirstlevel等
	 * 以shopCategory开头的key-value都会被清空
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
