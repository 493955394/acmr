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
    console.log(indexCode)
    var st = new Date().getTime();//时间戳，用于生成zb的code code=st+Math.random().toString(36).substr(2)
    var zbs=[];//[{code1,zbcode1,zbname1,dscode,dsname,cocode,coname,unitcode,unitname}]
    zbs=editjsp.zbs
    var zb=[];//[zbcode,zbname]
    var ds=[];//[dsdcode,dsname]
    var co=[];//[cocode,coname]
    var unit=[];//[unitcode,unitname


    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=testTreeNode',
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
    var rootNode = [{"id":"","name":"指标分组树",pId:"", "open":"false", "isParent":"true"}];
    var treeObj = $.fn.zTree.init($("#treeDemo1"), setting, rootNode);

    
/*    function zTreeOnAsyncSuccess(event, treeid, treeNode, msg) {
        var treeObj = $.fn.zTree.getZTreeObj(treeid);
        var nodes=treeObj.getNodes();
        var children=treeNode.children;
        treeObj.expandNode(children[1])
        console.log(nodes)
        console.log(children)
    }*/


    function clickEvent(event, treeid, treeNode) {
        if(treeNode.isParent==false){
            zb=[treeNode.id,treeNode.name]
            if (zbclick(zb)){

                $(".zb_panel").css("background-color","inherit")
                $(".zb_panel_add").css("background-color","inherit")
                $(".zb_add").css("display","inline")
                $(".panel_zbname").html(treeNode.name).attr("code",treeNode.id)
            }
        }
        else {
            $(".zb_add").css("display","none")
        }
    }

    function zbclick(zb) {
        $("#zb_data_body").empty().append("<p>查询中……</p>");
        if (isexit(zb[0])){
            alert("该指标已被选择！")
            $(".zb_add").css("display","none")
            $("#zb_data_head").empty()
            $("#zb_data_body").empty()
            return false
        }
        else {
            $(".zb_add").css("display","inline")
            //console.log(zb)
            var data={
                "zbcode":zb[0]
            };
            $.ajax({
                url:common.rootPath+'zbdata/zsjhedit.htm?m=getDsCoUnit',
                type:'post',
                dataType:'json',
                data:data,
                success:function (re) {
                    $(".zb_select").empty()
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
            return true
        }
    }

    //判断选择的指标是否已经存在
    function isexit(code) {
        for (var i=0;i<zbs.length;i++){
            if (zbs[i].zbcode==code){
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
            url:common.rootPath+'zbdata/zsjhedit.htm?m=getDataTest&zbcode='+zbcode+'&cocode='+cocode+'&dscode='+dscode+'&unitcode='+unitcode+'&indexcode='+indexCode+"&st="+st,
            container:'.J_zsjh_zbdata_table',
            timeout:50000
        })
        $(document).on('pjax:success', function() {
            console.log("pjax:success")
        });
    }

    function search() {
        var value=$("#queryValue").val()
        if(value!=""){
            $.ajax({
                url:common.rootPath+'zbdata/zsjhedit.htm?m=testFind&query='+value,
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
                        if(zbclick(zb)){
                            $(".zb_panel").css("background-color","inherit")
                            $(".zb_panel_add").css("background-color","inherit")
                            $(".panel_zbname").html(name).attr("code",clickcode)
                            //获得路径
                            $.ajax({
                                url:common.rootPath+'zbdata/zsjhedit.htm?m=getZBpath&code='+clickcode,
                                type:'get',
                                success:function (re) {
                                    console.log(re)
                                    re.push(clickcode)
                                    expandTree(re)
                                }
                            })
                        }
                    })

                }
            })
        }
    }

    function clearFindResult() {
        $("#find_panel").remove();
        $("#treeDemo1").css("display","block")
    }

    function addZb() {
        $(".zb_add").css("display","none")
        console.log("addZB")
        ds=[$('#ds_select option:selected').attr("class"),$('#ds_select option:selected') .val()]
        co=[$('#co_select option:selected').attr("class"),$('#co_select option:selected') .val()]
        unit=[$('#unit_select option:selected').attr("class"),$('#unit_select option:selected') .val()]
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
        console.log(zbs)
        addZBpanel(zbs[zbs.length-1])
        //drawTable();
    }

    function addZBpanel(content) {
        console.log(content)
        $(".panel_container").append("<div class='panel panel-default zb_panel_add'><div class='panel-body '><input type='hidden' class='input_code' value='" +
            content.code+"'><input type='hidden' class='input_zbcode' value='" +
            content.zbcode+"'>" +
            "<h5>" +
            content.zbname+"</h5>" +
            "<h6>主体：" +
            content.coname+"</h6>" +
            "<h6>数据来源：" +
            content.dsname+"</h6>" +
            "<h6>计量单位：" +
            content.unitname+"</h6>" +
            "<button type='button' class='btn btn-primary btn-sm' style='float: left;'>保存</button>" +
            "<button type='button' class='btn btn-primary btn-sm' style='float: right;'>删除</button>" +
            "</div> </div>")
        $(".zb_panel_add").click(panelClick)

    }

    function expandTree(path) {
        $.ajaxSettings.async=false
        var node=treeObj.getNodeByParam("id","")
        treeObj.expandNode(node)
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
        }
/*
        var nodes=pnode.children
        console.log(nodes)
        for (var i=0;i<nodes.length;i++){
            if (nodes[i].id==id){
                treeObj.expandNode(nodes[i])
            }
        }
*/

    }

    function panelClick(){
        console.log("panelClick")
        $(".zb_panel").css("background-color","inherit")
        $(".zb_panel_add").css("background-color","inherit")

        $(this).css("background-color","orange")
        var zbname=$(this).
        console.log(zbname)


    }

    function zbSave(event){
        event.stopPropagation();
        console.log("zbsave")
    }

    function zbDelete(event){
        event.stopPropagation();
        console.log("zbdelete")
    }

    module.exports={
        zbs:zbs
    }


})