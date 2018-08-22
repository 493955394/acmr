define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common');
    var delMenuNodes = [],
        insertMenuNodes = [],
        Pagination = require('pagination'),
        menutree = '';



    // 新增
    $(document).on('submit', '.J_add_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        //检查code
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'required', 'msg': '角色代码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'ch', 'msg': '角色代码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'maxlength', 'msg': '角色代码最大长度为20', 'param': 21 }])) {
            flag = false;
        } else if (!checkCode($('input[name="code"]').val(), checkDelegate)) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'required', 'msg': '角色名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'maxlength', 'msg': '角色名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="memo"]'), [{ 'name': 'maxlength', 'msg': '角色名称最大长度为1000', 'param': 1001 }])) {
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
                        window.location.href = common.rootPath + 'system/role.htm?m=find&pageNum=' + data.returndata + '&selId=' + data.param1;
                    }, 1000);
                } else {
                    common.commonTips('插入角色信息出错');
                }
            },
            error: function() {

                common.commonTips('插入角色信息出错');
            }
        })
    });

    function checkCode(code, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=checkCode',
            timeout: 5000,
            type: 'post',
            async: false,
            data: 'code=' + code,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="code"]'), true);
                    flag = true;
                } else {
                    checkDelegate.viewTipAjax($('input[name="code"]'), false, "该用户代码已存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    $(document).on('blur', 'input[name="code"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkCode($('input[name="code"]').val(), checkDelegate);
    });
    $(document).on('click', '.J_back', function(event) {
        event.preventDefault();
        parent.location.href = common.rootPath + 'system/role.htm';
    })

});
