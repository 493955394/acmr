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



    $("#zssx").dragwidth({
        maxwidth:500
    });
    autodrag();
    $(window).resize(function(){
        autodrag();
    });
    function autodrag(){
       // $(".right-panel").css('height','auto');
       // var rch = $(".panel-height").height()-$("#top_div").height();
        var rch = $(window).height()-$(".ict-header").outerHeight(true) - $('.savediv').height() - $('.ict-footer').height() - $('#top_div').height() - $('#ict-header').outerHeight() - 80;

        $(".dragline").height(rch);

       // console.log($('.left-panel').height())
    }

    autoHeight();
    $(window).resize(function(){
        autoHeight()
    });

    function autoHeight() {
        var rch = $(window).height()-$(".ict-header").outerHeight(true) - $('.savediv').height() - $('.ict-footer').height() - $('#top_div').height() - $('#ict-header').outerHeight() - 70;
        $(".tab-content").height(rch);
        //$('.panel_container').height(300);
        $('#zssx #tree_and_find, #zssx .zssx-right,.panel_container').height(rch - 10);
    }

    //全屏
    $('#btn-fullscreen').click(function(e){
        var $all_pane = $("#edit_container");
        $all_pane.toggleClass('fullscreen');

        var $tab_pane = $(".tab-content");
        $tab_pane.toggleClass('leftfull');

        var $edit_tabs=$(".edit_tab")
        $edit_tabs.each(function () {
            $(this).toggleClass('leftfull')
        })

        //zssx
        var $zssx_right=$('.zssx-right');
        $zssx_right.toggleClass('rightfull');
        var $right_panel=$('.right-panel');
        $right_panel.toggleClass('rightfull');
        var $panel_container=$('.panel_container');
        $panel_container.toggleClass('rightfull');
        var $left_panel=$('#tree_and_find');
        $left_panel.toggleClass('leftfull');
        var $dragline=$('.dragline');
        $dragline.toggleClass('leftfull');

        //jsfw
        var $jsfw_col=$(".jsfw_col")
        $jsfw_col.each(function () {
            $(this).toggleClass('container_full')
        })
        var $dqs=$("#dqs")
        $dqs.toggleClass('leftfull')
        var $dqlb=$("#dqlb")
        $dqlb.toggleClass('leftfull')
        var $data_check_container=$("#data_check_container")
        $data_check_container.toggleClass('leftfull')
        var $regtable=$(".regtable")
        $regtable.toggleClass("container_full")
        $(".add-or-del").toggleClass("add-padding")

        //mxgh
        var $module_tab_container=$(".module_tab_container")
        $module_tab_container.each(function () {
            $(this).toggleClass('container_full')
        })
        var $module_tree_container_body=$("#module_tree_container_body")
        $module_tree_container_body.toggleClass('rightfull')
        var $module_table=$(".J_zsjh_module_table")
        $module_table.toggleClass("container_full")


        //save and footer
        $(".savediv").toggleClass('savedivfull');
        $(".ict-footer").toggleClass('savedivfull');
        $(".btn-fullscreen").toggleClass('min-fullscreen')
    });

    var rch = $(window).height()-$(".ict-header").outerHeight(true) - $('.savediv').height() - $('.ict-footer').height() - $('#top_div').height() - $('#ict-header').outerHeight() - 70;
    $("#module_tree_container").height(rch);

    $("#module_container").height(rch);
    $("#module_tree_container_body").height(rch);
    $(".J_zsjh_module_table").height(rch*0.7);
    $("#dqs").height(rch*0.8);
    $("#dqlb").height(rch*0.8);
    $(".data_single").height(rch*0.8);
    $(".regtable").height(rch*0.8);


    var incode=$("#index_code").val();
    var select = [];
    var select_li = "error";//选择移除的li的下标
    var timesort = $("#index_sort option:selected").val();
    //默认选中第一个指标

    var zNodes =[
        { id:"!1", pId:0, name:"指数",isParent:true,icon:"../css/img/mark1.png"}
    ];
    var st = new Date().getTime();//时间戳
    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getCateTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback:{
            onClick:clickEvent
        }
    };
    function clickEvent(event,treeId,treeNode) {
        if (treeNode.id != '!1') {
            $('input[name=proname]').val(treeNode.name);
            $('input[name=index_procode]').val(treeNode.id);
            //console.log($('input[name=index_procode]').val())
        } else {
            $('input[name=proname]').val('指数');
        }
        $.fn.zTree.init($("#tree"), setting, zNodes);
    }

    /* //修复图标，使没有子节点的目录也显示为目录
     function fixIcon(){
         var treeObj = $.fn.zTree.getZTreeObj("tree");
         //过滤出sou属性为true的节点（也可用你自己定义的其他字段来区分，这里通过sou保存的true或false来区分）
         var folderNode = treeObj.getNodesByFilter(function (node) { return node.sou});
         for(var j=0 ; j<folderNode.length; j++){//遍历目录节点，设置isParent属性为true;
             folderNode[j].isParent = true;
         }
         treeObj.refresh();//调用api自带的refresh函数。
     }*/
    $(document).ready(function(){
        if($('input[name=index_procode]').val()==""){
            $('input[name=proname]').val("指数");
        }
        $('ul.regul').find('li').each(function() {
            select.push({code:$(this).attr("id"),name:$(this).text()});
        })
        $.fn.zTree.init($("#tree"), setting, zNodes);
        //修正添加的table的classname，方便和树联动
    });
    //点击指标筛选，激活第一个已选指标
    $(document).on('click','a[href="#zssx"]',function (event) {
        event.preventDefault()
        //console.log("zssx")
        if ($(".zb_panel").length>0){
            $(".zb_panel")[0].click()
        }
    })
    /**
     * 菜单树
     */
    var treeNodeId = $("#procode").val();
    var treeNodeName = "地区树";
    var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
    var setting1 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=findRegTree&st='+st+"&icode="+incode,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent1
        }
    };
    function clickEvent1(event, treeid, treeNode) {
        treeNodeId = treeNode.id;
        treeNodeName = treeNode.name;
        if (treeNode.id != '') {
            $('input[name=regname]').val(treeNode.name);
            $('input[name=regcode]').val(treeNode.id);}


    }
    var rootNode = [{"id":"","name":"地区树", "open":"true", "isParent":"true",icon:"../css/img/mark1.png"}];
    var treeObj = $.fn.zTree.init($("#treeDemo"), setting1, rootNode);
    var treenodes = treeObj.getNodes();
    treeObj.expandNode(treenodes[0], true, true, true);
    /**
     * 选中单个地区
     */

    $(document).on("click","#sigglechoose",function (event) {
        event.preventDefault();
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
        $(".regs-title i").remove();
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
    $(document).on('click','#delsiggle',function (event) {
        event.preventDefault();
        $(".regs-title i").remove();
        if(select_li !="error"){
            var code = $('ul.regul').find("li").eq(select_li).attr("id");
            for(var i=0;i<select.length;i++){
                if(select[i].code== code){
                    select.splice(i, 1);
                }
            }
            $('ul.regul').find("li").eq(select_li).remove();
            //地区列表中的数字也要对应变，如果有的话
            var regflag=0;
            var total=0 ;
            $(".regul li i").each(function(){
                var arr = $(this).attr("data-check");
                if(arr==0){
                    regflag++;
                }
                total++;
            });
            if(total>0){
                $(".regs-title").append("<i style='float: right;color: red'>"+regflag+"/"+total+"</i>")
            }
            select_li = "error";
        }
    });
    /**
     * 选中某地区下所有地区
     */
    $(document).on("click","#chooseall",function (event) {
        event.preventDefault();
        var procode =  $('input[name=regcode]').val();
        if (procode){
            $.ajax({
                url: common.rootPath+'zbdata/zsjhedit.htm?m=getResultLeft',
                type: "post",
                data: {"procode": procode,"icode":incode},
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
                    $(".regs-title i").remove();
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
    $(document).on("click","#delall",function (event) {
        event.preventDefault();
        $(".regs-title i").remove();
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
        $("#single_reg").val($(this).attr('id'))
    }) ;
    /**
     * 时间选择自动补上中间的时间期，以及数据检查
     */
    var selecttime = "",//时间
        checknum = 0,
        checkreturn=[];
    var timesort = $("#index_sort option:selected").val();

    $(document).on("click","#datachecks",function (event) {
        event.preventDefault();

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
                var sub = begintime.substring(begintime.length-2);
                if(sub == "00"||parseInt(sub)>12){
                    alert("您的时间格式有误")
                    return;
                }
                var dsub = endtime.substring(endtime.length-2);
                if(dsub == "00"||parseInt(dsub)>12){
                    alert("您的时间格式有误")
                    return;
                }
                if(r==null||s==null){
                    alert("您的时间格式有误");
                    return;
                }
                else{
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
            $("#down_data").attr("style","float: right;font-size: 10px;margin-left: 10px;display:block;");
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
                async:false,
                data: {"reg": regselect,"regname":regselectname,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit},
                container:'.data_check_show',
                timeout:50000
            })
            mc('tabledata',0,0,0);
            $(".data_single").hide();
            $(".regtable").show();
            var headHeight=$(".regs-data-check1").height()
            //$(".table_body").css("margin-top",headHeight)
            var bodyWidth=$(".regs-data-check1").width()
            $(".regs-data-check").width(bodyWidth)
            $('.tb-head').height(headHeight);
            $('.tb-head').width($('.tb-body').width()-17);
            $(".table-th").each(function () {
                var thisId=$(this).attr("id")
                var thisWidth=$("."+thisId).width()
                $(this).width(thisWidth)
            })
            $('.tb-body').on('scroll', function(){
                var left = $(this).scrollLeft();
                if(left > 0){
                    $('.tb-head table').css({'left': -left});
                }else{
                    $('.tb-head table').css({'left': 0});
                }
            });
            $('ul.regul').html("");
            select_li = "error";
            var showreg ="";
           /* checkdata = $('input[id=checkreturn]').val();*/
            checkreturn=[];
            checknum=0;
            for (var i = 2; i <$("#tabledata th").length ; i++) {
                var checkRed = false;
                $("#tabledata tr").each(function()
                {
                    if($(this).find("td").eq(i).hasClass("red"))//要是有红这个class,表示数据不全
                        checkRed=true
                })
                if (checkRed){//数据不全
                    checkreturn.push(1);
                }else { checknum++;
                checkreturn.push(0);}
            }
            for(var i=0;i<select.length;i++){
                if(select[i].name=="" && select[i].code==""){
                    showreg +="";
                }else {
                    if(checkreturn[i]=="0"){
                        showreg += '<li class="list-group-item selectedli"  id="'+select[i].code+'">'+select[i].name+'<span  class="badge"><i class="data-ok clickli" data-id="'+select[i].code+'" data-check="0"></i></span></li>';
                    }
                    else {
                        showreg += '<li class="list-group-item selectedli"  id="'+select[i].code+'">'+select[i].name+'<span  class="badge"><i class="data-remove clickli" data-id="'+select[i].code+'" data-check="1"></i></span></li>';
                    }
                }
            }
            $("#selectreg").append(showreg);
            $(".regs-title i").remove();//先把之前的移除
            $(".regs-title").append("<i style='float: right;color: red'>"+checknum+"/"+checkreturn.length+"</i>")
        }
    });

    $("#selectreg").on('click','.clickli', function() {
        var reg =$(this).attr("data-id");
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
            async:false,
            data: {"reg": reg,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit},
            container:'.data_check_show',
            timeout:50000
        })
        $(".regtable").hide();
        $(".data_single").show();
        var headHeight=$(".regs-data-check3").height()
        var bodyWidth=$(".regs-data-check3").width()
        $(".regs-data-check2").width(bodyWidth)
        $('.tb-head1').height(headHeight);
        $('.tb-head1').width($('.tb-body1').width()-17);
        $(".table-th1").each(function () {
            var thisId=$(this).attr("id")
            var thisWidth=$("."+thisId).width()
            $(this).width(thisWidth)
        })
        $('.tb-body1').on('scroll', function(){
            var left = $(this).scrollLeft();
            if(left > 0){
                $('.tb-head1 table').css({'left': -left});
            }else{
                $('.tb-head1 table').css({'left': 0});
            }
        });
    }) ;

    /*   /!**
        * 检查数据是否完整
        *!/
       function checkTable(tableId){
           var tb = document.getElementById(tableId);
           if(tb.rows.length==0) return false;
           if(tb.rows[0].cells.length==0) return false;
           for(var i=0;i<tb.rows.length;i++){
               if(tb.rows[0].cells.length!=tb.rows[i].cells.length) return false;
           }
           return true;
       }*/
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
            data:{"icode":incode},
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
     * 单个地区的下载
     */
    $(document).on('click', '.J_excel_singlereg', function() {
        var reg=$("#single_reg").val()
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
        var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toExcelSinglereg&indexcode='+incode+'&reg='+reg+'&sj='+selecttime+'&zb='+zbcode+'&co='+zbco+'&ds='+zbds+'&zbname='+zbname+'&zbunit='+zbunit;
        $.ajax({
            url: url,
            type: 'post',
            //data: {"reg": reg,"sj":selecttime,"zb":zbcode,"co":zbco,"ds":zbds,"zbname":zbname,"zbunit":zbunit},
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
            } else {
                startRow = i + 1;
            }
        }
    }
    //基本信息页取消退出该页
    $(".resetindex").click(function () {
        window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+incode;
    })
    //右下角保存按钮
    $(document).on('click','.tosaveall',function (event) {
        event.preventDefault();
        var checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查

        if (!checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'required', 'msg': '计划名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'maxlength', 'msg': '计划名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="startpeirod"]'), [{ 'name': 'required', 'msg': '起始数据期不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="startpeirod"]'), [{ 'name': 'ch', 'msg': '起始数据期不能包含汉字' }])) {
            flag = false;
        }
        if ($('input[name="delayday"]').val()=="") {
            flag = false;
            $('.normalday').append('<label class="control-label tips-error">该项不能为空</label>');
        }
        if (flag == false) {
            alert("基本信息不全！")
            $('#bjjhTab li:eq(0) a').tab('show');
            return;
        }

        var timetext = $("#startpeirod").val();
        if(timesort == "y"){
            var reg=/^\d{4}$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的"+"年度"+"起始数据期格式有误")
                $('#bjjhTab li:eq(0) a').tab('show');
                return;
            }
        }else if(timesort == "q"){
            var reg=/^(\d{4})([A-D]{1})$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的季度起始数据期格式有误")
                $('#bjjhTab li:eq(0) a').tab('show');
                return;
            }
        }else if(timesort == "m"){
            var reg=/^\d{6}$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的"+"月度"+"起始数据期格式有误")
                $('#bjjhTab li:eq(0) a').tab('show');
                return;
            }
            var sub = timetext.substring(timetext.length-2);
            if(sub == "00"||parseInt(sub)>12){
                alert("您的"+"月度"+"起始数据期格式有误")
                $('#bjjhTab li:eq(0) a').tab('show');
                return;
            }
        }
        var regDelayDays=/^[0-9]*$/;
        var r=$('input[name="delayday"]').val().match(regDelayDays);
        if(r==null){
            alert("您的"+"数据期时间间隔"+"有误");
            $('#bjjhTab li:eq(0) a').tab('show');
            return;
        }
        //名字只能是中文和字母
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="index_cname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            $('#bjjhTab li:eq(0) a').tab('show');
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
        if(zbs.length==0){
            alert("指标未选择")
            $('#bjjhTab li:eq(1) a').tab('show');
        }
        if(select.length==0){
            alert("地区未选择")
            if(zbs.length==0){
                $('#bjjhTab li:eq(1) a').tab('show');
            }
            else {
                $('#bjjhTab li:eq(2) a').tab('show');
            }
        }
        if(zbs.length==0 || select.length==0){
            return;
        }
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
                    var index = $("#bjjhTab li.active").index();
                    if(index == 3)//如果是模型规划的tab页才回首页
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+incode;
                }
                if (data.returncode == 301) {
                    alert("该计划名称已经存在！");
                }
                if (data.returncode == 501) {
                    alert("保存失败！");
                }
            }
        })

    })
});