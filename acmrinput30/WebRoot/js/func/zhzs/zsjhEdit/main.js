define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        dropdown = require('dropdown'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        ZeroClipboard = require('ZeroClipboard'),
        tab = require('tab'),
     listjsp= require('listjsp');

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
        var proCode=treeNode.id
        var classname="pro-"+proCode;
        console.log(classname)
        $("."+classname).css("color","red")

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

});