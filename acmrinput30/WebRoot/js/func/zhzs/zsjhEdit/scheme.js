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


    })
    //局部刷新方案列表
    function sendPjax() {
        $.pjax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st,
            container:'.J_zsjh_scheme_table',
            timeout:10000
        })
        $(document).on('pjax:success', function() {

        });
    }
    //设置单个方案权重
    function setSingleWeight(){
        var scode=$(this).attr("scheme_code")
        var sname=$(this).attr("scheme_name")
        //先判断是否存在空目录，如果存在，不跳转
        $.ajax({
            url: common.rootPath+'zbdata/indexlist.htm?m=checkModuleCat&icode='+icode,
            type:'get',
            datatype:'json',
            success:function (re){
                console.log(re)
                if(re == false){
                    alert("指数不能为空！");
                    return;
                }else{
                    window.open(common.rootPath+"zbdata/weightset.htm?m=editSingleWeight&icode="+icode+'&scode='+scode+'&sname='+sname)
                }
            }
        })
    }
    /**
     * 新增方案
     */
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
                    alert("保存成功！");
                    window.location.href= common.rootPath+"zbdata/indexlist.htm?schemecode="+sch_code+"&icode="+icode;
                    // window.location.reload(true);
                }else if (data.returncode == 300) {
                    alert(data.returndata);
                    $("#scheme_modal").modal('show');
                } else {
                    alert("添加失败");
                }
            }
        })
    });
    /**
     * 删除方案
     */
    $(document).on('click','.J_sch_del',function(event){
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
                    var url=window.location.href;
                    $.pjax({
                        url: url,
                        container: '.J_zsjh_scheme_table'
                    });
                    alert("删除成功！");
                    refreshNode(id)
                }
            }
        });
    });


})