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
                if (value<=0){
                    alert("权重不能为小于等于0的数")
                    flag=false;
                    return
                }
                var pcode=$(this).attr("pcode")
                console.log(pcode)
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
            var values=[]
            $("input[pcode~="+pcode+"]").each(function () {
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
                console.log(codes[i])
                flag=false
                alert("同一级别权重和必须为1")
                return
            }
        }

        //检查通过，保存
        if (flag){
            var cws=""
            $(".input_weight").each(function () {
                var code=$(this).parent().attr("id")
                var weight=$(this).val()
                //cws.push(code+":"+weight)
                cws=cws+code+':'+weight+','
            })
            //alert("success")
            $.ajax({
                url:common.rootPath+"zbdata/weightset.htm?m=setWeights&scode="+scode,
                type:'post',
                data:{
                    cws:cws
                },
                success:function (re) {
                    console.log("success")
                    alert("保存成功")
                }
            })
        }
    })
    var icode=$(".indexCode").val()
    var scode=$(".scheme_code").val()
    var sname=$(".scheme_name").val()
    var st = new Date().getTime();//时间戳
    //console.log(icode)


    function sendPjax() {
        $.pjax({
            url:common.rootPath+'zbdata/weightset.htm?m=editSingleWeight&icode='+icode+'&scode='+scode+'&sname='+sname+'&st='+st,
            container:'.J_weight_table',
            timeout:10000
        })

    }

    $(document).on('click','.edit_formula',function (event) {
        event.preventDefault();
        var modcode = $(this).attr("modcode");
        window.open(common.rootPath+'zbdata/zsjhedit.htm?m=formularEdit&indexCode='+icode+'&scode='+scode+'&modcode='+modcode);
    })


})