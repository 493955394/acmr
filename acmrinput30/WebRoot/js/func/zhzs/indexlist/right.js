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

    var currentuser = $(".currentUser").val();

    var treeNodeId = "";
    var treeNodeName = "";
    var treeNodeSort = "";

    /**
     * 权限管理模态框弹出
     */
    $(document).on('click', '#rightbutton', function (event) {
        event.preventDefault();
        var icode = $(this).attr("name");
        $("#currentIcode").val(icode);//打开模态框就给那个input框赋值
        /**
         * 用户组织树
         */
        treeNodeId = "";
        treeNodeName = "";
        treeNodeSort = "";
        var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
        var setting12 = {
            async: {
                enable: true,
                url: common.rootPath + 'zbdata/indexlist.htm?m=depUserTree&st=' + st+"&icode="+$('#currentIcode').val(),
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
            treeNodeSort = treeNode.isParent;
        }

        var rootNode = [{"id": "", "name": "用户组织树", "open": "true", "isParent": "true"}];
        var treeObj = $.fn.zTree.init($("#treeRight"), setting12, rootNode);
        var treenodes = treeObj.getNodes();
        treeObj.expandNode(treenodes[0], true, true, true);
        $.ajax({
            url: common.rootPath + "zbdata/indexlist.htm?m=rightManager",
            data: {"indexcode": icode},
            type: 'post',
            datatype: 'json',
            timeout: 10000,
            success: function (data) {
                //初始化
                $("#selectList a").remove();
                $(".right-list li").remove();
                $("#select-input").val("");
                for(var i in data) {
                    var html = "";
                    html += "<li class='list-group-item' data-uid='" + data[i].depusercode + "' data-sort='" + data[i].sort + "' data-cuser='" + data[i].createuser + "'>"+ data[i].depusername+ "";
                    html += "<span class='fr'><select id='J_right_"+i+"'><option value='2'>管理</option><option value='1'>协作</option><option value='0'>只读</option></select>";
                    html += "&nbsp;&nbsp;&nbsp;<a onclick='delRemove(this)'><i class='glyphicon glyphicon-remove' style='color: #FF7F19;'></i></a></span></li>";
                    $(".right-list").append(html);
                    $('#J_right_'+i+'').find("option[value='"+data[i].right+"']").attr("selected",true);
                }
                $("#mymodal-right").modal("show");
            }
        })
    })

    /*权限管理的搜索框*/
    $(document).on('click', '.right-select', function (event) {
        event.preventDefault();
        $("#selectList a").remove();
        var keyword = $("#select-input").val();
        if(keyword =="")
            return;
        $.ajax({
            url: common.rootPath + "zbdata/indexlist.htm?m=searchList",
            data: {"keyword": keyword,"icode":$('#currentIcode').val(),"id":treeNodeId,"sort":treeNodeSort},
            type: 'post',
            datatype: 'json',
            timeout: 10000,
            success: function (data) {
                for(var i in data) {
                    var html = "";
                    html += "<a href='#' data-uid='" + data[i].depusercode + "' data-sort='" + data[i].sort + "' data-name='" + data[i].depusername + "' class='list-group-item '>"+ data[i].depusername+ "</a>";
                    $("#selectList").append(html);
                    if($("#selectList").text()!=""){
                        $(document).bind('click', function(e) {
                            var e = e || window.event; //浏览器兼容性
                            var elem = e.target || e.srcElement;
                            while (elem) { //循环判断至跟节点，防止点击的是div子元素
                                if (elem.id && elem.id == 'selectList') {
                                    return;
                                }
                                elem = elem.parentNode;
                            }
                            $("#selectList a").remove(); //点击的不是div或其子元素
                        });
                    }
                }
            }
        })
    })
    /**
     * 权限管理搜索后添加搜索结果
     */
    $("#selectList").on('click',"a",function (event) {
        event.preventDefault();
            //去重处理
        var thisid = $(this).attr("data-uid");
        var thissort = $(this).attr("data-sort");
        var thisname = $(this).attr("data-name");
            var check = false;
            $(".right-list li").each(function(){
                var id = $(this).attr("data-uid");
                var sort = $(this).attr("data-sort");
                if(thisid==id&&thissort==sort){
                    check = true;
                }
            })
            if(check){return;}
            //如果在右边的列表中不存在，将其添加到右边的列表中
            var html = "";
            var temp ="";
            html += "<li class='list-group-item' data-uid='" + thisid + "' data-sort='" + thissort + "' data-cuser='" + currentuser + "'>"+ thisname+ "";
            html += "<span class='fr'><select><option value='2'>管理</option><option value='1'>协作</option><option value='0'>只读</option></select>";
            html += "&nbsp;&nbsp;&nbsp;<a onclick='delRemove(this)'><i class='glyphicon glyphicon-remove' style='color: #FF7F19;'></i></a></span></li>";
            $(".right-list").append(html);
    })

    /**
     * 权限管理添加按钮
     */
    $(document).on('click',".right-add",function (event) {
        event.preventDefault();
        if(treeNodeId==""){return;}
        else {
            //去重处理
            var check = false;
            $(".right-list li").each(function(){
                var id = $(this).attr("data-uid");
                var sort = $(this).attr("data-sort");
                if(treeNodeId==id&&treeNodeSort==sort){
                    check = true;
                }
            })
            if(check){return;}
            //如果在右边的列表中不存在，将其添加到右边的列表中
            var html = "";
            var temp ="";
            if(treeNodeSort){
                temp=1;
            }
            else{
                temp=2;
            }
            html += "<li class='list-group-item' data-uid='" + treeNodeId + "' data-sort='" + temp + "' data-cuser='" + currentuser + "'>"+ treeNodeName+ "";
            html += "<span class='fr'><select><option value='2'>管理</option><option value='1'>协作</option><option value='0'>只读</option></select>";
            html += "&nbsp;&nbsp;&nbsp;<a onclick='delRemove(this)'><i class='glyphicon glyphicon-remove' style='color: #FF7F19;'></i></a></span></li>";
            $(".right-list").append(html);
        }
    })
    /**
     * 权限管理删除全部按钮
     */
    $(document).on('click',".remove-alllist",function (event) {
        event.preventDefault();
        $(".right-list li").remove();
    })

    /**
     * 权限管理保存按钮
     */
    $(document).on('submit', '.J_rightlist_form',function (event) {
        event.preventDefault();
        var  requestUrl = $(this).prop('action');
        var rightlist = "";
        var indexcode = $("#currentIcode").val();
        $(".right-list li").each(function(){
            var depusercode = $(this).attr("data-uid");
            var ifsort = $(this).attr("data-sort");
            var createuser = $(this).attr("data-cuser");
            var right = $(this).find("select option:selected").val();//获取当前li标签下的right值
            rightlist += ifsort+","+depusercode+","+right+","+createuser+";#"
        })
        if(rightlist!=""){
            rightlist = rightlist.substr(0, rightlist.length - 2);
        }
        $.ajax({
            url: requestUrl,
            data: {"rightlist": rightlist,"icode":indexcode},
            type: 'post',
            datatype: 'json',
            timeout: 10000,
            success: function (data) {
                if(data.returncode == 200){
                    alert("保存成功");
                    $("#mymodal-right").modal("hide");
                }
              else {
                  alert("保存失败");
                  $("#mymodal-right").modal("hide");
                }
            }
        })
    })
})