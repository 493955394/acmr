define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        pjax=require('pjax'),
        modal = require('modal');
    var ifzs = $("#ifzs").val();
    var formulartext = $("#formulatext").val();
    var type = $('input[name = "type"]').val();
    var scode = $('input[name = "scode"]').val();
    var icode = $('input[name = "icode"]').val();
    var sname =  $('input[name = "sname"]').val();
    var schemecodes =  $('input[name = "schemecodes"]').val();
    /**
     * 点击选择按钮之后隐藏和显示的内容
     */
    $(document).ready(function(){
        var id,userdefine;
        id = $('#selectifzs').attr("data-value");
        if(id == 1){
            $('#secend_zs').show();
        }
        else if(id == 0){
            $('#select_zb').show();
        }
        userdefine = $(".formula option:selected").val();

        if(userdefine == "userdefined" && ifzs !=1){
            $('.hidden_group').show();
        } //footer位置设置

        footerPosition();
        $(window).resize(footerPosition);
    })
    
    function footerPosition(){
        $(".footer").removeClass("fixed-footer");
        var contentHeight = document.body.scrollHeight,//网页正文全文高度

            winHeight = window.innerHeight;//可视窗口高度，不包括浏览器顶部工具栏
        if(!(contentHeight > winHeight)){
            //当网页正文高度小于可视窗口高度时，为footer添加类fixed-footer
            $(".footer").addClass("fixed-footer");
            $(".content").height(winHeight);
        } else {
            $(".footer").removeClass("fixed-footer");
        }
    }

    /**
     * 关闭
     */
    $(document).on('click',".close-edit",function () {
        if(type=="A")
            window.location.href = common.rootPath+"zbdata/weightset.htm?m=editSingleWeight&icode="+icode+'&scode='+scode+'&sname='+sname;
        else
            window.location.href =  common.rootPath+"zbdata/weightset.htm?m=editSchemesWeight&icode="+icode+"&schemecodes="+schemecodes;
    })

    $(document).on('change', '[name=formula]', function(event){

        var isGroup = $(this).val();
        if(isGroup == "userdefined"){
            cleanContents();
            $('.hidden_group').show();
            footerPosition();
        }else{
            $('.hidden_group').hide();
            footerPosition();
        }
    })

    /**
     * 编辑保存模型节点
     */
    $(document).on('submit', '.J_addZS_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var formulas = $(".formula option:selected").val();
        var formulatexts = $("#formulatext").val();
        if(formulas =="userdefined" &&(formulatexts == "" ||formulatexts == null) && ifzs == 0 ){
            alert("请筛选对应指标！");
            return;
        }
        $.ajax({
            url: currentUrl,
            data: $.param({'inputifzs':$('#selectifzs').attr("data-value")})+'&'+$(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
               if (data.returncode == 200) {
                    alert("保存成功");
                    if(type=="A")
                   window.location.href = common.rootPath+"zbdata/weightset.htm?m=editSingleWeight&icode="+icode+'&scode='+scode+'&sname='+sname;
                   else
                       window.location.href =  common.rootPath+"zbdata/weightset.htm?m=editSchemesWeight&icode="+icode+"&schemecodes="+schemecodes;
                }
                else if(data.returncode == 300){
                    alert("表达式有误");
                }
                else if(data.returncode == 301){
                    alert("该名称已经存在");
                }
                else if(data.returncode == 400){
                    alert("该指标已被删除");
                }
            },
            error: function() {
                common.commonTips('更新失败');

            }

        })

    });
    function  cleanContents(){
        //清空所选
        $("#formulatext").val(formulartext);
    }
    /**
     * 公式编辑器添加左边的筛选指标
     */
    $("#add_zb").click(function (){
        $(".zb_index").val();//获取当前选择项的值.
        var options=$(".zb_index option:selected");//获取当前选择项
        var code = options.val();//获取当前选择项的值
        var cname = options.text();//获取当前选择项的文本
        var str = "#"+cname+"#";
        if(cname !=""){
            addExpressContent(str);
        }
    })
    /**
     * 选择框清空
     */

   /* $("#hanshu").click(function () {
        $('#shuzu').find("option").each(function() {
            $(this).removeAttr("selected");
        });
    })

    $("#shuzu").click(function () {
        $('#hanshu').find("option").each(function() {
            $(this).removeAttr("selected");
        });
    })*/
    /**
     * 函数的添加
     */
    $("#add_hanshu").click(function () {
        $("#hanshu").val();//获取当前选择项的值.
        var options=$("#hanshu option:selected");//获取当前选择项
        if(options.text()=="") options = $("#shuzu option:selected");//获取当前选择项
        if(options.text()){
            var str = options.val();//获取当前选择项的值
            var text = options.text();//获取当前选择项的文本
            addExpressContent(str);
        }
    });

    /**
     * 检查自定义公式是否合理
     * @param str
     * @returns {boolean}
     */
    function checkZB(str) {
        $(".zb_index option").each(function () {
            var text = $(this).text(); //获取option的text
            var cname = "#" + text + "#";
            str = str.replace(cname, " 2.0 ");
        });
        return str;
    }

    $(document).on('click',".resetbutton" ,function(event) {//初始化一次
        window.location.reload()
    })
})