define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        pjax = require('pjax'),
        tree = require('tree'),
        modal = require('modal'),
        dragwidth = require('dragwidth'),
        common = require('common');
    $("#mainpanel").dragwidth();
    var fcode = '', //查询代码 
        fcname = '', //查询名称		
        depcode = $('input[name="depcode"]').val(); // 当前组织编码
    //菜单树
    function initTree(baseNode) {
        var curMenu, zTree_Menu;
        //菜单树
        var st = new Date().getTime(); //时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
        var setting = {
            async: {
                enable: true,
                url: common.rootPath + 'system/dep.htm?m=getchilds&st=' + st,
                contentType: 'application/json',
                type: 'get',
                autoParam: ["id"]
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: clickEvent
            }
        }

        function clickEvent(event, treeid, treeNode) {
            depcode = treeNode.id;
            var data = "depcode=" + treeNode.id;
            $.pjax({
                url: common.rootPath + "system/person.htm?m=find&" + data,
                container: '.J_template_data_table'
            })
        }
        $.fn.zTree.init($("#treeDemo"), setting, baseNode);
        zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
        depcode = $('input[name="depcode"]').val();
        curMenu = zTree_Menu.getNodesByParam('id', depcode)[0];
        zTree_Menu.selectNode(curMenu);
    }

    function init() {
        var rootNode = [{
            "id": "",
            "name": "组织机构树",
            "open": "true",
            "pId": "0",
            "isParent": "true"
        }];
        if (depcode !== '') {
            $.ajax({
                url: common.rootPath + 'system/dep.htm?m=getchilds&selid=' + depcode,
                timeout: 5000,
                dataType: 'json',
                success: function(data) {
                    var len = data.length;
                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            rootNode.push(data[i]);
                        }
                    }
                    initTree(rootNode);
                }
            });
        } else {
            initTree(rootNode);
        }
    }
    init();

    /**
     * 导出人员信息到excel
     */
    $(document).on('click', '.J_person_excel', function(event) {
        event.preventDefault();
        var url = common.rootPath + 'system/person.htm?m=toExcel';
        if (depcode != '' && depcode != undefined) {
            url += '&depcode=' + depcode;
        }
        if (fcode != '' && fcode != undefined) {
            url += '&userId=' + fcode;
        }
        if (fcname != '' && fcname != undefined) {
            url += '&userName=' + fcname;
        }
        //判断是否有这个findValue被查询
        var test = window.location.search;
        if(test.indexOf("&findValue=") >= 0 ) { 
        	var findValue = document.getElementById('queryValue').value;
            if (findValue != '' && findValue != undefined) {
                url += '&code=' + findValue;
            }
        } 
        
        window.location.href = url;
    });
    /**
     * 格式化数据格式
     */
    function formateData() {
        var searchTxt = $('.J_search_form input').val(),
            formateStr = '';
        formateStr = 'findValue=' + searchTxt;
        return formateStr;
    }


    /**
     * 绑定查询按钮
     */
    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            val = $('input', self).val();
        if (val !== '') {
            currentUrl = currentUrl + '&findValue=' + val;
        }
        if (depcode !== '') {
            currentUrl = currentUrl + '&depcode=' + depcode;
        }
        $.pjax({
            url: currentUrl,timeout:10000,
            container: '.J_template_data_table'
        })
    });


    /**
     * 删除用户
     */
    $(document).on('click', '.J_opr_del', function(event) {
        event.preventDefault();
        var self = $(this),
            id = $(self).attr('id'),
            currentUrl = self.attr('href');
        if (!confirm("确定删除用户？")) return;
        $.ajax({
            url:common.rootPath+ 'system/person.htm?m=delete',
            data: 'id=' + id,
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 400) {
                    alert("删除失败!该用户存在绑定审批流程，请先修改审批流程");
                    return;
                }
                common.commonTips('删除成功');
                $.pjax({
                    url: currentUrl,
                    container: '.J_template_data_table'
                })
            }
        });
    });
    /**
     * 刷新table用于ajax 请求 
     */
    function formatBackTable(data) {
        var list = data.data,
            len = list.length,
            trs = '';
        if (len === 0) {
            $('.J_person_table > tbody').html(trs);
            return;
        }
        for (var i = 0; i < len; i++) {
            trs += '<tr>';
            var index = (data.pageNum - 1) * data.pageSize + (i + 1);
            trs += '<td>' + index + '</td>';
            trs += '<td>' + list[i].userId + '</td>';
            trs += '<td>' + list[i].cname + '</td>';
            trs += '<td>' + list[i].email + '</td>';
            trs += '<td>' + list[i].tel + '</td>';
            trs += '<td><a href=' + common.rootPath + '"system/person.htm?m=toEdit&userId=' + list[i].userId + '" class="btn-opr J_opr_edit">编辑</a><a href="javascript:;" class="btn-opr J_opr_del" id="' + list[i].userId + '">删除</a><a href="" class="btn-opr J_opr_pwd" id="' + list[i].userId + '">重置密码</a><a href="" class="btn-opr J_add_role" id="' + list[i].userId + '">设置角色</a></td>';
            trs += '</tr>';
        }
        $('.J_person_table > tbody').html(trs);
    }

    /**
     * 跳转到新增页面
     */
    $(document).on('click', '#addBtn', function(event) {
        event.preventDefault();
        depcode = $('input[name="depcode"]').val();
        if (depcode === '') {
            common.commonTips('请选择用户所属组织');
            return;
        }
        window.location.href = common.rootPath + "system/person.htm?m=toAdd&depcode=" + depcode;
    });

    /**
     * 重置密码
     */
    $(document).on('click', '.J_opr_pwd', function(event) {
        event.preventDefault();
        var self = this,
            currentId = $(self).attr('id'),
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
            checkDelegate;
        //检查code
        checkDelegate = new VaildNormal();

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

        var data = 'userid=' + $('input[name="userId"]').val() + '&oldpwd=' + $('input[name="oldPwd"]').val() + '&newpwd=' + $('input[name="newPwd"]').val();
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
    /**
     * 人员角色绑定
     */
    var delNodes = [],
        insertNodes = [],
        menutree = '';

    /**
     * 用户绑定角色
     */
    $(document).on('click', '.J_add_role', function(event) {
        event.preventDefault();
        var self = this,
            currentId = $(self).attr('id'),
            modalContent = '';
        if ($('#menutree_model').length > 0) {
            $('#menutree_model').remove();
        }
        modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title" id="myModalLabel">设置角色</h4>';
        modalContent += '</div>';
        modalContent += '<form class="J_add_role_form">';
        modalContent += '<input type="hidden" name="userId" value="' + currentId + '"/>';
        modalContent += '<div class="modal-body">';
        modalContent += '<div id="roleList" class="modal-body">';
        modalContent += '</div>'
        modalContent += '</div>'
        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="submit" class="btn btn-primary">保存</button><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
        modalContent += '</div>';
        modalContent += '</form>';
        buildModelHTML('menutree_model', modalContent);
        $('#menutree_model').modal('show');
        getRoleList(currentId);
    });
    /**
     * 取得角色列表
     */
    function getRoleList(userId) {
        var trs = '';
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=findRoletoUser',
            data: 'userId=' + userId,
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            success: function(data) {
                trs = format(data);
                $("#roleList").html(trs);
            }
        })

    }
    /**
     * 格式化用户列表
     */
    function format(data) {
        var list = data,
            len = list.length;
        var trs = '';
        if (len === 0) {
            return;
        }
        for (var i = 0; i < len; i++) {
            if (list[i].checked === true) {
                trs += '<div class="checkbox"><label><input type="checkbox" value="' + list[i].id + '" checked="checked" name="roleBox">';
            } else {
                trs += '<div class="checkbox"><label><input type="checkbox" value="' + list[i].id + '" name="roleBox">';
            }
            trs += list[i].name + '</label></div>';
        }

        return trs;

    }

    function buildModelHTML(targetName, targetContent) {
        var len = $('#' + targetName).length,
            tempHTML;
        if (len === 0) {
            tempHTML = '<div class="modal fade" id="' + targetName + '" role="dialog"><div class="modal-dialog"><div class="modal-content">' + targetContent + '</div></div></div>';
            $('body').append(tempHTML);
        }
    }
    //得到选中角色，获取checkbox值
    function getRoleToUserChecked() {
        var values = ''; // 获取所有更改勾选状态的结点
        $("input[name='roleBox']:checked").each(function() {
            values += this.value + ',';
        });
        return values;
    }

    //保存选中菜单
    $(document).on('submit', '.J_add_role_form', function(event) {
        event.preventDefault();
        var self = this;
        var values = getRoleToUserChecked();
        var params = 'userId=' + $('input[name="userId"]').val() + '&values=' + values;
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=updateRoletoUser',
            data: params,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                $('#menutree_model').modal('hide');
                common.commonTips('保存成功');
            }
        })

    });
    /**
     * 翻页操作
     */
    $(document).pjax('.J_person_pagination a', '.J_template_data_table');

    $(document).on('click', '.J_test', function(event) {
        event.preventDefault();
    });

    /**
     * 同步用户
     */
    $(document).on('click', '#syncBtn', function(event) {
        $('#J_modal_data_upLoad_now').remove();
        event.preventDefault();
        var self = this;
        var modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title" id="myModalLabel">同步用户</h4>';
        modalContent += '</div>';
        modalContent += '<div class="modal-body">';
        modalContent += '<div class="form-group"><h5 id="syncUser" style="color:red;font-weight: bold;font-size:18px;text-align:center;">同步中　<img src=' + common.rootPath + '"images/load.gif" width="18px"></h5></div>';
        modalContent += '</div>';
        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_save_file">关闭</button>';
        modalContent += '</div>';
        common.buildModelHTML('J_modal_data_upLoad_now', modalContent);
        $('#J_modal_data_upLoad_now').modal('show');
        // 同步
        $.ajax({
            url: common.rootPath + 'system/person.htm?m=syncUser',
            data: '',
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    $("#syncUser").html("同步完成!")
                } else {
                    $("#syncUser").html("同步失败!")
                }
            }
        })
    });

    /**
     * 批量匹配详情关闭界面
     */
    $(document).on('click', '.J_btn_save_file', function(event) {
        event.preventDefault();
        $('#J_modal_data_upLoad_now').modal('hide');
    });

});
