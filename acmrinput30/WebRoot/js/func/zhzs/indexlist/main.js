define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        tree = require('tree'),
        common = require('common'),
        modal = require('modal'),
        listjsp= require('listjsp');
    /**
     * 新增目录ajax提交，忽略数据检查
     */
    $(document).on('submit', '.J_add_catalogue', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('action');
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功");
                    $('#mymodal-data').modal('hide');
                    //common.commonTips('保存成功！');
                } else {
                    alert("保存失败");
                    $('#mymodal-data').modal('hide');
                   // common.commonTips('保存出错！');
                }
            }

        })

    });

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true,sou:true},
        { id:"#2", pId:0, name:"我收到的指数",isParent:true,sou:true},
        { id:"#3", pId:0, name:"我共享的指数", isParent:true,sou:true}
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
    var setting1 = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:clickEvent1
        }
    };
    var setting2 = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:clickEvent2
        }
    };
    var setting3 = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:clickEvent3
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
    function clickEvent1(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=cataname]').val(treeNode.name);
            $('input[name=idcata]').val(treeNode.id);
        } else {
            $('input[name=cataname]').val('');
        }

    }
    function clickEvent2(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=planname]').val(treeNode.name);
            $('input[name=itcata]').val(treeNode.id);
        } else {
            $('input[name=planname]').val('');
        }

    }
    function clickEvent3(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=indexname]').val(treeNode.name);
            $('input[name=proid]').val(treeNode.id);
        } else {
            $('input[name=indexname]').val('');
        }

    }
    function clickEvent4(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=zname]').val(treeNode.name);
            $('input[name=search]').val(treeNode.id);
        } else {
            $('input[name=zname]').val('');
        }

    }

    //修复图标，使没有子节点的目录也显示为目录
    function fixIcon(treeid){
        var treeObj = $.fn.zTree.getZTreeObj(treeid);
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
        fixIcon("treeDemo");
        addPath();

        $.fn.zTree.init($("#treeCata"), setting1, CategoryNodes);
        fixIcon("treeCata");
        $.fn.zTree.init($("#treePlan"), setting2, CategoryNodes);
        fixIcon("treePlan");
        $.fn.zTree.init($("#treeZs"), setting3, zNodes);
        fixIcon("treeZs");

    });


    //CategoryNode为只有目录的树结构nodes
    var CategoryNodes=[];
    for(i=0;i<zNodes.length;i++){
        if (zNodes[i].isParent==true&&zNodes[i].pId!="#2"&&zNodes[i].pId!="#3"){
            CategoryNodes.push(zNodes[i])
        }
    }
    console.log(CategoryNodes)

    module.exports={
        CategoryNodes:CategoryNodes
    }

})