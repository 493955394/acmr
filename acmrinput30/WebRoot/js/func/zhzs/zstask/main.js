define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        Pagination = require('pagination'),
        common = require('common'),
        modal = require('modal');


    $(".zs_calculate").click(hasSession)

    function hasSession(){
        $.ajax({
            url:common.rootPath+"zbdata/zstask.htm?m=findSession",
            type:'get',
            data:'json',
            success:function (re) {
                console.log(re)
                if (!re){
                    //console.log("跳转")
                    window.location.href=common.rootPath+"zbdata/zscalculate.htm"
                }
                else {
                    //有session，弹出框
                    $("#mymodal").modal('toggle')
                    $(".new_calculate").click(function () {
                        window.location.href=common.rootPath+"zbdata/zscalculate.htm"
                    })
                    $(".re_calculate").click(function () {
                        window.location.href=common.rootPath+"zbdata/zscalculate.htm?m=ReCalculate"
                    })
                }
            }
        })
    }
});