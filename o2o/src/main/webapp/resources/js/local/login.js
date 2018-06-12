$(function() {
	// 登录验证的controller url
	var loginUrl = '/o2o/local/logincheck';
	// 从地址栏的URL里获取shopowner
	// usertype=1则为customer，其余为shopowner
	var usertype = getQueryString('usertype');
	// 登陆次数，累计登录三次失败后，自动弹出验证码收入要求
	var loginCount = 0;
	$('#submit').click(function() {
		// 获取输入账号
		var userName = $('#username').val();
		// 获取输入密码
		var password = $('#psw').val();
		// 获取验证信息
		var verifyCodeActual = $('#j_captcha').val();
		// 是否需要验证验证码，默认为false
		var needVerify = false;
		// 如果三次登录失败
		if (loginCount >= 3) {
			// 那么需要验证码校验了
			if (!verifyCodeActual) {
				$.toast('请输入验证码！');
				return;
			} else {
				needVerify = true;
			}
		}
		// 访问后台，进行登录验证
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual,
				needVerify : needVerify
			},
			success : function(data) {
				if (data.success) {
					$.toast('登陆成功！');
					if (usertype == 1) {
						// 若用户在前端展示系统页面，则自动连接到前端展示系统首页
						window.location.href = '/o2o/frontend/index';
					} else {
						// 若用户是在店家管理系统页面，则自动连接到店铺列表页面中
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('登录失败！' + data.errMsg);
					loginCount++;
					if (loginCount >= 3) {
						// 登录失败三次，需要做验证码校验
						$('#verifyPart').show();
					}
				}
			}
		});
	});
});