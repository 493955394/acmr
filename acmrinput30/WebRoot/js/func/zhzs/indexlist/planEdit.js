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
    $(document).on('click',".J_Add" ,function(event) {//初始化一次
        $('input[name=cocode]').val("");
        $('input[name=cocname]').val("");
    })
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
            !checkDelegate.checkNormal($('input[name="cocname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        //code只能是数字和字母
        var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="cocode"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }
        //var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
        //var namecheck = /[^%&',;=?$\x22]+/;
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="cocname"]').val().match(namecheck);
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
    /*$(document).on('click',".resetcata" ,function(event) {//初始化一次
        window.location.reload()
    })*/


    /*$('#mymodal-data').on('hidden.bs.modal', function (){

        //document.getElementById("mymodal-data").reset();
        $('.J_add_catalogue').reset();
    });*/
    /**
     * 新增计划
     */
    $(document).on('click',".J_Add_Plan" ,function(event) {//初始化一次
        $('input[name="plancode"]').val("");
        $('input[name="plancname"]').val("");
    })
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
        var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="plancode"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }
        //名称只能是中文和字母
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="plancname"]').val().match(namecheck);
        if(z==null){
            alert("您的名称不符合规则");
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
            { id:"!1", pId:0, name:"指数",isParent:true,icon:"../../../../zhzs/css/img/mark1.png"}
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
        var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="editcode"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }
        var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
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
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert('请选择所属目录');
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
    $(document).off('click',".J_zsjh_data_table :checkbox").on('click',".J_zsjh_data_table :checkbox",function(){
        var flag = $(this).prop("checked"); //先记录下点击后应该的状态
        $("input[type='checkbox']").prop("checked", false);
        $(this).prop("checked", flag);
    });

    /**
     * 复制到
     */
    $(document).on('click', '.J_AddCopy',mycopy)

    function mycopy() {
        //  console.log("copy")
        if ($('input:checkbox:checked').length==0){
            alert("请选择指标")
        }
        else {
            var code =$('input:checkbox:checked').attr('id');
            var ifdata = $('input:checkbox:checked').attr('if');
            var name =$('input:checkbox:checked').attr('getname');
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

    }
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
            !checkDelegate.checkNormal($('input[name="zname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        //code只能是数字和字母
        var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="plcode"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }
        var namecheck = /^([a-zA-Z\u4e00-\u9fa5]*)$/;
        var z = $('input[name="zname"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            return;
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
                } else if (data.returncode == 400) {
                    alert("计划基本信息缺失，复制失败");

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
        if ($('input:checkbox:checked').length==0){
            alert("请选择指数")
        }
        else {
            var right = $('input:checkbox:checked').attr('getright');
            var code =$('input:checkbox:checked').attr('idcode');
            var name =$('input:checkbox:checked').attr('coname');
            if(right == 0){
                alert("本指数计划没有权限复制！");
            }
            else if(right == 1 || right == 2){
                $('input[name=putcode]').val(code);
                $('input[name=cosharecode]').val(code);
                $('input[name=putname]').val(name);
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
        if (!checkDelegate.checkNormal($('input[name="putcode"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="putcode"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="putcode"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="putname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="putname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
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
                    //window.location.reload(true);
                } else if (data.returncode == 300) {
                    alert("目录下存在计划，删除失败");
                } else {
                    alert("删除失败");
                }
            }
        });
    });
    $(document).on('click', '.start', function(event) {
        $(this).parent().prepend("<a href=#>启用</a>")
        $(this).remove()
        event.preventDefault();
        var code=$(this).attr('name')
        var state="1"

        $.ajax({
            url: common.rootPath + 'zbdata/indexlist.htm?m=switchState',
            data: {"code": code, "state":state},
            type: 'post',
            dataType: 'json',
            success:function(data){
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

                    var url=window.location.href;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_data_table',
                        timeout:5000
                    });

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