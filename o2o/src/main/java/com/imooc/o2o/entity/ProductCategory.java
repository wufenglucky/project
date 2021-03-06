package com.imooc.o2o.entity;
//商品类别实体类

import java.util.Date;

public class ProductCategory {
	private Long productCategoryId;//商品类别编号ID
	private Long shopId;//店铺ID
	private String productCategoryName;//商品类别名称
	private Integer priority;//商品类别的权重
	private Date createTime;//商品类别的创建时间
	
	public Long getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getProductCategoryName() {
		return productCategoryName;
	}
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
