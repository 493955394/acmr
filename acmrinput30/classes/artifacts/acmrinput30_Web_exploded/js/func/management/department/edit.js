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

    // 编辑
    $(document).on('submit', '.J_edit_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;

        checkDelegate = new VaildNormal();
        //前端检查
        var flag;
        if (!checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'required', 'msg': '部门名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'maxlength', 'msg': '部门名称最大长度为100', 'param': 101 }])) {
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
                    common.commonTips('编辑成功');
                    var code = data.returndata.code,
                        procode = data.returndata.parent,
                        pageNum = data.param1;
                    setTimeout(function() {
                        window.location.href = common.rootPath + 'system/dep.htm?m=find&depcode=' + procode + '&selId=' + code + '&pageNum=' + pageNum;
                    }, 1000);

                } else {
                    common.commonTips('编辑失败');
                }
            },
            error: function() {
                common.commonTips('编辑失败');
            }
        })
    });


});
