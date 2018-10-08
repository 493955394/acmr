define(function (require,exports,module) {

    var $ = require('jquery'),
        pjax=require('pjax'),
        modal = require('modal'),
        common = require('common');


    var tableRow="指标"; //表格行维度
    var tableCol="时间";  //表格列维度

    $(document).on('click','#wd-change',function () {
        console.log("wdchange")
        tableRow=$("#table-Row").val()
        tableCol=$("#table-Col").val()
    })

    module.exports={
        tableRow:tableRow,
        tableCol:tableCol
    }


})