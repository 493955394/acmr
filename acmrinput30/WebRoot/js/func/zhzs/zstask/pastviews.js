define(function (require,exports,module) {

    var $ = require('jquery'),
        pjax=require('pjax'),
        modal = require('modal'),
        common = require('common');

    var tableRow="指标"; //表格行维度
    var tableCol="时间";  //表格列维度

    module.exports={
        tableRow:tableRow,
        tableCol:tableCol
    }



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
     * 查看往期
     */
    /*$(document).on('click', '.past_task', function (event) {
        event.preventDefault();
        var code = $(this).attr('id');

        var self = this,
            currentUrl = $(self).attr('action');
        $.ajax({
            url: currentUrl,
            data: $(self).serialize(),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function (data) {
                if (data.returncode == 200) {
                    common.commonTips('保存成功！');
                    window.location.reload(true);
                } else if (data.returncode == 300) {
                    common.commonTips('添加失败');
                } else {
                    common.commonTips('添加失败');
                }
            },
            error: function () {
                common.commonTips('添加失败');

            }

        })

    });*/

})