define(function(require, exports, module) {
	var $ = require('$'),
		Handlebars = require('handlebars'),
		ui = require('alert'),
		rootPath = require('rootPath'),
		modId = $('#modId').val(),
		ss = require('excel');
	require('semantic');

	//打开备注面板
	$(document).on("click", "#setMemo", function() {
		var isSetWeidu = $("#rootPath").data("isSetWeidu");
		if (isSetWeidu) {
			$(".memo-box").removeClass("hidden").siblings(".floats").addClass("hidden");
			getMemoList();
		} else {
			ui.alert("未设置主宾栏！");
		}
	});

	//显示备注新建面板
	$(document).on("click", "#addMemoBtn", function() {
		var template = {
			"title": "新建"
		};

		var content = Handlebars.compile($('#addOrEditMemo').html())(template);
		$(".new-memo").empty().append(content).removeClass("hide");
	});

	//取消新建备注
	$(document).on("click", "#cancelAddMemo", function() {
		$(".new-memo").addClass("hide");
	});

	//显示备注编辑面板
	$(document).on("click", "#memoList .edit-memo-btn", function() {
		var curTable = $(this).parents("table"),
			data = curTable.data("info");
		var template = {
			"title": "编辑",
			"memoStr": data.memoStr,
			"memoId": data.memoId,
			"isEdit": "isEdit"
		};

		var content = Handlebars.compile($('#addOrEditMemo').html())(template);
		$(".new-memo").empty().append(content).removeClass("hide");
	});

	//删除备注
	$(document).on("click", "#memoList .remove-memo-btn", function() {
		var memoId = $(this).attr("memoId");
		var delHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "确认要删除此备注吗？",
			"iconClass": "trash"
		});
		$('#modal2').empty().append(delHtml).modal({
			closable: false,
			onDeny: function() {
				$(".ui.modal").empty();
			},
			onApprove: function() {
				removeMemoList(memoId);
			}
		}).modal('show');
	});

	//保存备注(新增或编辑)
	$(document).on("click", "#saveMemo", function() {
		//是否是编辑
		var isEdit = $(this).hasClass("isEdit") ? 1 : 0,
			memoStr = $('#memoStr').val(),
			d = new Date(),
			memoId = '' + d.getFullYear() + (d.getMonth() + 1) + d.getDate() + d.getHours() + d.getMinutes() + d.getSeconds();

		if (isEdit) {
			memoId = $('#memoStr').attr("memoId");
		}

		if ($.trim(memoStr) === "") {
			ui.alert("备注不能为空！");
			return false;
		}

		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=madd&modId=" + modId,
			type: "post",
			data: {
				"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
				"memoId": memoId,
				"datas": memoStr,
				"isEdit": isEdit
			},
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success("保存成功！");
					getMemoList();
					$(".new-memo").addClass("hide");
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	});


	function getMemoList() {
		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=mfind&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			dataType: "json",
			success: succ
		});

		function succ(data) {
			if (data.returncode === 200) {
				var list = JSON.parse(data.returndata);
				$("#memoList").empty();

				for (var i = 0; i < list.length; i++) {
					var dataJson = {
						"memoId": list[i].memoId,
						"memoStr": list[i].memoStr
					};

					var listhtml = Handlebars.compile($('#memoTemplate').html())(dataJson);
					var table = $(listhtml);
					$("#memoList").append(table);
					table.data("info", dataJson); //保存所有信息到data对象上，便于编辑
				}
			}
		}
	}

	function removeMemoList(memoId) {
		$.ajax({
			url: rootPath + "/bookoffice/BookCalCheckMemo.htm?m=mdel&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&memoId=" + memoId,
			type: "post",
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success(data.returndata);
					getMemoList();
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	}
});