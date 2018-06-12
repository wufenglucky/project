package com.imooc.o2o.util;

public class PathUtil {
	//获取系统中的分隔符，由于win和Linux系统的分隔符不一样
	private static String separator = System.getProperty("file.separator");
	//返回项目图片的根路径
	public static String getImgBasePath(){
		String os = System.getProperty("os.name");//获取系统的名称
		String basePath = "";
		if(os.toLowerCase().startsWith("win")){
			basePath = "D:/Users/Administrator/Image/";
		}else {
			basePath = "/Users/Administrator/Image";
		} 
		basePath = basePath.replace("/", separator);
		return basePath;
	}
	//根据业务逻辑的不同，返回项目图片的子路径
	public static String getShopImagePath(long shopId){
		String imagePath = "/upload/images/item/shop/" + shopId + "/";
		return imagePath.replace("/", separator);
	}
}
