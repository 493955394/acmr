define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        editjsp = require('editjsp');


    $(".btn_search").click(search)
    $(".zb_add").click(addZb)
    $(".zb_select").change(sendPjax)
    $(".zb_panel").click(panelClick)
    $(".zb_save").click(zbSave)
    $(".zb_delete").click(zbDelete)

    var indexCode=$("#index_code").val();
    var st = new Date().getTime();//时间戳，用于生成zb的code code=st+Math.random().toString(36).substr(2)
    var zbs=[];//[{code1,zbcode1,zbname1,dscode,dsname,cocode,coname,unitcode,unitname}]
    zbs=editjsp.zbs
    var zb=[];//[zbcode,zbname]
    var ds=[];//[dsdcode,dsname]
    var co=[];//[cocode,coname]
    var unit=[];//[unitcode,unitname
    //默认展开第一级树



    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=findTreeNode&icode='+indexCode,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent,
            //onAsyncSuccess: zTreeOnAsyncSuccess
        },
        view:{
            selectedMulti:false
        }
    };
    var rootNode = [{"id":"","name":"指标分组树",pId:"", "open":"false", "isParent":"true",icon:"../../../../zhzs/css/img/mark1.png"}];
    var treeObj = $.fn.zTree.init($("#treeDemo1"), setting, rootNode);

    var pagenum=1;
    var pagesize=10;//默认显示10条数据预览
    var sort = $("#index_sort").val();//是年度，季度还是月度；
    var sjselect=getLast5(sort);//默认是最近五期

    $(document).on('click', '#zbtimeinput', function () {
        var timeinput = $("#timecode").val();
        var url = common.rootPath + "zbdata/zsjhedit.htm?m=timeCheck&sort=" + sort + "&timeinput=" + timeinput;
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
                        sendPjax();
                    }
                }
            }
        })
    })
/*    function zTreeOnAsyncSuccess(event, treeid, treeNode, msg) {
        var treeObj = $.fn.zTree.getZTreeObj(treeid);
        var nodes=treeObj.getNodes();
        var children=treeNode.children;
        treeObj.expandNode(children[1])
        console.log(nodes)
        console.log(children)
    }*/
      function getLast5(sort) {
          var now = new Date();
          var list = [];
          if(sort=="y") { //如果是年度
              for (var i = 0; i <5 ; i++) {
                  list.push((now.getFullYear()-i).toString());
              }
          }else  if(sort=="q") { //如果是季度
              var tmp = [];
              for (var i = 0; i <5/4+2 ; i++) {//除去第一年，先补全再看num来截取个数
                  var yd = (now.getFullYear()-i).toString();
                  for (var j = 68; j >=65 ; j--) {
                      tmp.push(yd+String.fromCharCode(j));
                  }
              }
              var nowtime = now.getFullYear()+getQ(now.getMonth()+1);//获得的月份比当前少一，所以需要+1
              list.push(tmp.slice(indexOf(tmp,nowtime),indexOf(tmp,nowtime)+5));//截取集合
          }
          if(sort=="m") { //如果是月度
              var tmp =[];
              for (var i = 0; i <5/12+2 ; i++) {//除去第一年，先补全再看num来截取个数
                  var yd = (now.getFullYear()-i).toString();
                  for (var j = 12; j >=1 ; j--) {
                      if(j>=10){
                          tmp.push(yd+j);
                      }
                      else {
                          tmp.push(yd+"0"+j);
                      }
                  }
              }
              var nowtime;
              var nowmonth = now.getMonth()+1;
              if(nowmonth>=10){
                  nowtime = now.getFullYear()+nowmonth.toString();
              }
              else {
                  nowtime = now.getFullYear()+"0"+nowmonth;//低于10月份的需要特殊处理，补一个零
              }
              list.push(tmp.slice(indexOf(tmp,nowtime),indexOf(tmp,nowtime)+5));//截取集合
          }
          return list.join(',');
    }
        function getQ(month) {
              var Q ="";
            if(month<4){
                Q ="A"
            }
            else if(month>=4&&month<7){Q="B"}
            else if(month>=7&&month<10){Q="C"}
            else {
                Q="D"
            }
            return Q;
        }
    function indexOf(arr, item) {

        if(Array.prototype.indexOf)
        {
            return arr.indexOf(item);
        }
        else
        {
            for(var i=0;i<arr.length;i++)
            {
                if(arr[i]==item)
                {
                    return i;
                }
            }
        }
        return -1;

    }

    function clickEvent(event, treeid, treeNode) {

        if(treeNode.isParent==false){

            zb=[treeNode.id,treeNode.name]
            $(".panel_zbname").html(treeNode.name).attr("code",treeNode.id)

            if (zbclick(zb,true)){

                $(".zb_panel").css("background-color","inherit")
                $(".zb_panel_add").css("background-color","inherit")
                $(".zb_add").css("display","inline")
                $(".zb_delete").css("display","none")
                $(".zb_save").css("display","none")
            }
        }
        else {
            //$(".zb_add").css("display","none")
        }
    }

    function zbclick(zb,isadd) {

        if(isadd==true){
            pagenum=1

            $("#zb_query_ing").remove()
            $("#zb_query_over").remove()
            $("#zb_data_body").empty().append("<p id='zb_query_start'>查询中……</p>");
            $(".zb_add").css("display","inline")
            //console.log(zb)
            var data={
                "zbcode":zb[0],
                "icode":indexCode
            };
            setTimeout(function () {
                $.ajax({
                    url:common.rootPath+'zbdata/zsjhedit.htm?m=getDsCoUnit',
                    type:'post',
                    dataType:'json',
                    data:data,
                    success:function (re) {
                        $(".zb_select").empty()
                        //$(".zb_select").append("<option>请选择</option>")
                        foreach(re.ds,"ds")
                        foreach(re.co,"co")
                        foreach(re.unit,"unit")
                        function foreach(innerre,zname) {
                            innerre.forEach(function (d,ind,ds){
                                var code=Object.keys(d)[0]
                                var name=d[code]
                                $("#"+zname+"_select").append("<option class='" +
                                    code+ "'>" +
                                    name+"</option>")
                            })
                            if (innerre.length==0){
                                $(".zb_select").append("<option>请选择</option>")

                            }
                        }
                        sendPjax();
                    }
                })
            },500)

            return true
        }
        else {
            pagenum=1
            $("#zb_query_ing").remove()
            $("#zb_query_over").remove()
            $("#zb_data_body").empty().append("<p id='zb_query_start'>查询中……</p>");
            //$(".zb_add").css("display","none")
            //console.log(zb)
            var data={
                    "zbcode":zb[0]
            };
            setTimeout(function () {
                $.ajax({
                    url:common.rootPath+'zbdata/zsjhedit.htm?m=getDsCoUnit&icode='+indexCode,
                    type:'post',
                    dataType:'json',
                    data:data,
                    success:function (re) {
                        $(".zb_select").empty()
                        $("zb_select").append("请选择")
                        foreach(re.ds,"ds")
                        foreach(re.co,"co")
                        foreach(re.unit,"unit")
                        function foreach(innerre,zname) {
                            innerre.forEach(function (d,ind,ds){
                                var code=Object.keys(d)[0]
                                var name=d[code]
                                $("#"+zname+"_select").append("<option class='" +
                                    code+ "'>" +
                                    name+"</option>")
                            })
                        }
                        sendPjax();
                    }
                })
            },500)

        }

    }

    //判断选择的指标是否已经存在
    function isexit(code,dscode,cocode,unitcode) {
        for (var i=0;i<zbs.length;i++){
            if (zbs[i].zbcode==code&&zbs[i].dscode==dscode&&zbs[i].cocode==cocode&&zbs[i].unitcode==unitcode){
                return true
            }
        }
        return false
    }

    function sendPjax(){
        var zbcode=$(".panel_zbname").attr("code")
        //console.log(zbcode)
        var cocode=$('#co_select option:selected').attr("class")
        //console.log(cocode)
        var dscode=$('#ds_select option:selected').attr("class")
        //console.log(dscode)
        var unitcode=$('#unit_select option:selected').attr("class")
        //console.log(unitcode)


        $.pjax({
            url:common.rootPath+'zbdata/zsjhedit.htm?m=getData&zbcode='+zbcode+'&cocode='+cocode+'&dscode='+dscode+'&unitcode='+unitcode+'&indexcode='+indexCode+"&pagesize="+pagesize+"&sjselect="+sjselect+"&st="+st,
            container:'.J_zsjh_zbdata_table',
            timeout:50000
        })
    }


    //表格下拉加载数据
    $("#zb_data_table").scroll(function () {
        var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
        var nScrollTop = 0;   //滚动到的当前位置
        var nDivHight = $("#zb_data_table").height();//overflow所在的div
        nScrollHight = $(this)[0].scrollHeight;
        nScrollTop = $(this)[0].scrollTop;
        if(nScrollTop + nDivHight >= nScrollHight){
            //到底部后进入此方法
            if ($("#zb_query_ing").length==0&&$("#zb_query_over").length==0&&$("#zb_query_start").length==0){
                $("#zb_data_table").append("<p id='zb_query_ing'>"+"查询中……"+"</p>")
            }
            setTimeout(function () {
                var zbcode=$(".panel_zbname").attr("code")
                //console.log(zbcode)
                var cocode=$('#co_select option:selected').attr("class")
                //console.log(cocode)
                var dscode=$('#ds_select option:selected').attr("class")
                //console.log(dscode)
                var unitcode=$('#unit_select option:selected').attr("class")
                //console.log(unitcode)
                $.ajax({
                    url:common.rootPath+'zbdata/zsjhedit.htm?m=getDataWithPage&zbcode='+zbcode+'&cocode='+cocode+'&dscode='+dscode+'&unitcode='+unitcode+'&indexcode='+indexCode+"&pagesize="+pagesize+"&pagenum="+pagenum+"&sjselect="+sjselect+"&st="+st,
                    type:'get',
                    success:function (re) {
                        $("#zb_query_ing").remove()
                        pagenum++
                        if (typeof re=="string"){
                            if ($("#zb_query_over").length==0){
                                $("#zb_data_table").append("<p id='zb_query_over'>"+""+"</p>")
                            }
                        }
                        else {
                            //console.log(re)
                            re.forEach(function (d) {
                                //console.log(d)
                                var row="<tr>";
                                d.forEach(function (r) {
                                    row=row+"<td>"+r+"</td>"
                                })
                                row=row+"</tr>"
                                $("#zb_data_body").append(row)
                            })

                        }
                    }
                })
            },500)

        }

    })



    function search() {
        $("#find_panel").remove()
        var value=$("#queryValue").val()
        if(value!=""){
            $.ajax({
                url:common.rootPath+'zbdata/zsjhedit.htm?m=ZbFind&query='+value+"&icode="+indexCode,
                type:'get',
                success:function (re) {
                    var zbre=[];
                    for(var i=0;i<re.length;i++){
                        zbre.push({zbname:re[i].cname,zbcode:re[i].code})
                    }
                    $("#treeDemo1").css("display","none")
                    $("#tree_and_find").append("<div id='find_panel' class='panel panel-default'><div class='panel-heading'><button type='button'class='btn btn-primary btn-sm' id='clear_find'>清除搜索结果</button></div>")
                    for(var j=0;j<zbre.length;j++){
                        $("#find_panel").append("<div class='panel-body result_panel'><a class='result_choose' href='#' id='" +
                            zbre[j].zbcode+"'>" +
                            zbre[j].zbname+"</a></div></div>")
                    }
                    $("#clear_find").click(clearFindResult)
                    $(".result_choose").click(function () {
                        var clickcode= $(this).attr("id")
                        var name=$(this).html();
                        clearFindResult()
                        //treeObj.expandNode( treeObj.getNodeByParam("id",""))
                        zb=[clickcode,name]
                        //zbclick(zb);
                        if(zbclick(zb,true)){
                            $(".zb_panel").css("background-color","inherit")
                            $(".zb_panel_add").css("background-color","inherit")
                            $(".panel_zbname").html(name).attr("code",clickcode)
                            //获得路径
                            $.ajax({
                                url:common.rootPath+'zbdata/zsjhedit.htm?m=getZBpath&code='+clickcode+"&icode="+indexCode,
                                type:'get',
                                success:function (re) {
                                    re.push(clickcode)
                                   // console.log(re)
                                    expandTree(re)
                                }
                            })
                        }
                    })

                }
            })
        }
    }

    function expandTree(path) {
        $.ajaxSettings.async=false
        var treeObj = $.fn.zTree.init($("#treeDemo1"), setting, rootNode);
        var node=treeObj.getNodeByParam("id","")
        treeObj.expandNode(node)
        treeObj.selectNode(node)
        setting.callback.onClick(null,treeObj.setting.treeId,node)
        //setting.callback.onClick(null, treeObj.setting.treeId, node);

        //  console.log("expandtree")
        for(var i=0;i<path.length;i++){
            //console.log(node)
            if(node.isParent==true){
                var nodes=node.children;
                //console.log(nodes)
                for(var j=0;j<nodes.length;j++){
                    if (nodes[j].id==path[i]){
                        //console.log("found")
                        treeObj.expandNode(nodes[j]);
                        node=treeObj.getNodeByParam("id",path[i])
                        break;
                    }
                }
            }
            treeObj.selectNode(node);
            setting.callback.onClick(null, treeObj.setting.treeId, node);

        }
    }


    function clearFindResult() {
        $("#find_panel").remove();
        $("#treeDemo1").css("display","block")
    }

    function addZb() {
        //$(".zb_add").css("display","none")
        ds=[$('#ds_select option:selected').attr("class"),$('#ds_select option:selected') .val()]
        co=[$('#co_select option:selected').attr("class"),$('#co_select option:selected') .val()]
        unit=[$('#unit_select option:selected').attr("class"),$('#unit_select option:selected') .val()]
        if (isexit(zb[0],ds[0],co[0],unit[0])) {
            alert("该指标已被选择")
            $(".zb_add").css("display","inline")
           // $("#zb_data_head").empty()
           // $("#zb_data_body").empty()
        }
        else {
            zbs.push({
                code:st+Math.random().toString(36).substr(2),
                zbcode:zb[0],
                zbname:zb[1],
                dscode:ds[0],
                dsname:ds[1],
                cocode:co[0],
                coname:co[1],
                unitcode:unit[0],
                unitname:unit[1]
            })
            //console.log(zbs)
            addZBpanel(zbs[zbs.length-1])
            //drawTable();
        }
    }

    function addZBpanel(content) {
        //console.log("addZbpanel")
        //console.log(content)
        $(".panel_container").append("<div class='panel panel-default zb_panel_add'><div class='panel-body '><input type='hidden' class='input_code' value='" +
            content.code+"'><input type='hidden' class='input_zbcode' value='" +
            content.zbcode+"'><input type='hidden' class='input_cocode' value='" +
            content.cocode+"'><input type='hidden' class='input_dscode' value='" +
            content.dscode+"'><input type='hidden' class='input_unitcode' value='" +
            content.unitcode+"'>" +
            "<h5>" +
            content.zbname+"</h5>" +
            "<h6>主体：" +
            content.coname+"</h6>" +
            "<h6>数据来源：" +
            content.dsname+"</h6>" +
            "<h6>计量单位：" +
            content.unitname+"</h6>" +
            "<button type='button' class='btn btn-primary btn-sm zb_save' style='float: left;display: none;'>保存</button>" +
            "<button type='button' class='btn btn-primary btn-sm zb_delete' style='float: right;display: none;'>删除</button>" +
            "</div> </div>")

        clickbind()
    }

    function panelClick(){
        //console.log("panelClick")
        $(".zb_panel").css("background-color","inherit")
        $(".zb_panel_add").css("background-color","inherit")
        $(this).css("background-color","#F4F5F9")
        var _zb=$($(this).children()[0]).children()
        $(".zb_save").css("display","none")
        $(".zb_delete").css("display","none")
        _zb[9].style.cssText="float: left; display: block;"
        _zb[10].style.cssText="float: right; display: block;"

        var zbname=_zb[5].innerHTML
        var zbcode=_zb[1].defaultValue
        $(".zb_add").css("display","inline")
        //console.log(zb)

        /*        var coname=zb[6].innerHTML.split("：")[1]
                var cocode=zb[2].defaultValue
                var dsname=zb[7].innerHTML.split("：")[1]
                var dscode=zb[3].defaultValue
                var unitname=zb[8].innerHTML.split("：")[1]
                var unitcode=zb[4].defaultValue*/
        $(".panel_zbname ").attr("code",zbcode).html(zbname)
        /*console.log($("option [class='" +
            unitcode+"'"))
        $("option [class='" +
            cocode+"']").selected=true
        $("option [class='" +
            dscode+"']").selected=true
        $("option [class='" +
            unitcode+"']").selected=true*/
        //$.ajaxSettings.async=false
        //指标回显
        zbclick([zbcode,zbname],false)
        var cocode=$(this).children()[0].children[2].value
        var dscode=$(this).children()[0].children[3].value
        var unitcode=$(this).children()[0].children[4].value
        //console.log(unitcode)
        $("#unit_select").find("option[class='" +
            unitcode+"']").attr("selected",true);
        $("#co_select").find("option[class='" +
            cocode+"']").attr("selected",true);
        $("#ds_select").find("option[class='" +
            dscode+"']").attr("selected",true);

        zb=[zbcode,zbname]


    }

    function zbSave(event){
        event.stopPropagation();
        //console.log("zbsave")
        var zbcode=$(this).prevAll()[7].defaultValue
        var zbname=$(this).prevAll()[3].innerHTML
        zb=[zbcode,zbname]
        //console.log($(this).next())
        $(this).next().trigger("click")
        addZb()
    }

    function zbDelete(event){
        event.stopPropagation();
        //console.log("zbdelete")
        var zbcode=$(this).prevAll()[8].defaultValue
        var code=$(this).prevAll()[9].defaultValue
       // console.log(code)
        var isUsed=true
        //console.log(zbcode)
        $.ajaxSettings.async=false
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=checkModule&code="+code,
            type:'get',
            data:'json',
            success:function (re) {
                //console.log(re)
                if (re==false){
                    //console.log("不被占用")
                    isUsed=false
                }
                else {
                    alert("该指标被模型节点引用，不能删除！")
                }
            }
        })
        if (isUsed==false){
            for(var i=0;i<zbs.length;i++){
                //console.log("zbcssa")
                if (zbs[i].zbcode==zbcode){
                    zbs.splice(i,1)
                }
            }
            $($(this).parent()[0]).parent().remove();
        }

    }

    function clickbind(){
       // console.log("clickbind")
        $(".zb_panel_add").unbind("click")
        $(".zb_save").unbind("click")
        $(".zb_delete").unbind("click")
        $(".zb_panel_add").click(panelClick)
        $(".zb_save").click(zbSave)
        $(".zb_delete").click(zbDelete)
    }
    expandTree([])



    module.exports={
        zbs:zbs
    }


})