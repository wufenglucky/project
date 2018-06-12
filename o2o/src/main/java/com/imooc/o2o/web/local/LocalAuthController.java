package com.imooc.o2o.web.local;
//还没有验证

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.LocalAuthExcution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exception.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "local", method = { RequestMethod.GET, RequestMethod.POST })
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;

	/**
	 * 将用户信息与平台账号绑定
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取输入的账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 从session中获取当前用户信息(一旦用户通过微信登录之后，便能获得用户信息)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断，要求账号密码以及当前的用户session非空
		if (userName != null && password != null && user.getUserId() != null) {
			// 创建localAuth对象，并赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(userName);
			localAuth.setPassword(password);
			localAuth.setPersoninfo(user);
			// 绑定账号
			LocalAuthExcution lae = localAuthService.bindLocalAuth(localAuth);
			if (lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", lae.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpsw", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> changeLocalPsw(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 校验验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取原密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 获取新的密码
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		// 从session中获取当前用户信息，用户一旦通过微信登陆之后，便能获取到用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断，要求账号新旧密码以及当前的用户session不为空，且新旧密码不相同
		if (userName != null && password != null && user != null && user.getUserId() != null
				&& password != newPassword) {
			try {
				// 查看原先账号，看看与输入的账号是否一致，不一致则认为是非法操作
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				if (localAuth == null || !localAuth.getUsername().equals(userName)) {
					// 不一致，则退出
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入账号非本次登录的账号");
					return modelMap;
				}
				// 修改平台账号的用户密码
				LocalAuthExcution lae = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
						newPassword);
				if(lae.getState() == LocalAuthStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", lae.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}
		return modelMap;
	}
	
	/**
	 * 验证登录信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logincheck", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取是否需要进行验证码检验的标识符
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(needVerify && !CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", true);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//获取输入的账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		//获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//非空校验
		if(userName != null && password != null){
			//传入账号和密码去获取平台账号的信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if(localAuth != null){
				//若能取到账号，则成功登录
				modelMap.put("success", true);
				//同时在session中设置用户信息
				request.getSession().setAttribute("user", localAuth.getPersoninfo());
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或者密码错误");
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名或者密码不能为空");
		}
		return modelMap;
	}
	
	/**
	 * 当用户点击登出按钮的时候，注销session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//设置session为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
