define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        tab = require('tab'),
        zbAdd=require('js/func/zhzs/zsjhEdit/zbAdd'),
        editjsp = require('editjsp'),
        dragwidth = require('dragwidth');

    $("#zssx").dragwidth();
    autodrag();
    $(window).resize(function(){
        autodrag();
    });
    function autodrag(){
        $(".right-panel").css('height','auto');
        var rch = $(window).height() - $('.top_div').outerHeight();
        if($(".right-panel").height() <= rch){
            $(".right-panel").height(rch);
            $(".left-panel, .dragline").height(rch);
        }else{
            $(".left-panel, .dragline").height($(".right-panel").height());
        }
    }

    var incode=$("#index_code").val();
    var select = [];
    var select_li = "error";//选择移除的li的下标
    var timesort = $("#index_sort option:selected").val();
    //默认选中第一个指标
    $(document).ready(function () {
        if ($(".zb_panel").length>0){
            $(".zb_panel")[0].click()
        }
    })

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
            $('input[name=index_procode]').val(treeNode.id);
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
        $('ul.regul').find('li').each(function() {
            select.push({code:$(this).attr("id"),name:$(this).text()});
            })
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
    $("#selectreg").on('click','.selectedli', function() {
        $(this).siblings('li').css("background","#ffffff");
        $(this).css("background","#f6f6f6");
        select_li = $(this).index();
    }) ;
    /**
     * 时间选择自动补上中间的时间期，以及数据检查
     */
    var selecttime = "",//时间
        checkdata="",
        checkreturn=[];
    var timesort = $("#index_sort option:selected").val();

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
            //对时间做补齐操作
            if(timesort == "y"){//如果是年度
                var reg=/^\d{4}$/;
                var r= begintime.match(reg);
                var s= endtime.match(reg);
                if(r==null||s==null){
                    alert("您的时间格式有误");
                    return;
                }else{
                    for (var i = endtime; i >= begintime ; i--) {
                        selecttime += i+",";
                    }
                }
            }else if(timesort == "q"){//如果是季度的话
                var reg=/^(\d{4})([A-Da-d]{1})$/;
                var r= begintime.match(reg);
                var s= endtime.match(reg);
                if(r==null||s==null){
                    alert("您的时间格式有误");
                    return;
                }else{
                    var eyear = endtime.slice(0,4);
                    var byear = begintime.slice(0,4);
                    var eyear1 = endtime.toUpperCase().slice(4,5).charCodeAt();
                    var byear1 = begintime.toUpperCase().slice(4,5).charCodeAt();//转为ascii码
                    if(eyear == byear){//如果是同一年
                        for (var j = eyear1; j >=byear1 ; j--) {
                            selecttime += eyear+String.fromCharCode(j)+",";
                        }
                    }else{//如果不是同一年
                        for (var i = eyear; i >= byear ; i--) {
                            if(i == eyear){//处理第一年
                                for (var j = eyear1; j >64 ; j--) {
                                    selecttime += i+String.fromCharCode(j)+",";
                                }
                            }
                            else if(byear<i && i<eyear) {//处理中间年
                                for (var j = 68; j >64 ; j--) {
                                    selecttime += i+String.fromCharCode(j)+",";
                                }
                            }
                            else if(i == byear){
                                    for (var j = 68; j >=byear1 ; j--) {
                                        selecttime += i+String.fromCharCode(j)+",";
                                    }
                                }
                            }
                        }
                    }
            }else if(timesort == "m"){//如果是月度的话
                var reg=/^\d{6}$/;
                var r= begintime.match(reg);
                var s= endtime.match(reg);
                if(r==null||s==null){
                    alert("您的时间格式有误");
                    return;
                }else{
                    var eyear = endtime.slice(0,4);
                    var byear = begintime.slice(0,4);
                    var eyear1 = parseInt(endtime.slice(4,6));
                    var byear1 = parseInt(begintime.slice(4,6));
                    if(eyear == byear){//如果是同一年
                        for (var j = eyear1; j >=byear1 ; j--) {
                            if(j<10){
                                selecttime += eyear.toString()+"0"+j.toString()+",";
                            }
                            else{
                                selecttime += eyear.toString()+j.toString()+",";
                            }
                        }
                    }else{//如果不是同一年
                        for (var i = eyear; i >= byear ; i--) {
                            if(i == eyear){//处理第一年
                                for (var j = eyear1; j >=1 ; j--) {
                                    if(j<10){
                                        selecttime += i.toString()+"0"+j+",";
                                    }
                                    else{
                                        selecttime += i.toString()+j.toString()+",";
                                    }
                                }
                            }
                            else if(byear<i && i<eyear) {//处理中间年
                                for (var j = 12; j >1 ; j--) {
                                    if(j<10){
                                        selecttime += i.toString()+"0"+j.toString()+",";
                                    }
                                    else{
                                        selecttime += i.toString()+j.toString()+",";
                                    }
                                }
                            }
                            else if(i == byear){
                                for (var j = 12; j >=byear1 ; j--) {
                                    if(j<10){
                                        selecttime += i.toString()+"0"+j.toString()+",";
                                    }
                                    else{
                                        selecttime += i.toString()+j.toString()+",";
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var zbcode = "";//指标code
            var zbco = "";//指标主体
            var zbds = "";//指标数据来源
            var zbunit ="";//指标单位
            var zbname = "";//指标名称
            var zbs=zbAdd.zbs;//获取指标的信息
            if(zbs.length == 0){
                alert("未筛选指标");
                return;
            }
            if(select.length == 0){
                alert("未筛选地区");
                return;
            }
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
            })
            $(document).on('pjax:success', function() {
                    mc('tabledata',0,0,0);
                    $("#data_single").hide();
                    $("#regtable").show();
                    $('ul.regul').html("");
                    select_li = "error";
                    var showreg ="";
                    checkdata = $('input[id=checkreturn]').val();
                    checkreturn = checkdata.split(",");
                    for(var i=0;i<select.length;i++){
                        if(select[i].name=="" && select[i].code==""){
                            showreg +="";
                        }else {
                            if(checkreturn[i]=="0"){
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
        var zbcode = "";//指标code
        var zbco = "";//指标主体
        var zbds = "";//指标数据来源
        var zbunit ="";//指标单位
        var zbname = "";//指标名称
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
        $.pjax({
            url: common.rootPath+'zbdata/zsjhedit.htm?m=getCheckSingle&indexcode='+incode,
            type: "post",
            data: {"reg": reg,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit,"checkdata":checkdata},
            container:'.data_check_show'
        })
        $(document).on('pjax:success', function() {
            $("#regtable").hide();
            $("#data_single").show();
        });
    }) ;

    /**
     * 检查数据是否完整
     */
    function checkTable(tableId){
        var tb = document.getElementById(tableId);
        if(tb.rows.length==0) return false;
        if(tb.rows[0].cells.length==0) return false;
        for(var i=0;i<tb.rows.length;i++){
            if(tb.rows[0].cells.length!=tb.rows[i].cells.length) return false;
        }
        return true;
    }
    /**
     * 数据下载
     * @author wf
     * @date
     * @param []
     * @return
     */
    $(document).on('click', '.J_plan_excel', function() {
        var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toExcel';
        $.ajax({
            url: url,
            type: 'post',
            success: function(data) {
                if (data.returncode == 300) {
                    alert("请选择筛选条件");
                } else {
                    alert("下载成功!");
                }
            }
        })
        window.location.href = url;
    });
    $(document).on('click', '.J_excel_singlereg', function() {
        var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toExcelSinglereg';
        $.ajax({
            url: url,
            type: 'post',
            success: function(data) {
                if (data.returncode == 300) {
                    alert("请选择筛选条件");
                } else {
                    alert("下载成功!");
                }
            }
        })
        window.location.href = url;
    });
    /**
     * 合并第一列
     * @param tb的id
     * @param colLength要合并的列数
     */
    function mc(tableId, startRow, endRow, col) {
        var tb = document.getElementById(tableId);
        if (col >= tb.rows[0].cells.length) {//第一行的列数
            return;
        }
        if (col == 0) { endRow = tb.rows.length-1; }
        for (var i = startRow; i < endRow; i++) {
            if (tb.rows[startRow].cells[col].innerHTML == tb.rows[i + 1].cells[0].innerHTML) {
                tb.rows[i + 1].removeChild(tb.rows[i + 1].cells[0]);
                tb.rows[startRow].cells[col].rowSpan = (tb.rows[startRow].cells[col].rowSpan | 0) + 1;
                if (i == endRow - 1 && startRow != endRow) {
                    mc(tableId, startRow, endRow, col + 1);
                }
            } else {
                mc(tableId, startRow, i + 0, col + 1);
                startRow = i + 1;
            }
        }
    }
    /**
     * 模型规划搜索框
     */
    var delIds = [];
    var isMove = true;
    var searchField = "";
    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            requestUrl = $(self).prop('action'),
            key = $('select',self).val(),
            val = $('input',self).val(),
            str = "";
        var requestData = common.formatData(key,val);
        if(requestData.length>0){
            requestData="&"+requestData;
        }
        searchField = requestData+str;
        isMove = false;
        $.pjax({
            url: requestUrl+searchField+'&icode='+incode,
            container: '.J_zsjh_module_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
    });
    //基本信息页重置
    $(".resetindex").click(function () {
        $("input[type=reset]").trigger("click");
    })
    //右下角保存按钮
    $(document).on('click','.tosaveall',function (event) {
        event.preventDefault();
       var checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        var timetext = $("#startpeirod").val();
            if(timesort == "y"){
                var reg=/^\d{4}$/;
                var r= timetext.match(reg);
                if(r==null){
                    alert("您的"+"年度"+"起始数据期格式有误")
                    return;
                }
            }else if(timesort == "q"){
                var reg=/^(\d{4})([A-D]{1})$/;
                var r= timetext.match(reg);
                if(r==null){
                    alert("您的季度起始数据期格式有误")
                    return;
                }
            }else if(timesort == "m"){
                var reg=/^\d{6}$/;
                var r= timetext.match(reg);
                if(r==null){
                    alert("您的"+"月度"+"起始数据期格式有误")
                    return;
                }
            }
                var regDelayDays=/^[0-9]*$/;
                var r=$('input[name="delayday"]').val().match(regDelayDays);
                if(r==null){
                    alert("您的"+"数据期时间间隔"+"有误");
                    return;
                }
        if (!checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'required', 'msg': '计划名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'maxlength', 'msg': '计划名称最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="startpeirod"]'), [{ 'name': 'required', 'msg': '起始数据期不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="startpeirod"]'), [{ 'name': 'ch', 'msg': '起始数据期不能包含汉字' }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="delayday"]'), [{ 'name': 'required', 'msg': '该项不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="delayday"]'), [{ 'name': 'ch', 'msg': '不能包含汉字' }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var zbcode = "";//指标code
        var zbco = "";//指标主体
        var zbds = "";//指标数据来源
        var zbunit ="";//指标单位
        var sxcode = "";//指标名称
        var regselect ="";//地区信息
        var zbs=zbAdd.zbs;//获取指标的信息
        for (var i = 0; i <zbs.length ; i++) {
            zbcode += zbs[i].zbcode+",";
            zbco += zbs[i].cocode+",";
            zbds += zbs[i].dscode+",";
            sxcode += zbs[i].code+",";
            zbunit += zbs[i].unitcode+",";
        }
        zbcode = zbcode.substr(0, zbcode.length - 1);//去除最后一个逗号
        zbco = zbco.substr(0, zbco.length - 1);//去除最后一个逗号
        zbds = zbds.substr(0, zbds.length - 1);//去除最后一个逗号
        sxcode = sxcode.substr(0, sxcode.length - 1);//去除最后一个逗号
        zbunit = zbunit.substr(0, zbunit.length - 1);//去除最后一个逗号
        for (var i = 0; i <select.length ; i++) {
            regselect += select[i].code+",";
        }
        regselect = regselect.substr(0, regselect.length - 1);//去除最后一个逗号
        $("#indexForm").serialize();
        $.ajax({
            url: common.rootPath+'zbdata/zsjhedit.htm?m=toSaveAll',
            data: $.param({'zbcode':zbcode,'zbco':zbco,'zbds':zbds,'sxcode':sxcode,'zbunit':zbunit,'regselect':regselect})+'&'+$("#indexForm").serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.href= common.rootPath+"zbdata/indexlist.htm";
                }
               else {
                    alert("添加失败");
                }
            }
        })

    })
});