package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin", method=RequestMethod.GET)
/**
 * 该函数的意义是将前端显示的url，通过后台的路由将WEB-INFO中的html文件导入到相关页面
 * @author Administrator
 *
 */
public class ShopAdminController {
	@RequestMapping(value="shopoperation")
	public String shopOperation(){
		//由于spring-web.xml文件中，已经通过视图解析器加载了路径的前缀和后缀，
		//所以我们只需返回中间部分的路径
		return "shop/shopoperation";
	}
	
	@RequestMapping(value="shoplist")
	public String shopList(){
		return "shop/shoplist";
	}
	
	@RequestMapping(value="shopmanagement")
	public String shopManagement(){
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value="productcategorymanagement", method=RequestMethod.GET)
	public String productCategoryManagement(){
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value="productoperation")
	public String productOperation(){
		return "shop/productoperation";
	}
	
	@RequestMapping(value="productmanagement")
	public String productManagement(){
		return "shop/productmanagement";
	}
}
