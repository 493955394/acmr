define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common');

    $(document).on('click', '.J_back', function(event) {
        event.preventDefault();
        var self = this;
        $('.J_submit').hide();
        $('.J_reset').hide();
        $('.J_edit_form')[0].reset();
        $(self).hide();
        $('input[name=cname]').attr('disabled', true);
        $('input[name=email]').attr('disabled', true);
        $('input[name=tel]').attr('disabled', true);
        $('input[name=duty]').attr('disabled', true);
    });
    $(document).on('click', '.J_edit', function(event) {
        event.preventDefault();
        var self = this;
        $('input[name=cname]').attr('disabled', false);
        $('input[name=email]').attr('disabled', false);
        $('input[name=tel]').attr('disabled', false);
        $('input[name=duty]').attr('disabled', false);
        $('.J_submit').show();
        $('.J_reset').show();
        $('.J_back').show();

    });
    $(document).on('submit', '.J_edit_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('action'),
            checkDelegate;
        //检查code
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'required', 'msg': '用户姓名不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cname"]'), [{ 'name': 'maxlength', 'msg': '用户姓名最多为10位', 'param': 11 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="email"]'), [{ 'name': 'required', 'msg': '电子邮件不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="email"]'), [{ 'name': 'email', 'msg': '电子邮件格式不正确' }])) {
            flag = false;
        } else if (!checkEmail('userId=' + $('input[name="userId"]').val() + '&email=' + $('input[name="email"]').val(), checkDelegate)) {
            flag = false;
        }
        //电话号码校验
        if (!checkDelegate.checkNormal($('input[name="tel"]'), [{ 'name': 'maxlength', 'msg': '联系电话最多为30位', 'param': 31 }])) {
            flag = false;
        }
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
                    $('input[name=cname]').attr('disabled', true);
                    $('input[name=email]').attr('disabled', true);
                    $('input[name=tel]').attr('disabled', true);
                    $('input[name=duty]').attr('disabled', true);
                    $('.J_submit').hide();
                    $('.J_reset').hide();
                    $('.J_back').hide();

                } else {
                    common.commonTips('提交人员信息出错');
                }
            },
            error: function() {
                common.commonTips('提交人员信息出错');

            }
        })

    });
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
    $(document).on('blur', 'input[name="email"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkEmail('userId=' + $('input[name="userId"]').val() + '&email=' + $('input[name="email"]').val(), checkDelegate);
    });

    function buildModelHTML(targetName, targetContent) {
        var len = $('#' + targetName).length,
            tempHTML;
        if (len === 0) {
            tempHTML = '<div class="modal fade" id="' + targetName + '" role="dialog"><div class="modal-dialog"><div class="modal-content">' + targetContent + '</div></div></div>';
            $('body').append(tempHTML);
        }
    }
    /**
     * 重置密码
     */
    $(document).on('click', '.J_opr_pwd', function(event) {
        event.preventDefault();
        var self = this,
            currentId = $('input[name=userId]').val(),
            modalContent = '';
        if ($('#pwd_model').length > 0) {
            $('#pwd_model').remove();
        }
        modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title" id="myModalLabel">重置密码</h4>';
        modalContent += '</div>';
        modalContent += '<form class="form-horizontal J_pwd_form">';
        modalContent += '<div class="modal-body">';
        modalContent += '<input type="hidden" name="userId" value="' + currentId + '"/>';
        modalContent += '<div class="form-group">';
        modalContent += '<label class="col-sm-2 control-label">原密码</label>';
        modalContent += '<div class="col-sm-6"><input type="password" class="form-control" name="oldPwd"></div>';
        modalContent += '</div>'
        modalContent += '<div class="form-group">';
        modalContent += '<label class="col-sm-2 control-label">新密码</label>';
        modalContent += '<div class="col-sm-6"><input type="password" class="form-control" name="newPwd"></div>';
        modalContent += '</div>'
        modalContent += '<div class="form-group">';
        modalContent += '<label class="col-sm-2 control-label">确认新密码</label>';
        modalContent += '<div class="col-sm-6"><input type="password" class="form-control" name="newPPwd"></div>';
        modalContent += '</div>'
        modalContent += '</div>';
        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="submit" class="btn btn-primary">保存</button><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
        modalContent += '</div>';
        modalContent += '</form>';
        buildModelHTML('pwd_model', modalContent);
        $('#pwd_model').modal('show');
    });
    $(document).on('submit', '.J_pwd_form', function(event) {
        event.preventDefault();
        var self = this,
            checkDelegate,
            userId = $('input[name=userId]').val(),
            oldPwd = $('input[name=oldPwd]').val();
        //检查code
        checkDelegate = new VaildNormal();
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=checkPwd',
            data: 'userId=' + userId + '&oldPwd=' + oldPwd,
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            success: function(data) {
                if (data.returndata == "false") {
                    checkDelegate.viewTipAjax($('input[name="oldPwd"]'), false, "原密码错误");
                    return;
                }
            }
        });

        if (!checkDelegate.checkNormal($('input[name="newPwd"]'), [{
                'name': 'required',
                'msg': '密码不能为空'
            }]) ||
            !checkDelegate.checkNormal($('input[name="newPwd"]'), [{
                'name': 'minlength',
                'msg': '密码最少为6位',
                'param': 5
            }]) ||
            !checkDelegate.checkNormal($('input[name="newPwd"]'), [{
                'name': 'maxlength',
                'msg': '密码最多为20位',
                'param': 21
            }]) ||
            !checkDelegate.checkNormal($('input[name="newPwd"]'), [{
                'name': 'equal',
                'msg': '两次输入密码不一致',
                'param': $('input[name="newPPwd"]').val()
            }])
        ) {
            return;
        }

        var data = 'userId=' + $('input[name="userId"]').val() + '&oldPwd=' + $('input[name="oldPwd"]').val() + '&newPwd=' + $('input[name="newPwd"]').val();
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=updatePwd',
            data: data,
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    $('#pwd_model').modal('hide');
                    common.commonTips('保存成功');
                }
            }
        })
    });
});
