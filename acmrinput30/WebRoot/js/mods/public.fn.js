define(function(require, exports, module) {
    "use strict";
    var $ = require('jquery'),
    modId = $('#modId').val(),
    ui = require('alert'),
    common = require('common'),
    ss = require('excel');
    require('tree');
    require('semantic');

    //识别区参数设置-树demo
    var pub = {};
    pub.initArgTree = function(wd) {
        //tree
        var setting = {
            async: {
                enable: true,
                url: common.rootPath + '/template.htm?m=getResultLeftTree&wd=' + wd,
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
                onClick: pub.clickEvent
            }
        };

        $.ajax({
            url: common.rootPath + "/template.htm?m=getResultLeftTree",
            type: "get",
            data: {
                "wd": wd,
                "proCode": ""
            },
            async: false,
            dataType: "json",
            success: function(data) {
                if ($("#weiduTree").length) {
                    $.fn.zTree.init($("#weiduTree"), setting, data);
                    $("#weiduTree").data("itree", data);
                }
                if ($("#weiduTree2").length) {
                    $.fn.zTree.init($("#weiduTree2"), setting, data);
                }
                if ($("#attrWdTree").length) {
                    $.fn.zTree.init($("#attrWdTree"), setting, data);
                }
                if ($("#weiduTreeFixed").length) {
                    $.fn.zTree.init($("#weiduTreeFixed"), setting, data);
                    $("#weiduTreeFixed").data("itree", data);
                }
            }
        });
    };
    pub.clickEvent = function(event, treeid, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeid);
        if (treeNode.ifData === "0") {
            zTree.cancelSelectedNode();
            ui.alert("分类不能选！");
            return false;
        }
        if (treeid === "weiduTreeFixed") {
            $("[name='attrFixedVal']").val(treeNode.name).attr('code', treeNode.id);
        }
    };
    pub.addAreaItem = function(table, treeNode, isPro) {
        if (!treeNode) {
            ui.alert("没有选中节点！");
            return;
        }
        var isAdd = true;
        var html = '<tr><td width="110"><span class="code">' + treeNode.id + '</span>';
        var curCode = treeNode.id;
        var disabled = "";
        if (isPro && treeNode.isParent) {
            disabled = "disabled";
            curCode += "+";
            html += '<a class="ui red right corner label">+</a>';
        }
        html += '</td><td width="140"><span class="name">' + treeNode.name + '</span></td><td><input class="diyname"' + disabled + ' type="text"><i class="remove circle icon"></i></td></tr>';
        $("tr", table).each(function() {
            var codeEl = $("td", this).first().find(".code");
            var nextEl = codeEl.next();
            var code = codeEl.html();
            if (nextEl.length) {
                code = codeEl.html() + "+";
            }
            if (curCode === code) {
                isAdd = false;
            }
        });

        if (isAdd) {
            $("tbody", table).append(html);
        } else {
            ui.alert("重复添加！");
            return;
        }
    };
    /*初始化下拉列表、单选框、复选框、tab选项卡*/ //动态生成的dom无效，再调用一次initDTC()=========
    pub.initDTC = function(root) {
        var r = root || "";
        $(r + ' .dropdown.public').dropdown({ selector: { item: '.item' } });
        $(r + ' .menu .item').tab();
        $(r + ' .checkbox').checkbox();
    };
    pub.selRange = function(el, handler) {
        var parent = el.parent(),
            excelContent = $(".main-content"),
            fz = el.css("font-size");

        el.toggle(function() {
            var currentPosiTop = el.offset().top,
                currentPosiLeft = el.offset().left;
            el.addClass("blue");
            ss.setDataSourceRegion();
            ss.addEventListener("regionChange", handler);
            //
            $("body").append('<div class="ui active dimmer iDimmer"></div>');
            el.css({
                "position": "fixed",
                "z-index": 1002,
                "top": currentPosiTop,
                "left": currentPosiLeft,
                "border": "1px solid #2185d0",
                "border-bottom-left-radius": 0,
                "border-top-left-radius": 0,
                "margin": 0,
                "font-size": fz
            });
            $("body").append(el);
            excelContent.addClass("zindex");
        }, function() {
            el.removeClass("blue");
            ss.setSelectRegion();
            ss.destroyDataSoureRegion();
            ss.removeEventListener("regionChange", handler);
            //
            $(".iDimmer.ui.dimmer").remove();
            parent.append(el.removeAttr("style"));
            excelContent.removeClass("zindex");
        });
    };
    pub.scroll = function(s, e) {
        $(".scroll-container").scroll(function() {
            var maxRow = $("#rootPath").attr("maxRow");
            var maxRow1 = $(".row-head-panel").last().find(".row-head-item").last().text();
            if (parseInt(maxRow1) > parseInt(maxRow)) {
                ss.setFillColor("1", "rgb(184, 204, 228)", [s + maxRow, e + maxRow1]);
                $("#rootPath").attr("maxRow", maxRow1);
            }
        });
    };
    pub.refreshFilter = function() {
        $.ajax({
            url: common.rootPath + '/distinguish.htm?m=getFilterName&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
            type: "get",
            dataType: "json",
            success: function(d) {
                if (d.returncode === 200) {
                    $("#filterList").empty();
                    for (var i = 0; i < d.returndata.length; i++) {
                        $("#filterList").append('<a class="ui label empty">' + d.returndata[i] + '<i class="chevron down icon"></i></a>');
                    }
                }
            }
        });
    };

    $(document).on("click", ".iSearchList .item", function() {
        var wd = $(this).attr("wd"),
            code = $(this).attr("code"),
            box = $(this).parent(),
            ztreeId = box.prev().find(".ztree").attr("id");

        $.ajax({
            url: common.rootPath + '/template.htm?m=positionTree&wd=' + wd + '&keycode=' + code,
            type: 'get',
            dataType: "json",
            beforeSend: function() {
                var t = box.scrollTop();
                box.append('<div class="ui active dimmer searchDimmer" style="top:' + t + 'px !important;"><div class="ui loader"></div></div>');
            },
            success: function(data) {
                if (data.success === "true") {
                    box.addClass("hide");
                    //resetTree
                    var setting = {
                        async: {
                            enable: true,
                            url: common.rootPath + '/template.htm?m=getResultLeftTree&wd=' + wd,
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
                            onClick: pub.clickEvent
                        }
                    };
                    $.fn.zTree.init($("#" + ztreeId), setting, data.results);
                    var zTree = $.fn.zTree.getZTreeObj(ztreeId);
                    var node = zTree.getNodeByParam("id", code);
                    zTree.selectNode(node);
                }
            }
        });
    });

    //维度查找
    $(document).on("click", ".iSearchBtn", function() {
        var text = $(this).prev().val(),
            wd = $("[name='wdType']").val(),
            $list = $(this).parent().next().find(".iSearchList");
        if ($.trim(text) !== "") {
            $.ajax({
                url: common.rootPath + '/template.htm?m=find&wd=' + wd + '&text=' + text,
                type: 'get',
                dataType: "json",
                success: function(data) {
                    var i, l = data.results.length;
                    if (data.success === "true" && l) {
                        $list.empty().removeClass("hide");
                        for (i = 0; i < l; i++) {
                            $list.append('<div class="item" code="' + data.results[i].value + '" title="' + data.results[i].value + '" wd="' + wd + '">' + data.results[i].name + '</div>');
                        }
                    } else {
                        $list.empty().addClass("hide");
                    }
                }
            });
        }
    });

    module.exports = pub;
});
