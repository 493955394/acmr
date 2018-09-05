define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        tree = require('tree'),
        common = require('common'),
        pjax=require('pjax'),
        modal = require('modal');
    /**
     * 新增模型节点
     */
    $(document).on('submit', '.J_addZS_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).attr('action');
        console.log(currentUrl)
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                if (data.returncode == 200) {
                    alert("保存成功");
                }else {
                    alert("保存失败");
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
        var obj, index, id;
        obj = document.getElementById('selectifzs');
        index = obj.selectedIndex;
        id = obj.options[index].value;
        if(id == 1){
            $('#secend_zs').show();
        }
        else if(id == 0){
            $('#select_zb').show();
        }
    })
    $(document).on('change', '[name=ifzs]', function(event){
        var isGroup = $(this).val();
        console.log(isGroup)
        if(isGroup === '1'){
            $('#select_zb').hide();
            $('#secend_zs').show();
        }else if(isGroup === '0'){
            //clearContent();
            $('#secend_zs').hide();
            $('#select_zb').show();
        }else {
            $('#select_zb').hide();
            $('#secend_zs').hide();
        }
    })
    /**
     * 新增模型节点
     */
    $(document).on('submit', '.J_addZS_form', function(event) {
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
                    alert("保存成功");
                }else {
                    alert("保存失败");
                }
            },
            error: function() {
                common.commonTips('添加失败');

            }

        })

    });
})