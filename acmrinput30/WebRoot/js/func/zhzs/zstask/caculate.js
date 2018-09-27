define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        pjax=require('pjax'),
        common = require('common');

    $("#data_reload").click(reData)
    var taskcode=$("#t_code").val();
    $(".weight_select").change(reWeight)


    function reWeight() {
        if ($(".weight_select").val()=="恢复默认值"){
            console.log("恢复")
            $.pjax({
                url:common.rootPath+'zbdata/zscalculate.htm?m=ReWeight&taskcode='+taskcode,
                container:'.J_zsjs_weight',
                timeout:5000
            })

        }
    }

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
                return
            }
            var pcode=$(this).attr("class").split(" ")[1]
            console.log(pcode)
            if (!codes.contain(pcode)){
                codes.push(pcode)
            }
        })
        //检查同一父节点的值和1

        for (var i=0;i<codes.length;i++){
            var pcode=codes[i]
            var values=[]
            var value=$("input[class*="+pcode+"]").each(function () {
                values.push($(this).val())
            })
            var sum=parseFloat("0")
            for (var j=0;j<values.length;j++){
                sum=parseFloat(sum)+parseFloat(values[j].valueOf())
            }
            sum=sum.toFixed(1)
            //console.log(sum)
            if (sum!=1){
                console.log(codes[i])
                alert("同一级别权重和必须为1")
                return;
            }
        }

        //检查通过，保存
        var cws="";
        $(".input_weight").each(function () {
            var code=$(this).parent().prev().attr("code")
            var weight=$(this).val()
            cws +=code+":"+weight+","
        });
      //  console.log(cws)
        $.pjax({
            url: common.rootPath+'zbdata/zscalculate.htm?m=docalculate&taskcode='+taskcode,
            data:{"cws":cws},
            type: "post",
            container:'.calculate_result'
        })
        $(document).on('pjax:success', function() {
            //alert("ok");
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
        $.ajax({
            url:common.rootPath+"zbdata/zscalculate.htm?m=goback",
            data:{"taskcode":taskcode},
            type:'post',
            datatype:'json',
            timeout: 10000,
            success:function (re) {
                window.location.href=common.rootPath+"zbdata/zstask.htm?id="+re.returndata;
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
                return
            }
            var pcode=$(this).attr("class").split(" ")[1]
            console.log(pcode)
            if (!codes.contain(pcode)){
                codes.push(pcode)
            }
        })
        //检查同一父节点的值和1

        for (var i=0;i<codes.length;i++){
            var pcode=codes[i]
            var values=[]
            var value=$("input[class*="+pcode+"]").each(function () {
                values.push($(this).val())
            })
            var sum=parseFloat("0")
            for (var j=0;j<values.length;j++){
                sum=parseFloat(sum)+parseFloat(values[j].valueOf())
            }
            sum=sum.toFixed(1)
            //console.log(sum)
            if (sum!=1){
                console.log(codes[i])
                alert("同一级别权重和必须为1")
                return;
            }
        }

        //检查通过，保存
        var cws="";
        $(".input_weight").each(function () {
            var code=$(this).parent().prev().attr("code")
            var weight=$(this).val()
            cws +=code+":"+weight+","
        });
        //  console.log(cws)
        $.pjax({
            url: common.rootPath+'zbdata/zscalculate.htm?m=savecalculate&taskcode='+taskcode,
            data:{"cws":cws},
            type: "post",
            container:'.calculate_result'
        })
        $(document).on('pjax:success', function() {
            //alert("ok");
        });
    })


});