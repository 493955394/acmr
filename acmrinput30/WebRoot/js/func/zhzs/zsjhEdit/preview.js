define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax = require('pjax'),
        common = require('common');
    $(document).ready(function(){
       // mc('preview-table',0,0,0);
        var zbcode = $("#zblist").find("option:selected").val();
        sendzbPjax(zbcode);
        footerPosition();
        $(window).resize(footerPosition);
        drawtable()
    })
    var icode = $("#preview-code").val();
    /**
     * 合并第一列
     * @param tb的id
     * @param colLength要合并的列数
     */
    function mc(tableId, startRow, endRow, col) {
        var tb = document.getElementById(tableId);
        if (col >= tb.rows[0].cells.length) {//第一行的列数
            return;
        }
        if (col == 0) { endRow = tb.rows.length-1; }
        for (var i = startRow; i < endRow; i++) {
            if (tb.rows[startRow].cells[col].innerHTML == tb.rows[i + 1].cells[0].innerHTML) {
                tb.rows[i + 1].removeChild(tb.rows[i + 1].cells[0]);
                tb.rows[startRow].cells[col].rowSpan = (tb.rows[startRow].cells[col].rowSpan | 0) + 1;
            } else {
                startRow = i + 1;
            }
        }
    }

    function footerPosition(){
        $(".footer").removeClass("fixed-footer");
        var contentHeight = document.body.scrollHeight,//网页正文全文高度

            winHeight = window.innerHeight;//可视窗口高度，不包括浏览器顶部工具栏
        if(!(contentHeight > winHeight)){
            //当网页正文高度小于可视窗口高度时，为footer添加类fixed-footer
            $(".footer").addClass("fixed-footer");
            $(".content").height(winHeight);
        } else {
            $(".footer").removeClass("fixed-footer");
        }
    }
    var sjselect=$("#timecode").val();
    var scodes = $("#scheme_codes").val();
    $(document).on('click', '#timeinput', function () {
        var timeinput = $("#timecode").val();
        var url = common.rootPath + "zbdata/zsjhedit.htm?m=previewTimeCheck&timeinput="+timeinput+"&icode="+icode;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                if (data.returncode == 300) {
                    alert("时间格式有误");
                } else if (data.returncode == 200) {
                    if(!(data.returndata ==""||data.returndata==null)){
                        sjselect = data.returndata;
                       // console.log(sjselect);
                        sendPjax();
                    }
                }
            }
        })
    })

    function sendPjax(){
        var url = common.rootPath + "zbdata/zsjhedit.htm?m=preCaculate&icode="+icode +"&time="+sjselect;
        $.pjax({
            url: url,
            async:false,
            container: '.J_preview_data_table',
            timeout: 10000
        })
            mc('preview-table',0,0,0);
            footerPosition();
            $(window).resize(footerPosition);
        drawtable();

    }

    /**
     * 触发切换指标的值
     */
    $("#zblist").change(function (e) {
        var zbcode = $(this).val();
        sendzbPjax(zbcode);
    })

    function sendzbPjax(zbcode){
        var url = common.rootPath + "zbdata/zsjhedit.htm?m=preZbValue&icode="+icode +"&time="+sjselect+"&scodes="+scodes+"&zbcode="+zbcode;
        $.pjax({
            url: url,
            async:false,
            container: '.J_zb_data_table',
            timeout: 10000
        })
        mc('previewzb-table',0,0,0);
        footerPosition();
        $(window).resize(footerPosition);
     //   drawtable();

    }

    //重新绘制表格，定宽
    function drawtable() {
        var windowwidth = $(window).width();
        var table = $("#preview-table").width();
        if(windowwidth>table){
            $(".table").css("width","100%")
        }
        else {
            $(".table").css("width","auto")
        }
    }
})