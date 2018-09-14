define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        modal = require('modal');
    var icode = $(".geticode").val();
    $(".zs_calculate").click(hasSession)

    function hasSession(){
        var taskcode=$(this).parent().prev().val();
        $.ajax({
            url:common.rootPath+"zbdata/zstask.htm?m=findSession&taskcode="+taskcode,
            type:'get',
            data:'json',
            success:function (re) {
                console.log(re)
                if (!re){
                    //console.log("跳转")
                    window.location.href=common.rootPath+"zbdata/zscalculate.htm?m=ZsCalculate&taskcode="+taskcode
                }
                else {
                    //有session，弹出框
                    $("#mymodal").modal('toggle')
                    $(".new_calculate").click(function () {
                        window.location.href=common.rootPath+"zbdata/zscalculate.htm?m=ZsCalculate&taskcode="+taskcode
                    })
                    $(".re_calculate").click(function () {
                        window.location.href=common.rootPath+"zbdata/zscalculate.htm?m=ReCalculate&taskcode="+taskcode
                    })
                }
            }
        })
    }
    //删除任务
    $(".zs_delete").click(function () {
        if(!confirm("确定要删除该期任务吗？")){
            return;
        }
        var code=$(this).parent().prev().val();
        $.ajax({
            url:common.rootPath+"zbdata/zstask.htm?m=delTask",
            data:{"code":code},
            type:'post',
            datatype:'json',
            timeout: 10000,
            success:function (re) {
                if(re.returncode == 200){
                    alert("删除成功！")
                    window.location.reload(true);
                }else {
                    alert("删除失败！")
                }
            }
        })
    });
    //查询框

    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            requestUrl = $(self).prop('action');
        var time = $("#time").val();
        $.pjax({
            url: requestUrl+"&time="+time+"&id="+icode,
            container: '.task_table'
        });
    });
});