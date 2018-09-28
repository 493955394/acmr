define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        dropdown = require('dropdown'),
        pjax=require('pjax'),
        modal = require('modal'),
       // listjsp= require('listjsp'),
        AjaxMods = require('AjaxMods');


    var st = new Date().getTime();//时间戳


    $("#test").click(function () {
        console.log("test")
        $.ajax({
            url:common.rootPath + 'zbdata/zstask.htm?id=092702&right=2' ,
            type: 'get',
            timeout: 10000,
            success:function () {
                console.log("success")
            }
        })
    })


    /**
     * 新增目录ajax提交
     */

    $(document).on('submit', '.J_add_catalogue', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="cocname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cocname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.reload(true);
                }else if (data.returncode == 300) {
                    alert("该编码已存在");
                    $("#mymodal-data").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });
    console.log($("input[name='plancode']").attr("class"))
    $(document).on('submit', '.J_add_plan', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($("input[name='plancode']"), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($("input[name='plancode']"), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($("input[name='plancode']"), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="plancname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="plancname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert("该编码已存在");
                    $("#mymodal-data1").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });
    /**
     * 编辑数据
     */
    $(document).on('click','.category_edit',function (event) {
        event.preventDefault();
        var code =$(this).attr('id');
        var name = $(this).attr('name');
        $('input[name=editcode]').val(code);
        $('input[name=editcname]').val(name);
        $("#mymodal-data3").modal('show');
    })

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
                   common.commonTips('保存成功！');
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    common.commonTips('添加失败');
                } else {
                    common.commonTips('添加失败');
                }
            },
            error: function() {
                common.commonTips('添加失败');

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
                var ifdata = $(this).attr('if');
                var name =$(this).attr('getname');
                if(ifdata == 0){
                    alert("目录无法复制！");
                }
                else if(ifdata == 1){
                    $('input[name=copycode]').val(code);
                    $('input[name=plcode]').val(code);
                    $('input[name=cifdata]').val(ifdata);
                    $('input[name=zname]').val(name);
                    $('#mymodal-data2').modal('show');
                }
            }
        })
        if(i==0){
            alert("请选择计划");
        }
    });
    $(document).on('submit', '.J_add_cope', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="plcode"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="plcode"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="plcode"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="zname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="zname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert("该编码已存在");
                    $("#mymodal-data2").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });
    /**
    * 后台检查
    */
   /* function checkcoCode(cocode, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=checkcoCode',
            timeout: 5000,
            type: 'post',
            async: false,
            data: 'cocode=' + cocode,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="cocode"]'), true);
                    flag = true;
                } else{
                    checkDelegate.viewTipAjax($('input[name="cocode"]'), false, "该编码已存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    $(document).on('blur', 'input[name="cocode"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkCode($('input[name="cocode"]').val(), checkDelegate);
    });
    function checkplCode(plancode, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=checkplCode',
            timeout: 5000,
            type: 'post',
            async: false,
            data: 'plancode=' + plancode,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="plancode"]'), true);
                    flag = true;
                } else {
                    checkDelegate.viewTipAjax($('input[name="plancode"]'), false, "该编码已存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    $(document).on('blur', 'input[name="plancode"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkCode($('input[name="plancode"]').val(), checkDelegate);
    });
    function checkCode(plncode, checkDelegate) {
        var flag;
        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=checknCode',
            timeout: 5000,
            type: 'post',
            async: false,
            data: 'plcode=' + plcode,
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    checkDelegate.viewTipAjax($('input[name="plcode"]'), true);
                    flag = true;
                } else {
                    checkDelegate.viewTipAjax($('input[name="plcode"]'), false, "该编码已存在");
                    flag = false;
                }
            }
        })
        return flag;
    }
    $(document).on('blur', 'input[name="plcode"]', function() {
        var self = this,
            checkDelegate;
        checkDelegate = new VaildNormal();
        checkCode($('input[name="plcode"]').val(), checkDelegate);
    });*/
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
                if (data.returncode == 200) {
                    alert("删除成功！");
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert("目录下存在计划，删除失败");
                } else {
                    alert("删除失败");
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
                    console.log(data)
                    if (data.checkhasMod!=true){
                        alert("计划没有模型节点，启动失败")
                    } 
                    if (data.checkInfo!=true){
                        alert("计划基本信息缺失，启动失败")
                    }
                    if (data.checkZbReg!=true){
                        alert("计划缺少指标或地区，启动失败")
                    }
                    if (data.checkmod!=true){
                        alert("计划模型节点设置及权重不符合规定，启动失败")
                    }
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
            container: '.J_zsjh_data_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
    });
    $(document).on('submit', '.J_search_form1', function(event) {
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
            container: '.J_zsjh_data_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
    });
    $(document).on('submit', '.J_search_form2', function(event) {
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
            container: '.J_zsjh_data_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
    });

    var zNodes =[
        { id:"!1", pId:0, name:"指数",isParent:true},
        { id:"!2", pId:0, name:"我收到的指数",isParent:true},
        { id:"!3", pId:0, name:"我共享的指数", isParent:true}
    ];
    var cNodes=[
        { id:"!1", pId:0, name:"指数",isParent:true}
    ]
    /*var indexlist=listjsp.indexlist;
    for(var i=0;i<indexlist.length;i++){
        zNodes.push(indexlist[i])
    }*/
    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getListTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback:{
            onClick:clickEvent
        }
    };
    /*var settingc = {
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
    };*/
    var setting1 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getCateTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback:{
            onClick:clickEvent1
        }
    };
    var setting2 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getCateTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback:{
            onClick:clickEvent2
        }
    };
    var setting3 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getCateTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback:{
            onClick:clickEvent3
        }
    };
    var setting4 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=getCateTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
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
   // console.log("rootpath:"+common.rootPath)
    function clickEvent(event,treeId,treeNode) {
       // console.log(treeNode.id)
        var code=treeNode.id

        $.pjax({
            url:common.rootPath+'zbdata/indexlist.htm?m=getIndexList&code='+code,
            container:'.J_zsjh_data_table',
            timeout:2000
        })

        /*if(treeNode.isParent==false){
            return false
        }
        $.ajaxSettings.async=false
        $("#my_index_all").children().removeAttr("class")
        reloadList()
        changeTrClass();
        var proCode=treeNode.id
        var classname="-"+proCode+"-"
        $(".my_index:not([class*='" +
            classname+"'])").detach()*/


    }
    function clickEvent1(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=cataname]').val(treeNode.name);
            $('input[name=idcata]').val(treeNode.id);
            if (treeNode.id=="!1"){
                $('input[name=idcata]').val(null);
            }
        } else {
            $('input[name=cataname]').val('');
        }

    }
    function clickEvent2(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=planname]').val(treeNode.name);
            $('input[name=idplan]').val(treeNode.id);
            if (treeNode.id=="!1"){
                $('input[name=idplan]').val(null);
            }
        } else {
            $('input[name=planname]').val('');
        }

    }
    function clickEvent3(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=indexname]').val(treeNode.name);
            $('input[name=newprocode]').val(treeNode.id);
            if (treeNode.id=="!1"){
                $('input[name=newprocode]').val(null);
            }
        } else {
            $('input[name=indexname]').val('');
        }

    }
    function clickEvent4(event,treeId,treeNode) {

        if (treeNode.id != '') {
            $('input[name=editname]').val(treeNode.name);
            $('input[name=editprocode]').val(treeNode.id);
            if (treeNode.id=="!1"){
                $('input[name=editprocode]').val(null);
            }
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

        //页面加载成功自动点击第一个节点（指数）
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var node = treeObj.getNodeByParam("id","!1");
        treeObj.expandNode(node,true,true,true)
        treeObj.selectNode(node)
        treeObj.setting.callback.onClick(null, treeObj.setting.treeId, node);


        //        $.fn.zTree.init($("#你的id"), settingc, cNodes);
        $.fn.zTree.init($("#treeCata"), setting1, cNodes);
        fixIcon("treeCata");
        addPath();
        $.fn.zTree.init($("#treePlan"), setting2, cNodes);
        fixIcon("treePlan");
        addPath();
        $.fn.zTree.init($("#treeZs"), setting3, cNodes);
        fixIcon("treeZs");
        addPath();
        $.fn.zTree.init($("#treeEditc"), setting4, cNodes);
        fixIcon("treeEditc");
        addPath();
    });

    $(document).pjax('.J_zsjh_pagination a', '.J_zsjh_data_table');

    /**
     * 撤回操作
     * @type
     */
    $(document).on('click',".share_withdraw",function (event) {
        event.preventDefault();
        if(!confirm("确定要撤回该计划吗？")){
            return;
        }
        var sort =$(this).prev().prev().prev().val();
        var sharecode = $(this).prev().prev().val();
            var usercode =$(this).prev().val();
        $.ajax({
            url:common.rootPath+"zbdata/indexlist.htm?m=shareWithDraw",
            data:{"indexcode":sharecode,"depusercode":usercode,"sort":sort},
            type:'post',
            datatype:'json',
            timeout: 10000,
            success:function (re) {
                if(re.returncode == 200){
                    alert("撤回成功！")
                    window.location.reload(true);
                }else {
                    alert("撤回失败！")
                }
            }
        })
    })

    //CategoryNode为只有目录的树结构nodes
    var CategoryNodes=[];
    //console.log(zNodes)
    /*for(i=0;i<zNodes.length;i++){
        //console.log(zNodes[i])
        if (zNodes[i].sou==true&&zNodes[i].pId!="#2"&&zNodes[i].pId!="#3"&&zNodes[i].id!="#2"&&zNodes[i].id!="#3"){
            CategoryNodes.push(zNodes[i])
            //console.log(CategoryNodes)
        }
    }*/
    //console.log(CategoryNodes)



    module.exports={
        CategoryNodes:CategoryNodes
    }

})

