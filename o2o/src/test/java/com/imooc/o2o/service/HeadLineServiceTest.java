package com.imooc.o2o.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;

public class HeadLineServiceTest extends BaseTest{
	@Autowired
	private HeadLineService headLineService;
	
	@Test
	public void testGetHeadLineList() throws Exception{
		List<HeadLine> headLineList = headLineService.getHeadLineList(new HeadLine());
		System.out.println(headLineList.get(0).getLineName());
		
		HeadLine headLineCondition = new HeadLine();
		headLineCondition.setEnableStatus(0);
		headLineList = headLineService.getHeadLineList(headLineCondition);
		System.out.println(headLineList.get(0).getLineName());
	}
}
