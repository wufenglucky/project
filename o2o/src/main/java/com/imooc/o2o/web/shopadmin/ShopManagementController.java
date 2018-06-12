package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired 
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@RequestMapping(value="/getshopmanagementinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0){
			//如果前端没有传入shopId，那我们就从session中获取
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj == null){
				//如果仍然获取不到，则重定向回到/o2o/shop/shoplist
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getshoplist", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("test");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errNsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="/modifyshop", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//检查验证码是否正确
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1、接收并转化相应的参数，包括店铺信息及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//创建一个mapper对象
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Shop shop = null;
		try {
			//通过jackson-databind中的方法，将shopStr所在的类对象返回
			//并存放在Shop类的shop对象中
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//接收图片
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//通过multipartHttpServletRequest的getFile()方法，找到shopImg的文件路径
			//然后，将该路径强制转换为CommonsMultipartFile文件流,该文件流可以被spring处理
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//2、修改店铺信息
		if(shop != null && shop.getShopId() != null){
//			File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
//			shopImgFile.createNewFile(); 
//			inputStreamToFile(shopImg.getInputStream(), shopImgFile);
			ImageHolder imageHolder;
			ShopExecution se;
			try {
				if(shopImg == null){
					se = shopService.modifyShop(shop, null);
				}else {
					imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if(se.getState() == ShopStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errmsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errmsg", "请输入店铺Id");
			return modelMap;
		}
	}
	
	@RequestMapping(value="/getshopbyid", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		//该方法通过店铺Id获取了店铺的信息以及区域信息列表
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取店铺Id
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1){
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="/registershop", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//检查验证码是否正确
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1、接收并转化相应的参数，包括店铺信息及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//创建一个mapper对象
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Shop shop = null;
		try {
			//通过jackson-databind中的方法，将shopStr所在的类对象返回
			//并存放在Shop类的shop对象中
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//接收图片
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//通过multipartHttpServletRequest的getFile()方法，找到shopImg的文件路径
			//然后，将该路径强制转换为CommonsMultipartFile文件流,该文件流可以被spring处理
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errmsg", "上传图片不能为空");
			return modelMap;
		}
		//2、注册店铺
		if(shop != null && shopImg != null){
			//通过session获取用户登录后的信息
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
//			PersonInfo owner = new PersonInfo();
//			owner.setUserId(1L);
			shop.setOwner(owner);
//			File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
//			shopImgFile.createNewFile();
//			inputStreamToFile(shopImg.getInputStream(), shopImgFile);
			ImageHolder imageHolder;
			try {
				imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				ShopExecution se = shopService.addShop(shop, imageHolder);
				if(se.getState() == ShopStateEnum.CHECK.getState()){
					modelMap.put("success", true);
					//该用户可以操作的列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size() == 0){
						shopList = new ArrayList<Shop>();	
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errmsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errmsg", "请输入店铺信息");
			return modelMap;
		}
		//3、返回结果
	}
	
//	private static void inputStreamToFile(InputStream ins, File file){
//		FileOutputStream os = null;
//		try {
//			os = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[1024];
//			while((bytesRead = ins.read(buffer)) != -1){
//				os.write(buffer,0,bytesRead);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("调用inputStreamToFile 产生异常：" + e.getMessage());
//		}finally {
//			try {
//				if(os != null){
//					os.close();
//				}
//				if(ins != null){
//					ins.close();
//				}
//			} catch (IOException e2) {
//				throw new RuntimeException("inputStreamToFile关闭io产生异常" + e2.getMessage());
//			}
//		}
//	}
}
