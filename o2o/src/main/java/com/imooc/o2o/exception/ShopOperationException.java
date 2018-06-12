package com.imooc.o2o.exception;
//对RuntimeException进行封装 
public class ShopOperationException extends RuntimeException {
	/**
	 * 该类继承RuntimeException可以实现回滚效果(也就是当抛出错误时，不往数据库中插入记录)
	 * 如果该类继承Exception，则一定会往数据库中插入记录
	 */
	private static final long serialVersionUID=2361446884822298905L;
	
	public ShopOperationException(String msg){
		super(msg);
	}
}
