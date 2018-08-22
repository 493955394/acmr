define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        dropdown = require('dropdown'),
        VaildNormal = require('vaildnormal'),
        tree = require('tree'),
        common = require('common');
    require('uidropdown');
    var depcode = $('#deptCode').val(),
        isEdit = $(".J_edit_form").length,
        rootNode = [{
            "id": "",
            "name": "组织树",
            "open": "true",
            "pId": "0",
            "isParent": "true"
        }],
        /**
         * config user selector list
         * @author wlchair
         * @description update 2016/3/4 and standard code format
         */
        selector = {
            processradio: 'input[name="processradio"]',
            optionStream: '.option-stream',
            processAddBtn: '.J-process-add',
            processStream: '.J-process-stream',
            institutionFrom: '.J_add_form',
            typeCode: 'input[name="code"]',
            //typeName: 'input[name="cname"]',
            //org: 'input[name="deptCode"]',
            processOption: 'input[name="processradio"]:checked',
            //hideProCode: 'input[name="proCode"]',
            //hideModCode: 'input[name="modCode"]',
            //hideFlowCode: 'input[name="flowCode"]',
            //hideFlag: 'input[name="flag"]'
        },
        settings = {
            processValue: '1'
        },
        className = {
            streamPanel: 'panel panel-primary'
        },
        streamList = [],
        templates = {
            processStream: '<div class="panel-heading"><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button><div class="radio-inline"><label><input type="radio" name="vittedno{alias}" value="single" checked>任意一人审核</label></div><div class="radio-inline"><label><input type="radio" name="vittedno{alias}" value="all">均需审核</label></div></div><div class="panel-body"><div class="ui selection multiple dropdown mini {alias}uidropdown" style="width:100%;height:34px;overflow:hidden;"><input type="hidden"><i class="dropdown icon"></i><div class="default text">请选择审批人</div><div class="menu"><div class="divider" style="margin:0;"></div><ul class="ztree select-tree hid-top person" id="{alias}tree"></ul><div class="scrolling menu" style="max-height:none;"></div></div></div>'
        },
        BUILTIN_OBJECT = {
            '[object Function]': 1,
            '[object RegExp]': 1,
            '[object Date]': 1,
            '[object Error]': 1,
            '[object CanvasGradient]': 1
        };
    (function init() {
        if (depcode !== '') {
            $.ajax({
                url: common.rootPath + 'sysindex/sys.htm?m=findPersonTree&selid=' + depcode,
                timeout: 5000,
                dataType: 'json',
                success: function(data) {
                    var len = data.length,
                        currentRootNode = clone(rootNode);
                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            currentRootNode.push(data[i]);
                        }
                    }
                    initTree(currentRootNode);
                    buildDropdown({
                        dropdown: '#organization',
                        tree: 'organizationTree'
                    });
                }
            });
        } else {
            initTree(rootNode);
            buildDropdown({
                dropdown: '#organization',
                tree: 'organizationTree'
            });
        }
    })();

    function initTree(baseNode, settings) {
        var curMenu, zTree_Menu, cfg = {},
            settingtree = {},
            currentSettings = settings || {};
        cfg.url = currentSettings.url || common.rootPath + 'sysindex/sys.htm?m=findPersonTree';
        cfg.tree = currentSettings.tree || 'organizationTree';
        //菜单树
        settingtree = {
            async: {
                enable: true,
                url: cfg.url,
                contentType: 'application/json',
                type: 'get',
                autoParam: ["id"]
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: clickEvent
            }
        };

        function clickEvent(event, treeid, treeNode) {
            if (treeNode.id === "") {
                $(event.target).parent().addClass("disabled");
                return false;
            }
            if (treeid !=='organizationTree' && treeNode.canselect === false) {
                $(event.target).parent().addClass("disabled");
                common.commonTips("请选择审批人员");
                return false;
            }
            var currentNodeId, node;
            currentNodeId = treeNode.tId + '_a';
            node = $('#' + currentNodeId);
            node.data('value', treeNode.id);
        }
        $.fn.zTree.init($('#' + cfg.tree), settingtree, baseNode);
        zTree_Menu = $.fn.zTree.getZTreeObj(cfg.tree);
        if (depcode) {
            curMenu = zTree_Menu.getNodesByParam('id', depcode)[0];
            zTree_Menu.selectNode(curMenu);
        }
    }

    function buildDropdown(settings) {
        var cfg = {
            dropdown: settings.dropdown || '#organization',
            tree: settings.tree || 'organizationTree'
        };
        $(cfg.dropdown).uidropdown({
            preserveHTML: false,
            treeWidget: cfg.tree,
            selector: {
                item: '.item > label, #' + cfg.tree + ' li a:not(.disabled)'
            }
        });
    }

    $(selector.processradio).on('change', function() {
        var value = $(this).val(),
            $optionStream = $(selector.optionStream);
        if (value === settings.processValue) {
            $optionStream.show();
        } else {
            $optionStream.hide();
        }
    });
    $(selector.processAddBtn).on('click', function(e) {
        var buildMap = {
                alias: buildAlias(),
            },
            $streamFrame = $('<div>'),
            map = {};
        map = clone(buildMap);
        streamList.push(map.alias);
        $streamFrame.addClass(className.streamPanel).addClass(map.alias).html(substitute(templates.processStream, map));
        $(selector.processStream).append($streamFrame);
        $streamFrame.on('click', 'button', function(e) {
            $streamFrame.remove();
            streamList.removevalue(map.alias);
        });
        buildDropdown({
            dropdown: '.' + map.alias + 'uidropdown',
            tree: map.alias + 'tree'
        });
        initTree(rootNode, {
            tree: map.alias + 'tree'
        });
    });

    Array.prototype.removevalue = function (val) {  
        var index = this.indexOf(val);  
        if (index > -1) {  
            this.splice(index, 1);  
        }  
    };

    function buildData() {
        var i,
            len = streamList.length,
            data = [],
            singleObj = {};
        for (i = 0; i < len; i++) {
            singleObj.vittedno = $('input[name="vittedno' + streamList[i] + '"]:checked').val();
            singleObj.person = $('.' + streamList[i] + 'uidropdown >input').val();
            data.push(clone(singleObj));
        }
        return data;
    }

    function buildAlias() {
        var date = new Date();
        return 'stream' + parseInt(Math.random() * 1000) + date.getTime();
    }

    function substitute(template, map) {
        return template.replace(/\\?\{([^{}]+)\}/g, function(match, name) {
            return (map[name] === undefined) ? '' : map[name];
        });
    }

    function clone(source) {
        if (typeof source == 'object' && source !== null) {
            var result = source;
            if (source instanceof Array) {
                var i, len;
                result = [];
                for (i = 0, len = source.length; i < len; i++) {
                    result[i] = clone(source[i]);
                }
            } else if (!BUILTIN_OBJECT[Object.prototype.toString.call(source)]) {
                result = {};
                for (var key in source) {
                    if (source.hasOwnProperty(key)) {
                        result[key] = clone(source[key]);
                    }
                }
            }

            return result;
        }
        return source;
    }

    // 编辑
    //if (isEdit) {
        var flowType = $("[name=processradio]:checked").val(),
            i,
            j;
        if (flowType === "1") {
            var flowContent = JSON.parse($("#iflow").val());
            $(selector.optionStream).show();
            for (i = 0; i < flowContent.length; i++) {
                var codeArr = flowContent[i].person.split(","),
                    nameArr = flowContent[i].name.split(","),
                    vittednoType = flowContent[i].vittedno;
                var html = '<div class="panel panel-primary stream' + i + '">';
                html += '<div class="panel-heading">';
                html += '<button type="button" class="close editClose" stream="stream' + i + '" data-dismiss="modal" aria-label="Close">';
                html += '<span aria-hidden="true">×</span>';
                html += '</button>';
                html += '<div class="radio-inline">';
                html += '<label><input type="radio" name="vittednostream' + i + '" value="single"';
                if(vittednoType === "single"){
                    html += ' checked';
                }
                html += '>任意一人审核</label>';
                html += '</div>';
                html += '<div class="radio-inline">';
                html += '<label><input type="radio" name="vittednostream' + i + '" value="all"';
                if(vittednoType === "all"){
                    html += ' checked';
                }
                html += '>均需审核</label>';
                html += '</div>';
                html += '</div>';
                html += '<div class="panel-body">';
                html += '<div class="ui selection multiple dropdown mini stream' + i + 'uidropdown" style="width:100%;height:34px;overflow:hidden;">';
                html += '<input type="hidden" value="'+ flowContent[i].person +'">';
                html += '<i class="dropdown icon"></i>';

                for (j = 0; j < codeArr.length; j++) {
                    html += '<a class="ui label transition" data-value="'+ codeArr[j] +'" style="display: inline-block ! important;">'+ nameArr[j] +'<i class="delete icon"></i></a>';
                }

                html += '<div class="default text">请选择审批人</div>';
                html += '<div class="menu">';
                html += '<div class="divider" style="margin:0;"></div>';
                html += '<ul class="ztree select-tree hid-top person" id="stream' + i + 'tree"></ul>';
                html += '<div class="scrolling menu" style="max-height:none;"></div>';
                html += '</div></div></div>';
                $(selector.processStream).append(html);
                streamList.push("stream"+i);
                buildDropdown({
                    dropdown: '.stream' + i + 'uidropdown',
                    tree: 'stream' + i + 'tree'
                });
                initTree(rootNode, {
                    tree: 'stream' + i + 'tree'
                });

            }
            $(document).on('click', '.editClose', function(event) {
                var stream = $(this).attr("stream");
                streamList.removevalue(stream);
                $(this).parent().parent().remove();
            });
        }
    //}

    $(document).on('submit', '.J_add_form', function(event) {
        event.preventDefault();
        var self = this,
            currentUrl = $(self).prop('action'),
            checkDelegate = new VaildNormal(),
            fType = $(selector.processOption).val(),
            stitchObj = {};
        /**
         * comment out end
         */
        function formatData() {
            stitchObj = {
                //procode: $(selector.hideProCode).val(),
                //modcode: $(selector.hideModCode).val(),
                //flowcode: $(selector.hideFlowCode).val(),
                //flag: $(selector.hideFlag).val(),
                //cname: $(selector.typeName).val(),
                //deptCode: $(selector.org).val(),
                code: $(selector.typeCode).val(),
                flowType: $(selector.processOption).val(),
                stream: JSON.stringify(buildData())
            };
            return stitchObj;
        }
        if (typeof formatData() !== 'object') {
            return;
        }

        //审核流程校验
        if (fType === '1') {
            var streams = buildData(),
                isempty = false;
            if(streams.length){
                var $persons = $(".J-process-stream input[type=hidden]");
                $persons.each(function(){
                    if( !$(this).val() ){
                        isempty = true;
                    }
                });
                if(isempty){
                    common.commonTips('请选择审批人');
                    return;
                }
            }else{
                return;
            }
        } else {
            $(".ui.label.circular").eq(1).hide();
        }
        //console.log($.param(stitchObj));
        //return;

        $.ajax({
            url: currentUrl,
            data: $.param(stitchObj),
            type: 'post',
            dataType: 'json',
            timeout: 10000,
            success: function(data) {
                 if (data.returncode == 200) {
                    common.commonTips('保存成功');
                    setTimeout(function() {
                    	history.go(-1);
                        //window.location.href = common.rootPath + "sys.htm?m=find&proCode=" + data.returndata;
                    }, 1000);
                 } else {
                    common.commonTips('保存失败');
                 }
            },
            error: function() {
                common.commonTips("添加审核流程出错！");
            }
        });
    });
});
