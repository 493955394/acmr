define(function (require,exports,module) {
    var $ = require('jquery'),
        pjax=require('pjax'),
        //weightset=require('weightset'),
        common = require('common');


    $(document).on('click','.save_weight',function (event) {
        event.stopPropagation();
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
        var flag=true;

        // console.log("保存")
        //检查不为负值
        $(".input_weight").each(function () {
            //console.log($(this).val())
            if (flag){
                var value=$(this).val()
                if (value<0){
                    alert("权重不能为小于0的数")
                    flag=false;
                    return
                }
                var pcode=$(this).attr("pcode")
                //console.log(pcode)
                if (!codes.contain(pcode)){
                    codes.push(pcode)
                }
            }
        })

        //检查同一父节点的值和1
        //小数点先写死1，还没取

        for (var i=0;i<codes.length;i++){
            var pcode=codes[i]
            //console.log("pcode:"+pcode)
            $(".scodes").each(function () {
                var values=[]
                $("input[pcode~="+pcode+"][scode="+$(this).val()+"]").each(function () {
                    values.push($(this).val())
                    //console.log($(this).val())
                })
                var sum=parseFloat("0")
                for (var j=0;j<values.length;j++){
                    sum=parseFloat(sum)+parseFloat(values[j].valueOf())
                }
                sum=sum.toFixed(1)
                //console.log(sum)
                if (sum!=1){
                    //console.log(codes[i])
                    flag=false
                    alert("同一级别权重和必须为1")
                    return
                }
            })

        }

        //检查通过，保存
        if (flag){
            count=0;
            $.ajaxSettings.async=false
            $(".scodes").each(function () {
                var cws=""
                var scode=$(this).val()
                $(".input_weight[scode=" + scode+"]").each(function () {
                    var code=$(this).parent().attr("code")
                    var weight=$(this).val()
                    cws=cws+code+':'+weight+','
                })
                $.ajax({
                    url:common.rootPath+"zbdata/weightset.htm?m=setWeights&scode="+scode,
                    type:'post',
                    data:{
                        cws:cws
                    },
                    success:function (re) {
                        count++;
                    }
                })
            })
            if (count==$(".scodes").length){
                alert("success")
                window.close()
            }
            else {
                alert("保存失败！")
            }


        }
    })
    var icode=$(".indexCode").val()
    $(document).on('click','.edit_formula',function (event) {
        event.preventDefault();
        var modcode = $(this).attr("modcode");
        var scode=$(this).attr("scode")
        window.open(common.rootPath+'zbdata/zsjhedit.htm?m=formularEdit&indexCode='+icode+'&scode='+scode+'&modcode='+modcode);
    })

})