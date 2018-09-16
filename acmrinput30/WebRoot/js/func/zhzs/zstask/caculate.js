define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        pjax=require('pjax'),
        common = require('common');

    $("#data_reload").click(reData)
    var taskcode=$("#t_code").val();

    function reData() {
        console.log("重新读取数据")
        $.pjax({
            url:common.rootPath+'zbdata/zscalculate.htm?m=ReData&taskcode='+taskcode,
            container:'.J_zsjs_data',
            timeout:2000
        })
    }

    //重新计算弹框
    $(document).on('click','#recalculate',function () {
        $.pjax({
            url: common.rootPath+'zbdata/zscalculate.htm?m=docalculate&taskcode='+taskcode,
            type: "get",
            container:'.calculate_result'
        })
        $(document).on('pjax:success', function() {
            alert("ok");
        });
    })


});