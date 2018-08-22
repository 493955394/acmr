define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common');
    // 编辑
    $(document).on('submit', '.J_edit_form', function(event) {
        event.preventDefault();
        var checkDelegate;
        //检查code
        checkDelegate = new VaildNormal();
        var flag = true;

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

        var self = this,
            currentUrl = $(self).prop('action');
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    common.commonTips('编辑成功');
                    setTimeout(function() {
                        window.location.href = common.rootPath + 'system/role.htm?m=find&pageNum=' + data.returndata + '&selId=' + data.param1;
                    }, 1000);
                } else {
                    common.commonTips('编辑角色信息信息出错');
                }
            },
            error: function() {
                common.commonTips('编辑角色信息信息出错');
            }
        })
    });
    $(document).on('click', '.J_back', function(event) {
        event.preventDefault();
        parent.location.href = common.rootPath + 'system/role.htm';
    })
});
