package com.imooc.o2o.entity;
//店铺类别的实体类

import java.util.Date;

public class ShopCategory {
	private Long shopCategoryId;//店铺类别ID
	private String shopCategoryName;//店铺类别名称
	private String shopCategoryDesc;//店铺类别的描述
	private String shopCategoryImg;//店铺类别的图片
	private Integer priority;//店铺类别的权重
	private Date createTime;//店铺类别的创建时间
	private Date lastEditTime;//店铺类别的最后修改时间
	private ShopCategory parent;//店铺类别的上级ID
	
	public Long getShopCategoryId() {
		return shopCategoryId;
	}
	public void setShopCategoryId(Long shopCategoryId) {
		this.shopCategoryId = shopCategoryId;
	}
	public String getShopCategoryName() {
		return shopCategoryName;
	}
	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}
	public String getShopCategoryDesc() {
		return shopCategoryDesc;
	}
	public void setShopCategoryDesc(String shopCategoryDesc) {
		this.shopCategoryDesc = shopCategoryDesc;
	}
	public String getShopCategoryImg() {
		return shopCategoryImg;
	}
	public void setShopCategoryImg(String shopCategoryImg) {
		this.shopCategoryImg = shopCategoryImg;
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
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public ShopCategory getParent() {
		return parent;
	}
	public void setParent(ShopCategory parent) {
		this.parent = parent;
	}
	
}
