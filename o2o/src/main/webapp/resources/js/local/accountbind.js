$(function() {
	// 绑定账号的controller url
	var bindUrl = "/o2o/local/bindlocalauth";
	// 从地址栏的URL里获取usertype
	// usertype=1，则为前端展示系统，其余为店家管理系统
	var usertype = getQueryString('usertype');
	$('#submit').click(function() {
		// 获取输入的账号
		var userName = $('#username').val();
		// 获取输入密码
		var password = $('#psw').val();
		// 获取输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码!');
			return;
		}
		// 访问后台，绑定账号
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('绑定成功');
					if (usertype == 1) {
						window.location.href = '/o2o/frontend/index';
					} else {
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('提交失败！' + data.errMsg());
					$('#captcha_img').click();
				}
			}
		});
	});

});