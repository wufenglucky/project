package com.imooc.o2o.service;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoService {
	/**
	 * 根据用户id获取personInfo
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);
}
