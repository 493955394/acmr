define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        zbAdd=require('js/func/zhzs/zsjhEdit/zbAdd'),
        editjsp = require('editjsp');

    var indexCode=$("#index_code").val();
    var st = new Date().getTime();//时间戳


    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=getModTree&icode='+indexCode,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent,
            //onAsyncSuccess: zTreeOnAsyncSuccess
        },
        view:{
            selectedMulti:false
        }
    };
    var rootNode = [{"id":"","name":"指数结构",pId:"", "open":"false", "isParent":"true"}];
    var treeObj = $.fn.zTree.init($("#moduleTree"), setting, rootNode);

    function clickEvent() {
        console.log("clickevent")
    }
    
    function sendPjax(code) {

        $.pjax({
            url:common.rootPath+'zbdata/zsjhedit.htm?m=getModList&icode='+indexCode+'&code='+code,
            container:'.J_zsjh_module_table',
            timeout:2000
        })
        $(document).on('pjax:success', function() {
            console.log("pjax:success")
        });
    }

    $(document).ready(function () {
        sendPjax("")
    })
})