define(function (require,exports,module) {

    var $ = require('jquery'),
        pjax=require('pjax'),
        modal = require('modal'),
        common = require('common');


    var tableRow="指标"; //表格行维度
    var tableCol="时间";  //表格列维度
    var indexcode=$(".indexcode").val()
    var time="";//查询的时间
    var code;//传给后台重新请求数据的单个维度code

    $(document).on('click','#wd-change',function () {
        console.log("wdchange")
        tableRow=$("#table-Row").val()
        tableCol=$("#table-Col").val()
        //console.log(tableRow)
        //console.log(tableCol)
        //发送请求刷新
        sendPjax();

    })

    $(document).on('click','#timeinput',function () {
       var timeinput = $("#timecode").val();
        var url = common.rootPath+"zbdata/pastviews.htm?m=timeCheck&icode="+indexcode+"&timeinput="+timeinput;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function (data) {

                if (data.returncode == 300) {
                    alert("时间格式有误");
                } else if (data.returncode == 200){
                    console.log(data.returndata)
                }
            }
        })
    })

    function sendPjax(){
        var url=common.rootPath+"zbdata/pastviews.htm?m=reTable&icode="+indexcode+"&tableRow"+tableRow+"&tableCol"+tableCol+"&code"+code+"&time"+time

    }

    module.exports={
        tableRow:tableRow,
        tableCol:tableCol
    }
    //下载
    $(document).on('click', '.pastview_download', function(event) {
        event.preventDefault();
        var regcode=$("#select-data option:selected").val();
        var url = common.rootPath+"zbdata/pastviews.htm?m=toRegExcel&regcode="+regcode;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function (data) {

                if (data.returncode == 300) {
                    alert("数据为空");
                } else {
                    alert("下载成功!");
                }
            }
        })
        window.location.href = url;
    })
    /**
     * 序列变换pjax请求
     */
    $("#select-data").change(function(){
        var icode = $('input[name=indexcode]').val();
        var code=$("#select-data option:selected").val();
        console.log(code)
        //要触发的事件
        $.pjax({
            url: common.rootPath+'zbdata/pastviews.htm?m=regDatas&code='+code
                +'&icode='+icode+'&tableRow='+tableRow+'&tableCol='+tableCol,
            type: "get",
            container:'.J_pastviews_data_table'
        })
    });

})