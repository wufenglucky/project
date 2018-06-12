package com.imooc.o2o.util;

public class PageCalculator {
	/**
	 * 将页数转换为行数
	 * @param pageIndex 页数
	 * @param pageSize 一页包含的行数
	 * @return
	 */
	public static int  calculatorRowIndex(int pageIndex, int pageSize){
		return (pageIndex > 0) ? (pageIndex-1) * pageSize : 0;
	}
}
