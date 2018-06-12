package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest{
	@Autowired
	private LocalAuthDao localAuthDao;
	
	private static final String username = "testusername";
	private static final String password = "testpassword";

	@Test
	public void testAInsertLocalAuth() throws Exception {
		// 新增一条平台信息
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		// 给平台账户绑定上用户信息
		localAuth.setPersoninfo(personInfo);
		// 设置上用户名和密码
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());

		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryLocalAuthByUserNameAndPassword() throws Exception {
		// 按照账号和密码查询用户信息
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
		assertEquals("测试", localAuth.getPersoninfo().getName());
		System.out.println(localAuth.getPersoninfo().getName());
	}

	@Test
	public void testCQueryLocalAuthByUserId() throws Exception {
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		assertEquals("测试", localAuth.getPersoninfo().getName());
		System.out.println(localAuth.getPersoninfo().getName());
	}

	@Test
	public void testDUpdateLocalAuth() throws Exception {
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, password + "new", now);
		assertEquals(1, effectedNum);
		//查询该条平台账号的最新信息
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		System.out.println(localAuth.getPassword());
	}
}
