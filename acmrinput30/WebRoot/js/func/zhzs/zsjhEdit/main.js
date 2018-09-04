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

    var incode=$("#index_code").val();
    var select_li = "error";//选择移除的li的下标
    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true}
    ];
    var indexlist = editjsp.indexlist;
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
        $('ul.regul').html("");
        var showreg ="";
        select_li = "error";
        for(var i=0;i<select.length;i++){
            if(select[i].name=="" && select[i].code==""){
                showreg +="";
            }else {
                showreg += '<li class="list-group-item selectedli"  id="'+select[i].code+'">'+select[i].name+'</li>';
            }
        }
        $("#selectreg").append(showreg);
    });
    /**
     * 删除单个地区
     */
    $("#delsiggle").click(function () {
        if(select_li !="error"){
            var code = $('ul.regul').find("li").eq(select_li).attr("id");
            for(var i=0;i<select.length;i++){
                if(select[i].code== code){
                    select.splice(i, 1);
                }
            }
            $('ul.regul').find("li").eq(select_li).remove();
            select_li = "error";
        }
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
                    $('ul.regul').html("");
                    select_li = "error";
                    var showreg ="";
                    for(var i=0;i<select.length;i++){
                        if(select[i].name=="" && select[i].code==""){
                            showreg +="";
                        }else {
                            showreg += '<li class="list-group-item selectedli"  id="'+select[i].code+'">'+select[i].name+'</li>';
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
        $('ul.regul').html("");
        select_li = "error";
        select = [];
    });
    /**
     * 绑定li标签点击效果
     */
    $("#selectreg").on('click','.list-group-item', function() {
        $(this).siblings('li').css("background","#ffffff");
        $(this).css("background","#f6f6f6");
        select_li = $(this).index();
    }) ;
    /**
     * 时间选择自动补上中间的时间期，以及数据检查
     */
    var selecttime = "";//时间
    var zbcode = "";//指标code
    var zbco = "";//指标主体
    var zbds = "";//指标数据来源
    var zbunit ="";//指标单位
    var zbname = "";//指标名称

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
            for (var i = endtime; i >= begintime ; i--) {
                selecttime += i+",";
            }
            var zbs=zbAdd.zbs;//获取指标的信息
            for (var i = 0; i <zbs.length ; i++) {
                zbcode += zbs[i].zbcode+",";
                zbco += zbs[i].cocode+",";
                zbds += zbs[i].dscode+",";
                zbname += zbs[i].zbname+",";
                zbunit += zbs[i].unitcode+",";
            }
            zbcode = zbcode.substr(0, zbcode.length - 1);//去除最后一个逗号
            zbco = zbco.substr(0, zbco.length - 1);//去除最后一个逗号
            zbds = zbds.substr(0, zbds.length - 1);//去除最后一个逗号
            zbname = zbname.substr(0, zbname.length - 1);//去除最后一个逗号
            zbunit = zbunit.substr(0, zbunit.length - 1);//去除最后一个逗号
            selecttime = selecttime.substr(0, selecttime.length - 1);//去除最后一个逗号
            var regselect ="";
            var regselectname ="";
            for (var i = 0; i <select.length ; i++) {
                regselect += select[i].code+",";
                regselectname +=select[i].name+",";
            }
            regselect = regselect.substr(0, regselect.length - 1);//去除最后一个逗号
            regselectname = regselectname.substr(0, regselectname.length - 1);//去除最后一个逗号

            $.pjax({
                url: common.rootPath+'zbdata/zsjhedit.htm?m=getCheckData&indexcode='+incode,
                type: "post",
                data: {"reg": regselect,"regname":regselectname,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit},
                container:'.data_check_show'
            });
            $(document).on('pjax:success', function() {
               // uniteTable(tabledata,1);
                $("#tabledata").show();
                $('ul.regul').html("");
                select_li = "error";
                var showreg ="";
                var checkdata = $('input[id=checkreturn]').val();
                checkdata = checkdata.substr(0, checkdata.length - 1);//去除最后一个逗号
                var checkreturn = checkdata.split(",");
                console.log(checkdata)
                for(var i=0;i<select.length;i++){
                    if(select[i].name=="" && select[i].code==""){
                        showreg +="";
                    }else {
                        if(checkreturn[i]==0){
                            showreg += '<li class="list-group-item clickli"  id="'+select[i].code+'">'+select[i].name+'<span  class="badge"><i class="glyphicon glyphicon-ok"></i></span></li>';
                        }
                       else {
                            showreg += '<li class="list-group-item clickli"  id="'+select[i].code+'">'+select[i].name+'<span  class="badge"><i class="glyphicon glyphicon-remove"></i></span></li>';
                        }
                    }
                }
                $("#selectreg").append(showreg);
            });
        }
    });
    $("#selectreg").on('click','.clickli', function() {
        var reg =$(this).attr("id");
        $.pjax({
            url: common.rootPath+'zbdata/zsjhedit.htm?m=getCheckSingle&indexcode='+incode,
            type: "post",
            data: {"reg": reg,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit},
            container:'.data_check_show'
        });
        $(document).on('pjax:success', function() {
            $("#tabledata").hide();
            $("#data_single").show();
        });
    }) ;
    /**
     * 合并第一列
     * @param tb的id
     * @param colLength要合并的列数
     */
    function   uniteTable(tb,colLength){
        var   i=0;
        var   j=0;
        var   rowCount=tb.rows.length; //   行数
        var   colCount=tb.rows[0].cells.length; //   列数
        var   obj1=null;
        var   obj2=null;
        //   为每个单元格命名
        for   (i=0;i<rowCount;i++){
            for   (j=0;j<colCount;j++){
                tb.rows[i].cells[j].id="tb__"   +   i.toString()   +   "_"   +   j.toString();
            }
        }
        //   逐列检查合并
        for   (i=0;i<colCount;i++){
            if   (i==colLength)   return;
            obj1=document.getElementById("tb__0_"+i.toString())
            for   (j=1;j<rowCount;j++){
                obj2=document.getElementById("tb__"+j.toString()+"_"+i.toString());
                if   (obj1.innerText   ==   obj2.innerText){
                    obj1.rowSpan++;
                    obj2.parentNode.removeChild(obj2);
                }else{
                    obj1=document.getElementById("tb__"+j.toString()+"_"+i.toString());
                }
            }
        }
    }
});