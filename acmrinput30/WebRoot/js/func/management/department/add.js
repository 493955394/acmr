define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common'),
        pjax = require('pjax');

    // 新增
    $(document).on('submit', '.J_add_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;

        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'required', 'msg': '部门编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'ch', 'msg': '部门代码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="code"]'), [{ 'name': 'maxlength', 'msg': '部门编码最大长度为20', 'param': 21 }])) {
            flag = false;
        } else if (!checkCode($('input[name="code"]').val(), checkDelegate)) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="name"]'), [{ 'name': 'required', 'msg': '部门名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="name"]'), [{ 'name': 'maxlength', 'msg': '部门名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="memo"]'), [{ 'name': 'maxlength', 'msg': '备注最大长度为1000', 'param': 1001 }])) {
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
                    common.commonTips('添加成功');
                    var code = data.returndata.code || '',
                        procode = data.returndata.parent || '',
                        pageNum = data.param1 || '';
                    setTimeout(function() {
                        window.location.href = common.rootPath + 'system/dep.htm?m=find&depcode=' + procode + '&selId=' + code + '&pageNum=' + pageNum;
                    }, 1000);
                } else if (data.returncode == 300) {
                    common.commonTips('添加失败');
                } else {
                    common.commonTips('添加失败');
                }
            },
            error: function() {
                common.commonTips('添加失败');

            }
        })
    });

    function checkCode(code, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'system/dep.htm?m=checkCode',
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
                    checkDelegate.viewTipAjax($('input[name="code"]'), false, "该机构代码已存在");
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
});
