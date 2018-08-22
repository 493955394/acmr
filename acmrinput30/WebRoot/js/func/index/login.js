define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        common = require('common'),
        flag = false, //判断是否是支持的浏览器
        doc = $(document);

    //判断浏览器是否支持
    $(document).ready(function() {
        isversion();
        if (flag == false) {
            window.location.href = common.rootPath + "login.htm?m=toBrower";
        }
    });

    doc.on('submit', '.J_login_form', function(event) {
        isversion();
        var self = this,
            reqestUrl = $(self).prop('action'),
            checkDelegate;
        //检查ucode
        checkDelegate = new VaildNormal();
        if (!(checkDelegate.checkNormal($('input[name="email"]'), [{ 'name': 'required', 'msg': '用户邮箱不能为空' }])) ||
            !(checkDelegate.checkNormal($('input[name="pwd"]'), [{ 'name': 'required', 'msg': '密码不能为空' }]))) {
            event.preventDefault();
            return false;
        }
        //var screen=window.screen.height+"*"+window.screen.width;
        /*$.ajax({
        	url: reqestUrl,
        	timeout: 20000,
        	type: 'post',
        	data: $(self).serialize()+'&screen='+screen,
        	dataType: 'json',
        	success: function(data) {
        		if(data.returncode == 200){
        			window.location.href= common.rootPath+data.returndata;
        		} else {
        			$("#msg").html(data.returndata);
        		}
        	},
        	error: function(e) {

        	}
        })*/
    });

    function isversion() {
        var bro = $.browser;
        if (bro.msie) { //IE浏览器
            if (parseInt(bro.version, 10) >= 9) { //只支持IE9及以上版本
                flag = true;
            } else {
                flag = false;
            }
        } else if (bro.mozilla) { //火狐浏览器
            flag = true;
        } else if (bro.chrome) { //谷歌浏览器
            flag = true;
        } else {
            flag = false;
        }
    }
});
