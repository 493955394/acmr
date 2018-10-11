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
    /**
     * 新增计划
     */
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
        $('input[name=oldcode]').val(code);
        $('input[name=editcode]').val(code);
        $('input[name=editcname]').val(name);
        $('input[name=oldname]').val(name);
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
            !checkDelegate.checkNormal($('input[name="putname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
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
                    alert("启用成功")
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
})