define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        tab = require('tab'),
        zbAdd=require('zbAdd'),
        editjsp = require('editjsp');

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true}
    ];
    var indexlist=editjsp.indexlist;
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
    $(document).on("click","#chooseall",function () {
        var procode =  $('input[name=regcode]').val();
        if (procode){
            $.ajax({
                url: common.rootPath+'zbdata/zsjhedit.htm?m=getResultLeft',
                type: "post",
                data: {"procode": procode},
                async: false,
                dataType: "json",
                success: function(data) {
                    for (var i = 0; i <data.length; i++) {
                        select.push({code:data[i].code,name:data[i].name});
                    }
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
                }
            });
        }
    });
    /**
     * 删除所有地区
     */
    $("#delall").click(function (){
        $('select.regul').html("");
        select = [];
    });
    /**
     * 时间选择自动补上中间的时间期，以及数据检查
     */
    var selecttime = "";
    $("#datachecks").click(function () {
        selecttime = "";//初始化时间
       $("#data_check_show").empty();//初始化表格
        var begintime = $('input[name = begintime]').val();
        var endtime = $('input[name = endtime]').val();
        if(!begintime){
            alert("请输入开始时间");
        }
        else if(!endtime){
            alert("请输入结束时间");
        }
        else if(begintime>endtime){
            alert("开始时间不能大于结束时间，请重新输入");
        }
        else{
            for (var i = begintime; i <= endtime ; i++) {
                selecttime += i+",";
            }
            selecttime = selecttime.substr(0, selecttime.length - 1);//去除最后一个逗号
          var sj = selecttime.split(",");
            var regselect ="";
            for (var i = 0; i <select.length ; i++) {
                regselect += select[i].code+",";
            }
            regselect = regselect.substr(0, regselect.length - 1);//去除最后一个逗号
            var zbs=zbAdd.zbs;
            $.ajax({
                url: common.rootPath+'zbdata/zsjhedit.htm?m=getData',
                type: "post",
                data: {"reg": regselect,"sj":selecttime},
                async: true,
                dataType: "json",
                success: function(data) {
                    console.log(data)
                    /**
                     * 开始做数据检查
                     */

                    var tabledata = "<table class='table table-bordered' id='tabledata'><span>检查结果</span><thead><th>时间</th><th>指标</th>";
                    //表头
                    for (var i = 0; i <select.length ; i++) {
                        tabledata +="<th>"+select[i].name+"</th>";
                    }
                    tabledata += "</thead>";
                    tabledata += "<tbody>";
                    //表内容
                    var z=0;
                    for (var i = 0; i <sj.length ; i++) {
                        for (var j = 0; j <2 ; j++) {
                            tabledata +="<tr><td>"+sj[i]+"</td>";
                            tabledata +="<td>指标"+j+"</td>";
                            for (var k = 0; k <select.length ; k++) {
                                tabledata += "<td>"+data[z]+"</td>";
                                z++;
                            }
                            tabledata +="</tr>";
                        }
                    }
                    tabledata += "</tbody>";
                    tabledata += "</table>";
                    $("#data_check_show").append(tabledata);
                }
            });
        }
    });
});