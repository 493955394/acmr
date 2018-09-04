define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        zbAdd=require('js/func/zhzs/zsjhEdit/zbAdd'),
        editjsp = require('editjsp');

    var indexCode=$("#index_code").val();

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
})