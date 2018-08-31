define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        rootPath = require('rootPath'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        tab = require('tab'),
     listjsp = require('listjsp');

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true}
    ];
    var indexlist=listjsp.indexlist;
    for(var i=0;i<indexlist.length;i++){
        zNodes.push(indexlist[i])
    }
    var setting = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:clickEvent
        }
    };
    function clickEvent(event,treeId,treeNode) {
        if (treeNode.id != '') {
            $('input[name=proname]').val(treeNode.name);
            $('input[name=procode]').val(treeNode.id);
            console.log(treeNode.id)
        } else {
            $('input[name=proname]').val('');
        }
    }

    //修复图标，使没有子节点的目录也显示为目录
    function fixIcon(){
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        //过滤出sou属性为true的节点（也可用你自己定义的其他字段来区分，这里通过sou保存的true或false来区分）
        var folderNode = treeObj.getNodesByFilter(function (node) { return node.sou});
        for(var j=0 ; j<folderNode.length; j++){//遍历目录节点，设置isParent属性为true;
            folderNode[j].isParent = true;
        }
        treeObj.refresh();//调用api自带的refresh函数。
    }
    $(document).ready(function(){
        $.fn.zTree.init($("#tree"), setting, zNodes);
        fixIcon();

        //修正添加的table的classname，方便和树联动

    });
    /**
     * 菜单树
     */
    var initTreePara = $("#initTreePara").val();
    var treeNodeId = $("#procode").val();
    var treeNodeName = "地区树";
    var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
    var setting1 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=findZbTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent1,
            onAsyncSuccess: zTreeOnAsyncSuccess
        }
    };
    function clickEvent1(event, treeid, treeNode) {
        treeNodeId = treeNode.id;
        treeNodeName = treeNode.name;
        if (treeNode.id != '') {
        $('input[name=regname]').val(treeNode.name);
        $('input[name=regcode]').val(treeNode.id);}
        else {
            $('input[name=regname]').val("");
        }

    }
    var rootNode = [{"id":"","name":"地区树", "open":"true", "isParent":"true"}];
    var treeObj = $.fn.zTree.init($("#treeDemo"), setting1, rootNode);
    var treenodes = treeObj.getNodes();
    treeObj.expandNode(treenodes[0], true, true, true);

    function zTreeOnAsyncSuccess(event, treeid, treeNode, msg){
        if (initTreePara.length>0){
            var zbs = initTreePara.split("/");
            var nodes;
            var treeObj = $.fn.zTree.getZTreeObj(treeid);

            if (treeNode == null){	// 第一层结点
                nodes = treeObj.getNodes();
            } else {
                nodes = treeNode.children;
            }
            var isBreak = false;
            for (var i = 0; i < nodes.length; i++){
                var node = nodes[i];
                for (var j = 0; j < zbs.length; j++){
                    if (zbs[j] == node.id){
                        if (node.isParent){
                            treeObj.expandNode(node, true);
                            if(node.id== zbs[zbs.length-1]){
                                treeObj.selectNode(node);
                                treeNodeId = node.id;
                                treeNodeName = node.name;
                            }
                        } else {
                            treeObj.selectNode(node);
                            treeNodeId = node.id;
                            treeNodeName = node.name;
                        }
                        isBreak = true;
                        break;
                    }
                }
                if (isBreak){
                    break;
                }
            }
        }
    }

    /**
     * 选中单个地区
     */
    var select = [];
    $("#sigglechoose").click(function () {
        var regcode =  $('input[name=regcode]').val();
        var regcname =  $('input[name=regname]').val();
        select.push({code:regcode,name:regcname});
       //去重
        for (var i = 0; i < select.length; i++) {
            for (var j =i+1; j <select.length; ) {
                if (select[i].code == select[j].code && select[i].name == select[j].name) {
                    select.splice(j, 1);
                }
                else j++;
            }
        }
        //每触发一次先初始化
       $('select.regul').html("");
        var showreg ="";
        for(var i=0;i<select.length;i++){
            if(select[i].name=="" && select[i].code==""){
                showreg +="";
            }else {
                showreg += '<option class="list-group-item"  value="'+select[i].code+'">'+select[i].name+'</option>';
            }
        }
        $("#selectreg").append(showreg);
    });
    /**
     * 删除单个地区
     */
    $("#delsiggle").click(function () {
        var obj = document.getElementById("selectreg");
        var index = obj.selectedIndex;
       var code =  obj.options[index].value;
        for(var i=0;i<select.length;i++){
            if(select[i].code== code){
                select.splice(i, 1);
            }
        }
        obj.options.remove(index);
    });
    /**
     * 选中某地区下所有地区
     */
    $(document).on("click","#chooseall",function ( ) {
        var procode =  $('input[name=regcode]').val();
        $.ajax({
            url: rootPath + "/zbdata/zsjhedit.htm?m=getResultLeft",
            type: "post",
            data: {
                "procode": procode
            },
            async: false,
            dataType: "json",
            success: function(data) {
            }
        });
    })

});