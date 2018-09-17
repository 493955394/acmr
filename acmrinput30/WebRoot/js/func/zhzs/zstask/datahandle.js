define(function(require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        dropdown = require('dropdown'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax = require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        modal = require('modal'),
        ZeroClipboard = require('ZeroClipboard');
/*
    /!**
     * 重新读取数据
     *!/
    $(document).on("click","#data_reload",reloadOrigindata)

    function reloadOrigindata(){
        var taskcode=$(".reloaddata").val();
        //var time = $("#time").val();
        $.ajax({
            url:common.rootPath+"zbdata/datahandle.htm?m=reGetData&taskcode="+taskcode,
            type:'get',
            data:'json',
            success:function (re) {
                console.log(re)
                window.location.href = common.rootPath+"zbdata/datahandle.htm?m=reGetData&taskcode="+taskcode
            }
        })
    }
*/

    /**
     * 数据下载
     */
    $(document).on("click","#data_download",downdata)

    function downdata(){
        var istmp=$(".istmpdata").val();
        var url = common.rootPath+"zbdata/zscalculate.htm?m=toExcel";
        //var time = $("#time").val();
        $.ajax({
            url:url,
            type:'post',
            dataType: 'json',
            data:"istmp"+istmp,
            success:function (data) {
                if (data.returncode == 300) {
                    alert("数据为空");
                } else {
                    alert("下载成功!");
                }
            }
        })
        window.location.href = url;
    }
    /**
     * 文件上传
     */

    $('#J_fileupload_importTask').click(uploadData);

    function uploadData() {
        $('#J_fileupload_importTask', document).fileupload({
            url: common.rootPath + 'zbdata/datahandle.htm?m=updateTaskData',
            dataType: 'json',
            done: function (e, data) {
                var result = data.result;
                if (result.returncode == 200) {
                    $("#import_count").html("文件上传成功");
                } else if (result.returncode == 400) {
                    $("#import_count").html("<span style='color:red'>上传失败！" + result.returndata + "</span>");
                } else {
                    alert("数据不正确,上传失败");
                }
            }
        })
    }
});