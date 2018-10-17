define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        zbAdd=require('js/func/zhzs/zsjhEdit/zbAdd'),
        editjsp = require('editjsp');

    var indexCode=$("#index_code").val();
    var st = new Date().getTime();//时间戳
    $(".weight_set").click(setWeight);

    var choosedprocode = "",
        choosedname = "";

    var setting = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/zsjhedit.htm?m=getModTree&icode='+indexCode+'&st='+st,
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
        choosedprocode = treeNode.id;
        choosedname = treeNode.name;
        if (treeNode.isParent == true) {
            sendPjax(treeNode.id)
            refreshNode()
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
            $(".mod_up").unbind("click")
            $(".mod_up").click(upmove)
            $(".mod_down").unbind("click")
            $(".mod_down").click(downmove)
            $(".mod_edit").unbind("click")
            $(".mod_edit").click(jumpedit)
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
                    $.fn.zTree.init($("#moduleTree"), setting, rootNode);
                }
            }
        })
    }

    function upmove(){
        //console.log("上移")
        var pcode=$(this).parent().prevAll()[0].value;
        var prevs=$(this).parent().parent().prevAll()
        var nexts=$(this).parent().parent().nextAll()
        var precodes=getcodes(prevs)
        var nextcodes=getcodes(nexts)
        var thiscode=$(this).parent().prevAll()[6].innerHTML
        var switchcode=precodes[precodes.length-1];
        precodes[precodes.length-1]=thiscode
        precodes.push(switchcode);
        //console.log(precodes)
        //var codes=precodes+","+nextcodes
        for (var i=nextcodes.length-1;i>=0;i--){
            precodes.push(nextcodes[i])
        }
        console.log(precodes)
        //return precodes
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=resort&codes="+precodes,
            type:'get',
            data:'json',
            success:function (re) {
                sendPjax(pcode)
            }
        })

    }

    function downmove() {
        var pcode=$(this).parent().prevAll()[0].value;
        var prevs=$(this).parent().parent().prevAll()
        var nexts=$(this).parent().parent().nextAll()
        var precodes=getcodes(prevs)
        var nextcodes=getcodes(nexts)
        var thiscode=$(this).parent().prevAll()[6].innerHTML
        //console.log(precodes)
        //console.log(nextcodes)
        //console.log(thiscode)
        var codes=[];
        for (var j=0;j<precodes.length;j++){
            codes.push(precodes[j])
        }
        codes.push(nextcodes[nextcodes.length-1])
        codes.push(thiscode)
        for(var i=nextcodes.length-2;i>=0;i--){
            codes.push(nextcodes[i])
        }
        //console.log(codes)
        $.ajax({
            url:common.rootPath+"zbdata/zsjhedit.htm?m=resort&codes="+codes,
            type:'get',
            data:'json',
            success:function (re) {
                sendPjax(pcode)
            }
        })

    }

    function getcodes(nodes){
        var codes=[]
        for(var i=nodes.length-1;i>=0;i--){
            //console.log(nodes[i].innerText.split("\t")[0])
            codes.push(nodes[i].innerText.split("\t")[0])
        }
        //console.log(codes)
        return codes
    }

    function setWeight(){
        //先判断是否存在空目录，如果存在，不跳转
        $.ajax({
            url: common.rootPath+'zbdata/indexlist.htm?m=checkModuleCat&icode='+indexCode,
            type:'get',
            datatype:'json',
            success:function (re){
                console.log(re)
                if(re == false){
                    alert("指数不能为空！");
                    return;
                }else{
                    window.open(common.rootPath+"zbdata/weightset.htm?m=editweight&icode="+indexCode)
                }
            }
        })
    }

    $(document).ready(function () {
        sendPjax("")
    })
    /**
     * 新增模型节点
     */
    $(document).on('click', '.J_Add_ZS', function(event) {
        event.preventDefault();
            $.ajax({
                url: common.rootPath+'zbdata/zsjhedit.htm?m=toAdd&procodeId='+choosedprocode+'&procodeName='+choosedname+'&indexCode='+indexCode,
                type:'get',
                datatype:'json',
                success:function (re){
                    if(re != "1"){
                        alert("指标下不能添加内容！");
                        return;
                    }else{
                        window.open(common.rootPath+'zbdata/zsjhedit.htm?m=toAddShow&procodeId='+choosedprocode+'&procodeName='+choosedname+'&indexCode='+indexCode);
                    }
                }
            })
    });
    function jumpedit() {
        var code = $(this).parent().prevAll()[6].innerHTML
        window.open(common.rootPath+'zbdata/zsjhedit.htm?m=toEditShow&indexCode='+indexCode+'&code='+code);
    }

    /**
     * 模型规划搜索框
     */
    var delIds = [];
    var isMove = true;
    var searchField = "";
    $(document).on('submit', '.J_search_form', function(event) {
        event.preventDefault();
        var self = this,
            requestUrl = $(self).prop('action'),
            key = $('select',self).val(),
            val = $('input',self).val(),
            str = "&id=";
        var requestData = common.formatData(key,val);
        if(requestData.length>0){
            requestData="&"+requestData;
        }
        if(choosedprocode.length>0){
            str += choosedprocode;
        }
        searchField = requestData+str;
        isMove = false;
        $.pjax({
            url: requestUrl+searchField+'&icode='+indexCode,
            container: '.J_zsjh_module_table'
        });
        $(document).on('pjax:success', function() {
            delIds = [];
        });
    });

    /**
     * 刷新当前节点
     */
    function refreshNode() {
        /*根据 treeId 获取 zTree 对象*/
        var zTree = $.fn.zTree.getZTreeObj("moduleTree"),
            type = "refresh",
            silent = false,
            /*获取 zTree 当前被选中的节点数据集合*/
            nodes = zTree.getSelectedNodes();
        /*强行异步加载父节点的子节点。[setting.async.enable = true 时有效]*/
        zTree.reAsyncChildNodes(nodes[0], type, silent);
    }
})