define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common'),
        Pagination = require('pagination');

    /**
     * 新增
     */
    $(document).on('submit', '.J_add_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        //检查code
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="userid"]'), [{ 'name': 'required', 'msg': '用户代码不能为空' }]) ||
                !checkDelegate.checkNormal($('input[name="userid"]'), [{ 'name': 'maxlength', 'msg': '用户代码最大长度不能超过20', 'param': 21 }]) ||
                !checkDelegate.checkNormal($('input[name="userid"]'), [{ 'name': 'minlength', 'msg': '用户代码最小长度为8', 'param': 7 }]) ||
            !checkDelegate.checkNormal($('input[name="userid"]'), [{ 'name': 'ch', 'msg': '用户代码不能包含汉字' }])) {
            flag = false;
        } else if (!checkCode($('input[name="userid"]').val(), checkDelegate)) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="username"]'), [{ 'name': 'required', 'msg': '用户姓名不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="username"]'), [{ 'name': 'maxlength', 'msg': '用户姓名最多为10位', 'param': 11 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="pwd"]'), [{ 'name': 'required', 'msg': '密码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="pwd"]'), [{ 'name': 'minlength', 'msg': '密码最少为6位', 'param': 5 }]) ||
            !checkDelegate.checkNormal($('input[name="pwd"]'), [{ 'name': 'maxlength', 'msg': '密码最多为20位', 'param': 21 }]) ||
            !checkDelegate.checkNormal($('input[name="pwd"]'), [{ 'name': 'equal', 'msg': '两次输入密码不一致', 'param': $('input[name="ppwd"]').val() }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="ppwd"]'), [{ 'name': 'required', 'msg': '确认密码不能为空' }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="email"]'), [{ 'name': 'required', 'msg': '电子邮件不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="email"]'), [{ 'name': 'email', 'msg': '电子邮件格式不正确(@前面不能少于四位字符)' }])) {
            flag = false;
        } else if (!checkEmail('userid=' + $('input[name="userid"]').val() + '&email=' + $('input[name="email"]').val(), checkDelegate)) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="tel"]'), [{ 'name': 'maxlength', 'msg': '联系电话最多为30位', 'param': 31 }])) {
            flag = false;
        }
        //电话号码校验
        if (!$('input[name="tel"]').val() == '') {
            if ((checkDelegate.checkNormal($('input[name="tel"]'), [{ 'name': 'tele', 'msg': '' }]) == false) &&
                (checkDelegate.checkNormal($('input[name="tel"]'), [{ 'name': 'mobile', 'msg': '' }]) == false)) {
                flag = false;
                checkDelegate.checkNormal($('input[name="tel"]'), [{ 'name': 'mobile', 'msg': '电话格式不正确' }]);
            }
        }
        if (!checkDelegate.checkNormal($('input[name="duty"]'), [{ 'name': 'maxlength', 'msg': '职位最多为20位', 'param': 21 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    common.commonTips('保存成功');
                    setTimeout(function() {
                        window.location.href = common.rootPath + 'system/person.htm?m=find&depcode=' + data.returndata.depcode + '&selId=' + data.returndata.userId + '&pageNum=' + data.param1;
                    }, 1000);
                } else {
                    common.commonTips('保存失败');
                }
            }
        })
    });
    //---------------------新增 后台校验----------------------
    /**
     * 用户Id是否已存在
     */
    function checkCode(userId, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=checkUserId',
            timeout: 5000,
            type: 'post',
            async: false,
            data: 'userId=' + userId,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="userid"]'), true);
                    flag = true;
                } else {
                    checkDelegate.viewTipAjax($('input[name="userid"]'), false, "该用户代码已存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    /**
     * 邮箱是否已存在
     */
    function checkEmail(data, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=checkEmail',
            timeout: 5000,
            type: 'post',
            async: false,
            data: data,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="email"]'), true);
                    flag = true;
                } else {
                    checkDelegate.viewTipAjax($('input[name="email"]'), false, "该邮箱已经存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    /**
     * 鼠标焦点离开验证
     */
    $(document).on('blur', 'input[name="userId"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkCode($('input[name="userId"]').val(), checkDelegate);
    });
    $(document).on('blur', 'input[name="email"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkEmail('userId=' + $('input[name="userId"]').val() + '&email=' + $('input[name="email"]').val(), checkDelegate);
    });


});
