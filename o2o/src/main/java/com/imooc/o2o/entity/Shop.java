package com.imooc.o2o.entity;
//店铺的实体类

import java.util.Date;

public class Shop {
	private Long shopId;//店铺ID
	private String shopName;//店铺的名字
	private String shopDesc;//店铺的描述
	private String shopAddr;//店铺的地址
	private String phone;//店铺的联系电话
	private String shopImg;//店铺的图片
	private Integer priority;//店铺的优先级
	private Date createTime;//店铺的创建时间
	private Date lastEditTime;//店铺的最后修改时间
	//-1.不可用   0.审核中    1.可用
	private Integer enableStatus;//店铺状态
	//超级管理员给店家的建议和提醒
	private String advice;
	private Area area;//表示店铺属于哪一块区域
	private PersonInfo owner;//表示店铺是由谁创建的
	private ShopCategory shopCategory;//表示店铺属于哪一个列表
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
	public String getShopAddr() {
		return shopAddr;
	}
	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
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
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public PersonInfo getOwner() {
		return owner;
	}
	public void setOwner(PersonInfo owner) {
		this.owner = owner;
	}
	public ShopCategory getShopCategory() {
		return shopCategory;
	}
	public void setShopCategory(ShopCategory shopCategory) {
		this.shopCategory = shopCategory;
	}
	
}
