define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        dropdown = require('dropdown'),
        pjax = require('pjax'),
        modal = require('modal'),
        // listjsp= require('listjsp'),
        AjaxMods = require('AjaxMods');



    /**
     * 新增目录ajax提交
     */

    /*function get_cuuid(){
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        zs_code= uuid;
    }*/
    $(document).on('submit', '.J_add_catalogue', function(event) {
        event.preventDefault();

        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        /*if (!checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="cocode"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }*/
        if (!checkDelegate.checkNormal($('input[name="cocname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="cocname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        //code只能是数字和字母
        /*var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="cocode"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }*/
        //var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
        //var namecheck = /[^%&',;=?$\x22]+/;

        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="cocname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            return;
        }
        var zs_code="";
        function get_uuid(){
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4";
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            zs_code= uuid;
        }
        get_uuid();
        $('input[name="cocode"]').val(zs_code);
        console.log(zs_code)
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    console.log(zs_code)
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+zs_code;
                   // window.location.reload(true);
                }else if (data.returncode == 300||data.returncode == 301) {
                    alert(data.returndata);
                    $("#mymodal-data").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });
    /*$(document).on('click',".resetcata" ,function(event) {//初始化一次
        window.location.reload()
    })*/


    /*$('#mymodal-data').on('hidden.bs.modal', function (){

        //document.getElementById("mymodal-data").reset();
        $('.J_add_catalogue').reset();
    });*/

    $(document).on('submit', '.J_add_plan', function(event) {
        event.preventDefault();

        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="plancname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="plancname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        var selectObj = document.getElementById("sort");
        //var selectObj = $("#sort");
        var activeIndex = selectObj.options[selectObj.selectedIndex].value;
        if (activeIndex == ""){
            alert ("请选择统计周期！");
            selectObj.focus();
            flag = false;
        }
        if (flag == false) {
            return;
        }
        //code只能是数字和字母

        //名称只能是中文和字母
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="plancname"]').val().match(namecheck);
        if(z==null){
            //alert("您的名称不符合规则");
            alert("名称含有不规则字符，请修改");
            return;
        }
        var zs_code="";
        function get_uuid(){
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4";
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            zs_code= uuid;
        }
        get_uuid();
        $('input[name="plcode"]').val(zs_code);

        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    console.log(zs_code)
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+zs_code;
                    //window.location.reload(true);
                } else if (data.returncode == 300||data.returncode == 301) {
                    alert(data.returndata);
                    $("#mymodal-data1").modal('show');
                }
                else {
                    alert("添加失败");
                }

            }
        })
    });
    /*$(document).on('click',".resetplan" ,function(event) {//初始化一次
        window.location.reload()
    })
*/

    /**
     * 编辑数据
     */
    $(document).on('click','.category_edit',function (event) {
        event.preventDefault();
        var code =$(this).attr('id');
        var name = $(this).attr('name');
        $('input[name=editcode]').val(code);
        $('input[name=editcname]').val(name);
        //点进来才开始初始化树
        var cNodes=[
            { id:"!1", pId:0, name:"指数",isParent:true,icon:"../css/img/mark1.png"}
        ]
        function clickEvent4(event,treeId,treeNode) {
            if (treeNode.id != '') {
                $('input[name=editname]').val(treeNode.name);
                $('input[name=editprocode]').val(treeNode.id);
                if (treeNode.id == "!1") {
                    $('input[name=editprocode]').val("");
                }
            }else{ $('input[name=editname]').val(treeNode.name);
            }
            $.fn.zTree.init($("#treeEditc"), setting4, cNodes);
        }
        var setting4 = {
            async: {
                enable: true,
                url: common.rootPath+'zbdata/indexlist.htm?m=getTree&st='+new Date().getTime()+'&indexcode='+code,
                contentType: 'application/json',
                type: 'get',
                autoParam: ["id"]
            },
            callback:{
                onClick:clickEvent4
            }
        };
        $.fn.zTree.init($("#treeEditc"), setting4, cNodes);

        $("#mymodal-data3").modal('show');
    })
    /**
     * 编辑目录
     */
    $(document).on('submit', '.J_add_edit', function(event) {
        event.preventDefault();

        var self = this,
            currentUrl = $(self).attr('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="editcname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="editcname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        //code只能是数字和字母

        var zs_code = $('input[name="editcode"]').val();


        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="editcname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
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
                    alert('保存成功！');
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+zs_code;
                } else if (data.returncode == 300||data.returncode == 301) {
                    alert(data.returndata);
                    $("#mymodal-data3").modal('show');
                } else {
                    alert('添加失败');
                }
            },

        })

    });
    /**
     把复选框改成单选框
     */
    /*$(function(){
        $("input[type='checkbox']").click(function() {
            var flag = $(this).prop("checked"); //先记录下点击后应该的状态
            $("input[type='checkbox']").prop("checked", false);
            $(this).prop("checked", flag);
        });
    });*/
   /* $(document).off('click',".J_zsjh_data_table :radio").on('click',".J_zsjh_data_table :radio",function(){
        var flag = $(this).prop("checked"); //先记录下点击后应该的状态
        $("input[type='radio]").prop("checked", false);
        $(this).prop("checked", flag);
    });*/


    /*$(function(){
        $('input:radio').click(function(){
            //alert(this.checked);
            //

            var domName = $(this).attr('name');

            var $radio = $(this);
            // if this was previously checked

            if ($radio.data('waschecked') == true){
                console.log($radio.data('waschecked') == true);
                $radio.prop('checked', false);
                $("input:radio[name='radio" + domName + "']").data('waschecked',false);
                //$radio.data('waschecked', false);
            } else {
                console.log($radio.data('waschecked') == true);
                $radio.prop('checked', true);
                $("input:radio[name='radio" + domName + "']").data('waschecked',true);
                //$radio.data('waschecked', true);
            }
        });
    });*/
    $(document).on('click','input[name="radiotype"]',function() {

        var name = $(this).attr("name");
        $(":radio[name="+ name +"]:not(:checked)").attr("tag",0);
        if( $(this).attr("tag") == 1 ) {
            $(this).attr("checked",false);
            $(this).attr("tag",0);
        }else {
            $(this).attr("tag",1);
        }

    });

    /**
     * 复制到
     */
    $(document).on('click', '.J_AddCopy',mycopy)

    function mycopy() {
        //  console.log("copy")
        if ($('input:radio:checked').length==0){
            alert("请选择指数")
        }
        else {
            var code =$('input:radio:checked').attr('id');
            var ifdata = $('input:radio:checked').attr('if');
            var name =$('input:radio:checked').attr('getname');
            var procode =$('input:radio:checked').attr('getprocode');
            if(ifdata == 0){
                alert("目录无法复制！");
            }
            else if(ifdata == 1){
                $('input[name=copycode]').val(code);
                $('input[name=cifdata]').val(ifdata);
                $('input[name=zname]').val(name);
                $('input[name=newprocode]').val(procode);
                $('#mymodal-data2').modal('show');
            }
        }

    }
    $(document).on('submit', '.J_add_cope', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="zname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="zname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        //var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
        var z = $('input[name="zname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            return;
        }
        if (flag == false) {
            return;
        }
        var zs_code="";
        function get_uuid(){
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4";
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            zs_code= uuid;
        }
        get_uuid();
        $('input[name="conewcode"]').val(zs_code);
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+zs_code;
                } else if (data.returncode == 300||data.returncode == 301) {
                    alert(data.returndata);
                    $("#mymodal-data2").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });

    /**
     * 收到的指数 复制到
     */
    $(document).on('click', '.J_Share_AddCopy',sharecopy)

    function sharecopy() {
        //  console.log("copy")
        if ($('input:radio:checked').length==0){
            alert("请选择指数")
        }
        else {
            var right = $('input:radio:checked').attr('getright');
            var code =$('input:radio:checked').attr('idcode');
            var name =$('input:radio:checked').attr('coname');
            var procode =$('input:radio:checked').attr('coprocode');
            if(right == 0){
                alert("本指数计划没有权限复制！");
            }
            else if(right == 1 || right == 2){
                $('input[name=cosharecode]').val(code);
                $('input[name=putname]').val(name);
                $('input[name=shareprocode]').val(procode);
                $('#mymodal-data4').modal('show');
            }
        }

    }
    $(document).on('submit', '.J_share_addcope', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="putname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="putname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        //var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
        var z = $('input[name="putname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            return;
        }
        if (flag == false) {
            return;
        }
        var zs_code="";
        function get_uuid(){
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4";
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            zs_code= uuid;
        }
        get_uuid();

        $('input[name="newsharecode"]').val(zs_code);
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功！");
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+zs_code;
                } else if (data.returncode == 300||data.returncode == 301) {
                    alert(data.returndata);
                    $("#mymodal-data4").modal('show');
                } else if (data.returncode == 400) {
                    alert("计划基本信息缺失，复制失败");
                } else {
                    alert("添加失败");
                }
            }
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
                if (data.returncode == 200) {
                    var url=window.location.href;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_data_table'
                    });
                    alert("删除成功！");
                 /*   var code = url.match(/&code=(\S*)/)[1];
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?icode="+code;*/
                    refreshNode(id)
                    //window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert("目录下存在计划，删除失败");
                } else {
                    alert("删除失败");
                }
            }
        });
    });

    /**
     * 刷新当前删除节点的父节点
     */
    function refreshNode(id) {
        /*根据 treeId 获取 zTree 对象*/
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            type = "refresh",
            silent = false,
            /*获取 zTree 当前被删除的节点数据集合*/
            thisNodes = zTree.getNodeByParam('id',id),
            pNodes = thisNodes.getParentNode();
            zTree.selectNode(pNodes);//选中父节点
        /*强行异步加载父节点的子节点。[setting.async.enable = true 时有效]*/
        zTree.reAsyncChildNodes(pNodes, type, silent);
    }
    /**
     * 启用
     */
    $(document).on('click', '.start', function(event) {
        $(this).parent().prepend("<a href=#>启用</a>")
        $(this).remove()
        event.preventDefault();
        var code=$(this).attr('name')
        var state="1"
        $("#start_ing").css("display","block")
        setTimeout(function () {
            $.ajax({
                url: common.rootPath + 'zbdata/indexlist.htm?m=switchState',
                data: {"code": code, "state":state},
                type: 'post',
                dataType: 'json',
                success:function(data){
                    $("#start_ing").css("display","none")
                    if(data.returncode == 200){
                        var url=window.location.href;
                        alert("启用成功")
                        $.pjax({
                            url: url,
                            container: '.J_zsjh_data_table',
                            timeout:5000
                        });
                    }else{
                        // console.log(data)

                        var info=""
                        if (data.checkhasMod!=true){
                            info=info+"计划没有模型节点，"
                            // alert("计划没有模型节点，启动失败")
                        }
                        if (data.checkInfo!=true){
                            info=info+"计划基本信息缺失，"
                            //alert("计划基本信息缺失，启动失败")
                        }
                        if (data.checkZbReg!=true){
                            info=info+"计划基本信息缺失，"
                            //alert("计划缺少指标或地区，启动失败")
                        }
                        if (data.checkmod!=true){
                            info=info+"计划模型节点设置及权重不符合规定，"
                            // alert("计划模型节点设置及权重不符合规定，启动失败")
                        }
                        if (data.checkScheme!=true){
                            info=info+"请选用计划方案，"
                        }
                        if (data.checkFormula!=true){
                            info=info+"公式不能为空，"
                        }
                        info=info+"启用失败！"
                        alert(info)

                        var url=window.location.href;
                        $.pjax({
                            url: url,
                            container: '.J_zsjh_data_table',
                            timeout:5000
                        });

                    }
                }
            });
        },50)

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
                    var url=window.location.href;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_data_table',
                        timeout:5000
                    });
                }else{
                    alert("停用失败");
                }
            }
        });
    });
})