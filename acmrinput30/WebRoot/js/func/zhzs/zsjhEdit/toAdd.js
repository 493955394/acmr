define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        pjax=require('pjax'),
        modal = require('modal');



    var ciji = $(".cjzs option:selected").val();
    var zhibiao = $(".zb_ifzs option:selected").val();
    var ifzs = $("#selectifzs option:selected").val();

    /**
     * 新增模型节点
     */
    $(document).on('submit', '.J_addZS_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('action'),
            checkDelegate;
        checkDelegate = new VaildNormal();
        var flag = true;
        //前端检查
        if (!checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }
        if (!checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }
        var ifavalible =  /^([0-9a-zA-Z]*)$/;
        var zs_code = $('input[name="ZS_code"]').val();
        var check =zs_code.match(ifavalible);
        if(check == null){
            alert("非法的编码");
            return;
        }
        var formulas = $(".formula option:selected").val();
        var formulatexts = $("#formulatext").val();
        var ifzscheck = $("#selectifzs option:selected").val();
        if(formulas =="userdefined" &&(formulatexts == "" ||formulatexts == null) && ifzscheck == 0  ){
            alert("请筛选对应指标！");
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
                    alert("保存成功");
                }else if(data.returncode == 501){
                    alert("该编码已经存在");
                }
                else if(data.returncode == 300){
                    alert("表达式有误");
                }
            },
            error: function() {
                common.commonTips('添加失败');

            }

        })

    });
    /**
     * 点击选择按钮之后隐藏和显示的内容
     */
    $(document).ready(function(){
        var obj, index, id,userdefine;
        obj = document.getElementById('selectifzs');
        index = obj.selectedIndex;
        id = obj.options[index].value;
        if(id == 1){
            $('#secend_zs').show();
        }
        else if(id == 0){
            $('#select_zb').show();
        }
        userdefine = $(".formula option:selected").val();
        if(userdefine == "userdefined" && ifzs == 0){
            $('.hidden_group').show();
        }
    })
    /**
     * 控制显示和隐藏
     */
    $(document).on('change', '[name=ifzs]', function(event){
        var isGroup = $(this).val();
        if(isGroup === '1'){
           cleanContents();
            $('#select_zb').hide();
            $('.hidden_group').hide();
            $('#secend_zs').show();
        }else if(isGroup === '0'){
            $('#secend_zs').hide();
            cleanContents();
            $('#select_zb').show();
            var userdefine = $(".formula option:selected").val();
            if(userdefine == "userdefined"){
                $('.hidden_group').show();
            }
        }else {
            $('#select_zb').hide();
            $('#secend_zs').hide();
        }
    })
    $(document).on('change', '[name=formula]', function(event){
        var isGroup = $(this).val();
        if(isGroup == "userdefined"){
            $('.hidden_group').show();
        }else{
            $('.hidden_group').hide();
        }
    })
    function  cleanContents(){
        //清空所选
        $('select.formula').prop('selectedIndex', 0);
        $(".cjzs").find("option[value = '"+ciji+"']").attr('selected',true);
        $(".zb_ifzs").find("option[value = '"+zhibiao+"']").attr('selected',true);
        $('[name=dotcount]').val('1');
        $("#formulatext").val("");
    }

    /**
     * 公式编辑器添加左边的筛选指标
     */
    $("#add_zb").click(function (){
        $(".zb_index").val();//获取当前选择项的值.
        var options=$(".zb_index option:selected");//获取当前选择项
        if(options){
            var code = options.val();//获取当前选择项的值
            var cname = options.text();//获取当前选择项的文本
            var str = "#"+cname+"#";
            if(cname !=""){
                addExpressContent(str);
            }
        }
    })
    /**
     * 函数的添加
     */
    $("#add_hanshu").click(function () {
        $("#hanshu").val();//获取当前选择项的值.
        var options=$("#hanshu option:selected");//获取当前选择项
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
})