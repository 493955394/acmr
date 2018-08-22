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
	var areaJson = {},
		isNext = true;

	//显示识别区新建面板
	$(document).on("click", "#addRecognitionArea", function() {
		var template = {
			"title": "新建",
			"argbtn": "addArgument",
			"atype": "0",
			"active0": "active",
			"rscheck0": "checked",
			"recheck0": "checked",
			"cscheck0": "checked",
			"cecheck0": "checked"
		};

		var content = Handlebars.compile($('#addOrEditArea').html())(template);
		$(".new-recognition-area").empty().append(content).removeClass("hide");
		pub.initDTC(".new-recognition-area");
		pub.selRange($("#selAreaRange"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".celled-text").val(range);
		});
	});

	//显示识别区编辑面板
	$(document).on("click", "#recognition-area-list .edit-area-btn", function() {
		var curTable = $(this).parents("table"),
			data = curTable.data("info");
		var template = {
			"title": "编辑",
			"name": data.name,
			"argbtn": "editArgument",
			"isEdit": "isEdit",
			"atype": data.ainfo.positionType
		};
		template["active" + data.ainfo.positionType] = "active";
		if (data.ainfo.positionType === "0") {
			template.celledText = data.ainfo.colStartIndex + ":" + data.ainfo.colEndIndex;
		} else if (data.ainfo.positionType === "1") {
			template.rowText = data.ainfo.rowStartIndex + ":" + data.ainfo.rowEndIndex;
			template["rscheck" + data.ainfo.stype] = "checked";
			template["recheck" + data.ainfo.etype] = "checked";
			template.csText = data.ainfo.svalue;
			template.ceText = data.ainfo.evalue;
		} else { //2
			template.colText = data.ainfo.colStartIndex + ":" + data.ainfo.colEndIndex;
			template["cscheck" + data.ainfo.stype] = "checked";
			template["cecheck" + data.ainfo.etype] = "checked";
			template.rsText = data.ainfo.svalue;
			template.reText = data.ainfo.evalue;
		}

		var content = Handlebars.compile($('#addOrEditArea').html())(template);
		$(".new-recognition-area").empty().append(content).removeClass("hide");
		$("#editArgument").data("info", data);
		pub.initDTC(".new-recognition-area");
		pub.selRange($("#selAreaRange"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".celled-text").val(range);
		});
	});

	//取消新建识别区
	$(document).on("click", "#cancelAddArea", function() {
		$(".new-recognition-area").addClass("hide");
	});

	//识别区参数设置(新增)
	$(document).on("click", "#addArgument", function() {
		//areaName
		var areaName = $("#areaName").val();
		if ($.trim(areaName) === "") {
			ui.alert("识别区名称不能为空！");
			return false;
		}

		$.ajax({
			url: rootPath + "/templatecenter/DistinguishArea.htm?m=checkName&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + areaName,
			type: "post",
			async: false,
			dataType: "json",
			success: function(data) {
				if (data.returncode === 500) {
					ui.error(data.returndata);
					isNext = false;
					return false;
				} else {
					isNext = true;
				}
			}
		});
		if (!isNext) {
			return false;
		}
		var d = new Date();
		var template = {
			"weidu": "zb", //新增默认为指标
			"ifdiy0": "checked",
			"likeType0": "checked",
			"filter0": "checked",
			"farea0": "checked",
			"inputArg0": "checked",
			"inputVal1": "sheetname[]",
			"outArg0": "checked",
			"fname": "fName" + d.getFullYear() + (d.getMonth() + 1) + d.getDate() + d.getHours() + d.getMinutes()
		};
		var argHtml = Handlebars.compile($('#areaArg').html())(template);
		$('#modal').empty().append(argHtml).modal({
			closable: false,
			onShow: function() {
				var wd = $("[name=wdType]").val();
				pub.initArgTree(wd);
			},
			onApprove: function() {
				//确认回调
				setJson();
				if (!isNext) {
					return false;
				}
			}
		}).modal('show');

		initArgumentModal();
	});

	//识别区参数设置(编辑)
	$(document).on("click", "#editArgument", function() {
		var data = $(this).data("info"),
			dd = new Date(),
			template = {
				"weidu": data.wdflag,
				"wds": data.wds
			};
		template["ifdiy" + data.ifdiy] = "checked";
		template["likeType" + data.liketype] = "checked";
		if (data.ifdiy) {
			template.showBoxOne = "active";
		}
		if (data.filterInfo) {
			template.filter1 = "checked";
			template.showFilterBox = "active";
			template.fname = data.filterInfo.fname;
			template["farea" + data.filterInfo.farea] = "checked";
			template["inputArg" + data.filterInfo.itype] = "checked";
			template["inputVal" + data.filterInfo.itype] = data.filterInfo.ival;
			template["outArg" + data.filterInfo.otype] = "checked";
			template["outVal" + data.filterInfo.otype] = data.filterInfo.oval;
			template.oldFilterName = data.filterInfo.fname;
			if (data.filterInfo.farea === "2") {
				template.showBoxTwo = "active";
				template.fwd = data.filterInfo.fwd;
			}
		} else {
			template.filter0 = "checked";
			template.farea0 = "checked";
			template.inputArg0 = "checked";
			template.outArg1 = "checked";
			template.fname = "fName" + dd.getFullYear() + (dd.getMonth() + 1) + dd.getDate() + dd.getHours() + dd.getMinutes();
		}

		var argHtml = Handlebars.compile($('#areaArg').html())(template);
		$('#modal').empty().append(argHtml).modal({
			closable: false,
			onShow: function() {
				var wd = $("[name=wdType]").val();
				pub.initArgTree(wd);
			},
			onApprove: function() {
				//确认回调
				setJson();
				if (!isNext) {
					return false;
				}
			}
		}).modal('show');

		initArgumentModal();

		if (data.filterInfo && data.filterInfo.otype === "0") {
			var i,
				html = '',
				onameArr = data.filterInfo.oname.split(","),
				ovalueArr = data.filterInfo.oval.split(",");
			for (i = 0; i < onameArr.length; i++) {
				html += '<a class="ui label transition visible" data-value="' + ovalueArr[i] + '" style="display: inline-block ! important;">' + onameArr[i] + '<i class="delete icon"></i></a>';
			}
			$("#outArgSelect .dropdown.icon").after(html);
		}
	});

	//打开识别区面板----(查询列表)
	$(document).on("click", "#setRecognitionArea", function() {
		var isSetWeidu = $("#rootPath").data("isSetWeidu");
		if (isSetWeidu) {
			$(".area-box").removeClass("hidden").siblings(".floats").addClass("hidden");
			getAreaList();
		} else {
			ui.alert("未设置主宾栏！");
		}
	});

	//删除识别区
	$(document).on("click", "#recognition-area-list .remove-area-btn", function() {
		var list = $(this).parents("table"),
			name = $(".listName", list).text();
		var delHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "确认要删除此识别区吗？",
			"iconClass": "trash"
		});
		$('#modal2').empty().append(delHtml).modal({
			closable: false,
			onApprove: function() {
				removeArea(name);
			}
		}).modal('show');
	});

	//保存识别区(新增或编辑)
	$(document).on("click", "#saveArea", function() {
		//是否是编辑
		var isEdit = $(this).hasClass("isEdit") ? 1 : 0,
			areaName = $("#areaName").val(),
			oldAreaName = $("#areaName").attr("oldName"),
			type = $("[name='atype']").val(),
			stype,
			etype,
			svalue,
			evalue;
		if (isEdit && !areaJson.wdflag) { //如果是编辑并且没有设置参数，把编辑之前所有数据赋过来
			var infos = $("#editArgument").data("info");
			areaJson = infos; //直接赋值所有数据
		}

		if ($.trim(areaName) === "") {
			ui.alert("识别区名称不能为空！");
			return false;
		}

		if (!areaJson.wdflag) {
			ui.alert("未设置参数！");
			return false;
		}

		areaJson.name = areaName;
		areaJson.oldName = oldAreaName;
		areaJson.ainfo = {};
		areaJson.ainfo.positionType = type;
		if (type === "0") {
			var celledText = $(".celled-text").val();
			areaJson.ainfo.colStartIndex = celledText.split(":")[0];
			areaJson.ainfo.colEndIndex = celledText.split(":")[1];
			if ($.trim(celledText) === "") {
				ui.alert("区域不能为空！");
				return false;
			}
		} else if (type === "1") {
			var rowText = $("#rowindex").val();
			stype = $("[name='startcol']:checked").val();
			etype = $("[name='endcol']:checked").val();
			svalue = $("#startcol").val();
			evalue = $("#endcol").val();

			areaJson.ainfo.rowStartIndex = rowText.split(":")[0];
			areaJson.ainfo.rowEndIndex = rowText.split(":")[1];
			areaJson.ainfo.stype = stype;
			areaJson.ainfo.svalue = svalue;
			areaJson.ainfo.etype = etype;
			areaJson.ainfo.evalue = evalue;
			if (stype === "0") {
				areaJson.ainfo.startColText = svalue;
			} else {
				areaJson.ainfo.colStartIndex = svalue;
			}
			if (etype === "0") {
				areaJson.ainfo.endColText = evalue;
			} else {
				areaJson.ainfo.colEndIndex = evalue;
			}
			if ($.trim(rowText) === "") {
				ui.alert("区域不能为空！");
				return false;
			}
		} else { //type === "2"
			var colText = $("#colindex").val();
			stype = $("[name='startrow']:checked").val();
			etype = $("[name='endrow']:checked").val();
			svalue = $("#startrow").val();
			evalue = $("#endrow").val();

			areaJson.ainfo.colStartIndex = colText.split(":")[0];
			areaJson.ainfo.colEndIndex = colText.split(":")[1];
			areaJson.ainfo.stype = stype;
			areaJson.ainfo.svalue = svalue;
			areaJson.ainfo.etype = etype;
			areaJson.ainfo.evalue = evalue;
			if (stype === "0") {
				areaJson.ainfo.startRowText = svalue;
			} else {
				areaJson.ainfo.rowStartIndex = svalue;
			}
			if (etype === "0") {
				areaJson.ainfo.endRowText = evalue;
			} else {
				areaJson.ainfo.rowEndIndex = evalue;
			}
			if ($.trim(colText) === "") {
				ui.alert("区域不能为空！");
				return false;
			}
		}

		$.ajax({
			url: rootPath + "/templatecenter/DistinguishArea.htm?m=add",
			type: "post",
			data: {
				"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
				"datas": JSON.stringify(areaJson),
				"isEdit": isEdit,
				"modId": modId
			},
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					ui.success("保存成功！");
					getAreaList();
					$(".new-recognition-area").addClass("hide");
					//刷新筛选区
					if (areaJson.filterInfo) {
						pub.refreshFilter();
					}
					/*var newText = data.returndata,
						oldText;
					if(data.param2 !== ""){
						// 编辑
						oldText = data.param2.split("##")[0];
					}else{
						// 新增
						oldText = ss.getTextByCoordinate("1", data.param1);
					}
					if (oldText !== "") {
						newText = oldText + newText;
					}
					ss.setCellContent("1", newText, data.param1);*/
					ss.reload();
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	});

	//参数json串
	function setJson() {
		var wds = [],
			ifdiy = $("[name='wdItemType']:checked").val(),
			likeType = $("[name='likeType']:checked").val(),
			isFilter = $("[name='isFilter']:checked").val(),
			filterInfo = {};

		//自定义
		if (ifdiy === "1") {
			$("#weiduTreeList tr").each(function() {
				var code = $(".code", this).text(),
					ifprocode = false,
					name = $(".name", this).text(),
					diyname = $(".diyname", this).val();
				if ($(".code", this).next().text() === "+") {
					ifprocode = true;
					diyname = "";
				}
				wds.push({
					"code": code,
					"name": name,
					"nickname": diyname,
					"ifprocode": ifprocode
				});
			});

			if (!wds.length) {
				isNext = false;
				ui.alert("识别范围不能为空！！");
				return false;
			} else {
				isNext = true;
			}
		} else {
			isNext = true;
		}
		//filter
		if (isFilter === "1") {
			var filterName = $("[name='filterName']").val(),
				oldFilterName = $("[name='filterName']").attr("oldName"),
				farea = $("[name='wdItemType2']:checked").val(),
				fwd = [];
			if ($.trim(filterName) === "") {
				ui.alert("筛选名称不能为空！！");
				isNext = false;
				return false;
			} else {
				isNext = true;
			}
			if (filterName !== oldFilterName) {
				$.ajax({
					url: rootPath + "/templatecenter/DistinguishArea?m=checkFname&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + filterName,
					type: "post",
					async: false,
					dataType: "json",
					success: function(data) {
						if (data.returncode === 500) {
							ui.error(data.returndata);
							isNext = false;
							return false;
						}
					}
				});
			}
			if (farea === "0") {
				fwd = wds;
			} else if (farea === "2") {
				$("#filterTreeList tr").each(function() {
					var code = $(".code", this).text(),
						ifprocode = false,
						name = $(".name", this).text(),
						diyname = $(".diyname", this).val();
					if ($(".code", this).next().text() === "+") {
						ifprocode = true;
						diyname = "";
					}
					fwd.push({
						"code": code,
						"name": name,
						"nickname": diyname,
						"ifprocode": ifprocode
					});
				});

				if (!fwd.length) {
					isNext = false;
					ui.alert("筛选范围不能为空！！");
					return false;
				} else {
					isNext = true;
				}
			}
			var itype = $("[name='inputArg']:checked").val(),
				ival = $("#inputArg" + itype).val(),
				otype = $("[name='outArg']:checked").val(),
				oval = $("#outArg" + otype).val(),
				oname = "";
			if (oval === "") {
				ui.alert("输出参数不能为空！！");
				isNext = false;
				return false;
			} else {
				isNext = true;
			}

			$("#outArgSelect > a").each(function() {
				oname += $(this).text() + ",";
			});
			oname = oname.substring(0, oname.length - 1);

			filterInfo = {
				"fname": filterName,
				"farea": farea,
				"fwd": fwd,
				"itype": itype,
				"ival": ival,
				"otype": otype,
				"oval": oval,
				"oname": oname
			};
		} else {
			isNext = true;
		}
		areaJson = {
			"wdflag": $("[name='wdType']").val(),
			"ifdiy": ifdiy,
			"wds": wds,
			"likeType": likeType
		};
		if (isFilter === "1") {
			areaJson.filterInfo = filterInfo;
		}
	}

	function getAreaList() {
		$.ajax({
			url: rootPath + "/templatecenter/DistinguishArea.htm?m=find&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			dataType: "json",
			success: succ
		});

		function succ(data) {
			if (data.returncode === 200) {
				var list = JSON.parse(data.returndata);
				$("#recognition-area-list").empty();

				for (var i = 0; i < list.length; i++) {
					var type = list[i].ainfo.positionType,
						areaText = "";
					if (type === "0") {
						areaText = list[i].ainfo.colStartIndex + ':' + list[i].ainfo.colEndIndex;
					} else if (type === "1") {
						areaText = '[' + list[i].ainfo.rowStartIndex + ',' + list[i].ainfo.rowEndIndex + '],[' + list[i].ainfo.svalue + ',' + list[i].ainfo.evalue + ']';
					} else { //type===2
						areaText = '[' + list[i].ainfo.colStartIndex + ',' + list[i].ainfo.colEndIndex + '],[' + list[i].ainfo.svalue + ',' + list[i].ainfo.evalue + ']';
					}
					var dataJson = {
						"name": list[i].name,
						"areaText": areaText
					};

					var listhtml = Handlebars.compile($('#areaList').html())(dataJson);
					var table = $(listhtml);
					$("#recognition-area-list").append(table);
					table.data("info", list[i]); //保存所有信息到data对象上，便于编辑
				}
			}
		}
	}

	function removeArea(name) {
		$.ajax({
			url: rootPath + "/templatecenter/DistinguishArea.htm?m=del&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + name,
			type: "post",
			dataType: "json",
			success: function(data) {
				if (data.returncode === 200) {
					if(data.param1 !== ""){
						ss.reload();
						//ss.setCellContent("1", data.param1.split("##")[0], data.param1.split("##")[1]);
					}
					ui.success(data.returndata);
					getAreaList();
					//刷新筛选区
					pub.refreshFilter();
				} else {
					ui.alert(data.returndata);
				}
			}
		});
	}

	function initArgumentModal() {
		//默认功能
		$(".modal .checkbox").checkbox();
		$('.modal .dropdown.public').dropdown({
			selector: {
				item: '.item'
			}
		});
		//维度切换
		$('#isAllWeidu .dropdown.public').dropdown({
			selector: {
				item: '.item'
			},
			onChange: function() {
				var wd = $("[name=wdType]").val();
				pub.initArgTree(wd);

				//clear table
				$("#filterTreeList tbody, #weiduTreeList tbody").empty();
				//outArg
				setTimeout(getOutArgDropdown, 300);
				//getOutArgDropdown();
			}
		});

		$("#isAllWeidu .checkbox").checkbox({
			onChange: function() {
				$(".weidu-box-one").toggleClass("active");
				$('#modal').modal("refresh");
				//outArg
				getOutArgDropdown();
			}
		});
		$("#isFilter .checkbox").checkbox({
			onChange: function() {
				$(".filter-box").toggleClass("active");
				$('#modal').modal("refresh");
				//outArg
				getOutArgDropdown();
			}
		});
		$("#likeType .checkbox").checkbox();
		//筛选范围
		$("#filterArea .checkbox").checkbox({
			onChange: function(value) {
				if ($(this).val() === "2") {
					$(".weidu-box-two").addClass("active");
				} else {
					$(".weidu-box-two").removeClass("active");
				}
				$('#modal').modal("refresh");
				//outArg
				getOutArgDropdown();
			}
		});
		//addCode、treeNode
		$("#onceItem").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("weiduTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#weiduTreeList"), nodes[0]);
			//outArg
			getOutArgDropdown();
		});
		$("#procodeAllItem").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("weiduTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#weiduTreeList"), nodes[0], true);
			//outArg
			getOutArgDropdown();
		});

		$("#onceItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("weiduTree2");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0]);
			//outArg
			getOutArgDropdown();
		});
		$("#procodeAllItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("weiduTree2");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0], true);
			//outArg
			getOutArgDropdown();
		});
		//remove
		$("#filterTreeList .remove, #weiduTreeList .remove").live("click", function() {
			$(this).parents("tr").remove();
			//outArg
			getOutArgDropdown();
		});
		getOutArgDropdown();
	}

	//输出参数下拉列表实时刷新
	function getOutArgDropdown() {
		var isFilter = $("[name='isFilter']:checked").val(),
			wd = $("[name='wdType']").val(),
			wdType = $("[name='wdItemType']:checked").val(),
			fType = $("[name='wdItemType2']:checked").val(),
			nodes = [];
		if (isFilter === "1") {
			if (fType === "0") {
				if (wdType === "1") {
					$("#weiduTreeList tr").each(function() {
						var code = $(".code", this).text(),
							name = $(".name", this).text(),
							diyNode = {
								"isParent": true,
								"id": code,
								"name": name,
								"pId": ""
							};
						if ($(".code", this).next().text() !== "+") {
							diyNode.isParent = false;
						}
						nodes.push(diyNode);
					});
				}else{
					nodes = $("#weiduTree").data("itree");
				}
			} else if (fType === "1") {
				nodes = $("#weiduTree").data("itree");
			} else if (fType === "2") {
				$("#filterTreeList tr").each(function() {
					var code = $(".code", this).text(),
						name = $(".name", this).text(),
						diyNode = {
							"isParent": true,
							"id": code,
							"name": name,
							"pId": ""
						};
					if ($(".code", this).next().text() !== "+") {
						diyNode.isParent = false;
					}
					nodes.push(diyNode);
				});
			}
		}

		var setting = {
			async: {
				enable: true,
				url: rootPath + '/templatecenter/TemplateHandel.htm?m=getResultLeftTree&wd=' + wd,
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
				onClick: outArgTreeclick
			}
		};

		function outArgTreeclick(event, treeId, treeNode) {
			if (treeNode.ifData === "0") {
				$(event.target).parent().addClass("disabled");
				ui.alert("分类不能选！");
				return false;
			}
			var currentNodeId, node;
			currentNodeId = treeNode.tId + '_a';
			node = $('#' + currentNodeId);
			node.data('value', treeNode.id);
		}
		if (nodes.length) {
			$.fn.zTree.init($("#outArgTree"), setting, nodes);
		} else {
			$("#outArgTree").empty();
		}

		$("#outArgSelect.multiple.dropdown").dropdown({
			preserveHTML: false,
			treeWidget: 'outArgTree',
			selector: {
				item: '.item > label, #outArgTree li a:not(.disabled)'
			}
		});
		$("> .label > .delete.icon", $("#outArgSelect")).trigger("click.dropdown");
	}
});