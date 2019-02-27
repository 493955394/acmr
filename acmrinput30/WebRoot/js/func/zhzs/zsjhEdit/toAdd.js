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

        //footer位置设置
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
        footerPosition();
        $(window).resize(footerPosition);
    })

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
        $('input[name="ZS_code"]').val(uuid);
    }
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
       /* if (!checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'required', 'msg': '编码不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'ch', 'msg': '编码不能包含汉字' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_code"]'), [{ 'name': 'maxlength', 'msg': '编码最大长度为20', 'param': 21 }])) {
            flag = false;
        }*/
        if (!checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'required', 'msg': '名称不能为空' }]) ||
            !checkDelegate.checkNormal($('input[name="ZS_cname"]'), [{ 'name': 'maxlength', 'msg': '名称最大长度为100', 'param': 101 }])) {
            flag = false;
        }
        if (flag == false) {
            return;
        }

        get_uuid();//获取编码
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功");
                    window.close();
                }else if(data.returncode == 501){
                    alert("该编码已经存在");
                }
                else if(data.returncode == 301){
                    alert("该名称已经存在");
                }
                else if(data.returncode == 400){
                    alert("该指标已被删除");
                }
            },
            error: function() {
                common.commonTips('添加失败');

            }

        })

    });
    /**
     * 控制显示和隐藏
     */
    $(document).on('change', '[name=ifzs]', function(event){
        var isGroup = $(this).val();
        if(isGroup === '1'){
           cleanContents();
            $('#select_zb').hide();
            $('#secend_zs').show();
        }else if(isGroup === '0'){
            $('#secend_zs').hide();
            cleanContents();
            $('#select_zb').show();
        }else {
            $('#select_zb').hide();
            $('#secend_zs').hide();
        }
    })

    function  cleanContents(){
        //清空所选
        $(".cjzs").find("option[value = '"+ciji+"']").attr('selected',true);
        $(".zb_ifzs").find("option[value = '"+zhibiao+"']").attr('selected',true);
        $('[name=dotcount]').val('1');
    }

    $(document).on('click',".resetbutton" ,function() {//初始化一次
        window.location.reload()
    })
})