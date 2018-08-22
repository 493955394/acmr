define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        dropdown = require('dropdown'),
        AjaxMods = require('AjaxMods'),
        tree = require('tree'),
        modal = require('modal'),
        common = require('common'),
        treehide = require('treehide'),
        dragwidth = require('dragwidth'),
        pjax = require('pjax');
    $("#mainpanel").dragwidth();
    var depcode = $('input[name="depcode"]').val(), // 当前组织编码
        deptName = "", // 当前组织名称
        fcode, //查询代码
        fcname = ''; //查询名称
    //菜单树
    function initTree(baseNode) {
        var curMenu,
            zTree_Menu,
            setting = {
                async: {
                    enable: true,
                    url: common.rootPath + 'system/dep.htm?m=getchilds',
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
                url: common.rootPath + "system/dep.htm?m=find&" + data,
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
                url: common.rootPath + 'system/dep.htm?m=getchilds&selid=' + depcode + '&time=' + common.buildTimeStamp(),
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


    // 绑定查询按钮
    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            val = $('input', self).val();
        if(val !== ''){
        	currentUrl = currentUrl + '&findvalue=' + val;
        }
        if (depcode !== '') {
            currentUrl = currentUrl + '&depcode=' + depcode;
        }
        $.pjax({
            url: currentUrl,
            container: '.J_template_data_table'
        })
    });

    /**
     * 翻页操作
     */
    $(document).pjax('.J_person_pagination a', '.J_template_data_table');



    // 删除
    $(document).on('click', '.J_opr_del', function(event) {
        event.preventDefault();
        if (!confirm("确认要删除？")) {
            return;
        }
        var self = this,
            currentUrl = $(self).attr('href'),
            id = $(self).attr('id');
        $.ajax({
            url: common.rootPath + 'system/dep.htm?m=delete' + '&time=' + common.buildTimeStamp(),
            data: "id=" + id,
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    var noteid = data.param1;
                    common.commonTips('删除成功');
                    setTimeout("window.location.reload()", 1500);

//                    //获取树
//                    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
//                    //删除节点
//                    var nodes = zTree.getNodeByParam("id", noteid, null);
//                    if (nodes != null) {
//                        zTree.hideNode(nodes);
//                    }
//                    $.pjax({
//                        url: currentUrl,
//                        container: '.J_template_data_table'
//                    })
                } else if (data.returncode == 300) {
                    common.commonTips('删除失败，该机构下存在用户');
                } else if (data.returncode == 320) {
                    common.commonTips('删除失败，该机构下存在子机构');
                } else {
                    common.commonTips('删除失败');
                }
            },
            error: function(e) {
                common.commonTips('删除失败');
            }
        })
    });
    // 跳转到新增页面

    $(document).on('click', '#addBtn', function(event) {
        event.preventDefault();
        window.location.href = common.rootPath + "system/dep.htm?m=toAdd&depcode=" + depcode + "&deptName=" + deptName;
    });


    //组织信息导出到excel
    $(document).on('click', '.J_department_excel', function(event) {
        event.preventDefault();
        var url = common.rootPath + 'system/dep.htm?m=toExcel';
        if (depcode != '' && depcode != undefined) {
            url += '&depcode=' + depcode;
        }
        if (fcode != '' && fcode != undefined) {
            url += '&code=' + fcode;
        }
        if (fcname != '' && fcname != undefined) {
            url += '&cname=' + fcname;
        }
        window.location.href = url;
    });

    //翻页操作
    $(document).pjax('.J_department_pagination a', '.J_template_data_table');


    /**
     * 上移
     */
    $(document).on('click', '.J_opr_moveup', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('href'),
            indexVal = parseInt($(self).parents('tr:eq(0)').index()),
            currentId = $(self).attr('id'),
            len = $(self).parents('tbody').children('tr').length,
            siblingsId;
        if (indexVal === 0) {
            return;
        }
        siblingsId = $(self).parents('tr').prev('tr').children('td').children('.J_opr_movedown').attr("id");
        $.ajax({
            url: common.rootPath + 'system/dep.htm?' + '&time=' + common.buildTimeStamp(),
            type: 'get',
            dataType: 'json',
            data: 'm=move&currentId=' + currentId + '&siblingsId=' + siblingsId,
            success: function(data) {
                if (data.returncode == 200) {
                    $.pjax({
                        url: currentUrl,
                        container: '.J_template_data_table'
                    });
                    common.commonTips('上移成功');
                } else {
                    common.commonTips('上移失败');
                }
            },
            error: function(e) {}
        });
    });
    /**
     * 下移
     */
    $(document).on('click', '.J_opr_movedown', function(event) {
        event.preventDefault();
        var self = this,
            requestUrl = $(self).attr('href'),
            currentUrl = $(self).attr('href'),
            indexVal = parseInt($(self).parents('tr:eq(0)').index()),
            len = $(self).parents('tbody').children('tr').length,
            currentId = $(self).attr('id'),
            siblingsId;
        if (indexVal === len - 1) {
            return;
        }
        siblingsId = $(self).parents('tr').next('tr').children('td').children('.J_opr_moveup').attr("id");
        $.ajax({
            url: common.rootPath + 'system/dep.htm?' + '&time=' + common.buildTimeStamp(),
            type: 'get',
            dataType: 'json',
            data: 'm=move&currentId=' + currentId + '&siblingsId=' + siblingsId,
            success: function(data) {
                if (data.returncode == 200) {
                    $.pjax({
                        url: currentUrl,
                        container: '.J_template_data_table'
                    });
                    common.commonTips('下移成功');
                } else {

                    common.commonTips('下移失败');
                }
            },
            error: function(e) {}
        });
    });
});
