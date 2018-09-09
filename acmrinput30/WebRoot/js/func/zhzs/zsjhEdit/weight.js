define(function (require,exports,module) {
    var $ = require('jquery'),
        pjax=require('pjax'),
        weightset=require('weightset'),
        common = require('common');


    $(".mod_up").click(moveup)
    $(".mod_down").click(movedown)
    $(".save_weight").click(save)
    var icode=$(".indexCode").val()
    var st = new Date().getTime();//时间戳


    function moveup() {
       // console.log("上移")
        var thiscode=$(this).parent().prev().prev().attr("code")
        var pcode=$(this).parent().prev().prev().attr("procode")
        //console.log(thiscode)
        //console.log(pcode)
        var codes=[]
        $("[procode="+pcode+"]").each(function () {
            codes.push($(this).attr("code"))
        })
       // console.log(codes)

        for(var i=0;i<codes.length;i++){
            if (codes[i]==thiscode){
                bcode=codes[i-1]
                codes[i-1]=thiscode
                codes[i]=bcode
                break
            }
        }
       // console.log(codes)
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=resort&codes="+codes,
            type:'get',
            data:'json',
            success:function (re) {
               // console.log("success")
                window.location.reload()
            }
        })
    }

    function movedown() {
        console.log("下移")
        var thiscode=$(this).parent().prev().prev().attr("code")
        var pcode=$(this).parent().prev().prev().attr("procode")
        var codes=[]
        $("[procode="+pcode+"]").each(function () {
            codes.push($(this).attr("code"))
        })
        for(var i=0;i<codes.length;i++){
            if (codes[i]==thiscode){
                bcode=codes[i+1]
                codes[i+1]=thiscode
                codes[i]=bcode
                break
            }
        }
        // console.log(codes)
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=resort&codes="+codes,
            type:'get',
            data:'json',
            success:function (re) {
                // console.log("success")
                window.location.reload()
            }
        })
    }

    function save() {
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

        // console.log("保存")
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
        //小数点先写死1，还没取

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
                return
            }
        }

        //检查通过，保存
        var cws=[]
        $(".input_weight").each(function () {
            var code=$(this).parent().prev().attr("code")
            var weight=$(this).val()
            cws.push(code+":"+weight)
        })
        console.log(cws)
        $.ajax({
            url:common.rootPath+"zbdata/weightset.htm?m=setWeights&cws="+cws,
            type:'get',
            data:'json',
            success:function (re) {
                console.log("success")
                alert("保存成功")
            }
        })


    }

/*    function sendPjax() {
        $.pjax({
            url:common.rootPath+'zbdata/weightset.htm?m=editweight&icode='+icode+'&st='+st,
            container:'.J_weight_table',
            timeout:5000
        })
    }*/


})