package com.imooc.o2o.enums;

public enum WechatAuthStateEnum {
	LOGINFALL(-1,"openId输入有误"), SUCCESS(0,"操作成功"), NULL_AUTH_INFO(-1006,"注册信息为空");
	
	private int state;
	
	private String stateInfo;
	
	private WechatAuthStateEnum(int state, String stateInfo){
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public static WechatAuthStateEnum stateOf(int index){
		for(WechatAuthStateEnum state : values()){
			if(state.getState() == index)
				return state;
		}
		return null;
	}
}
