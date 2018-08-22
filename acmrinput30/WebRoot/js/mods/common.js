define(function(require, exports, module) {
    'use strict';
    var $ = require('jquery'),
        tab = require('tab');

    //top搜索
    window.topsearch = function() {
        var str = $("#keyword").val();
        if ($.trim(str) != "" && str != "如：全球 电话用户数 2016") {
            window.location = $('#cfgjs').attr('pub') + "/search.htm?s=" + encodeURIComponent(str);
        }
    }
    $("#keyword").focus(function() {
        $(this).val() == "如：全球 电话用户数 2016" && $(this).val("");
    }).blur(function() {
        $.trim($("#keyword").val()) == "" && $(this).val("如：全球 电话用户数 2016");
    }).keydown(function(e) {
        e.keyCode == 13 && window.topsearch();
    });
    //top搜索end

    return {
        rootPath: $('#cfgjs').attr('base') + '/',
        dragTemplate: 'templatedrag.htm',
        formatData: function(key, val) {
            if (key == '' || val == '' || key == 'undefined') {
                return '';
            }
            return key + '=' + encodeURI(val);
        },
        commonTips: function(msg) {
            $('.common-tips').html('<span class="common-content">' + msg + '</span>');
            $('.common-tips').show();
            setTimeout(function() {
                $('.common-tips').hide();
            }, 2000)
        },
        buildModelHTML: function(targetName, targetContent) {
            var len = $('#' + targetName).length,
                tempHTML;
            if (len === 0) {
                tempHTML = '<div class="modal fade" id="' + targetName + '" role="dialog"><div class="modal-dialog"><div class="modal-content">' + targetContent + '</div></div></div>';
                $('body').append(tempHTML);
            }
        },
        oprArray: function(oprType, oprId, currentArray) {
            var i,
                len = currentArray.length;
            if (oprType == 'add') {
                for (i = 0; i < len; i++) {
                    if (currentArray[i] == oprId) {
                        return currentArray;
                    }
                }
                currentArray.push(oprId);
            }
            if (oprType == 'del') {
                for (i = 0; i < len; i++) {
                    if (currentArray[i] == oprId) {
                        currentArray.splice(i, 1);
                        break;
                    }
                }
            }
            return currentArray;
        },
        checkAll: function(targetObj, checkState, currentArray) {
            //涓嶇閫変腑杩樻槸鍙栨秷鍏堝皢鏁扮粍娓呯┖
            currentArray = [];
            if (checkState) {
                $(targetObj).prop('checked', true);
                var i = 0,
                    len = $(targetObj).length;
                for (; i < len; i++) {
                    currentArray.push($(targetObj).eq(i).attr('value'));
                }
            } else {
                $(targetObj).prop('checked', false);
            }
            return currentArray;
        },
        pageUrlSplit: function(requestUrl) {
            var wz = requestUrl.indexOf("?"),
                url = requestUrl.substring(0, wz),
                params = requestUrl.substring(wz + 1, requestUrl.length),
                returnobj = {
                    url: url,
                    params: params
                }
            return returnobj;
        },
        cancalCheck: function(targetObj, currentArray) {
            if (currentArray != undefined) {
                var len = currentArray.length;
                if (len > 0) {
                    currentArray.length = 0;
                }
            }
            if ($(targetObj).prop('checked')) {
                $(targetObj).prop('checked', false);
            }
        },
        substitute: function(template, map) {
            return template.replace(/\\?\{([^{}]+)\}/g, function(match, name) {
                return (map[name] === undefined) ? '' : map[name];
            });
        },
        clearNullData: function(currentArray, partIndex) {
            var i, len, tempArray;

            function clone(source) {
                var BUILTIN_OBJECT = {
                    '[object Function]': 1,
                    '[object RegExp]': 1,
                    '[object Date]': 1,
                    '[object Error]': 1,
                    '[object CanvasGradient]': 1
                };

                if (typeof source == 'object' && source !== null) {
                    var result = source;
                    if (source instanceof Array) {
                        result = [];
                        for (var i = 0, len = source.length; i < len; i++) {
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
            tempArray = clone(currentArray);
            len = tempArray.length;
            if (partIndex !== undefined) {
                var itemArray, itemLen, j;
                for (i = len - 1; i >= 0; i--) {
                    itemArray = tempArray[i]['part_regin'];
                    itemLen = itemArray.length;
                    for (j = itemLen; j >= 0; j--) {
                        if (itemArray[j] === null) {
                            itemArray.splice(j, 1);
                        }
                    }
                    if (itemArray.length === 0) {
                        tempArray.splice(i, 1);
                    }
                }
            } else {
                for (i = len - 1; i >= 0; i--) {
                    if (tempArray[i] === null) {
                        tempArray.splice(i, 1);
                    }
                }
            }
            return tempArray;
        },
        buildTimeStamp: function() {
            return (new Date * 1000);
        }
    }
});
