define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        pjax = require('pjax'),
        common = require('common');
    var delMenuNodes = [],
        insertMenuNodes = [],
        menutree = '';

    var find; //查询条件

    // 绑定查询按钮
    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            val = $('input', self).val();
        if (val !== '') {
            currentUrl = currentUrl + '&findvalue=' + val;
        }
        $.pjax({
            url: currentUrl,
            container: '.J_template_data_table'
        });
        find=val;
    });
    //组织机构树
    $(document).on('click', '.J_add_user', function(event) {
        event.preventDefault();
        var self = this,
            currentId = $(self).attr('id'),
            modalContent = '';
        if ($('#adduser_model').length > 0) {
            $('#adduser_model').remove();
        }
        modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title" id="myModalLabel">添加用户</h4>';
        modalContent += '</div>';
        modalContent += '<form class="J_add_user_form">';
        modalContent += '<input type="hidden" name="roleId" value="' + currentId + '"/>';
        modalContent += '<div class="modal-body">';
        modalContent += '<ul id="usertree" class="ztree"></ul>';
        modalContent += '</div>';
        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="submit" class="btn btn-primary btn-sm">保存</button><button type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>';
        modalContent += '</div>';
        modalContent += '</form>';
        buildModelHTML('adduser_model', modalContent);
        $('#adduser_model').modal('show');
        getTree(currentId);

    });
    //角色菜单树
    $(document).on('click', '.J_write_load_task', function(event) {
        event.preventDefault();
        var self = this,
            currentId = $(self).attr('id'),
            modalContent = '';

        if ($('#addmenu_model').length > 0) {
            $('#addmenu_model').remove();
        }

        modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title" id="myModalLabel">设置权限</h4>';
        modalContent += '</div>';
        modalContent += '<form class="J_add_menu_form">';
        modalContent += '<input type="hidden" name="roleId" value="' + currentId + '"/>';
        modalContent += '<div class="modal-body">';
        modalContent += '<ul id="menutree" class="ztree"></ul>';
        modalContent += '</div>';
        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="submit" class="btn btn-primary btn-sm">保存</button><button type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>';
        modalContent += '</div>';
        modalContent += '</form>';
        buildModelHTML('addmenu_model', modalContent);
        $('#addmenu_model').modal('show');
        getMenuTree(currentId);

    });
    // 保存选择人员
    $(document).on('submit', '.J_add_user_form', function(event) {
        event.preventDefault();
        var self = this;

        if (usertree == '') {
            return;
        }
        getChangeCheckedNodes();

        var params = 'rolecode=' + $('input[name="roleId"]').val() + "&delNodes=" + delNodes.join(",") + "&insertNodes=" + insertNodes.join(",");
        var st = new Date().getTime();
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=updateUserToRole&st=' + st,
            data: params,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {

                common.commonTips('保存成功');
                $('#adduser_model').modal('hide');
            },
            complete: function(e) {
                insertNodes = [],
                    delNodes = [];

            }
        });

    });
    var delNodes = [],
        insertNodes = [],
        usertree = '';

    function getTree(rolecode) {
        /**
         * 人员树
         */
        var st = new Date().getTime();
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=getRolePersonTree&st=' + st,
            data: 'rolecode=' + rolecode,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                var setting = {
                    check: {
                        enable: true
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId",
                            rootPId: 0
                        }
                    }
                };
                usertree = $.fn.zTree.init($("#usertree"), setting, data);
            }
        });

    }

    //保存选中菜单
    $(document).on('submit', '.J_add_menu_form', function(event) {
        event.preventDefault();
        var self = this;

        if (menutree == '') {
            return;
        }
        getRoleToMenuChecked();
        var params = 'rolecode=' + $('input[name="roleId"]').val() + "&rightnodes=" + insertMenuNodes.join(",");
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=setRightTree',
            data: params,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                common.commonTips('保存成功');
                $('#addmenu_model').modal('hide');
            },
            complete: function(e) {
            	insertMenuNodes=[];
            }
        });
    });
    /**
     * 取回当前角色的菜单树
     */
    function getMenuTree(rolecode) {
        var st = new Date().getTime();
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=getRightTree&st=' + st,
            data: 'rolecode=' + rolecode,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                var setting = {
                    check: {
                        enable: true,
                        chkboxType: { "Y": "ps", "N": "s" }
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId",
                            rootPId: 0
                        }
                    }
                };
                menutree = $.fn.zTree.init($("#menutree"), setting, data);
            }
        });
    }

    function getChangeCheckedNodes() {
        var nodes = usertree.getChangeCheckedNodes(); // 获取所有更改勾选状态的结点
    	console.log(nodes);
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].p1 == "user") { // 过滤掉组织结点
            	//console.log(nodes[i]);
                if (nodes[i].checked) { // 需要插入进数据库的
                    insertNodes.push(nodes[i].id);
                } else { // 需要从数据库删除的
                    delNodes.push(nodes[i].id);
                }
            }
        }
    }

    function getRoleToMenuChecked() {
        var nodes = menutree.getCheckedNodes(); // 获取所有更改勾选状态的结点
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].checked) { // 需要插入进数据库的
                insertMenuNodes.push(nodes[i].id);
            }
        }
    }

    function buildModelHTML(targetName, targetContent) {
        var len = $('#' + targetName).length,
            tempHTML;
        if (len === 0) {
            tempHTML = '<div class="modal fade" id="' + targetName + '" role="dialog"><div class="modal-dialog"><div class="modal-content">' + targetContent + '</div></div></div>';
            $('body').append(tempHTML);
        }
    }

    // 启用，停用
    $(document).on('click', '.J_opr_del', function(event) {
        event.preventDefault();
        var self = this,
            code = $(self).attr('id'),
            currentUrl = $(self).attr('href');
        $.ajax({
            url: common.rootPath + 'system/role.htm?m=delete',
            data: "code=" + code,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    $.pjax({
                        url: currentUrl,
                        container: '.J_template_data_table'
                    });
                }
            }
        });

    });
    
    /**
     * 翻页操作
     */
    $(document).pjax('.J_role_pagination a', '.J_template_data_table');
    /**
     * 导出角色信息到excel
     */
    $(document).on('click', '.J_role_excel', function(event) {
        event.preventDefault();
        var url = common.rootPath + 'system/role.htm?m=toExcel';
        
        if (find !== '' && find != undefined) {
            url += '&findvalue=' + find;
        }
        window.location.href = url;
    });
});
