define(function (require,exports,module) {

    var $ = require('jquery'),
        pjax = require('pjax'),
        modal = require('modal'),
        dropList = require('dropList'),
        common = require('common');


    var tableRow = "指标"; //表格行维度
    var tableCol = "时间";  //表格列维度
    var indexcode = $(".indexcode").val()
    console.log(indexcode)
    var time = null;//查询的时间
    var spancode = null;//传给后台重新请求数据的单个维度code
    var timeinput = "";

    //查询的时间
    $(function(){
        var json2 = {
            wdcode:'sj',
            wdname:'时间',
            nodes:[
                {code:"last5",name:'最近五期'}
            ]
        };
        var dt2 = $('#mySelect2');
        dt2.dropList(json2,{isText:true},function(o){		//事件处理
            timeinput =  o.getItem().code;
           timeClick();
        });
    });


    //维度转换

    $(document).on('click', '#wd-change', function () {
        //console.log("wdchange")
        tableRow = $("#table-Row").val()
        tableCol = $("#table-Col").val()
        spancode=null
        //console.log(tableRow)
        //console.log(tableCol)
        //发送请求刷新
        sendPjax();

    })

  function  timeClick () {
      var timeinput = $("#timecode").val();
      var url = common.rootPath + "zbdata/pastviews.htm?m=timeCheck&icode=" + indexcode + "&timeinput=" + timeinput;
      $.ajax({
          url: url,
          type: 'get',
          dataType: 'json',
          success: function (data) {
              console.log(data.returncode)
              if (data.returncode == 300) {
                  alert("时间格式有误");
              } else if (data.returncode == 200) {
                  if (!(data.returndata == "" || data.returndata == null)) {
                      time = data.returndata;
                      sendPjax();
                  }
                  else {
                      $(".pastviewtable tbody").html("");
                      $(".pastviewtable tbody").append(" <tr><td colspan='" + $('.tbgs').val() + 1 + "'>没有查询到数据</td></tr>");
                  }
              }
          }
      })
  }


    //下拉框选择

    $(document).on('change','.wd_selector',function () {
        var thiscode=$('.wd_option:selected').attr("id")
        if (thiscode=="change"){
            //序列化
            var nRow=$(this).prev().text().substr(0,2)
            //console.log(nRow)
            $("#table-Row").val(nRow)
            spancode=null
            sendPjax()
        }
        else {
            spancode=thiscode
            //重新请求数据
            sendPjax()
        }
    })

    function sendPjax() {
        tableRow = $("#table-Row").val()
        tableCol = $("#table-Col").val()
        tableRow = tableMapping(tableRow)
        tableCol = tableMapping(tableCol)

        console.log("spancode:" + spancode)
        console.log("time:" + time)
        console.log("tablerow:" + tableRow)
        console.log("tablecol:" + tableCol)
        var url = common.rootPath + "zbdata/pastviews.htm?m=reTable&icode=" + indexcode + "&tableRow=" + tableRow + "&tableCol=" + tableCol + "&spancode=" + spancode + "&time=" + time

        $.pjax({
            url: url,
            container: '.J_pastviews_data_table',
            timeout: 10000
        })

        $(document).on('pjax:success', function() {
            tableRow = $("#table-Row").val()
            tableCol = $("#table-Col").val()
            if (tableRow!="时间"&&tableCol!="时间"){
                $("#mySelect2").css("display","none")
                time=null
            }
            else {
                $("#mySelect2").css("display","inline")
            }
        });
    }


    //映射tablerow，tablecol
    function tableMapping(i) {
        if (i == "指标") return "zb"
        else if (i == "时间") return "sj"
        else if (i == "地区") return "reg"
    }

    module.exports = {
        tableRow: tableRow,
        tableCol: tableCol
    }
    //下载
    $(document).on('click', '.pastview_download', function (event) {
        event.preventDefault();
        var thiscode = $('.wd_option:selected').attr("id")
        tableRow = $("#table-Row").val()
        tableCol = $("#table-Col").val()
        tableRow = tableMapping(tableRow)
        tableCol = tableMapping(tableCol)
        var url = common.rootPath + "zbdata/pastviews.htm?m=toExcel&icode=" + indexcode + "&tableRow=" + tableRow + "&tableCol=" + tableCol + "&spancode=" + thiscode + "&time=" + time
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                if (data.returncode == 300) {
                    alert("数据为空");
                } else {
                    window.location.href = url;
                }
            }
        })

    })


})