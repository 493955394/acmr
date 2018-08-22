define(function(require, exports, module) {
	var $ = require('$'),
		Handlebars = require('handlebars'),
		ui = require('alert'),
		pub = require('publicfn'),
		rootPath = require('rootPath'),
		modId = $('#modId').val(),
		ss = require('excel');
	require('semantic');
	//数据json
	var figuresJson = {};

	//打开计算面板
	$(document).on("click", "#setFigures", function() {
		var isSetWeidu = $("#rootPath").data("isSetWeidu");
		if (isSetWeidu) {
			$(".figures-box").removeClass("hidden").siblings(".floats").addClass("hidden");
			getFiguresList();
		} else {
			ui.alert("未设置主宾栏！");
		}
	});

	//显示计算新建面板
	$(document).on("click", "#addFiguresBtn", function() {
		var template = {
			"title": "新建"
		};

		var content = Handlebars.compile($('#addOrEditFigures').html())(template);
		$(".new-figures").empty().append(content).removeClass("hide");
		pub.initDTC(".new-figures");
		pub.selRange($("#selFiguresRange"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".figures-posi").val(range);
		});
	});

	//取消新建计算
	$(document).on("click", "#cancelAddFigures", function() {
		$(".new-figures").addClass("hide");
	});

	//显示计算编辑面板
	$(document).on("click", "#figuresList .edit-figures-btn", function() {
		var curTable = $(this).parents("table"),
			data = curTable.data("info");
		var template = {
			"title": "编辑",
			"figuresName": data.name,
			"figuresPosi": data.posi,
			"figuresFun": data.content,
			"isEdit": "isEdit"
		};

		var content = Handlebars.compile($('#addOrEditFigures').html())(template);
		$(".new-figures").empty().append(content).removeClass("hide");
		pub.initDTC(".new-figures");
		pub.selRange($("#selFiguresRange"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".figures-posi").val(range);
		});
	});

	//删除计算
	$(document).on("click", "#figuresList .remove-figures-btn", function() {
		var list = $(this).parents("table"),
			name = $(".listName", list).text();
		var delHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "确认要删除此计算信息吗？",
			"iconClass": "trash"
		});
		$('#modal2').empty().append(delHtml).modal({
			closable: false,
			onDeny: function() {
				$(".ui.modal").empty();
			},
			onApprove: function() {
				removeFiguresList(name);
			}
		}).modal('show');
	});

	//保存计算(新增或编辑)
	$(document).on("click", "#saveFigures", function() {
		//是否是编辑
		var isEdit = $(this).hasClass("isEdit") ? 1 : 0,
			figuresName = $("#figuresName").val(),
			oldFiguresName = $("#figuresName").attr("oldFiguresName"),
			figuresPosi = $(".figures-posi").val(),
			figuresFun = $("#figuresFun").val(),
			isNext = true;

		if ($.trim(figuresName) === "") {
			ui.alert("计算名称不能为空！");
			return false;
		}

		if ($.trim(figuresPosi) === "") {
			ui.alert("结果位置不能为空！");
			return false;
		}

		if ($.trim(figuresFun) === "") {
			ui.alert("计算方法不能为空！");
			return false;
		}
		//检查重名
		if (figuresName !== oldFiguresName) {
			$.ajax({
				url: rootPath + "/templatecenter/calCheckMemo.htm?m=echeckName&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + figuresName,
				type: "post",
				async: false,
				dataType: "json",
				success: function(data) {
					if (data.returncode === 500) {
						ui.error(data.returndata);
						isNext = false;
					} else {
						isNext = true;
					}
				}
			});
		}
		if (!isNext) {
			return false;
		}

		figuresJson = {
			"name": figuresName,
			"oldName": oldFiguresName,
			"posi": {
				"colStartIndex": figuresPosi.split(":")[0].substring(0, 1),
				"colEndIndex": figuresPosi.split(":")[1].substring(0, 1),
				"rowStartIndex": figuresPosi.split(":")[0].substring(1),
				"rowEndIndex": figuresPosi.split(":")[1].substring(1)
			},
			"content": figuresFun
		};

		$.ajax({
			url: rootPath + "/templatecenter/calCheckMemo.htm?m=eadd&modId=" + modId,
			type: "post",
			data: {
				"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
				"datas": JSON.stringify(figuresJson),
				"isEdit": isEdit
			},
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success("保存成功！");
					getFiguresList();
					$(".new-figures").addClass("hide");

					/*var oldText = ss.getTextByCoordinate("1", data.param1);
					var newText = data.returndata;
					if (oldText !== "") {
						newText = oldText + newText;
					}
					ss.setCellContent("1", newText, data.param1);*/
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	});


	function getFiguresList() {
		$.ajax({
			url: rootPath + "/templatecenter/calCheckMemo.htm?m=efind&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			dataType: "json",
			success: succ
		});

		function succ(data) {
			if (data.returncode === 200) {
				var list = JSON.parse(data.returndata);
				$("#figuresList").empty();

				for (var i = 0; i < list.length; i++) {
					var dataJson = {
						"name": list[i].name,
						"content": list[i].content,
						"posi": list[i].posi.colStartIndex + list[i].posi.rowStartIndex + ":" + list[i].posi.colEndIndex + list[i].posi.rowEndIndex
					};

					var listhtml = Handlebars.compile($('#figuresTemplate').html())(dataJson);
					var table = $(listhtml);
					$("#figuresList").append(table);
					table.data("info", dataJson); //保存所有信息到data对象上，便于编辑
				}
			}
		}
	}

	function removeFiguresList(name) {
		$.ajax({
			url: rootPath + "/templatecenter/calCheckMemo.htm?m=edel&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + name,
			type: "post",
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success(data.returndata);
					getFiguresList();
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	}
});