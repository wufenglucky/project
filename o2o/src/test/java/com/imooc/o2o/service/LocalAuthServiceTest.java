package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.LocalAuthExcution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.WechatAuthStateEnum;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest  extends BaseTest{
	@Autowired
	private LocalAuthService localAuthService;
	
	@Test
	@Ignore
	public void testABindLocalAuth(){
		//新增一条平台账号
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		//给用户设置上用户id，表明某个用户创建的账号
		personInfo.setUserId(1L);
		
		String username = "testusername";
		String password = "testpassword";
		//给平台账号设置用户信息
		localAuth.setPersoninfo(personInfo);
		//设置账号
		localAuth.setUsername(username);
		//设置上密码
		localAuth.setPassword(password);
		//绑定账号
		LocalAuthExcution lae = localAuthService.bindLocalAuth(localAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
		//通过userId找到新增的localAuth
		localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		//打印用户的名字和账号密码，看看跟预期是否一样
		System.out.println("用户昵称：" + localAuth.getPersoninfo().getName());
		System.out.println("平台账号密码：" + localAuth.getPassword());
	}
	
	@Test
	public void testBModifyLocalAuth(){
		//设置账号信息
		long userId = 1;
		String username = "testusername";
		String password = "testpassword";
		String newPassword = "newpassword";
		//修改该账号对应的密码
		LocalAuthExcution lae = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
		//通过账号和密码找到修改后的localAuth
		LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
		//打印用户名字看看跟预期是否相符
		System.out.println(localAuth.getPersoninfo().getName());
	}
	
}
