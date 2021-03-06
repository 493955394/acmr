define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        pjax=require('pjax'),
        VaildNormal = require('vaildnormal'),
        common = require('common'),
        dropdown = require('dropdown'),
        modal = require('modal'),
        common = require('common');



    var icode=$("#index_code").attr("value");
    var st = new Date().getTime();//时间戳

    $(document).ready(function () {
        //请求方案列表
        sendPjax();
        $(".single_weight_set").click(setSingleWeight);
        $("#set_scheme_weight_formula").click(setSchemesWeight);

    })
    //局部刷新方案列表
    function sendPjax() {
        $.pjax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st,
            container:'.J_zsjh_scheme_table',
            timeout:10000
        })
        $(document).on('pjax:success', function() {
            $(".single_weight_set").unbind('click')
            $(".single_weight_set").bind('click',setSingleWeight);
        });
    }
    //设置单个方案权重
    function setSingleWeight(){
        var scode=$(this).attr("scheme_code")
        var sname=$(this).attr("scheme_name")
        //先判断是否存在空目录，如果存在，不跳转，是否已经确认范围，没有，不跳转
        $.ajax({
            url: common.rootPath+'zbdata/indexlist.htm?m=checkZBModuleCat&icode='+icode,
            type:'get',
            datatype:'json',
            success:function (re){
                //console.log(re)
                if(re.bool == "false"){
                    alert(re.info);
                    if (re.norange == "true"){
                        //console.log("tiaozhuan")
                        $("#bjjhTab > li").removeClass("active")
                        $("a[aria-controls='jsfw']").parent().addClass("active")
                        $("#jsfw").addClass("active")
                    }
                    return;
                }else{
                    window.open(common.rootPath+"zbdata/weightset.htm?m=editSingleWeight&icode="+icode+'&scode='+scode+'&sname='+sname)
                }
            }
        })
    }
    //设置多个方案权重
    function setSchemesWeight(){
        /*var schemes=""
        $(".single_weight_set").each(function () {
            var thiscode=$(this).attr("scheme_code")
            var thisname=$(this).attr("scheme_name")
            schemes=schemes+thisname+":"+thiscode+","
        })*/
        //先判断是否存在空目录，如果存在，不跳转
        $.ajax({
            url: common.rootPath+'zbdata/indexlist.htm?m=checkZBModuleCat&icode='+icode,
            type:'get',
            dataType:'json',
            success:function (re){
                //console.log(re)
                if(re.bool == "false"){
                    alert(re.info);
                    if (re.norange == "true"){
                        //console.log("tiaozhuan")
                        $("#bjjhTab > li").removeClass("active")
                        $("a[aria-controls='jsfw']").parent().addClass("active")
                        $("#jsfw").addClass("active")
                    }
                    return;
                }else{
                    //判断是否有选中的方案，个数在1-3个
                    var len=$("input[class=scheme_check]:checked").length
                    var schemecodes=""
                    $("input[class=scheme_check]:checked").each(function () {
                        var code=$(this).attr("scheme_code")
                        schemecodes=schemecodes+code+";"
                    })
                    if (len==0) alert("请选择方案！")
                    else if (len>0&&len<=3){
                        window.open(common.rootPath+"zbdata/weightset.htm?m=editSchemesWeight&icode="+icode+"&schemecodes="+schemecodes)
                    }
                    else {
                        alert("最多只能选择3个方案！")
                    }
                }
            }
        })
    }
    /**
     * 新增方案
     */

    $(document).on('click','#add_scheme',function (event) {

        event.preventDefault();

        var name ="";
        var remark = "";
        $('input[name="schemename"]').val(name);
        $('input[name="showinfo"]').val(remark);

        $.ajax({
            url: common.rootPath+'zbdata/indexscheme.htm?m=schCheckMod',
            data: {"icode": icode},
            type: 'post',
            dataType: 'json',
            success:function (data){
                console.log(data.checkTureMod)
                if (data.checkTureMod==true) {
                    $("#scheme_modal").modal('show');
                }else{
                    alert("请添加模型节点")
                    $("#scheme_modal").modal('hide');
                }
            }
        })

    });
    $(document).on('submit', '.J_add_scheme', function(event) {
        event.preventDefault();
        $('input[name="indexcode"]').val(icode);
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        console.log(currentUrl)
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="schemename"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="schemename"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="schemename"]').val().match(namecheck);
        if(z==null){
            alert("名称含有不规则字符，请修改");
            return;
        }
        var sch_code="";
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
            sch_code= uuid;
            $('input[name="schemecode"]').val(sch_code);
        }
        get_uuid();
        console.log(sch_code)
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("保存成功！");
                    $("#scheme_modal").modal('hide');
                }else if (data.returncode == 300) {
                    alert(data.returndata);
                    $("#scheme_modal").modal('show');
                } else if(data.returncode == 400) {
                    alert(data.returndata);
                    $("#scheme_modal").modal('show');
                } else{
                    alert("添加失败");
                }
            }
        })
    });
    $(document).on('click','.J_edit',function (event) {

        event.preventDefault();
        var code =$(this).attr('id');
        var remark =$(this).attr('remark');
        var name =$(this).attr('name');
        console.log(remark)
        $('input[name="schediticode"]').val(icode);
        $('input[name="scheditcode"]').val(code);
        $('input[name="scheditname"]').val(name);
        $('input[name="remark"]').val(remark);
        $("#scheme_modal1").modal('show');
    });
    $(document).on('click','.J_clone',function (event) {
        event.preventDefault();
        var name ="";
        var remark = "";
        $('input[name="schclonename"]').val(name);
        $('input[name="newremark"]').val(remark);
        var code =$(this).attr('id');
        $('input[name="schcloneicode"]').val(icode);
        $('input[name="schclonecode"]').val(code);
        $("#scheme_modal2").modal('show');
    });
    /**
     * 停用
     */
    $(document).on('click','.J_stop',function(event){
        event.preventDefault();
        var self = this,
            id = $(self).attr('id');
        $.ajax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=schstop',
            data: "id=" + id,
            type:'post',
            dataType:'json',
            timeout:1000,
            success:function(data){
                if (data.returncode == 200) {
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("停用成功！");
                }
            }
        });

    });
    /**
     * 选用
     */
    $(document).on('click','.J_start',function(event){
        event.preventDefault();
        var self = this,
            code = $(self).attr('id');
        $.ajax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=schstart',
            data:  {"icode": icode, "code":code},
            type:'post',
            dataType:'json',
            timeout:1000,
            success:function(data){
                if (data.returncode == 200) {
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("选用成功！");
                }/*else if(data.returncode == 300){
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("请设置方案权重");
                }*/
            }
        });

    });
    /**
     * 编辑方案
     */
    $(document).on('submit', '.J_sch_edit', function(event) {
        event.preventDefault();

        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="scheditname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="scheditname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="scheditname"]').val().match(namecheck);
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
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("编辑成功！");
                    $("#scheme_modal1").modal('hide');
                }else if (data.returncode == 300) {
                    alert(data.returndata);
                    $("#scheme_modal1").modal('show');
                } else {
                    alert("编辑失败");
                }
            }
        })
    });
    /**
     * 删除方案
     */
    $(document).on('click','.J_del',function(event){
        event.preventDefault();
        var self = this,
            id = $(self).attr('id');
        if(!confirm("确定要删除选中记录吗？")){
            return;
        }
        $.ajax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=schdelete',
            data: "id=" + id,
            type:'post',
            dataType:'json',
            timeout:1000,
            success:function(data){
                if (data.returncode == 200) {
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("删除成功！");
                }
            }
        });

    });
    /**
     * 克隆方案
     */
    $(document).on('submit', '.J_sch_clone', function(event) {
        event.preventDefault();

        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        if (!checkDelegate.checkNormal($('input[name="schclonename"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="schclonename"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为50', 'param': 51 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var namecheck = /^[0-9a-zA-z-_\u4e00-\u9fa5]+$/;
        var z = $('input[name="schclonename"]').val().match(namecheck);
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
                    var url=common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("克隆成功！");
                    $("#scheme_modal2").modal('hide');
                }else if (data.returncode == 300) {
                    alert(data.returndata);
                    $("#scheme_modal2").modal('show');
                } else {
                    alert("克隆失败");
                }
            }
        })

    });


})