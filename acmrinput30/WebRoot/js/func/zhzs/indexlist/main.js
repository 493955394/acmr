define(function (require,exports,module) {
    'use strict';
    var $ = require('jquery'),
        tree = require('tree'),
        modal = require('modal'),
        listjsp= require('listjsp');
    var setting = {
        async:{

        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var zNodes =[
        { id:"#1", pId:0, name:"指数",isParent:true},
        { id:"#2", pId:0, name:"我收到的指数",isParent:true},
        { id:"#3", pId:0, name:"我共享的指数", isParent:true},
    ];
    var indexlist=listjsp.indexlist;
    console.log(indexlist)
    for(var i=0;i<indexlist.length;i++){
        zNodes.push(indexlist[i])
    }

    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    });

})