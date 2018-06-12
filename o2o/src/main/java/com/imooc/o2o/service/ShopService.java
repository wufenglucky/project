package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exception.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应的店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	/**
	 * 通过店铺Id获取店铺信息
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);

	/**
	 * 更新图片信息，包括对图片的处理
	 * 
	 * @param shop
	 * @param imageHolder
	 * @return
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

	/**
	 * 注册店铺信息，包括图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 */
	ShopExecution addShop(Shop shop, ImageHolder shopImg) throws ShopOperationException;
}
