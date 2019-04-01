define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        editjsp = require('editjsp'),
        main = require('js/func/zhzs/zsjhEdit/main'),
        querytable = require('queryLockTable');


    //绑定点击复选框事件
    $('.J_zsjh_rangedata_table').on('pjax:success',function () {
        console.log("pjaxsuccess")

        setTimeout(setTable,500);
        //setTable()

    })
    
    function setTable() {
        var lockCol=$('input[name=lockColNum]').val();
        var w = $("#rangeTable").width(),
            h=$("#rangeTable thead").height(),
            h2=$("#rangeTable tbody").height();
        h2 = h2 >= 650 ? 650 : h2+18;
        $("#rangeTable > table").css('width',w);
        $("#rangeTable").yflockTable({"width":w,"height":h+h2,"fixColumnNumber":lockCol});


        update()
        $(".zb_checkbox").each(function () {
            $(this).change(update)
        })
        $(".reg_checkbox").each(function () {
            $(this).change(update)
        })
    }


    function update(){
        //console.log("change")
        var regnum=$(".table_container_column .reg_checkbox:checked").length;
        var zbnum=$(".table_container_head  .zb_checkbox:checked").length;
        var info="注：已选择地区数"+regnum+"个，已选择指标数"+zbnum+"个"
        $("#check_info").text(info)
    }


})