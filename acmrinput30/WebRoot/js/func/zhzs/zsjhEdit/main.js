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

        $(".add-or-del").toggleClass("add-padding")


        var $regtable=$(".regtable")
        $regtable.toggleClass("container_full")
        var $reg_tb_body=$("#reg_tb_body")
        $reg_tb_body.toggleClass("container_full")



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
        if ($(".zb_panel").length>0){
            $(".zb_panel")[0].click()//激活第一个已选指标
        }
        $.fn.zTree.init($("#tree"), setting, zNodes);
        //修正添加的table的classname，方便和树联动

        //默认显示全屏
        $(".btn-fullscreen").click();
        var currenttab = $("#currentTab").val();
        if(currenttab!="")
            $("#bjjhTab a[href='#jsfw']").click();
        if($("#sjss").val()!=""){
            $("#mySelectTime1 .dttext").val($("#sjss").val())
            $("#mySelectTime1 .dttextbtn").click();
        }


        /*
        $(".zb_checkbox").each(function () {
            console.log($(this).attr('id'))
            setTimeout(function () {
                $(this).change(function () {
                    console.log("change")
                })
            },500)

        })*/

        //绑定点击复选框事件
        $('.J_zsjh_rangedata_table').on('pjax:success',function () {
            console.log("pjaxsuccess")
            update()
            $(".zb_checkbox").each(function () {
                $(this).change(update)
            })
            $(".reg_checkbox").each(function () {
                $(this).change(update)
            })
        })
    });

    function update(){
        //console.log("change")
        var regnum=$(".reg_checkbox:checked").length;
        var zbnum=$(".zb_checkbox:checked").length;
        var info="注：已选择地区数"+regnum+"个，已选择指标数"+zbnum+"个"
        $("#check_info").text(info)
    }

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
            $('input[name=regcode]').val(treeNode.id);
        }


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

    /*
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
    //基本信息页取消退出该页
    $(".resetindex").click(function () {
        window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+incode;
    })
    //右下角保存按钮
    $(document).on('click','.tosaveall',function (event) {
        event.preventDefault();
        if( $("#bjjhTab li.active").index()==5){
            var checkDelegate = new VaildNormal();
            var flag = true;
            //前端检查

            if (!checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'required', 'msg': '计划名称不能为空' }]) ||
                !checkDelegate.checkNormal($('input[name="index_cname"]'), [{ 'name': 'maxlength', 'msg': '计划名称最大长度为50', 'param': 51 }])) {
                flag = false;
            }
            if ($('input[name="delayday"]').val()=="") {
                flag = false;
                $('.normalday').append('<label class="control-label tips-error">该项不能为空</label>');
            }
            if (flag == false) {
                return;
            }
            var regDelayDays=/^[0-9]*$/;
            var r=$('input[name="delayday"]').val().match(regDelayDays);
            if(r==null){
                alert("您的"+"数据期时间间隔"+"有误");
                $('#bjjhTab li:eq(5) a').tab('show');
                return;
            }
            //名字只能是中文和字母
            var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
            var z = $('input[name="index_cname"]').val().match(namecheck);
            if(z==null){
                alert("名称含有不规则字符，请修改");
                $('#bjjhTab li:eq(5) a').tab('show');
                return;
            }
        }
        $.ajax({
            url: common.rootPath+'zbdata/zsjhedit.htm?m=toSaveAll',
            data: $("#indexForm").serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    var index = $("#bjjhTab li.active").index();
                    if(index == 5)//如果是模型规划的tab页才回首页
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+incode;
                }
               else if (data.returncode == 301) {
                    alert("该计划名称已经存在！");
                }
                else{
                    alert("保存失败！");
                }
            }
        })
    })
    
    /*
    * 地区树新增搜索
    * 
    */
    $(".reg_search").click(regsearch)
   function regsearch(){
       var value=$("#regQueryData").val()
       if(value!=""){
           $("#reg_find").remove();
           $.ajax({
               url:common.rootPath+'zbdata/zsjhedit.htm?m=regFind&query='+value+"&icode="+incode,
               type:'get',
               success:function (re) {
                   var regre=[];
                   for(var i=0;i<re.length;i++){
                       regre.push({name:re[i].cname,code:re[i].code})
                   }
                   $("#treeDemo").css("display","none")
                   $("#dqs").append("<div id='reg_find' class='panel panel-default'><div class='panel-heading'><button type='button' class='btn btn-primary btn-sm clear_find'>清除搜索结果</button></div>")
                   for(var j=0;j<regre.length;j++){
                       $("#reg_find").append("<div class='panel-body result_panel'><a class='regname_choose' href='#' id='" +
                           regre[j].code+"'>" +
                           regre[j].name+"</a></div></div>")
                   }
                   $(".clear_find").click(clearFindResult)
                 $(".regname_choose").click(function () {
                       var clickcode= $(this).attr("id")
                       var regname=$(this).html();
                       clearFindResult()
                     //获得路径
                     $.ajax({
                         url:common.rootPath+'zbdata/zsjhedit.htm?m=getRegpath&code='+clickcode+"&icode="+incode,
                         type:'get',
                         success:function (re) {
                             re.push(clickcode)
                             expandRegTree(re)
                             $('input[name=regname]').val(regname);
                             $('input[name=regcode]').val(clickcode);
                         }
                     })
                   })
               }
           })
       }
   }
    function clearFindResult() {
        $("#reg_find").remove();
        $("#treeDemo").css("display","block");
    }
    function expandRegTree(path) {
        $.ajaxSettings.async = false
        var treeObj = $.fn.zTree.init($("#treeDemo"), setting1, rootNode);
        var node = treeObj.getNodeByParam("id", "")
        treeObj.expandNode(node)
        treeObj.selectNode(node)
        setting1.callback.onClick(null, treeObj.setting.treeId, node)

        for (var i = 0; i < path.length; i++) {
            if (node.isParent == true) {
                var nodes = node.children;
                for (var j = 0; j < nodes.length; j++) {
                    if (nodes[j].id == path[i]) {
                        treeObj.expandNode(nodes[j]);
                        node = treeObj.getNodeByParam("id", path[i])
                        break;
                    }
                }
            }
            treeObj.selectNode(node);
            setting1.callback.onClick(null, treeObj.setting.treeId, node);
        }
    }

    (function pageLoadTip(time) {
        var ictlosading = false;
        $(document).ajaxStart(function(e) {
            ictlosading = true;
            setTimeout(function() {
                if (ictlosading == true) {
                    $(".rangData_ing").show();
                }
            }, time || 300);
        });

        $(document).ajaxComplete(function(event, xhr, settings) {
            var status = xhr.status;
            if (ictlosading == true) {
                $(".rangData_ing").hide();
            }
            ictlosading = false;
        });
    }());
    /**
     * 预览结果
     */
    $("#scheme_timeinput").click(function () {
        var schemecheck= "";
        $(".scheme_check").each(function () {
            if($(this).is(':checked'))
            schemecheck += ","+$(this).attr("scheme_code")
        })
        if(schemecheck==""){
            alert("请至少选择一个方案")
            return;
        }
        schemecheck=schemecheck.substring(1);
        var schemetime = $('#scheme_timeval').val();
        $(".rangData_ing").show();
        setTimeout(function () {
            $.ajax({
                url: common.rootPath + 'zbdata/zsjhedit.htm?m=checkPreview',
                data: {"id": incode, "scodes": schemecheck,"timeinput":schemetime},
                type: 'post',
                global: true,
                dataType: 'json',
                timeout: 50,
                success: function (re) {
                    if (re.return == 200) {
                        $(".rangData_ing").hide();
                        window.open(common.rootPath + 'zbdata/zsjhedit.htm?m=previewIndex&id=' + incode + "&timeinput=" + schemetime + "&scodes=" + schemecheck);
                    }
                    else {
                        alert(re.return + "无法查看预览结果！")
                        $(".rangData_ing").hide();
                    }
                }
            })
        },30);
    });


    /**
     * 计算范围时间搜索框
     */
    var sjselect;
    var sort = $("#index_sort").val();//是年度，季度还是月度；
    $(document).on('click', '#fwtimeinput', function () {
        var zbcode=$(".panel_zbname").attr("code")
        //console.log(zbcode)
        var cocode=$('#co_select option:selected').attr("class")
        //console.log(cocode)
        var dscode=$('#ds_select option:selected').attr("class")
        //console.log(dscode)
        var timeinput = $("#timeval").val();
        var url = common.rootPath + "zbdata/zsjhedit.htm?m=timeCheck&sort=" + sort + "&timeinput=" + timeinput+"&icode="+incode+"&zbcode="+zbcode+"&cocode="+cocode+"&dscode="+dscode;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                if (data.returncode == 300) {
                    alert("时间格式有误");
                } else if (data.returncode == 200) {
                    if(!(data.returndata ==""||data.returndata==null)){
                        sjselect = data.returndata;
                        console.log(sjselect)

                        //重新绘制表格
                        reTable()
                    }
                }
            }
        })
    })
    $("#fwtimeinput").click();
    /**
     *  范围确认
     */
    $(document).on('click','#rangeConfirm',function () {
        var timetext = $("#startpeirod").val();
        if(timesort == "y"){
            var reg=/^\d{4}$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的"+"年度"+"计划起始时间格式有误")
                return;
            }
        }else if(timesort == "q"){
            var reg=/^(\d{4})([A-D]{1})$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的季度计划起始时间格式有误")
                return;
            }
        }else if(timesort == "m") {
            var reg = /^\d{6}$/;
            var r = timetext.match(reg);
            if (r == null) {
                alert("您的" + "月度" + "计划起始时间格式有误")
                return;
            }
            var sub = timetext.substring(timetext.length - 2);
            if (sub == "00" || parseInt(sub) > 12) {
                alert("您的" + "月度" + "计划起始时间格式有误")
                return;
            }
        }


        //过滤zbs
        var oldzb=zbAdd.zbs;
        var zbs=[];
        //获取指标的信息
        for (var m=0;m<oldzb.length;m++){
            zbs.push(oldzb[m])
        }
        var codelist=[];
        var unchoose = "";//没选择的指标
        var check=$("input:checkbox[class='zb_checkbox']:checked");
        for (var i=0;i<check.length;i++){
            codelist.push(check[i].id)
        }
        for (var i=0;i<zbs.length;i++){
            var code=zbs[i].code
            var flag=false;
            for (var j=0;j<codelist.length;j++){
                if (code==codelist[j]){
                    flag=true
                }
            }
            if (flag==false){
                zbs.splice(i,1)
                unchoose += ","+code;
                i--;
            }
        }
        //过滤regs
        var regs=select;
        var reglist=[];
        var regcheck=$("input:checkbox[class='reg_checkbox']:checked");
        for (var i=0;i<regcheck.length;i++){
            reglist.push(regcheck[i].id)
        }
        //console.log(reglist)
        //console.log(regs)
        for (var i=0;i<regs.length;i++){
            var code=regs[i].code
            var flag=false;
            for (var j=0;j<reglist.length;j++){
                if (code==reglist[j]){
                    flag=true
                }
            }
            if (flag==false){
                regs.splice(i,1)
                i--
            }
        }
        //console.log(regs)
        /**
         * 入库
         */
        var zbcode = "";//指标code
        var zbco = "";//指标主体
        var zbds = "";//指标数据来源
        var zbunit ="";//指标单位
        var sxcode = "";//指标名称
        var regselect ="";//地区信息
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
        if (unchoose != "")
            unchoose = unchoose.substring(1);
        if(zbs.length==0){
            alert("指标未选择")
        }
        if(regs.length==0){
            alert("地区未选择")
        }
        if(zbs.length==0 || regs.length==0){
            return;
        }
        for (var i = 0; i <regs.length ; i++) {
            regselect += regs[i].code+",";
        }
        regselect = regselect.substr(0, regselect.length - 1);//去除最后一个逗号
        var delayday = $('#delayday').val();
        $.ajax({
            url: common.rootPath+'zbdata/zsjhedit.htm?m=toSaveRange',
            data: {'zbcode':zbcode,'zbco':zbco,'zbds':zbds,'sxcode':sxcode,'zbunit':zbunit,'regselect':regselect,'icode':incode,'startpeirod':timetext,'delayday':delayday,'unchoose':unchoose},
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200)
                {
                    alert("保存成功！");
                    window.location.href= common.rootPath+"zbdata/zsjhedit.htm?id="+incode+"&currentTab=2&sjss="+$("#timeval").val();
                }
                else if(data.returncode ==303)
                    alert(data.returndata+"被模型节点引用，无法删除！")
                else if(data.returncode == 501||data.returncode == 301)
                    alert("保存失败！");
            }
        })
    })



    //点击tab重新绘制计算范围表格

    $("#bjjhTab  a[href='#jsfw']").click(function () {
        reTable()
    })

    function reTable() {
        var zbs=zbAdd.zbs;//获取指标的信息
        var regs=select;//获取地区信息
        var sjs=sjselect;//获取时间信息
     /*   console.log(zbs)
        console.log(regs)
        console.log(sjs)*/
        var data={
            zbs:zbs,
            regs:regs,
            sjs:sjs
        }
        sendPjax(data)
    }

    function sendPjax(data) {
        var zbnum=data.zbs.length
        var regnum=data.regs.length
        var sjnum=data.sjs.split(",").length
        $.pjax({
            url:common.rootPath+'zbdata/zsjhedit.htm?m=getRangeData&zbnum='+zbnum+'&regnum='+regnum+'&sjnum='+sjnum+'&icode='+incode,
            container:'.J_zsjh_rangedata_table',
            type:'POST',
            data:data,
            timeout:50000
        })


    }



    /**
     * 计算范围数据下载
     */

    $(document).on('click', '#J_plan_excel', function() {

       /* var zbs=zbAdd.zbs;//获取指标的信息
        var regs=select;//获取地区信息
        var sjs=sjselect;//获取时间信息
        var zbcode = "";//指标code
        var zbco = "";//指标主体
        var zbds = "";//指标数据来源
        var zbunit ="";//指标单位
        var zbname = "";//指标名称
        var regcode ="";//地区code
        var regname ="";//地区name
        for (var i = 0; i <regs.length ; i++) {
            regcode += regs[i].code+",";
            regname += regs[i].name+",";
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
        regname = regname.substr(0, regname.length - 1);//去除最后一个逗号
        regcode = regcode.substr(0, regcode.length - 1);//去除最后一个逗号*/
        //var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toRangeExcel&indexcode='+incode+'&regcode='+regcode+'&regname='+regname+'&sj='+sjs+'&zb='+zbcode+'&co='+zbco+'&ds='+zbds+'&zbname='+zbname+'&zbunit='+zbunit;
        /*var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toRangeExcel&icode='+incode+'&regcode='+regcode+
            '&regname='+regname+'&sj='+sjs+'&zb='+zbcode+'&co='+zbco+'&ds='+zbds+'&zbname='+zbname+'&zbunit='+zbunit;*/

        var timetext = $("#startpeirod").val();
        if(timesort == "y"){
            var reg=/^\d{4}$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的"+"年度"+"计划起始时间格式有误")
                return;
            }
        }else if(timesort == "q"){
            var reg=/^(\d{4})([A-D]{1})$/;
            var r= timetext.match(reg);
            if(r==null){
                alert("您的季度计划起始时间格式有误")
                return;
            }
        }else if(timesort == "m") {
            var reg = /^\d{6}$/;
            var r = timetext.match(reg);
            if (r == null) {
                alert("您的" + "月度" + "计划起始时间格式有误")
                return;
            }
            var sub = timetext.substring(timetext.length - 2);
            if (sub == "00" || parseInt(sub) > 12) {
                alert("您的" + "月度" + "计划起始时间格式有误")
                return;
            }
        }

        var url = common.rootPath + 'zbdata/zsjhedit.htm?m=toRangeExcel&icode='+incode+'&plantime='+timetext;
        $.ajax({
            url: url,
            type: 'post',
            success: function(data) {
                if (data.returncode == 300) {
                    alert("请进行范围确认");
                }else{
                    alert("下载成功!");
                    window.location.href =url;
                }
            }
        })
    })

/*    $(".J_zsjh_rangedata_table").on('pjax:success', function() {
        //加载表格后设置表格

        setTable()


    });*/
/*
    //设置表格动态变化
    function setTable(){

        $(".value_col").each(function () {
            var thisID=$(this).attr("id")
            var tarCla="sj_"+thisID.split("_")[2]
            var thisWidth=$("."+tarCla).width()
            $(this).width(thisWidth)
        })

    }*/




});