define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        dropdown = require('dropdown'),
        Pagination = require('pagination'),
        common = require('common'),
        pjax=require('pjax'),
        tree = require('tree'),
        fileupload = require('fileupload'),
        PackAjax = require('Packajax'),
        modal = require('modal'),
        ZeroClipboard = require('ZeroClipboard'),
        tab = require('tab');
    var deptName;
    //所属目录树
    var setting = {
        async: {
            enable: true,
            url: common.rootPath + 'zbdata/zsjhedit.htm?m=getchilds',
            contentType: 'application/json',
            type: 'get',
            autoParam: ["id"]
        },
        callback: {
            onClick: clickEvent
        }
    }

    function clickEvent(event, treeid, treeNode) {
        if (treeNode.id != '') {
            $('input[name=proname]').val(treeNode.name);
            $('input[name=procode]').val(treeNode.id);
        } else {
            $('input[name=proname]').val('');
        }
    }
    var rootNode = [{
        "id": "",
        "name": "所属目录",
        "open": "true",
        "isParent": "true"
    }];
    $.fn.zTree.init($("#tree"), setting, rootNode);
});