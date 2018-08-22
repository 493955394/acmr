define(function(require, exports, module) {
	var $ = require('$'),
		Handlebars = require('handlebars'),
		ui = require('alert'),
		rootPath = require('rootPath'),
		pub = require('publicfn'),
		echarts = require('echarts'),
		code = $('#modId').val(),
		option = {},
		dataJson = {},
		isRow = '0',
		myChart = echarts.init($('#chart-main')[0]),
		doc = $(document),
		bookcode = $("#bookcode").val(),
		i;

	/*var dataJson = {
		"rows": ["累计总计费时长", "累计短信业务计费量", "彩铃业务下载客户数", "中高端客户保有率", "累计月均离网率", "量收增幅比"],
		"cols": ["浙江", "江苏", "北京", "上海", "山东", "福建", "湖南", "湖北"],
		"data": [
			[5793.66, 2575.4, 3057.78, 2607.15, 1450.63, 3057.78, 2607.15, 450.63],
			[6530.01, 2606.8, 3448.77, 2917.4, 1531.37, 3448.77, 2917.4, 531.37],
			[7925.58, 2685.38, 4359.12, 3697.83, 1661.29, 4359.12, 3697.83, 661.29],
			[10011.37, 2844.52, 5543.04, 4690.46, 1852.58, 5543.04, 4690.46, 852.58],
			[11409.6, 2940.01, 5975.18, 4981.01, 1994.17, 5975.18, 4981.01, 994.17],
			[7409.6, 2540.01, 4975.18, 2981.01, 3594.17, 4975.18, 2981.01, 1594.17]
		]
	};*/
	var colors = [
		["#b9baea", "#a3cce8", "#e98c9e", "#ea9775", "#e95d5c", "#46a4ea", "#e92fd2", "#1740e8", "#00cdea", "#e99f00"],
		["#012f2f", "#460046", "#014a5d", "#007500", "#008a8c", "#a45f00", "#00bb4a", "#0091ba", "#00b6e8", "#e9cd00"],
		["#00b6e8", "#0073ea", "#17e990", "#01ba7e", "#008ea4", "#5ed7ea", "#008c00", "#002e74", "#5eaee9", "#5d5c00"],
		["#e98cb9", "#5ed7ea", "#e9d12f", "#1769e9", "#ea7201", "#012ee9", "#d20066", "#00bb6e", "#4e00a4", "#00798c"],
		["#64cfb3", "#b98434", "#73a011", "#1c9126", "#248476", "#0a4f79", "#19396c", "#2a2f59", "#4684e9", "#5d45d9"],
		["#670001", "#800000", "#9a0000", "#cc0001", "#e60000", "#fe0000", "#ff3334", "#ff6666", "#ff999a", "#ffcccb"],
		["#015466", "#01677d", "#007891", "#00bae3", "#00bae3", "#01d1ff", "#2dd9ff", "#68e4fe", "#97ecff", "#cff5fe"],
		["#166701", "#1c8101", "#24a201", "#2ac100", "#33e400", "#37ff00", "#57fe28", "#88ff67", "#b9ffa6", "#deffd4"],
		["#0f2d61", "#163b7f", "#1d4ba0", "#235cc3", "#2a6ee9", "#4287ff", "#699fff", "#88b4ff", "#a9c8fe", "#d4e4fe"],
		["#634f1a", "#745a0d", "#8d6a02", "#b08502", "#c89b0e", "#e4af11", "#ffc617", "#ffd658", "#ffe696", "#fff3d0"]
	];

	if ($("#updataTemplate").length) {
		$.ajax({
			url: rootPath + '/bookoffice/BookReportChart.htm?m=getReportChart&code=' + code,
			type: 'post',
			dataType: 'json',
			async: false,
			success: function(data) {
				if (data.returncode === 200) {
					var index = data.returndata.sort,
						myDataJson = data.returndata.property;
					if (index === '') {
						index = 0;
					}
					if (myDataJson === '') {
						myDataJson = '{}';
					}
					option = JSON.parse(myDataJson);
					isRow = option.axisType === '0' ? true : false;
					$('.chart-type-panel a').eq(index).addClass('on').siblings().removeClass('on');
					myChart.setOption(option, true);
				}
			}
		});
	}

	//图表设置
	doc.on("click", "#setChartShow", function() {
		var template = {};
		$.ajax({
			url: rootPath + '/bookoffice/BookReportChart.htm?m=getReportChart&code=' + code,
			type: 'post',
			dataType: 'json',
			async: false,
			success: function(data) {
				if (data.returncode === 200) {
					var type = data.returndata.reportchartsort.charAt(0),
						posi = data.returndata.reportchartsort.charAt(1);
					template.exps = data.returndata.exps;
					template['type' + type] = type;
					template['posi' + posi] = posi;
				}
			}
		});

		var html = Handlebars.compile($('#show-setting').html())(template);
		$('#modal').empty().append(html).modal({
			closable: false,
			onApprove: function() {
				var sort = $('#showType').val() + $('#showPosi').val();
				var exps = $('#exps').val();
				$.ajax({
					url: rootPath + '/bookoffice/BookReportChart.htm?m=add&code=' + code + '&chartsort=' + sort + '&exps=' + exps,
					type: 'post',
					dataType: 'json',
					success: function(data) {
						if (data.returncode === 200) {
							ui.success(data.returndata);
						}
					}
				});
			}
		}).modal('show');
	});

	//返回上一页
	doc.on("click", "#windowBack", function() {
		window.location.href = rootPath + '/bookoffice/bookreport.htm?m=query&bookcode=' + bookcode;
	});

	/*表格-图表切换*/
	doc.on("click", "#toExcel", function() {
		$(".chart-panel").addClass("hide");
	});

	doc.on("click", "#toChart", function() {
		$.ajax({
			url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=getDrawData&code=' + code + '&sort=M',
			type: 'post',
			dataType: 'json',
			async: false,
			success: function(data) {
				if (data.returncode === 200) {
					dataJson = data.returndata;
					var newJson = isRow ? dataJson : dp(dataJson),
						tuType = $(".chart-type-panel a.on").index();
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
			}
		});
		$(".chart-panel").removeClass("hide");
	});

	doc.on("click", "#chart-setting", function() {
		var tuType = $(".chart-type-panel a.on").index(),
			template = {},
			prevType;
		if(tuType === 0){
			return;
		}
		if (option.title.show) {
			template.isTitle = true;
			template.title = option.title.text;
		}
		if (option.color) {
			template.colorval = true;
			template.title = option.title.text;
		}
		if (option.colorStr) {
			template.colorStr = option.colorStr;
		}
		if (option.legend.pos) {
			template['posi' + option.legend.pos] = option.legend.pos;
		}
		if (option.showLabel) {
			template['showLabel' + option.showLabel] = option.showLabel;
		}
		if (option.axisType) {
			template['axisType' + option.axisType] = option.axisType;
		}
		if (option.xAxis) {
			if(option.xAxis.axisLabel.show){
				template.labelX = true;
			}
			if(tuType !== 4 && option.xAxis.splitLine.show){
				template.splitLine = true;
			}
			if(tuType === 4 && option.xAxis.min === 'auto'){
				template.dataMin1 = true;
			}
		}
		if (option.yAxis) {
			if(option.yAxis.axisLabel.show){
				template.labelY = true;
			}
			if(tuType === 4 && option.yAxis.splitLine.show){
				template.splitLine = true;
			}
			if(tuType !== 4 && option.yAxis.min === 'auto'){
				template.dataMin1 = true;
			}
		}

		var html = Handlebars.compile($('#chart-arg-setting').html())(template);
		$('#modal').empty().append(html).modal({
			closable: false,
			onShow: function(){
				prevType = $('#axisType').val();
			},
			onApprove: function() {
				saveSetting(prevType);
			}
		}).modal('show');
		$('.ui.menu.tabular .item').tab();
		pub.initDTC(".modal");
	});

	function drawLine() {
		option = {};
		option = {
			axisType: '0',
			tuType: 1,
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: {},
			grid: {
				right: '30%',
				containLabel: true
			},
			backgroundColor: '#fff',
			legend: {
				pos: '4',
				orient: 'vertical',
				y: 'center',
				left: '75%',
				data: dataJson.rows
			},
			xAxis: {
				splitLine: { show: false },
				axisLabel: { show: true },
				data: dataJson.cols
			},
			yAxis: {
				min: 'auto',
				axisLabel: { show: true },
				type: 'value'
			},
			series: []
		};

		for (i = 0; i < dataJson.rows.length; i++) {
			option.series.push({ "name": dataJson.rows[i], "type": "line", "data": dataJson.data[i] });
		}
	}

	function drawStackLine() {
		option = {};
		option = {
			axisType: '0',
			tuType: 2,
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: { trigger: 'axis' },
			grid: {
				right: '30%',
				containLabel: true
			},
			backgroundColor: '#fff',
			legend: {
				pos: '4',
				y: 'center',
				left: '75%',
				data: dataJson.rows
			},
			xAxis: {
				type: 'category',
				boundaryGap: false,
				splitLine: { show: false },
				axisLabel: { show: true },
				data: dataJson.cols
			},
			yAxis: {
				min: 'auto',
				axisLabel: { show: true },
				type: 'value'
			},
			series: []
		};

		for (i = 0; i < dataJson.rows.length; i++) {
			option.series.push({ "name": dataJson.rows[i], "type": "line", "stack": '总量', "areaStyle": { normal: {} }, "data": dataJson.data[i] });
		}
	}

	function drawBar() {
		option = {};
		option = {
			axisType: '0',
			tuType: 3,
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: {},
			grid: {
				right: '30%',
				containLabel: true
			},
			backgroundColor: '#fff',
			legend: {
				pos: '4',
				orient: 'vertical',
				y: 'center',
				left: '75%',
				data: dataJson.rows
			},
			xAxis: {
				splitLine: { show: false },
				axisLabel: { show: true },
				data: dataJson.cols
			},
			yAxis: {
				min: 'auto',
				axisLabel: { show: true },
				type: 'value'
			},
			series: []
		};

		for (i = 0; i < dataJson.rows.length; i++) {
			option.series.push({ "name": dataJson.rows[i], "type": "bar", "data": dataJson.data[i] });
		}
	}

	function drawYBar() {
		option = {};
		option = {
			axisType: '0',
			tuType: 4,
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: {},
			grid: {
				bottom: '30%',
				containLabel: true
			},
			backgroundColor: '#fff',
			legend: {
				pos: '2',
				orient: 'horizontal',
				x: 'center',
				top: '75%',
				data: dataJson.rows
			},
			xAxis: {
				min: 'auto',
				axisLabel: { show: true },
				type: 'value'
			},
			yAxis: {
				type: 'category',
				splitLine: { show: false },
				axisLabel: { show: true },
				data: dataJson.cols
			},
			series: []
		};

		for (i = 0; i < dataJson.rows.length; i++) {
			option.series.push({ "name": dataJson.rows[i], "type": "bar", "data": dataJson.data[i] });
		}
	}

	function drawPie() {
		option = {};
		option = {
			axisType: '0',
			tuType: 5,
			showLabel: '1',
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c} ({d}%)"
			},
			backgroundColor: '#fff',
			legend: {
				pos: '4',
				orient: 'vertical',
				x: 'right',
				y: 'center',
				data: dataJson.cols
			},
			series: [{
				label: {
					normal: {
						show: true,
						position: 'outside'
					}
				},
				name: dataJson.rows[0],
				type: "pie",
				radius: '60%',
				center: ['40%', '50%'],
				data: [],
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				}
			}]
		};
		
		for (i = 0; i < dataJson.cols.length; i++) {
			option.series[0].data.push({ name: dataJson.cols[i], value: dataJson.data[0][i] });
		}
	}

	function drawEPie() {
		option = {};
		option = {
			axisType: '0',
			tuType: 6,
			showLabel: '0',
			title: {
				show: false,
				textStyle: {
					fontWeight: 'bold',
					fontFamuly: 'Calibri'
				},
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c} ({d}%)"
			},
			backgroundColor: '#fff',
			legend: {
				pos: '4',
				orient: 'vertical',
				x: 'right',
				y: 'center',
				data: dataJson.cols
			},
			series: [{
				name: dataJson.rows[0],
				type: "pie",
				radius: ['50%', '70%'],
				center: ['40%', '50%'],
				label: {
					normal: {
						show: false,
						position: 'center'
					},
					emphasis: {
						show: true,
						textStyle: {
							fontSize: '30',
							fontWeight: 'bold'
						}
					}
				},
				labelLine: {
					normal: {
						show: false
					}
				},
				data: []
			}]
		};

		for (i = 0; i < dataJson.cols.length; i++) {
			option.series[0].data.push({ name: dataJson.cols[i], value: dataJson.data[0][i] });
		}
	}

	function drawNone() {
		option = {};
	}

	function drawScatter() {
		option = {};
	}


	doc.on("click", ".chart-type-panel a", function() {
		var type = $(this).index();
		if ($(this).hasClass('on')) {
			return;
		}
		$(this).addClass('on').siblings().removeClass('on');
		switch (type) {
			case 1:
				drawLine();
				break;
			case 2:
				drawStackLine();
				break;
			case 3:
				drawBar();
				break;
			case 4:
				drawYBar();
				break;
			case 5:
				drawPie();
				break;
			case 6:
				drawEPie();
				break;
			case 7:
				drawScatter();
				break;
			default:
				drawNone();
				break;
		}
		myChart.setOption(option, true);
	});

	doc.on("click", "#chartPreview", function() {
		$.ajax({
			url: rootPath + '/bookoffice/BookDataShowHandel.htm?m=getDrawData&code=' + code + '&sort=M&bookcode='+bookcode,
			type: 'post',
			dataType: 'json',
			async: false,
			success: function(data) {
				if(!data){
					ui.error('维度不全，不能预览！');
				}
				if (data.returncode === 200) {
					window.open(rootPath + '/bookoffice/BookDataShowHandel.htm?m=showCatalogPage&code=' + code + '&sort=M&bookcode='+bookcode);
				}
			},
		});
	});

	doc.on("click", "#saveChart", function() {
		var chartType = $('.chart-type-panel a.on').index();
		$.ajax({
			url: rootPath + '/bookoffice/bookreportchart.htm?m=addProperty',
			type: 'post',
			data: {
				code: code,
				sort: chartType,
				property: JSON.stringify(option)
			},
			dataType: 'json',
			success: function(data) {
				if (data.returncode === 200) {
					ui.success("保存成功！");
				} else {
					ui.error(data.returndata);
				}
			}
		});
	});

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

	function saveSetting(prevAxisType) {
		var isTitle = $('#isShowTitle').is(':checked') ? true : false,
			labelX = $('#labelX').is(':checked') ? true : false,
			labelY = $('#labelY').is(':checked') ? true : false,
			splitLine = $('#splitLine').is(':checked') ? true : false,
			dataMin = $('#dataMin').val(),
			axisType = $('#axisType').val(),
			type = $('.chart-type-panel a.on').index(),
			titleText = $('#titleText').val(),
			legendPosi = $('#legendPosi').val(),
			showLabel = $('#showLabel').val(),
			colorStr = $('#legendColor').val(),
			jsonData;

		if(axisType === '0'){
			jsonData = dataJson;
		}else{
			jsonData = dp(dataJson);
		}
		//如果数据系列发生改变
		if(axisType !== prevAxisType){
			if(type === 1){
				option.legend.data = jsonData.rows;
				option.xAxis.data = jsonData.cols;
				option.series = [];
				for (i = 0; i < jsonData.rows.length; i++) {
					option.series.push({ "name": jsonData.rows[i], "type": "line", "data": jsonData.data[i] });
				}
			}else if(type === 2){
				option.legend.data = jsonData.rows;
				option.xAxis.data = jsonData.cols;
				option.series = [];
				for (i = 0; i < jsonData.rows.length; i++) {
					option.series.push({ "name": jsonData.rows[i], "type": "line", "stack": '总量', "areaStyle": { normal: {} }, "data": jsonData.data[i] });
				}
			}else if(type === 3){
				option.legend.data = jsonData.rows;
				option.xAxis.data = jsonData.cols;
				option.series = [];
				for (i = 0; i < jsonData.rows.length; i++) {
					option.series.push({ "name": jsonData.rows[i], "type": "bar", "data": jsonData.data[i] });
				}
			}else if(type === 4){
				option.legend.data = jsonData.rows;
				option.yAxis.data = jsonData.cols;
				option.series = [];
				for (i = 0; i < jsonData.rows.length; i++) {
					option.series.push({ "name": jsonData.rows[i], "type": "bar", "data": jsonData.data[i] });
				}
			}else if(type === 5 || type === 6){
				option.legend.data = jsonData.cols;
				option.series[0].name = jsonData.rows[0];
				option.series[0].data = [];
				for (i = 0; i < jsonData.cols.length; i++) {
					option.series[0].data.push({ name: jsonData.cols[i], value: jsonData.data[0][i] });
				}
			}
		}

		option.axisType = axisType;//自定义保存数据系列
		option.tuType = type;//保存图类型
		option.title.show = isTitle;
		option.title.text = titleText;
		if (colorStr !== "") {
			option.color = colors[colorStr];
			option.colorStr = colorStr;
		}
		if (type > 0 && type < 5) {
			option.legend = { data: jsonData.rows };
			option.grid = { containLabel: true };
			option.xAxis.axisLabel.show = labelX;
			option.yAxis.axisLabel.show = labelY;
			if(type === 4){
				option.yAxis.splitLine.show = splitLine;
				option.xAxis.min = dataMin;
			}else{
				option.xAxis.splitLine.show = splitLine;
				option.yAxis.min = dataMin;
			}
			switch (legendPosi) {
				case '0':
					option.legend.show = false;
					option.legend.pos = '0';
					break;
				case '1':
					option.legend.pos = '1';
					option.legend.show = true;
					option.legend.orient = 'horizontal';
					option.legend.x = 'center';
					option.legend.bottom = '75%';
					option.grid.top = '30%';
					break;
				case '2':
					option.legend.pos = '2';
					option.legend.show = true;
					option.legend.orient = 'horizontal';
					option.legend.x = 'center';
					option.legend.top = '75%';
					option.grid.bottom = '30%';
					break;
				case '3':
					option.legend.pos = '3';
					option.legend.show = true;
					option.legend.orient = 'vertical';
					option.legend.y = 'center';
					option.legend.right = '75%';
					option.grid.left = '30%';
					break;
				case '4':
					option.legend.pos = '4';
					option.legend.show = true;
					option.legend.orient = 'vertical';
					option.legend.y = 'center';
					option.legend.left = '75%';
					option.grid.right = '30%';
					break;
			}

			for (i = 0; i < option.series.length; i++) {
				option.series[i].label = {};
				option.series[i].label.normal = {};
				option.series[i].label.normal.position = 'top';
				if (showLabel === '1') {
					option.series[i].label.normal.show = true;
				} else {
					option.series[i].label.normal.show = false;
				}
			}
			option.showLabel = showLabel;
		} else if (type === 5 || type === 6) {
			if (showLabel === '1') {
				option.series[0].label.normal.show = true;
			} else {
				option.series[0].label.normal.show = false;
			}
			option.showLabel = showLabel;
			option.legend = { data: jsonData.cols };
			switch (legendPosi) {
				case '0':
					option.legend.show = false;
					option.legend.pos = '0';
					option.series[0].center = ['50%', '50%'];
					break;
				case '1':
					option.legend.pos = '1';
					option.legend.show = true;
					option.legend.orient = 'horizontal';
					option.legend.x = 'center';
					option.legend.y = '60';
					option.series[0].center = ['50%', '60%'];
					break;
				case '2':
					option.legend.pos = '2';
					option.legend.show = true;
					option.legend.orient = 'horizontal';
					option.legend.x = 'center';
					option.legend.y = 'bottom';
					option.series[0].center = ['50%', '40%'];
					break;
				case '3':
					option.legend.pos = '3';
					option.legend.show = true;
					option.legend.orient = 'vertical';
					option.legend.x = 'left';
					option.legend.y = 'center';
					option.series[0].center = ['60%', '50%'];
					break;
				case '4':
					option.legend.pos = '4';
					option.legend.show = true;
					option.legend.orient = 'vertical';
					option.legend.x = 'right';
					option.legend.y = 'center';
					option.series[0].center = ['40%', '50%'];
					break;
			}

		}

		myChart.setOption(option, true);
	}
});
