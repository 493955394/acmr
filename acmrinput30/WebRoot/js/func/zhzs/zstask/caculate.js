define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        pjax=require('pjax'),
        common = require('common');

    $("#data_reload").click(reData)
    var taskcode=$("#t_code").val();
    //$(".weight_select").change(reWeight)
    $(document).ready(function(){
        if($("#result-ifcomplete").val()=="true")
            alert("原始数据缺失");
    })
    $(document).on('click','.weight_select',function (event) {
   // function reWeight() {

            $.pjax({
                url:common.rootPath+'zbdata/zscalculate.htm?m=ReWeight&taskcode='+taskcode,
                container:'.J_zsjs_weight',
                timeout:5000
            })


    })

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
        //每次重新计算先校验
        var flag = false;
        Array.prototype.contain = function(val)
        {
            for (var i = 0; i < this.length; i++)
            {
                if (this[i] == val)
                {
                    return true;
                }
            }
            return false;
        };
        var codes=[]
        //检查不为负值
        $(".input_weight").each(function () {
            //console.log($(this).val())
            var value=$(this).val()
            if (value<0){
                alert("权重不能为小于0的数")
                flag = true;
                return
            }
            var pcode=$(this).attr("class").split(" ")[1]
            if (!codes.contain(pcode)){
                codes.push(pcode)
            }
        })
        //检查同一父节点的值和1

        for (var i=0;i<codes.length;i++){
            var pcode=codes[i]
            var values=[]
            var value=$("input[class~="+pcode+"]").each(function () {
                values.push($(this).val())
            })
            var sum=parseFloat("0")
            for (var j=0;j<values.length;j++){
                sum=parseFloat(sum)+parseFloat(values[j].valueOf())
            }
            sum=sum.toFixed(1)
            //console.log(sum)
            if (sum!=1){
                alert("同一级别权重和必须为1")
                flag = true;
                return;
            }
        }
        if(flag){
            return
        }
        //检查通过，检验原始数据
        var yy = false;
        $('#origin-data tr').each(function(i){                   // 遍历 tr
            $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
                if($(this).text() == ""){
                   yy=true;
                }
            });
        });
        if(yy){
            alert("原始数据缺失")
            $('#myTabs li:eq(1) a').tab('show');
            return;
        }
        var cws="";
        $(".input_weight").each(function () {
            var code=$(this).parent().prev().attr("code")
            var weight=$(this).val()
            cws +=code+":"+weight+","
        });
        $('#myTabs li:eq(2) a').tab('show');
        $.pjax({
            url: common.rootPath+'zbdata/zscalculate.htm?m=docalculate&taskcode='+taskcode+"&cws="+cws,
            type: "get",
            container:'.calculate_result',
            timeout: 10000
        })
        $(document).on('pjax:success', function() {
            alert("计算成功");
            $(this).off('pjax:success')
        });
    })
    /**
     * 关闭按钮
     */
    $(document).on('click','#goback',function () {
        if(!confirm("确定要关闭吗？")){
            return;
        }
        var taskcode = $("#t_code").val();
        var right = $(this).attr("data-right");
        $.ajax({
            url:common.rootPath+"zbdata/zscalculate.htm?m=goback",
            data:{"taskcode":taskcode},
            type:'post',
            datatype:'json',
            timeout: 10000,
            success:function (re) {
                window.location.href=common.rootPath+"zbdata/zstask.htm?icode="+re.returndata+"&right="+right;
            }
        })
    })

    /**
     * 重置按钮
     */
    $(document).on('click','#resetpage',function () {
        if(!confirm("确定要重置吗？")){
            return;
        }
        var taskcode = $("#t_code").val();
        $.ajax({
            url:common.rootPath+"zbdata/zscalculate.htm?m=reset",
            data:{"taskcode":taskcode},
            type:'post',
            datatype:'json',
            timeout: 10000,
            success:function (re) {
                if(re.returncode == 200){
                    window.location.reload(true);
                }
                else if(re.returncode == 300){
                    alert("重置失败！")
                    window.location.reload(true);
                }
            }
        })
    })

    /**
     * 保存并重新计算按钮
     */
    $(document).on('click','#save_calculate',function () {
        //每次重新计算先校验
        var flag = false;
        Array.prototype.contain = function(val)
        {
            for (var i = 0; i < this.length; i++)
            {
                if (this[i] == val)
                {
                    return true;
                }
            }
            return false;
        };
        var codes=[]
        //检查不为负值
        $(".input_weight").each(function () {
            //console.log($(this).val())
            var value=$(this).val()
            if (value<0){
                alert("权重不能为小于0的数")
                flag=true;
                return
            }
            var pcode=$(this).attr("class").split(" ")[1]
            if (!codes.contain(pcode)){
                codes.push(pcode)
            }
        })
        //检查同一父节点的值和1

        for (var i=0;i<codes.length;i++){
            var pcode=codes[i]
            var values=[]
            var value=$("input[class~="+pcode+"]").each(function () {
                values.push($(this).val())
            })
            var sum=parseFloat("0")
            for (var j=0;j<values.length;j++){
                sum=parseFloat(sum)+parseFloat(values[j].valueOf())
            }
            sum=sum.toFixed(1)
            //console.log(sum)
            if (sum!=1){
                alert("同一级别权重和必须为1")
                flag=true;
                return;
            }
        }
        if(flag){
            return;
        }
        //检查通过，检验原始数据
        var yy = false;
        $('#origin-data tr').each(function(i){                   // 遍历 tr
            $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
                if($(this).text() == ""){
                    yy=true;
                }
            });
        });
        if(yy){
            alert("原始数据缺失")
            $('#myTabs li:eq(1) a').tab('show')
            return;
        }
        var cws="";
        $(".input_weight").each(function () {
            var code=$(this).parent().prev().attr("code")
            var weight=$(this).val()
            cws +=code+":"+weight+","
        });
        $('#myTabs li:eq(2) a').tab('show')
        $.pjax({
            url: common.rootPath+'zbdata/zscalculate.htm?m=savecalculate&taskcode='+taskcode+"&cws="+cws,
            type: "get",
            container:'.calculate_result',
            timeout: 10000
        })
        $(document).on('pjax:success', function() {
            alert("保存成功");
            $(this).off('pjax:success')
        });
    })


});