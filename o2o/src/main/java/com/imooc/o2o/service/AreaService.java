package com.imooc.o2o.service;
//验证service

import java.util.List;

import com.imooc.o2o.entity.Area;

public interface AreaService {
	public static final String AREALISTKEY = "arealist";
	List<Area> getAreaList();
}
