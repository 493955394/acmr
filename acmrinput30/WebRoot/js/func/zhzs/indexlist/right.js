define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common'),
        dropdown = require('dropdown'),
        pjax=require('pjax'),
        modal = require('modal'),
        AjaxMods = require('AjaxMods');

    /**
     * 菜单树
     */
    var treeNodeId = "";
    var treeNodeName = "";
    var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
    var setting12 = {
        async: {
            enable: true,
            url: common.rootPath+'zbdata/indexlist.htm?m=depUserTree&st='+st,
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent12,
            onAsyncSuccess: zTreeOnAsyncSuccess
        }
    };
    function clickEvent12(event, treeid, treeNode) {
        treeNodeId = treeNode.id;
        treeNodeName = treeNode.name;
        if (treeNode.id != '') {
            $('input[name=regname]').val(treeNode.name);
            $('input[name=regcode]').val(treeNode.id);}
        else {
            $('input[name=regname]').val("");
        }

    }
    var rootNode = [{"id":"","name":"用户组织树", "open":"true", "isParent":"true"}];
    var treeObj = $.fn.zTree.init($("#treeRight"), setting12, rootNode);
    var treenodes = treeObj.getNodes();
    treeObj.expandNode(treenodes[0], true, true, true);

    function zTreeOnAsyncSuccess(event, treeid, treeNode, msg){
        if (initTreePara.length>0){
            var zbs = initTreePara.split("/");
            var nodes;
            var treeObj = $.fn.zTree.getZTreeObj(treeid);

            if (treeNode == null){	// 第一层结点
                nodes = treeObj.getNodes();
            } else {
                nodes = treeNode.children;
            }
            var isBreak = false;
            for (var i = 0; i < nodes.length; i++){
                var node = nodes[i];
                for (var j = 0; j < zbs.length; j++){
                    if (zbs[j] == node.id){
                        if (node.isParent){
                            treeObj.expandNode(node, true);
                            if(node.id== zbs[zbs.length-1]){
                                treeObj.selectNode(node);
                                treeNodeId = node.id;
                                treeNodeName = node.name;
                            }
                        } else {
                            treeObj.selectNode(node);
                            treeNodeId = node.id;
                            treeNodeName = node.name;
                        }
                        isBreak = true;
                        break;
                    }
                }
                if (isBreak){
                    break;
                }
            }
        }
    }

 })