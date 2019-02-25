define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        pjax=require('pjax'),
        modal = require('modal');


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
        $.ajax({
            url: currentUrl,
            data: $.param({'inputifzs':$('#selectifzs').attr("data-value")})+'&'+$(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功");
                    window.close();
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

    $(document).on('click',".resetbutton" ,function(event) {//初始化一次
       window.location.reload()
    })
})