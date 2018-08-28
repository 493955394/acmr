define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        tree = require('tree'),
        modal = require('modal'),
        listjsp= require('listjsp');
    var setting = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true},
        { id:"#2", pId:0, name:"我收到的指数",isParent:true},
        { id:"#3", pId:0, name:"我共享的指数", isParent:true}
    ];
    var indexlist=listjsp.indexlist;
    for(var i=0;i<indexlist.length;i++){
        zNodes.push(indexlist[i])
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



    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        fixIcon();
    });

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