define(function (require,exports,module) {
    var $ = require('jquery'),
        pjax=require('pjax')
        common = require('common');



    var icode=$("#index_code").attr("value");
    var st = new Date().getTime();//时间戳

    $(document).ready(function () {
        //请求方案列表
        sendPjax();

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

})