define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        dropdown = require('dropdown'),
        pjax = require('pjax'),
        modal = require('modal'),
        AjaxMods = require('AjaxMods');

    /**
     * 菜单树
     */
    var treeNodeId = "";
    var treeNodeName = "";
    var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
    var setting12 = {
        async: {
            enable: true,
            url: common.rootPath + 'zbdata/indexlist.htm?m=depUserTree&st=' + st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent12,
        }
    };

    function clickEvent12(event, treeid, treeNode) {
        treeNodeId = treeNode.id;
        treeNodeName = treeNode.name;
        if (treeNode.id != '') {
            $('input[name=regname]').val(treeNode.name);
            $('input[name=regcode]').val(treeNode.id);
        }
        else {
            $('input[name=regname]').val("");
        }

    }

    var rootNode = [{"id": "", "name": "用户组织树", "open": "true", "isParent": "true"}];
    var treeObj = $.fn.zTree.init($("#treeRight"), setting12, rootNode);
    var treenodes = treeObj.getNodes();
    treeObj.expandNode(treenodes[0], true, true, true);
    $(document).on('click', '#rightbutton', function (event) {
        event.preventDefault();
        var icode = $(this).attr("name");
        $.ajax({
            url: common.rootPath + "zbdata/indexlist.htm?m=rightManager",
            data: {"indexcode": icode},
            type: 'post',
            datatype: 'json',
            timeout: 10000,
            success: function (data) {
                $("#selectList li").remove();
                $(".right-list li").remove();
                for(var i in data) {
                    var html = "";
                    html += "<li><span data-uid='" + data[i].depusercode + "' data-sort='" + data[i].sort + "' style='float: left'>"+ data[i].depusername+ "</span>";
                    html += "<span style='float: right'><select><option value='2'>管理</option><option value='1'>协作</option><option value='0'>只读</option></select>";
                    html += "<i class='glyphicon glyphicon-remove'></i></span></li>";
                    $(".right-list").append(html);
                }
                $("#mymodal-right").modal("show");
            }
        })
    })
    /*权限管理的搜索框*/
    $(document).on('click', '.right-select', function (event) {
        event.preventDefault();
        $("#selectList li").remove();
        var keyword = $("#select-input").val();
        if(keyword =="")
            return;
        $.ajax({
            url: common.rootPath + "zbdata/indexlist.htm?m=searchList",
            data: {"keyword": keyword},
            type: 'post',
            datatype: 'json',
            timeout: 10000,
            success: function (data) {
                for(var i in data) {
                    var html = "";
                    html += "<li data-uid='" + data[i].depusercode + "' data-sort='" + data[i].sort + "'>"+ data[i].depusername+ "</li>";
                    $("#selectList").append(html);
                }
            }
        })
    })
})