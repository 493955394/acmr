define(function (require,exports,module) {
    var $ = require('jquery'),
        pjax=require('pjax')
        common = require('common');



    var icode=$("#index_code").attr("value");
    var st = new Date().getTime();//时间戳


    $(document).ready(function () {
        //请求方案列表
        sendPjax();
        $(".single_weight_set").click(setSingleWeight);


    })
    //局部刷新方案列表
    function sendPjax() {
        $.pjax({
            url:common.rootPath+'zbdata/indexscheme.htm?m=getSchemeList&icode='+icode+'&st='+st,
            container:'.J_zsjh_scheme_table',
            timeout:10000
        })
        $(document).on('pjax:success', function() {

        });
    }
    //设置单个方案权重
    function setSingleWeight(){
        var scode=$(this).attr("scheme_code")
        //先判断是否存在空目录，如果存在，不跳转
        $.ajax({
            url: common.rootPath+'zbdata/indexlist.htm?m=checkModuleCat&icode='+icode,
            type:'get',
            datatype:'json',
            success:function (re){
                console.log(re)
                if(re == false){
                    alert("指数不能为空！");
                    return;
                }else{
                    window.open(common.rootPath+"zbdata/weightset.htm?m=editSingleWeight&icode="+icode+'&scode='+scode)
                }
            }
        })
    }

})