(function() {
    'use strict';
    var ident = 0,
        version,
        widgetPageCollection,
        widgetsTypeCollection,
        isAddStatus,
        currentModel,
        currentType,
        currentExpress,
        selector = {},
        sysSelector = {},
        modal = {},
        template = {},
        $dragItems,
        $sortableItems,
        $editButton,
        $sortableContainer,
        rootPath = '/acmrinput30/',
        cacheGlobalFilter = [],
        globalCode = $('input[name="globalCode"]').val().trim(),
        globalBookCode = $('input[name="globalBookCode"]').val().trim(),
        //represent meaning: 0 edit, 1 page preview,2 apply preview
        applyMode = $('input[name="apply"]').val().trim();
    ident = 0;
    version = '1.0';
    isAddStatus = false;
    widgetPageCollection = new widgets.page.collection();
    widgetsTypeCollection = new widgets.type.collection();

    function setSysConfig() {
        sysSelector = {
            modalContainer: '#modal',
            sortableContainer: '.sortable'
        };
        $sortableContainer = $(sysSelector.sortableContainer);
    }

    function setConfig() {
        selector = {
            dragItems: '.sidebar-nav .item',
            editButton: '.demo .edit.item',
            removeButton: '.demo .del.item',
            sortableItems: '.demo'
        };
        modal = {
            title: $('.modal-title').html(),
            paragraph: $('.modal-paragraph').html(),
            layout: $('#modal-layout').html(),
            image: $('#modal-image').html(),
            table: $('#modal-table').html(),
            chart: $('#modal-chart').html()
        };
        template = {
            title: $('.template-title').html(),
            paragraph: $('.template-paragraph').html(),
            layout: $('#template-layout').html(),
            image: $('#template-image').html(),
            table: $('#template-table').html(),
            chart: $('#template-chart').html()
        };
        $sortableItems = $(selector.sortableItems);
        $dragItems = $(selector.dragItems);
        $editButton = $(selector.editButton);
    }

    function bindEffect() {
        function bindDragable() {
            $dragItems.draggable({
                connectToSortable: selector.sortableItems,
                helper: 'clone',
                drag: function(e, ui) {
                    ui.helper.width(400);
                },
                stop: function(e, ui) {
                    ui.helper.attr('id', ident);
                }
            });
        }

        function bindSortable() {
            //reset selector of sortableItems
            $sortableItems = $(selector.sortableItems);
            $sortableItems.sortable({
                revert: true,
                handle: '.render',
                receive: function(e, ui) {
                    currentType = ui.item.attr('type');
                    currentExpress = ui.item.attr('express');
                    if (currentType === 'report') {
                        currentModel = widgetsTypeCollection.getModelByType(currentExpress);
                    } else {
                        currentModel = widgetsTypeCollection.getModelByType(currentType);
                    }
                    currentModel.model.id = ++ident;
                    widgetPageCollection.addItem(clone(currentModel.model));
                    isAddStatus = true;
                },
                update: function(e, ui) {
                    if (isAddStatus) {
                        var currentTemplate;
                        if (currentType === 'report') {
                            currentTemplate = template[currentExpress];
                        } else {
                            currentTemplate = template[currentType];
                        }
                        ui.item.html(Handlebars.compile(currentTemplate)(currentModel.model));
                        isAddStatus = false;
                    }
                    if (currentType === 'layout') {
                        bindSortable();
                    }
                    if (currentType === 'image') {
                        bindUpload();
                    }
                }
            });
        }
        bindDragable();
        bindSortable();
    }

    function setCurrentType($self) {
        currentType = $self.parents('div.item:eq(0)').attr('type');
    }

    function setCurrentExpress($self) {
        currentExpress = $self.parents('div.item:eq(0)').attr('express');
    }
    /**
     * 数据和下拉列表组合控件
     * @param  {string} id       [下拉菜单的容器]
     * @param  {string} treeId   [树形菜单的容器]
     * @param  {string} wd       [url中一个参数，维度信息]
     * @param  {object} baseNode [树形菜单的第一个节点，为了不初始化就请求数据，等待用户触发]
     * @param  {string} transUrl [自定义的url，保证非维度树的请求也可以用]
     */
    function initTree(id, treeId, wd, baseNode, transUrl) {
        var setting,
            currentwd = wd || '',
            currentUrl = transUrl || rootPath + 'bookoffice/BookReport.htm?m=findReportTree&bookcode=' + globalBookCode;
        setting = {
            async: {
                enable: true,
                url: currentUrl,
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
            /*if (treeNode.isTable === "0") {
                $(event.target).parent().addClass("disabled");
                return false;
            }*/
            var currentNodeId, node;
            currentNodeId = treeNode.tId + '_a';
            node = $('#' + currentNodeId);
            node.data('value', treeNode.id);
        }
        $.fn.zTree.init($("#" + treeId), setting, baseNode);
        bindDropdown(id, treeId);
    }

    function bindElement() {

        // trigger edit function
        $(sysSelector.modalContainer).on('show.bs.modal', function(e) {
            var $sourceBtn = $(e.relatedTarget),
                id = $sourceBtn.data('id'),
                currentObj = widgetPageCollection.getPropById(id),
                $this = $(this),
                currentTemplate;
            setCurrentType($sourceBtn);
            if (currentType === 'report') {
                setCurrentExpress($sourceBtn);
                currentTemplate = modal[currentExpress];
            } else {
                currentTemplate = modal[currentType];
            }
            $this.html(Handlebars.compile(currentTemplate)(currentObj));
            if (currentObj.type === 'report') {
                $.ajax({
                    url: rootPath + 'bookoffice/BookReport.htm?m=findReportTree&bookcode=' + globalBookCode,
                    type: 'get',
                    success: function(data) {
                        if (data.length > 0) {
                            initTree('reportDataList', 'reportDataTreeId', '', data);
                        }
                    }
                });
            }
        });
        // trigger delete function
        $(document).on('click', selector.removeButton, function() {
            var $this = $(this),
                id,
                currentWidget;
            currentWidget = $this.parents('div.item:eq(0)');
            setCurrentType($this);
            if (currentType === 'layout') {
                var itemWidget = $('.del.item', currentWidget),
                    len = itemWidget.length,
                    i = 0;
                for (; i < len; i++) {
                    id = itemWidget.eq(i).data('id');
                    widgetPageCollection.removeItemById(id);
                }
            } else {
                id = $this.data('id');
                widgetPageCollection.removeItemById(id);
            }
            currentWidget.remove();
            if (currentType === 'report') {
                //判断确认是否会影响到公共filter部分
                hasGlobalFilter();
            }
        });
        // trigger update function
        $(document).on('submit', '#titleForm', function(e) {
            e.preventDefault();
            var $this = $(this),
                id = $('input[name="id"]', this).val(),
                JSONObj = $this.serializeObject();
            widgetPageCollection.updateItemById(id, JSONObj);
            $('.render.' + id, selector.sortableItems).parents('.item:eq(0)').html(Handlebars.compile(template[currentType])(JSONObj));
            $(sysSelector.modalContainer).modal('hide');
            if (currentType === 'image') {
                bindUpload();
            }
        });
        $(document).on('submit', '#tableFrom,#chartFrom', function(e) {
            e.preventDefault();
            var $this = $(this),
                id = $('input[name="id"]', this).val(),
                JSONObj = $this.serializeObject();
            if (JSONObj.reportDataValue === -1) {
                console.log('请选择数据表');
                return;
            }
            widgetPageCollection.updatePropById(id, 'limith', JSONObj.limith);
            widgetPageCollection.updatePropById(id, 'transName', $('.text', this).text());
            $('.render.' + id, selector.sortableItems).parents('.item:eq(0)').html(Handlebars.compile(template[currentExpress])(JSONObj));
            handleExpress(JSONObj, id, $this);
        });

        function handleExpress(JSONObj, id, $this) {
            //mock reverse
            // reportCode = 0;
            //end mock
            function hasFilter() {
                var qd = jQuery.Deferred();
                $.ajax({
                    //JSONObj.reportDataValue instance of mockcodeid
                    url: rootPath + 'bookoffice/BookReportPage.htm?m=getFilters&code=' + JSONObj.reportDataValue,
                    type: 'get',
                    success: function(data) {
                        var rData = data.returndata,
                            len = rData.length;
                        if (len) {
                            recordFilter(JSONObj);
                            qd.notify();
                            qd.resolve();
                        } else {
                            qd.reject();
                        }
                    },
                    error: function() {

                    }
                });
                return qd.promise();
            }
            /**
             * 记录filter的transid到自己的对象
             * @param  {[type]} transData [description]
             * @return {[type]}           [description]
             */
            function recordFilter(transData) {
                //JSONObj.transId instance of mockcodeid
                widgetPageCollection.updatePropById(id, 'transId', JSONObj.reportDataValue);
            }

            /**
             * 渲染表格
             * @param  {[type]} transData [description]
             * @return {[type]}           [description]
             */
            function renderReportTable(transData, express, hasfilterInfo) {
                var currentUrl = '';
                if (express === 'chart') {
                    currentUrl = rootPath + 'bookoffice/BookReportChart.htm?m=getReportChart&code=' + transData.code+'&bookcode=' + globalBookCode;
                }
                if (express === 'table') {
                    currentUrl = rootPath + 'bookoffice/BookDataShowHandel.htm?m=catalogPage&bookcode=' + globalBookCode;
                }
                $.ajax({
                    url: currentUrl,
                    type: 'post',
                    data: transData,
                    success: function(data) {
                        if (data.returncode === 200) {
                            if (!hasfilterInfo) {
                                widgetPageCollection.updatePropById(id, 'transId', JSONObj.reportDataValue);
                                widgetPageCollection.updatePropById(id, 'limith', JSONObj.limith);
                            }
                            if (express === 'chart') {
                                var mychart, option, currentDiv;
                                widgetPageCollection.updatePropById(id, 'formatJson', data.returndata.property);
                                currentDiv = $('.render.' + id, selector.sortableItems).children('.content').children('div')[0];
                                mychart = echarts.init(currentDiv);
                                option = JSON.parse(data.returndata.property);
                                mychart.setOption(option, true);
                                bindChart(data.returndata.property, currentDiv, true, transData);
                            }
                            if (express === 'table') {
                                widgetPageCollection.updatePropById(id, 'html', data.returndata);
                                $('.render.' + id, selector.sortableItems).children('.content').html(data.returndata);
                            }
                            $(sysSelector.modalContainer).modal('hide');
                        }
                    }
                });
            }
            $.when(hasFilter()).then(
                //resolve
                function() {
                    hasGlobalFilter();
                    var data = {
                            'code': JSONObj.reportDataValue
                        },
                        model = widgetPageCollection.getPropById(id);

                    renderReportTable(data, model.express, true);
                },
                //reject
                function() {
                    var data = {
                            'code': JSONObj.reportDataValue
                        },
                        model = widgetPageCollection.getPropById(id);

                    renderReportTable(data, model.express, false);
                },
                //notify
                function() {
                    $(sysSelector.modalContainer).modal('hide');
                }
            );
        }
        /**
         * 是否有全局filter信息
         * @return {Boolean} [description]
         */
        function hasGlobalFilter() {
            var propStr = getPropList();
            if (propStr === '') {
                cacheGlobalFilter = [];
                $('#filter-box').html('');
                return;
            }
            $.ajax({
                url: rootPath + 'bookoffice/BookReportPage.htm?m=getGlobalFilter&code=' + propStr,
                type: 'get',
                success: function(data) {
                    var rCode = data.returncode,
                        rData = data.returndata,
                        rDataLen = rData.length,
                        list = [],
                        rangeList = [],
                        i, j, k, m = 0,
                        codeArr,
                        nameArr,
                        rootNode;
                    if (rCode === 200) {
                        renderFilterItem(rData, $('#filter-box'), 0, true, '');
                        //触发每一个对象刷新
                        $('.refresh').trigger('click');
                    }
                }
            });
        }

        function getPropList() {
            var itemList = widgetPageCollection.getItemsByType('report'),
                listLen = itemList.length,
                n = 0,
                propList = [];
            for (; n < listLen; n++) {
                propList.push(itemList[n].transId);
            }
            return propList.toString();
        }
        $(document).on('click', '.searchItem,.forceSearch', function(e) {
            var i = 0,
                len = cacheGlobalFilter.length,
                globalFilterStr = '',
                transFilterStr = '',
                globalRangeStr = '',
                transRangeStr = '';
            for (; i < len; i++) {
                if (cacheGlobalFilter[i].defaultName.indexOf(',') === -1) {
                    //是单个选择
                    globalFilterStr += cacheGlobalFilter[i].wdtype + ':' + cacheGlobalFilter[i].defaultCode + ';';
                } else {
                    var rangeObjectName = cacheGlobalFilter[i].defaultName.split(','),
                        rangeObjectCode = cacheGlobalFilter[i].defaultCode.split(','),
                        rangeLen = rangeObjectName.length,
                        j = 0,
                        tempArray = [],
                        tmpString = '';
                    for (; j < rangeLen; j++) {
                        tmpString = rangeObjectCode[j] + '_' + rangeObjectName[j];
                        tempArray.push(tmpString);
                    }
                    globalRangeStr += cacheGlobalFilter[i].wdtype + ':' + tempArray.toString();
                }
            }
            transFilterStr = globalFilterStr + combinSingleData($(this).parents('.item'));
            transRangeStr = globalRangeStr + combinMutipleData($(this).parents('.item'));

            var renderId = $(this).data('renderid'),
                model = widgetPageCollection.getPropById(renderId),
                transData = {
                    code: model.transId,
                    filterstr: transFilterStr,
                    rangestr: transRangeStr
                };
            renderReportItem(transData, model.express)

            function renderReportItem(transData, express) {
                var currentUrl = '';
                if (express === 'chart') {
                    currentUrl = rootPath + 'bookoffice/BookReportChart.htm?m=getReportChart&code=' + transData.code+'&bookcode=' + globalBookCode;
                }
                if (express === 'table') {
                    currentUrl = rootPath + 'bookoffice/BookDataShowHandel.htm?m=catalogPage&bookcode=' + globalBookCode;
                }
                $.ajax({
                    url: currentUrl,
                    type: 'post',
                    data: transData,
                    success: function(data) {
                        if (data.returncode === 200) {
                            if (express === 'chart') {
                                var currentDiv;
                                widgetPageCollection.updatePropById(renderId, 'formatJson', data.returndata.property);
                                currentDiv = $('.render.' + renderId, selector.sortableItems).children('.content').children('div')[0];
                                bindChart(data.returndata.property, currentDiv, true, transData);
                            }
                            if (express === 'table') {
                                widgetPageCollection.updatePropById(renderId, 'html', data.returndata);
                                $('.render.' + renderId, selector.sortableItems).children('.content').html(data.returndata);
                            }

                        }
                    }
                });
            }
        });
        $(document).on('click', '.refresh', function(e) {
            var renderId = $(this).data('id'),
                $self = $(this),
                model = widgetPageCollection.getPropById(renderId),
                currentContainer = $(this).parents('.item');
            $.ajax({
                url: rootPath + 'bookoffice/BookReportPage.htm?m=getFilters&code=' + model.transId,
                type: 'get',
                success: function(data) {
                    var rData = data.returndata,
                        rCode = data.returncode,
                        len = rData.length,
                        cacheLen = cacheGlobalFilter.length;
                    if (rCode !== 200) {
                        return;
                    }
                    //去重
                    if (cacheLen > 0 && len > 0) {
                        for (var i = 0; i < len; i++) {
                            for (var j = 0; j < cacheLen; j++) {
                                if (rData[i].wdtype === cacheGlobalFilter[j].wdtype) {
                                    rData.splice(i, 1);
                                }
                            }

                        }
                    }
                    //渲染新的filter
                    renderFilterItem(rData, $('.filter', $self.parents('.item')), renderId, false, model.transId);

                },
                error: function() {

                }
            });
            /**
             * 渲染表格
             * @param  {[type]} transData [description]
             * @return {[type]}           [description]
             */
        });

        function combinMutipleData(currentContainer) {
            var tempStr = '',
                rangestr = '',
                rangeCodeArr, rangeNameArr,
                i;
            $('.rangeSel', currentContainer).each(function() {
                rangeCodeArr = $(this).val().split(",");
                rangeNameArr = $(this).siblings('a');
                for (i = 0; i < rangeCodeArr.length; i++) {
                    if (i !== 0) {
                        tempStr += ',';
                    }
                    tempStr += rangeCodeArr[i] + '_' + $.trim(rangeNameArr.eq(i).text());
                }
                rangestr += $(this).data('wdtype') + ':' + tempStr + ';';
            });
            return rangestr;
        }

        function combinSingleData(currentContainer) {
            var filterstr = '';
            $('.filterSel', currentContainer).each(function() {
                filterstr += $(this).data('wdtype') + ':';
                filterstr += $(this).val() + ';';
            });
            return filterstr;
        }

        function buildMutipleObject(currentContainer) {
            var tmpobj = {},
                rangeNameArr, tmparray = [],
                i;
            $('.rangeSel', currentContainer).each(function() {
                rangeNameArr = $(this).siblings('a');
                for (i = 0; i < rangeNameArr.length; i++) {
                    tmparray.push($.trim(rangeNameArr.eq(i).text()));
                }
                tmpobj = {
                    defaultName: tmparray.slice().toString(),
                    defaultCode: $(this).val(),
                    wdtype: $(this).data('wdtype')
                }
                cacheGlobalFilter.push(clone(tmpobj));
            });
        }

        function buildSingleObject(currentContainer) {
            var tmpobj = {};
            $('.filterSel', currentContainer).each(function() {
                tmpobj = {
                    defaultName: $(this).siblings('.text').text(),
                    defaultCode: $(this).val(),
                    wdtype: $(this).data('wdtype')
                }
                cacheGlobalFilter.push(clone(tmpobj));
            });
        }

        function cacheFilter(transList, auxInfo) {
            var tmp = {
                buildId: auxInfo.buildId,
                treeId: auxInfo.treeId,
                rootNode: auxInfo.rootNode,
                wdtype: transList.wdtype,
                defaultName: transList.defaultName,
                defaultCode: transList.defaultCode
            };
            cacheGlobalFilter.push(tmp);
        }

        function renderFilterItem(transReturnData, $container, renderId, isglobal, transId) {
            var rData = transReturnData,
                rDataLen = rData.length,
                list = [],
                rangeList = [],
                tmpArray = [],
                i, j, k, m = 0,
                codeArr,
                nameArr,
                rootNode;
            if (isglobal) {
                cacheGlobalFilter = [];
                $('#filter-box').html('');
            } else {
                widgetPageCollection.updatePropById(renderId, 'defaultInfo', []);
                $container.html('');
            }
            for (; m < rDataLen; m++) {
                if (rData[m]['ifmul']) {
                    rangeList.push(rData[m]);
                } else {
                    list.push(rData[m]);
                }
            }

            for (i = 0; i < list.length; i++) {
                var buildId = 'id' + i + renderId,
                    buildTreeId = 'treeId' + i + renderId,
                    selectGroupHtml, currentAuxInfo;
                rootNode = list[i].treelist;
                currentAuxInfo = {
                    buildId: buildId,
                    treeId: buildTreeId,
                    rootNode: rootNode.slice()
                };
                if (isglobal) {
                    cacheFilter(list[i], currentAuxInfo);
                } else {
                    cacheFilterInfo(list[i], currentAuxInfo);
                }
                selectGroupHtml = Handlebars.compile($('#select-group').html())({
                    id: buildId,
                    treeId: buildTreeId,
                    fname: list[i].filterName,
                    fclass: 'filterSel',
                    inincode: list[i].defaultCode,
                    ininname: list[i].defaultName,
                    wdtype: list[i].wdtype
                });
                $container.append('<div class="filter-group">' + selectGroupHtml + '</div>');
                initTree(buildId, buildTreeId, list[i].wdtype, rootNode);
                $('#' + buildId).find('input').val(list[i].defaultCode);
                $('#' + buildId).find('.default').html(list[i].defaultName);
            }
            for (j = i; j < rangeList.length + i; j++) {
                var buildId = 'id' + j + renderId,
                    buildTreeId = 'treeId' + j + renderId,
                    rselectGroupHtml, currentAuxInfo;
                rootNode = rangeList[j].treelist;
                codeArr = rangeList[j].defaultCode.split(",");
                nameArr = rangeList[j].defaultName.split(",");
                currentAuxInfo = {
                    buildId: buildId,
                    treeId: buildTreeId,
                    rootNode: rootNode.slice()
                };

                if (isglobal) {
                    cacheFilter(rangeList[i], currentAuxInfo);
                } else {
                    cacheFilterInfo(rangeList[i], currentAuxInfo);
                }
                rselectGroupHtml = Handlebars.compile($('#select-group').html())({
                    id: buildId,
                    treeId: buildTreeId,
                    fname: rangeList[j].filterName,
                    fclass: 'rangeSel',
                    ismult: 'multiple',
                    inincode: rangeList[j].defaultCode,
                    ininname: rangeList[j].defaultName,
                    wdtype: rangeList[i].wdtype
                });
                $container.append('<div class="filter-group">' + rselectGroupHtml + '</div>');
                initTree(buildId, buildTreeId, rangeList[j].wdtype, rootNode);
                $('#' + buildId).find('input').val(rangeList[j].defaultCode);

                for (k = 0; k < codeArr.length; k++) {
                    $('#' + buildId).find('.default').before('<a class="ui label transition" data-value="' + codeArr[k] + '" style="display: inline-block ! important;">' + nameArr[k] + '<i class="delete icon"></i></a>');
                }
            }
            if (!isglobal) {
                widgetPageCollection.updatePropById(renderId, 'defaultInfo', tmpArray);
            }
            if (list.length || rangeList.length) {
                if (isglobal) {
                    $container.append('<button class="btn btn-primary searchFilter" data-renderid="' + renderId + '">查询</button>').show();
                } else {
                    $container.append('<button class="btn btn-primary searchItem" data-renderid="' + renderId + '">查询</button>').show();
                }

            }

            function cacheFilterInfo(transObject, auxInfo) {
                tmpArray.push({
                    defaultName: transObject.defaultName,
                    defaultCode: transObject.defaultCode,
                    wdtype: transObject.wdtype,
                    treeId: auxInfo.treeId,
                    buildId: auxInfo.buildId,
                    rootNode: auxInfo.rootNode
                });
            }
        }

        $(document).on('click', '.searchFilter', function(e) {
            //清空全局cache信息
            cacheGlobalFilter = [];
            //重新生成cache信息
            buildSingleObject($('#filter-box'));
            buildMutipleObject($('#filter-box'));
            $('.forceSearch').trigger('click');

        });
        $(document).on('click', '.searchItemFilter', function(e) {});
        $(document).on('submit', '#layoutForm', function(e) {
            e.preventDefault();
            var $this = $(this),
                id = $('input[name="id"]', this).val();
            var JSONObj = $this.serializeObject();
            var scaleList = $('input[name="scale"]');
            var scaleLen = scaleList.length;
            var i = 0;

            function checkDivisable() {
                var totalNum = 0;
                for (; i < scaleLen; i++) {
                    totalNum += parseInt(scaleList.eq(i).val());
                }
                if (12 % totalNum) {
                    return false;
                } else {
                    JSONObj.enlarge = 12 / totalNum;
                    return true;
                }
            }
            if (!checkDivisable()) {
                return;
            }
            widgetPageCollection.updateItemById(id, JSONObj);
            $('.render.' + id, selector.sortableItems).parents('.item:eq(0)').html(Handlebars.compile(template[currentType])(JSONObj));
            bindEffect();
            $(sysSelector.modalContainer).modal('hide');
        });
        $(document).on('input prototypechange', '#layoutForm input[name="col"]', function() {
            var $this = $(this);
            var currentLen = parseInt($this.val());
            var scaleList = $('input[name="scale"]');
            var scaleLen = scaleList.length;
            var diff = currentLen - scaleLen;
            if (diff && currentLen < 12 && currentLen > 0) {
                var tmpContainer = '';
                while (currentLen--) {
                    tmpContainer += '<input type="text" class="form-control" name="scale" value="1">';
                }
                $('#scale-container').html(tmpContainer);
            }
        });
    }

    function bindToolbar() {
        function removeMenuClasses() {
            $("#navbar ul li").removeClass("active")
        }
        $('#preview').on('click', function(e) {
            $("body").removeClass("edit");
            $("body").addClass("devpreview sourcepreview");
            removeMenuClasses();
            $(this).addClass("active");
            return false
        });
        $('#reset').on('click', function(e) {
            var $self = $(this),
                $content = $('a', this);
            $content.text('开始清理...');
            cacheGlobalFilter = [];
            $('#filter-box').html('');
            widgetPageCollection.collection = [];
            $('#0.demo').html('');
            ident = 0;
            $content.text('清理完成');
            setTimeout(function() {
                $content.text('清理所有');
            }, 1000);
        });
        $("#edit").on('click', function() {
            $("body").removeClass("devpreview sourcepreview");
            $("body").addClass("edit");
            removeMenuClasses();
            $(this).addClass("active");
            return false
        });
        $('#save').on('click', function() {
            console.log(JSON.stringify(cacheGlobalFilter));
            console.log(JSON.stringify(widgetPageCollection.collection));
            // return;
            var self = this,
                transData = {
                    cacheGlobalFilter: cacheGlobalFilter,
                    globalHTML: $('#filter-box').html(),
                    pageCollection: widgetPageCollection.collection,
                    pageHTML: $('#0.demo').html(),
                    maxIdent: ident
                };
            $.ajax({
                url: rootPath + 'bookoffice/BookReportPage.htm?m=addBookPage&code=' + globalCode + '&bookcode=' + globalBookCode,
                type: 'post',
                data: {
                    content: JSON.stringify(transData)
                },
                beforeSend: function() {
                    $('a', self).text('保存中...');
                },
                success: function(data) {
                    if (data.returncode === 200) {
                        $('a', self).text('保存完成');
                    }
                    if (data.returncode === 500) {
                        $('a', self).text('保存失败,原因：' + data.returndata);
                    }
                    resetStatus();
                },
                error: function(data) {
                    $('a', self).text('连接失败');
                    resetStatus();
                }
            });

            function resetStatus() {
                setTimeout(function() {
                    $('a', self).text('保存');
                }, 2000);
            }
        });
    }

    function bindUpload() {
        var url = rootPath + 'bookoffice/BookReportPage.htm?m=imageUpload';
        $('.fileupload').fileupload({
                url: url,
                dataType: 'json',
                done: function(e, data) {
                    var $self = $(this),
                        id = $self.data('id'),
                        model, result;
                    setCurrentType($self);
                    result = data.result;
                    if (result.returncode == 200) {
                        model = widgetPageCollection.getPropById(id);
                        model.url = rootPath + 'bookoffice/BookReportPage.htm?m=showImage&code=' + result.returndata;
                        widgetPageCollection.updateItemById(id, model);
                        $('.render.' + id, selector.sortableItems).parents('.item:eq(0)').html(Handlebars.compile(template[currentType])(model));
                    }
                },
                progressall: function(e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#progress .progress-bar').css(
                        'width',
                        progress + '%'
                    );
                }
            }).prop('disabled', !$.support.fileInput)
            .parent().addClass($.support.fileInput ? undefined : 'disabled');
    }

    function bindChart(data, currentDiv, isRefresh, transData) {
        var mychart, chartOption;
        mychart = echarts.init(currentDiv);
        chartOption = JSON.parse(data);
        if (isRefresh) {
            $.ajax({
                url: rootPath + 'bookoffice/BookDataShowHandel.htm?m=getDrawData&bookcode=' + globalBookCode,
                type: 'post',
                data: transData,
                success: function(refreshData) {
                    overrideData(refreshData, chartOption, mychart);
                }
            });
        } else {
            mychart.setOption(chartOption, true);
        }
        //行列互转
        function dp(json) {
            var newjson = {},
                i, j;
            newjson.cols = json.rows;
            newjson.rows = json.cols;
            newjson.data = [];
            for (i = 0; i < json.cols.length; i++) {
                var item = [];
                for (j = 0; j < json.data.length; j++) {
                    item.push(json.data[j][i]);
                }
                newjson.data.push(item);
            }
            return newjson;
        }
        //重写数据
        function overrideData(data, option, transChart) {
            var i,
                tuType = option.tuType,
                isRow = option.axisType === '0' ? true : false,
                dataJson = data.returndata,
                newJson = isRow ? dataJson : dp(dataJson);

            if (tuType === 1) {
                option.legend.data = newJson.rows;
                option.xAxis.data = newJson.cols;
                option.series = [];
                for (i = 0; i < newJson.rows.length; i++) {
                    option.series.push({
                        "name": newJson.rows[i],
                        "type": "line",
                        "data": newJson.data[i]
                    });
                }
            } else if (tuType === 2) {
                option.legend.data = newJson.rows;
                option.xAxis.data = newJson.cols;
                option.series = [];
                for (i = 0; i < newJson.rows.length; i++) {
                    option.series.push({
                        "name": newJson.rows[i],
                        "type": "line",
                        "stack": '总量',
                        "areaStyle": {
                            normal: {}
                        },
                        "data": newJson.data[i]
                    });
                }
            } else if (tuType === 3) {
                option.legend.data = newJson.rows;
                option.xAxis.data = newJson.cols;
                option.series = [];
                for (i = 0; i < newJson.rows.length; i++) {
                    option.series.push({
                        "name": newJson.rows[i],
                        "type": "bar",
                        "data": newJson.data[i]
                    });
                }
            } else if (tuType === 4) {
                option.legend.data = newJson.rows;
                option.yAxis.data = newJson.cols;
                option.series = [];
                for (i = 0; i < newJson.rows.length; i++) {
                    option.series.push({
                        "name": newJson.rows[i],
                        "type": "bar",
                        "data": newJson.data[i]
                    });
                }
            } else if (tuType === 5 || tuType === 6) {
                option.legend.data = newJson.cols;
                option.series[0].name = newJson.rows[0];
                option.series[0].data = [];
                for (i = 0; i < newJson.cols.length; i++) {
                    option.series[0].data.push({
                        name: newJson.cols[i],
                        value: newJson.data[0][i]
                    });
                }
            }
            transChart.setOption(option, true);
        }
    }

    function bindDropdown(renderId, treeId) {
        $("#" + renderId + ".dropdown").uidropdown({
            preserveHTML: false,
            treeWidget: treeId,
            selector: {
                item: '.item > label, #' + treeId + ' li a:not(.disabled)'
            }
        });
    }
    var BUILTIN_OBJECT = {
        '[object Function]': 1,
        '[object RegExp]': 1,
        '[object Date]': 1,
        '[object Error]': 1,
        '[object CanvasGradient]': 1
    };

    function clone(source) {
        if (typeof source === 'object' && source !== null) {
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
    var reduction = {
        initialize: function(data) {
            reduction.revertCache(data);
            reduction.revertPage(data);
            reduction.revertBind(data);
        },
        revertCache: function(data) {
            ident = data.maxIdent;
            widgetPageCollection.collection = data.pageCollection;
            cacheGlobalFilter = data.cacheGlobalFilter;
        },
        revertPage: function(data) {
            $sortableContainer.html(data.pageHTML);
            $('#filter-box').html(data.globalHTML);
        },
        revertBind: function(data) {
            //revert bind dropdown
            // var $dropdown = $('.ui.dropdown'),
            //     len = $dropdown.length,
            //     i = 0;
            // for (; i < len; i++) {
            //     var buildId = $dropdown.attr('id'),
            //         treeId = 'treeId' + buildId.substring(buildId.indexOf('d') + 1, buildId.length);
            //         initTree(buildId, treeId,)
            //     // bindDropdown(buildId, treeId);
            // }
            var m, n,
                collectionList = data.pageCollection,
                collectionLen = collectionList.length,
                defaultList,
                defaultListLen,
                current;
            for (m = 0; m < collectionLen; m++) {
            	if(collectionList[m].type){
	                defaultList = collectionList[m].defaultInfo;
	                defaultListLen = defaultList.length;
	                for (n = 0; n < defaultListLen; n++) {
	                    current = defaultList[n];
	                    initTree(current.buildId, current.treeId, current.wdtype, current.rootNode);
	                }
            	}
            }

            // 重新绑定全局filter
            var i = 0,
                list = data.cacheGlobalFilter,
                len = list.length,
                current;
            for (; i < len; i++) {
                current = list[i];
                initTree(current.buildId, current.treeId, current.wdtype, current.rootNode);
            }
            //revert bind chart
            var chartList = widgetPageCollection.getItemsByExpress('chart'),
                chartLen = chartList.length,
                j = 0;
            for (; j < chartLen; j++) {
                var currentDiv = $('.content', '.render.' + chartList[j].id).children('div')[0];
                bindChart(chartList[j].formatJson, currentDiv, false);
            }
        }
    };

    function setApplayMode() {
        switch (applyMode) {
            case '2':
                $("body").removeClass("edit");
                $("body").addClass("devpreview sourcepreview applypreview");
                $('nav').addClass('hide');
                break;
        }
    }

    function initilaze() {
        setSysConfig();
        setApplayMode();
        $.ajax({
            url: rootPath + 'bookoffice/BookReportPage.htm?m=findById&code=' + globalCode,
            async: false,
            success: function(data) {
                if (data.returncode == 200) {
                    var formatContent = JSON.parse(data.returndata.content);
                    if (!formatContent) {
                        console.log('返回的数据有误，内容是:' + formatContent);
                        return;
                    }
                    reduction.initialize(formatContent);
                }
            }
        });
        setConfig();
        bindEffect();
        bindElement();
        bindToolbar();
        bindUpload();
        $('.forceSearch').trigger('click');
    }
    initilaze();



})();