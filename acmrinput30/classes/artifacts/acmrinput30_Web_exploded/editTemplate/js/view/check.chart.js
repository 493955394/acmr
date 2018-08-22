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
	var checkJson = {};

	//打开校验面板
	$(document).on("click", "#setCheck", function() {
		var isSetWeidu = $("#rootPath").data("isSetWeidu");
		if (isSetWeidu) {
			$(".check-box").removeClass("hidden").siblings(".floats").addClass("hidden");
			getCheckList();
		} else {
			ui.alert("未设置主宾栏！");
		}
	});

	//显示校验新建面板
	$(document).on("click", "#addCheckBtn", function() {
		var template = {
			"title": "新建",
			"errorType": "1"
		};

		var content = Handlebars.compile($('#addOrEditCheck').html())(template);
		$(".new-check").empty().append(content).removeClass("hide");
		pub.initDTC(".new-check");
	});

	//取消新建校验
	$(document).on("click", "#cancelAddCheck", function() {
		$(".new-check").addClass("hide");
	});

	//显示校验编辑面板
	$(document).on("click", "#checkList .edit-check-btn", function() {
		var curTable = $(this).parents("table"),
			data = curTable.data("info");
		var template = {
			"title": "编辑",
			"checkName": data.name,
			"errorType": data.type,
			"checkInfo": data.info,
			"checkFun": data.content,
			"isEdit": "isEdit"
		};

		var content = Handlebars.compile($('#addOrEditCheck').html())(template);
		$(".new-check").empty().append(content).removeClass("hide");
		pub.initDTC(".new-check");
	});

	//删除校验
	$(document).on("click", "#checkList .remove-check-btn", function() {
		var list = $(this).parents("table"),
			name = $(".listName", list).text();
		var delHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "确认要删除此校验信息吗？",
			"iconClass": "trash"
		});
		$('#modal2').empty().append(delHtml).modal({
			closable: false,
			onDeny: function(){
				$(".ui.modal").empty();
			},
			onApprove: function() {
				removeCheckList(name);
			}
		}).modal('show');
	});

	//保存校验(新增或编辑)
	$(document).on("click", "#saveCheck", function() {
		//是否是编辑
		var isEdit = $(this).hasClass("isEdit") ? 1 : 0,
			checkName = $("#checkName").val(),
			oldCheckName = $("#checkName").attr("oldCheckName"),
			errorType = $("[name='errorType']").val(),
			checkInfo = $("#checkInfo").val(),
			checkFun = $("#checkFun").val(),
			isNext = true;

		if ($.trim(checkName) === "") {
			ui.alert("校验名称不能为空！");
			return false;
		}

		if ($.trim(checkInfo) === "") {
			ui.alert("提示信息不能为空！");
			return false;
		}

		if ($.trim(checkFun) === "") {
			ui.alert("校验方法不能为空！");
			return false;
		}
		//检查重名
		if(checkName !== oldCheckName){
			$.ajax({
				url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=ccheckName&modId="+modId+"&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + checkName,
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

		checkJson = {
			"name": checkName,
			"oldName": oldCheckName,
			"type": errorType,
			"info": checkInfo,
			"content": checkFun
		};

		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=cadd&modId="+modId,
			type: "post",
			data: {
				"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
				"datas": JSON.stringify(checkJson),
				"isEdit": isEdit
			},
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success("保存成功！");
					getCheckList();
					$(".new-check").addClass("hide");
				}else{
					ui.alert(data.returndata);
				}
			}
		});
	});


	function getCheckList() {
		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=cfind&modId="+modId+"&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			dataType: "json",
			success: succ
		});

		function succ(data) {
			if (data.returncode === 200) {
				var list = JSON.parse(data.returndata);
				$("#checkList").empty();
				var errorTypeArr = {
					"1": "错误",
					"2": "警告"
				};

				for (var i = 0; i < list.length; i++) {
					var dataJson = {
						"name": list[i].name,
						"content": list[i].content,
						"info": list[i].info,
						"type": list[i].type,
						"typeExp": errorTypeArr[list[i].type]
					};

					var listhtml = Handlebars.compile($('#checkTemplate').html())(dataJson);
					var table = $(listhtml);
					$("#checkList").append(table);
					table.data("info", dataJson); //保存所有信息到data对象上，便于编辑
				}
			}
		}
	}

	function removeCheckList(name) {
		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=cdel&modId="+modId+"&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + name,
			type: "post",
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success(data.returndata);
					getCheckList();
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	}
});
