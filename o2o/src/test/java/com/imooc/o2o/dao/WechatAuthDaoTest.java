package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Test
	public void testAInsertWechatAuth() throws Exception{
		//增加一条微信账号
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		//给微信账号绑定上用户信息
		wechatAuth.setPersoninfo(personInfo);
		//随意的设置上openId
		wechatAuth.setOpenId("wufenglucky");
		wechatAuth.setCreateTime(new Date());
		int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1,effectedNum);
	}
	
	@Test
	public void testBQueryWechatByOpenId() throws Exception{
		WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("wufenglucky");
		assertEquals("测试", wechatAuth.getPersoninfo().getName());
		System.out.println(wechatAuth.getPersoninfo().getName());
	}
}
