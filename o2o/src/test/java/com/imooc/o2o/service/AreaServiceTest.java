package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;

public class AreaServiceTest extends BaseTest {
	@Autowired
	private AreaService areaService;
	@Autowired
	private CacheService cacheService;
		
	@Test
	public void testGetAreaList(){
		List<Area> areaList =areaService.getAreaList();
		if(areaList!=null && areaList.size() >0){
			assertEquals("西苑",areaList.get(0).getAreaName());
			System.out.println(areaList.get(0).getAreaName());
			cacheService.removeFromCache(AreaService.AREALISTKEY);
			areaList =areaService.getAreaList();
		}
	}
}
