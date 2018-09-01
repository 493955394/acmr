define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        pjax=require('pjax'),
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
                    window.location.reload(true);
                }else {
                    alert("保存失败");
                    $('#mymodal-data').modal('hide');
                }
            },
            error: function() {
                common.commonTips('添加失败');

            }

        })

    });
    $(document).on('submit', '.J_add_plan', function(event) {
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
                    $('#mymodal-data1').modal('hide');
                    window.location.reload(true);
                    //window.location.reload();
                    //common.commonTips('保存成功！');
                } else {
                    alert("保存失败");
                    $('#mymodal-data1').modal('hide');
                    // common.commonTips('保存出错！');
                }
            }

        })

    });
    /**
     * 编辑数据
     */
    $('.category_edit').click(function () {
        var code =$(this).attr('id');
        var name = $(this).attr('name');
        $('input[name=editcode]').val(code);
        $('input[name=editcname]').val(name);
        $("#mymodal-data3").modal('show');
    });
    $(document).on('submit', '.J_add_edit', function(event) {
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
                    $('#mymodal-data3').modal('hide');
                    window.location.reload(true);
                    //window.location.reload();
                    //common.commonTips('保存成功！');
                } else {
                    alert("保存失败");
                    $('#mymodal-data3').modal('hide');
                    // common.commonTips('保存出错！');
                }
            }

        })

    });
    /**
     * 复制到
     */
    /*
    把复选框改成单选框
     */
    $(function(){
        $("input[type='checkbox']").click(function() {
            var flag = $(this).prop("checked"); //先记录下点击后应该的状态
            $("input[type='checkbox']").prop("checked", false);
            $(this).prop("checked", flag);
        });
    });
    $('.J_AddCopy').click(function () {
        var i=0
        $('input:checkbox').each(function(){

            if(this.checked){
                i++;
                var code =$(this).attr('id');
                var ifdata = $(this).attr('name');
                if(ifdata == 0){
                    alert("目录无法复制！");
                }
                else if(ifdata == 1){
                    $('input[name=copycode]').val(code);
                    $('input[name=cifdata]').val(ifdata);
                    $('#mymodal-data2').modal('show');
                }
            }
        })
        if(i==0){
            alert("请勾选计划");
        }
    });
    $(document).on('submit', '.J_add_cope', function(event) {
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
                    $('#mymodal-data2').modal('hide');
                    window.location.reload(true);
                    //window.location.reload();
                    //common.commonTips('保存成功！');
                } else {
                    alert("保存失败");
                    $('#mymodal-data2').modal('hide');
                    // common.commonTips('保存出错！');
                }
            }
            /*success: function(data) {
                if (data.returncode == 300) {
                    alert('请选择计划');
                }else if(data.returncode == 200){
                    alert("保存成功") ;
                $('#mymodal-data2').modal('hide');
                window.location.reload(true);
                //common.commonTips('保存成功！');
                }else{
                    alert("保存失败") ;
                    $('#mymodal-data2').modal('hide');
                    // common.commonTips('保存出错！');
                }
            }*/

        })
    });
    /**
     * 删除数据
     */
    $(document).on('click','.J_opr_del',function(event){
        event.preventDefault();
        var self = this,
            id = $(self).attr('id');
        if(!confirm("确定要删除选中记录吗？")){
            return;
        }
        $.ajax({
            url:common.rootPath+'zbdata/indexlist.htm?m=delete',
            data: "id=" + id,
            type:'post',
            dataType:'json',
            timeout:1000,
            success:function(data){
                if(data.returncode == 200){
                    common.commonTips("删除成功");
                    window.location.reload(true);
                }else{
                    common.commonTips(data.returndata);
                }
            }
        });
    });
    // 启用
    $(document).on('click', '.start', function(event) {
        event.preventDefault();
        var self = this,
            code = $(self).attr('name'),
            state = "1";
        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=switchState',
            data: {"code": code, "state":state},
            type: 'post',
            dataType: 'json',
            success:function(data){
                if(data.returncode == 200){
                    window.location.reload(true);
                }else{
                    alert("启用失败");
                }
            }
        });

    });
    // 停用
    $(document).on('click', '.stop', function(event) {
        event.preventDefault();
        var self = this,
            code = $(self).attr('name'),
            state = "0";
        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=switchState',
            data: {"code":code, "state":state},
            type: 'post',
            dataType: 'json',
            success:function(data){
                if(data.returncode == 200){
                    window.location.reload(true);
                }else{
                    alert("停用失败");
                }
            }
        });
    });
    /**
     * 搜索框
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
            url: requestUrl+searchField,
            container: '.J_regmgr_data_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
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
    var setting4 = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:clickEvent4
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
            $('input[name=idplan]').val(treeNode.id);
        } else {
            $('input[name=planname]').val('');
        }

    }
    function clickEvent3(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=indexname]').val(treeNode.name);
            $('input[name=newprocode]').val(treeNode.id);
        } else {
            $('input[name=indexname]').val('');
        }

    }
    function clickEvent4(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=editname]').val(treeNode.name);
            $('input[name=editprocode]').val(treeNode.id);
        } else {
            $('input[name=editname]').val('');
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
        $.fn.zTree.init($("#treeEditc"), setting4, zNodes);
        fixIcon("treeEditc");
    });


    //CategoryNode为只有目录的树结构nodes
    var CategoryNodes=[];
    for(i=0;i<zNodes.length;i++){
        if (zNodes[i].isParent==true&&zNodes[i].pId!="#2"&&zNodes[i].pId!="#3"){
            CategoryNodes.push(zNodes[i])
        }
    }
    //console.log(CategoryNodes)

    module.exports={
        CategoryNodes:CategoryNodes
    }

})