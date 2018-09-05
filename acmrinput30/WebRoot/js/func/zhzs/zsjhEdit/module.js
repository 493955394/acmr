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

    function clickEvent(event, treeid, treeNode) {
        //console.log("clickevent")
        if (treeNode.isParent == true) {
            sendPjax(treeNode.id)
        }
        else {
            return
        }

    }

    //根据code加载子节点
    function sendPjax(code) {

        $.pjax({
            url:common.rootPath+'zbdata/zsjhedit.htm?m=getModList&icode='+indexCode+'&code='+code,
            container:'.J_zsjh_module_table',
            timeout:2000
        })
        $(document).on('pjax:success', function() {
            $(".mod_delete").unbind("click")
            $(".mod_delete").click(deleteMod)
        });
    }

    function deleteMod(){
        console.log("delete")
        var modcode=$(this).parent().prevAll()[6].innerHTML
        var procode=$(this).parent().prevAll()[0].value;
        console.log(procode)
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=deleteMod&code="+modcode+"&indexcode="+indexCode,
            type:'get',
            dataType:'json',
            success:function (re) {
                console.log(re)
                if (re==0){
                    alert("该节点下有子节点，不能删除")
                }
                else {
                    sendPjax(procode)
                }
            }
        })
    }

    $(document).ready(function () {
        sendPjax("")
    })
})