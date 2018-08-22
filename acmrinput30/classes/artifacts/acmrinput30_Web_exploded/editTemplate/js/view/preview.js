define(function(require, exports, module) {
	var $ = require('$'),
		rootPath = require('rootPath'),
		Handlebars = require('handlebars'),
		ui = require('alert'),
		echarts = require('echarts'),
		code = $('#reportCode').val(),
		bookcode = $('#bookcode').val(),
		option = {},
		myChart,
		isRow,
		tuType,
		bodyWidth = $('body').width(),
		bodyHeight = $('body').height(),
		contentHeight,
		sType,
		sPosi,
		rootNode;

	require('ztree');
	require('uidropdown');

	//树
	function initTree(id, treeId, wd, baseNode){
		var setting = {
			async: {
				enable: true,
				url: rootPath + '/bookoffice/BookTemplateHandel.htm?m=getResultLeftTree&bookcode=' + bookcode + '&wd=' + wd,
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
			var currentNodeId, node;
			currentNodeId = treeNode.tId + '_a';
			node = $('#' + currentNodeId);
			node.data('value', treeNode.id);
		}
		$.fn.zTree.init($("#" + treeId), setting, baseNode);

		$("#" + id + ".dropdown").uidropdown({
			preserveHTML: false,
			treeWidget: treeId,
			selector: {
				item: '.item > label, #' + treeId + ' li a:not(.disabled)'
			}
		});
	}

	function showType(type, posi, boxWidth, boxHeight){
		switch (type) {
			case '1':
				$('.content-t').hide();
				$('.resize').hide();
				$('.content-b').height(boxHeight);
				$('.content-b').width(boxWidth);
				break;
			case '2':
				$('.content-b').hide();
				$('.resize').hide();
				$('.content-t').height(boxHeight);
				$('.content-t').width(boxWidth);
				break;
			case '3':
				break;
			default:
				$('.content-t').hide();
				$('.resize').hide();
				$('.content-b').height(boxHeight);
				$('.content-b').width(boxWidth);
				break;
		}
	}

	function showPosi(posi, boxWidth, boxHeight){
		switch (posi) {
			case '1':
				$('.content-t, .content-b').css({'height': parseInt(boxHeight/2), 'width': '100%'});
				$('.content-t').css({'top': 0});
				$('.content-b').css({'bottom': 0});
				$('.resize').removeClass('col').addClass('irow');
				$('.screen').css({'right': 0, 'top': -20}).attr('class', 'screen rb');
				break;
			case '2':
				$('.content-t, .content-b').css({'height': parseInt(boxHeight/2), 'width': '100%'});
				$('.content-t').css({'bottom': 0});
				$('.content-b').css({'top': 0});
				$('.resize').removeClass('col').addClass('irow');
				$('.screen').css({'right': 0, 'top': 0}).attr('class', 'screen rt');
				break;
			case '3':
				$('.content-t, .content-b').css({'width': parseInt(boxWidth/2), 'height': '100%'});
				$('.content-t').css({'left': 0});
				$('.content-b').css({'right': 0});
				$('.resize').removeClass('row').addClass('icol');
				$('.screen').css({'left': -20, 'top': 0}).attr('class', 'screen rt');
				break;
			case '4':
				$('.content-t, .content-b').css({'width': parseInt(boxWidth/2), 'height': '100%'});
				$('.content-t').css({'right': 0});
				$('.content-b').css({'left': 0});
				$('.resize').removeClass('row').addClass('icol');
				$('.screen').css({'left': 0, 'top': 0}).attr('class', 'screen lt');
				break;
		}
	}

	//获取作图option
	$.ajax({
		url: rootPath + '/bookoffice/BookReportChart.htm?m=getReportChart&bookcode=' + bookcode + '&code=' + code,
		type: 'post',
		async: false,
		dataType: 'json',
		success: succ,
		error: function(){
			ui.error('数据异常！');
		}
	});

	function succ(data){
		if (data.returncode === 200) {
			sType = data.returndata.reportchartsort.charAt(0);
			sPosi = data.returndata.reportchartsort.charAt(1);
			if (data.returndata.property === '') {
				data.returndata.property = '{}';
			}
			option = JSON.parse(data.returndata.property);
			tuType = option.tuType;
			isRow = option.axisType === '0' ? true : false;
		}else{
			sType = '1';
			sPosi = '1';
			$('.content-t').hide();
		}
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

	//更新作图option并重绘图形
	function override(data){
		var dataJson = data.returndata,
			newJson = isRow ? dataJson : dp(dataJson);

		if(tuType === 1){
			option.legend.data = newJson.rows;
			option.xAxis.data = newJson.cols;
			option.series = [];
			for (i = 0; i < newJson.rows.length; i++) {
				if(option.showLabel === "1"){
					option.series.push({ "name": newJson.rows[i], "type": "line", "data": newJson.data[i], "label": { "normal": { "show": true, "position": "top" }} });
				}else{
					option.series.push({ "name": newJson.rows[i], "type": "line", "data": newJson.data[i] });
				}
			}
		}else if(tuType === 2){
			option.legend.data = newJson.rows;
			option.xAxis.data = newJson.cols;
			option.series = [];
			for (i = 0; i < newJson.rows.length; i++) {
				if(option.showLabel === "1"){
					option.series.push({ "name": newJson.rows[i], "type": "line", "stack": '总量', "areaStyle": { normal: {} }, "data": newJson.data[i], "label": { "normal": { "show": true, "position": "top" }} });
				}else{
					option.series.push({ "name": newJson.rows[i], "type": "line", "stack": '总量', "areaStyle": { normal: {} }, "data": newJson.data[i] });
				}
			}
		}else if(tuType === 3){
			option.legend.data = newJson.rows;
			option.xAxis.data = newJson.cols;
			option.series = [];
			for (i = 0; i < newJson.rows.length; i++) {
				if(option.showLabel === "1"){
					option.series.push({ "name": newJson.rows[i], "type": "bar", "data": newJson.data[i], "label": { "normal": { "show": true, "position": "top" }} });
				}else{
					option.series.push({ "name": newJson.rows[i], "type": "bar", "data": newJson.data[i] });
				}
			}
		}else if(tuType === 4){
			option.legend.data = newJson.rows;
			option.yAxis.data = newJson.cols;
			option.series = [];
			for (i = 0; i < newJson.rows.length; i++) {
				if(option.showLabel === "1"){
					option.series.push({ "name": newJson.rows[i], "type": "bar", "data": newJson.data[i], "label": { "normal": { "show": true, "position": "top" }} });
				}else{
					option.series.push({ "name": newJson.rows[i], "type": "bar", "data": newJson.data[i] });
				}
			}
		}else if(tuType === 5 || tuType === 6){
			option.legend.data = newJson.cols;
			option.series[0].name = newJson.rows[0];
			option.series[0].data = [];
			for (i = 0; i < newJson.cols.length; i++) {
				if(option.showLabel === "1"){
					option.series[0].data.push({ name: newJson.cols[i], value: newJson.data[0][i], "label": { "normal": { "show": true }} });
				}else{
					option.series[0].data.push({ name: newJson.cols[i], value: newJson.data[0][i] });
				}
			}
		}

		myChart.setOption(option, true);
	}

	//点击查询
	$(document).on('click', '#selContent', function(){
		var args = '',
			filterstr = '',
			rangestr = '',
			rangeCodeArr,
			rangeNameArr,
			i,
			tempStr = '';
		
		$('.filterSel').each(function(){
			filterstr += $(this).attr('fname') + ':' + $(this).val() + ';';
		});
		$('.rangeSel').each(function(){
			rangeCodeArr = $(this).val().split(",");
			rangeNameArr = $(this).siblings('a');
			for (i = 0; i < rangeCodeArr.length; i++) {
				if(i !== 0){
					tempStr += ',';
				}
				tempStr += rangeCodeArr[i] + '_' + $.trim(rangeNameArr.eq(i).text());
			}
			rangestr += $(this).attr('fname') + ':' + tempStr + ';';
		});

		//获取作图数据并替换option中的数据
		$.ajax({
			url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=getDrawData&sort=M&bookcode=' + bookcode,
			type: 'post',
			data: {
				code: code,
				filterstr: filterstr,
				rangestr: rangestr
			},
			dataType: 'json',
			success: function(data) {
				if (data.returncode === 200) {
					override(data);
				}
			},
			error: function(){
				ui.error('数据异常！');
			}
		});

		//获取table
		$.ajax({
			url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=catalogPage&sort=M&bookcode=' + bookcode,
			type: 'post',
			data: {
				code: code,
				filterstr: filterstr,
				rangestr: rangestr
			},
			dataType: 'json',
			beforeSend: function() {
				$("body").append('<div class="ui dimmer ciDimmer"><div class="ui loader"></div></div>');
			},
			success: function(data) {
				$(".ciDimmer.ui.dimmer").remove();
				if (data.returncode === 200) {
					$('.content-b').html(data.returndata);
				}
			},
			error: function(){
				ui.error('数据异常！');
				$(".ciDimmer.ui.dimmer").remove();
			}
		});
	});

	//获取filter&&range
	$.ajax({
		url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=getFilters&sort=M&bookcode=' + bookcode + '&code=' + code,
		type: 'post',
		dataType: 'json',
		async: false,
		success: function(data) {
			if (data.returncode === 200) {
				var list = data.returndata.notRangeFList,
					rangeList = data.returndata.rangeFList,
					i,
					j,
					k,
					codeArr,
					nameArr;
				for (i = 0; i < list.length; i++) {
					rootNode = list[i].treelist;
					var selectGroupHtml = Handlebars.compile($('#select-group').html())({
						id: 'id' + i,
						treeId: 'treeId' + i,
						fname: list[i].filterName,
						fclass: 'filterSel',
						inincode: list[i].defaultCode,
						ininname: list[i].defaultName
					});

					$('#filter-box').append('<div class="filter-group"><span class="f-name">' + list[i].filterName + '：</span>' + selectGroupHtml + '</div>');
					initTree('id' + i, 'treeId' + i, list[i].wdtype, rootNode);
					$('#id'+i).find('input').val(list[i].defaultCode);
					$('#id'+i).find('.default').html(list[i].defaultName);
				}
				for (j = 0; j < rangeList.length; j++) {
					rootNode = rangeList[j].treelist;
					codeArr = rangeList[j].defaultCode.split(",");
					nameArr = rangeList[j].defaultName.split(",");
					var rselectGroupHtml = Handlebars.compile($('#select-group').html())({
						id: 'rid' + j,
						treeId: 'rtreeId' + j,
						fname: rangeList[j].filterName,
						fclass: 'rangeSel',
						ismult: 'multiple',
						inincode: rangeList[j].defaultCode,
						ininname: rangeList[j].defaultName
					});

					$('#filter-box').append('<div class="filter-group"><span class="f-name">' + rangeList[j].filterName + '：</span>' + rselectGroupHtml + '</div>');
					initTree('rid' + j, 'rtreeId' + j, rangeList[j].wdtype, rootNode);
					$('#rid'+j).find('input').val(rangeList[j].defaultCode);

					for (k = 0; k < codeArr.length; k++) {
						$('#rid'+j).find('.default').before('<a class="ui label transition" data-value="'+ codeArr[k] +'" style="display: inline-block ! important;">'+ nameArr[k] +'<i class="delete icon"></i></a>');
					}
				}

				//第一次
				var isFilter = list.length || rangeList.length;
				if(isFilter){
					$('#filter-box').append('<button class="ui button blue" id="selContent">查询</button>').show();
					contentHeight = bodyHeight - $('#filter-box').outerHeight(true);
					$('.content').height(contentHeight);
					showPosi(sPosi, bodyWidth, contentHeight);
					showType(sType, sPosi, bodyWidth, contentHeight);
					myChart = echarts.init($('#chart')[0]);
					$('#selContent').trigger('click');
				}else{
					contentHeight = bodyHeight;
					$('.content').height(contentHeight);
					showPosi(sPosi, bodyWidth, contentHeight);
					showType(sType, sPosi, bodyWidth, contentHeight);
					myChart = echarts.init($('#chart')[0]);
					//没有filter和range
					//获取作图数据并替换option中的数据
					$.ajax({
						url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=getDrawData&sort=M&bookcode=' + bookcode + '&code=' + code,
						type: 'post',
						dataType: 'json',
						success: function(data){
							override(data);
						},
						error: function(){
							ui.error('数据异常！');
						}
					});
					//获取table
					$.ajax({
						url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=catalogPage&sort=M&bookcode=' + bookcode + '&code=' + code,
						type: 'post',
						dataType: 'json',
						beforeSend: function() {
							$("body").append('<div class="ui dimmer ciDimmer"><div class="ui loader"></div></div>');
						},
						success: function(data) {
							$(".ciDimmer.ui.dimmer").remove();
							if (data.returncode === 200) {
								$('.content-b').html(data.returndata);
							}
						},
						error: function(){
							ui.error('数据异常！');
							$(".ciDimmer.ui.dimmer").remove();
						}
					});
				}

				$(window).resize(function(){
					bodyWidth = $('body').width();
					bodyHeight = $('body').height();
					contentHeight = isFilter ? bodyHeight - $('#filter-box').outerHeight(true) : bodyHeight;
					$('.content').height(contentHeight);
					showPosi(sPosi, bodyWidth, contentHeight);
					showType(sType, sPosi, bodyWidth, contentHeight);
					if(sPosi === '1' || sPosi === '2'){
						$('.resize').css({'top': '50%'});
					}else{
						$('.resize').css({'left': '50%'});
					}
					myChart.resize();
				});

				//拖拽
				$('.resize').live('mousedown',function(){
					var self = $(this),
						show = $('.content-t').is(':hidden');
					if(show){
						return;
					}
					if(self[0].setCapture){
						self[0].onmousemove=fnMove;
						self[0].onmouseup=fnUp;
						self[0].setCapture();
					}
					else{
						document.onmousemove=fnMove;
						document.onmouseup=fnUp;
					}
					
					function fnMove(e){
						var e = e || window.event,
							w = e.clientX,
							h = isFilter ? e.clientY - $('#filter-box').outerHeight(true) : e.clientY;

						switch (sPosi) {
							case '1':
								(h < parseInt(contentHeight*0.3)) && (h = parseInt(contentHeight*0.3));
								(h > parseInt(contentHeight*0.7)) && (h = parseInt(contentHeight*0.7));
								moveing($('.content-t'), $('.content-b'), h, 'horizontal');
								break;
							case '2':
								(h < parseInt(contentHeight*0.3)) && (h = parseInt(contentHeight*0.3));
								(h > parseInt(contentHeight*0.7)) && (h = parseInt(contentHeight*0.7));
								moveing($('.content-b'), $('.content-t'), h, 'horizontal');
								break;
							case '3':
								(w < parseInt(bodyWidth*0.3)) && (w = parseInt(bodyWidth*0.3));
								(w > parseInt(bodyWidth*0.7)) && (w = parseInt(bodyWidth*0.7));
								moveing($('.content-t'), $('.content-b'), w, 'vertical');
								break;
							case '4':
								(w < parseInt(bodyWidth*0.3)) && (w = parseInt(bodyWidth*0.3));
								(w > parseInt(bodyWidth*0.7)) && (w = parseInt(bodyWidth*0.7));
								moveing($('.content-b'), $('.content-t'), w, 'vertical');
								break;
						}
					}
					
					function moveing(left, right, value, vertical){
						if(vertical === 'vertical'){
							left.width(value);
							right.width(bodyWidth - value);
							self.css('left', value);
						}else{
							left.height(value);
							right.height(contentHeight - value);
							self.css('top', value);
						}
					}
					
					function fnUp(){
						this.onmousemove=null;
						this.onmouseup=null;
						self[0].releaseCapture && self[0].releaseCapture();
						myChart.resize();
					}
					return false;
				});
			}
		}
	});

	var bWidth, bHeight;
	$(document).on('click', '.screen', function(){
		var show = $('.content-t').is(':visible');
		if(show){
			bWidth = $('.content-b').width();
			bHeight = $('.content-b').height();
		}

		switch (sPosi) {
			case '1':
				if(show){
					$('.content-t').hide();
					$('.content-b').css({'height': contentHeight});
					$('.resize').css({'top': 0});
					$('.screen').css({'top': 0}).attr('class', 'screen rt');
				}else{
					$('.content-t').show();
					$('.content-b').css({'height': bHeight});
					$('.resize').css({'top': contentHeight - bHeight});
					$('.screen').css({'top': -20}).attr('class', 'screen rb');
					myChart.resize();
				}
				break;
			case '2':
				if(show){
					$('.content-t').hide();
					$('.content-b').css({'height': contentHeight});
					$('.resize').css({'top': contentHeight-2});
					$('.screen').css({'top': -20}).attr('class', 'screen rb');
				}else{
					$('.content-t').show();
					$('.content-b').css({'height': bHeight});
					$('.resize').css({'top': bHeight});
					$('.screen').css({'top': 0}).attr('class', 'screen rt');
					myChart.resize();
				}
				break;
			case '3':
				if(show){
					$('.content-t').hide();
					$('.content-b').css({'width': bodyWidth});
					$('.resize').css({'left': 0});
					$('.screen').css({'left': 0}).attr('class', 'screen lt');
				}else{
					$('.content-t').show();
					$('.content-b').css({'width': bWidth});
					$('.resize').css({'left': bodyWidth - bWidth});
					$('.screen').css({'left': -20}).attr('class', 'screen rt');
					myChart.resize();
				}
				break;
			case '4':
				if(show){
					$('.content-t').hide();
					$('.content-b').css({'width': bodyWidth});
					$('.resize').css({'left': bodyWidth});
					$('.screen').css({'left': -20}).attr('class', 'screen rt');
				}else{
					$('.content-t').show();
					$('.content-b').css({'width': bWidth});
					$('.resize').css({'left': bWidth});
					$('.screen').css({'left': 0}).attr('class', 'screen lt');
					myChart.resize();
				}
				break;
		}
	});
});