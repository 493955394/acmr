define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        tree = require('tree'),
        modal = require('modal'),
        listjsp= require('listjsp');

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true},
        { id:"#2", pId:0, name:"我收到的指数",isParent:true},
        { id:"#3", pId:0, name:"我共享的指数", isParent:true}
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
    //局部刷新列表
    function reloadList() {
        var url=window.location.href
        $('#my_index_all').load(url + ' .my_index')
    }
    function clickEvent(event,treeId,treeNode) {
        if(treeNode.isParent==false){
            return false
        }
        $.ajaxSettings.async=false
        $("#my_index_all").children().removeAttr("class")
        reloadList()
        changeTrClass();
        var proCode=treeNode.id
        var classname="-"+proCode+"-"
        $(".my_index:not([class*='" +
            classname+"'])").detach()
    }

    //修复图标，使没有子节点的目录也显示为目录
    function fixIcon(){
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        //过滤出sou属性为true的节点（也可用你自己定义的其他字段来区分，这里通过sou保存的true或false来区分）
        var folderNode = treeObj.getNodesByFilter(function (node) { return node.sou});
        for(var j=0 ; j<folderNode.length; j++){//遍历目录节点，设置isParent属性为true;
            folderNode[j].isParent = true;
        }
        treeObj.refresh();//调用api自带的refresh函数。
    }
    //添加一个属性path，里面存放节点的的所有父节点（包括自己）
    function addPath(){
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes=treeObj.transformToArray(treeObj.getNodes());
        for(var i=0;i<nodes.length;i++){
            var paths=nodes[i].getPath();
            var path="";
            for(var j=0;j<paths.length;j++){
                path=path+"-"+paths[j].id
            }
            nodes[i].path=path;
        }
    }
    //操作列表的class
    function changeTrClass(){
       // 修正classname
        $("tr[class='pro-']").attr("class","pro-#1")
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes=treeObj.transformToArray(treeObj.getNodes());
        //把path添加到class中
        for(var i=0;i<nodes.length;i++){
            var comp=nodes[i].id;
            var path=nodes[i].path;
            //console.log(comp)
            $("tr[class*='pro-" +
                comp+"']").addClass(path)
        }
    }

    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        fixIcon();
        addPath();


    });

    //CategoryNode为只有目录的树结构nodes
    var CategoryNodes=[];
    for(i=0;i<zNodes.length;i++){
        if (zNodes[i].isParent==true&&zNodes[i].pId!="#2"&&zNodes[i].pId!="#3"){
            CategoryNodes.push(zNodes[i])
        }
    }

    module.exports={
        CategoryNodes:CategoryNodes
    }
})