package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExcution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exception.LocalAuthOperationException;

public interface LocalAuthService {
	/**
	 * 通过账号和密码获取平台账户信息
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

	/**
	 * 通过userId获取平台账户信息
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 绑定微信账号，生成平台专属账号
	 * 
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExcution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * 修改平台账号的密码
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newpassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExcution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;
}
