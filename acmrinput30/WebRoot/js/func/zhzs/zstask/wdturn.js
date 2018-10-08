define(function (require,exports,module) {
    var $ = require('jquery'),
        pjax=require('pjax'),
        modal = require('modal'),
        common = require('common'),
        pastviews = require('js/func/zhzs/zstask/pastviews');

    var tableRow=pastviews["tableRow"]; //表格行维度
    var tableCol=pastviews["tableCol"];  //表格列维度


    //根据tablerow和tablecol填充表格
    function getTable(){
        $(".wdturn-table").append("<tr><td>" +
            tableRow+"</td><td>" +
            tableCol+"</td><td>" +
            tableCol+"</td><td>" +
            tableCol+"</td></tr>").append("<tr><td>" +
            tableRow+"</td><td></td><td></td><td></td></tr>").append("<tr><td>" +
            tableRow+"</td><td></td><td></td><td></td></tr>")
    }
    //默认执行一次
    getTable()

    //点选col
    $(document).on("click",".top-select",function () {
        var choose=$(this).html()
        if (choose!=tableRow){
            tableCol=choose
            $(".wdturn-table").empty()
            getTable()
           /* $(".wdturn-table").append("<tr><td>" +
                tableRow+"</td><td>" +
                tableCol+"</td><td>" +
                tableCol+"</td><td>" +
                tableCol+"</td></tr>").append("<tr><td>" +
                tableRow+"</td><td></td><td></td><td></td></tr>").append("<tr><td>" +
                tableRow+"</td><td></td><td></td><td></td></tr>")*/
        }
        else {
            alert("行列维度相同")
        }
    })
    //点选row
    $(document).on("click",".left-select",function () {
        var choose=$(this).html()
        if (choose!=tableCol){
            tableRow=choose
            $(".wdturn-table").empty()
            getTable()
         /*   $(".wdturn-table").append("<tr><td>" +
                tableRow+"</td><td>" +
                tableCol+"</td><td>" +
                tableCol+"</td><td>" +
                tableCol+"</td></tr>").append("<tr><td>" +
                tableRow+"</td><td></td><td></td><td></td></tr>").append("<tr><td>" +
                tableRow+"</td><td></td><td></td><td></td></tr>")*/
        }
        else {
            alert("行列维度相同")
        }
    })

    module.exports={
        tableRow:tableRow,
        tableCol:tableCol
    }


})