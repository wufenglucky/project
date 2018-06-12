package com.imooc.o2o.util;
//验证输入的验证码与图片的验证码是否相同
import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//图片中的验证码
		String verifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//输入的验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
