define(function(require) {
	var $ = require('$'),
		Handlebars = require('handlebars'),
		cTips = require('cTips'),
		ui = require('alert'),
		pub = require('publicfn'),
		rootPath = require('rootPath'),
		modId = $('#modId').val(),
		sortType = $("#timeTreeType").val(),
		ss = require('excel'),
		doc = $(document),
		step;
	require('semantic');
	require('tools');
	require('dimension');
	require('synonym');
	pub.initDTC();

	var isSetWeidu = false, //主宾栏未设置之前，点击单元格不发请求（查询属性面板）
		prevClickCell; //保存上次点击的单元格位置


	$(window).resize(function() {
		sstab();
	});
	sstab();

	function sstab() {
		var tabsHeight = $(".J-label-tab").height(),
			tabsContentHeight = $(".weidu-trees").height();
		if (tabsContentHeight < tabsHeight) {
			$(".upordown").show();
		} else {
			$(".upordown").hide();
			$(".J-label-tab").css({ "top": 0, bottom: "auto" });
		}
	}
	doc.on('click', '.back-up', function() {
		$(".J-label-tab").css({ "top": 0, bottom: "auto" });
	});
	doc.on('click', '.back-down', function() {
		$(".J-label-tab").css({ "top": "auto", bottom: 15 });
	});

	//主宾栏选取单元格
	(function() {
		pub.selRange($(".excel-select0"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".selText0").val(range).data("posi", e.point);
		});
		pub.selRange($(".excel-select1"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".selText1").val(range).data("posi", e.point);
		});
		pub.selRange($(".excel-select2"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".selText2").val(range).data("posi", e.point);
		});
	})();
	//主栏行标记
	doc.on("click", "#rowSignBtn1", function() {
		pub.selRange($("#rowNumber1"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.row[0] + ":" + e.point.row[rowLen - 1];
			$("#rowindex1").val(range).data("posi", e.point);
		});
	});
	//宾栏列标记
	doc.on("click", "#colSignBtn1", function() {
		pub.selRange($("#colNumber1"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + ":" + e.point.col[colLen - 1];
			$("#colindex1").val(range).data("posi", e.point);
		});
	});

	//测试
	doc.on("click", "#preview", function() {
		step = ss.getStep();
		$.ajax({
			url: rootPath + '/dataload/datamanageload.htm?m=validateMod&step=' + step +'&excelId=' + window.SPREADSHEET_AUTHENTIC_KEY + "&sort=" + sortType,
			type: 'post',
			dataType: "json",
			beforeSend: function() {
				$("body").append('<div class="ui active dimmer piDimmer"><div class="ui loader"></div></div>');
			},
			success: function(data) {
				$(".piDimmer.ui.dimmer").remove();
				setTimeout(function(){
					if (data.returncode === 200) {
						ui.success("维度校验成功");
					}else{
						ui.error(data.returndata);
					}
				},2000);
				ss.reload(); //重新渲染
			}
		});
	});

	//提交
	doc.on("click", "#saveTempTemplate", function() {
		step = ss.getStep();
		$.ajax({
			url: rootPath + '/dataload/datamanageload.htm?m=validateMod&step=' + step +'&excelId=' + window.SPREADSHEET_AUTHENTIC_KEY + "&sort=" + sortType,
			type: 'post',
			dataType: "json",
			beforeSend: function() {
				$("body").append('<div class="ui active dimmer piDimmer"><div class="ui loader"></div></div>');
			},
			success: function(data) {
				$(".piDimmer.ui.dimmer").remove();
				if (data.returncode === 200) {
					$.ajax({
						url: rootPath + '/dataload/datamanageload.htm?m=submitToDraft&excelId=' + window.SPREADSHEET_AUTHENTIC_KEY + "&taskCode=" + modId + "&sort=" + sortType,
						type: 'post',
						dataType: "json",
						success: function(d) {
							if (d.returncode === 200) {
								ui.success("提交成功！");
								var html = Handlebars.compile($('#confirm-modal').html())({
									"title": "是否直接进入校验？",
									"iconClass": "search"
								});
								$('#modal2').empty().append(html).modal({
									closable: false,
									onApprove: function() {
										location.href = rootPath + '/dataload/datamanageload.htm?m=checkTableCss&id=' + modId + '&state=1';
									},
									onDeny: function() {
										location.href = rootPath + '/dataload/datamanageload.htm';
									}
								}).modal('show');
							}else{
								 ui.error(data.returndata);
							}
						}
					});
				} else{
					ui.error(data.returndata);
				}
				ss.reload(); //重新渲染
			}
		});
	});

	//返回
	doc.on("click", "#goBack", function() {
		location.href = document.referrer;
	});

	//切换到维度
	doc.on("click", "#changeToWeidu", function() {
		var weiduIndexInfo = {},
			zhuType = $("[name='firstItem']").val(),
			binType = $("[name='secondItem']").val(),
			stype,
			etype,
			colLen,
			rowLen;
		if (zhuType === "first0") {
			var zhu = $(".selText1").data("posi");
			if ($(".selText1").val() !== "") {
				colLen = zhu.col.length;
				rowLen = zhu.row.length;
				weiduIndexInfo.zhu = {
					"colStartIndex": zhu.col[0],
					"colEndIndex": zhu.col[colLen - 1],
					"rowStartIndex": zhu.row[0],
					"rowEndIndex": zhu.row[rowLen - 1],
					"positionType": 0
				};
			} else {
				ui.alert("未设置主栏！！");
				return false;
			}
		} else {
			if ($("#rowindex1").val() !== "") {
				weiduIndexInfo.zhu = {
					"rowStartIndex": $("#rowindex1").val().split(":")[0],
					"rowEndIndex": $("#rowindex1").val().split(":")[1],
					"positionType": 1
				};
				stype = $("[name='startcol1']:checked").val();
				etype = $("[name='endcol1']:checked").val();
				if (stype === "0") {
					weiduIndexInfo.zhu.startColText = $("#startcol1").val();
				} else {
					weiduIndexInfo.zhu.colStartIndex = $("#startcol1").val();
				}
				if (etype === "0") {
					weiduIndexInfo.zhu.endColText = $("#endcol1").val();
				} else {
					weiduIndexInfo.zhu.colEndIndex = $("#endcol1").val();
				}
			} else {
				ui.alert("未设置主栏！！");
				return false;
			}
		}

		if (binType === "second0") {
			var bin = $(".selText2").data("posi");
			if ($(".selText2").val() !== "") {
				colLen = bin.col.length;
				rowLen = bin.row.length;
				weiduIndexInfo.bin = {
					"colStartIndex": bin.col[0],
					"colEndIndex": bin.col[colLen - 1],
					"rowStartIndex": bin.row[0],
					"rowEndIndex": bin.row[rowLen - 1],
					"positionType": 0
				};
			} else {
				ui.alert("未设置宾栏！！");
				return false;
			}
		} else {
			if ($("#colindex1").val() !== "") {
				weiduIndexInfo.bin = {
					"colStartIndex": $("#colindex1").val().split(":")[0],
					"colEndIndex": $("#colindex1").val().split(":")[1],
					"positionType": 1
				};
				stype = $("[name='startrow1']:checked").val();
				etype = $("[name='endrow1']:checked").val();
				if (stype === "0") {
					weiduIndexInfo.bin.startRowText = $("#startrow1").val();
				} else {
					weiduIndexInfo.bin.rowStartIndex = $("#startrow1").val();
				}
				if (etype === "0") {
					weiduIndexInfo.bin.endRowText = $("#endrow1").val();
				} else {
					weiduIndexInfo.bin.rowEndIndex = $("#endrow1").val();
				}
			} else {
				ui.alert("未设置宾栏！！");
				return false;
			}
		}
		var title = $(".selText0").data("posi"),
			unit = $(".selText3").data("posi"),
			exps = $(".selText4").data("posi");
		if (title) {
			colLen = title.col.length;
			rowLen = title.row.length;
			weiduIndexInfo.title = {
				"colStartIndex": title.col[0],
				"colEndIndex": title.col[colLen - 1],
				"rowStartIndex": title.row[0],
				"rowEndIndex": title.row[rowLen - 1]
			};
		}
		if (unit) {
			colLen = unit.col.length;
			rowLen = unit.row.length;
			weiduIndexInfo.unit = {
				"colStartIndex": unit.col[0],
				"colEndIndex": unit.col[colLen - 1],
				"rowStartIndex": unit.row[0],
				"rowEndIndex": unit.row[rowLen - 1]
			};
		}
		if (exps) {
			colLen = exps.col.length;
			rowLen = exps.row.length;
			weiduIndexInfo.exps = {
				"colStartIndex": exps.col[0],
				"colEndIndex": exps.col[colLen - 1],
				"rowStartIndex": exps.row[0],
				"rowEndIndex": exps.row[rowLen - 1]
			};
		}

		$.ajax({
			url: rootPath + "/marker/minimarker.htm?m=add&sort=" + sortType + "&modId=" + modId+'&step='+ss.getStep(),
			type: "post",
			timeout: 10000,
			data: {
				"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
				"datas": JSON.stringify(weiduIndexInfo)
			},
			async: false,
			dataType: "json",
			success: function(d) {
				if (d.returncode === 200) {
					$(".weidu-index").addClass("hidden");
					$(".attr-panel").show();
					$("#attr-panel-btn input").attr("checked", true);
					$(".excel-panel").css({"bottom": 160, "height": "auto"});
					var h = $(".excel-panel").height();
					$(".excel-panel").css("height", h);
					$('#show-colors-btn input').attr('checked', true);
					$('[data-toolbar=background],[data-toolbar=color]').addClass('disable'); //禁用颜色按钮
					ss.adaptScreen();
					ss.reload(); //重新渲染

					isSetWeidu = true;
					$("#rootPath").data("isSetWeidu", isSetWeidu); //识别区是否可以点击
					$(".hasMainAndGuest").show(); //设置完主宾栏后显示测试和提交按钮

					//自动匹配模态框
					var html = Handlebars.compile($('#confirm-find').html())({
						zb_main: "checked",
						fl_main: "checked",
						reg_guest: "checked",
						sj_guest: "checked",
						tt_main: "checked",
						unit_main: "checked"
					});
					$('#modal').empty().append(html).modal({
						closable: false,
						onShow: function() {
							myFindTree('zb');
							myFindTree('fl');
							myFindTree('reg');
							myFindTree('unit');
						},
						onApprove: function() {
							var trs = $('#findTableContent tbody tr'),
								datas = [];
							trs.each(function() {
								var td = $('td', this),
									isMain = td.eq(2).find('input').is(':checked') ? '1' : '0',
									isGuest = td.eq(3).find('input').is(':checked') ? '1' : '0';
								datas.push({
									'item': td.eq(1).find('input').attr('item'),
									'range': td.eq(1).find('input').val(),
									'posi': td.eq(1).find('input').attr('level'),
									'main': isMain,
									'guest': isGuest
								});
							});

							$.ajax({
								url: rootPath + '/marker/minimarker.htm?m=autod&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY + "&sort=" + sortType,
								type: 'post',
								data: {
									"datas": JSON.stringify(datas)
								},
								dataType: "json",
								beforeSend: function() {
									$("body").append('<div class="ui active dimmer piDimmer"><div class="ui loader"></div></div>');
								},
								success: function(data) {
									if (data.returncode === 200) {
										$(".piDimmer.ui.dimmer").remove();
										ss.reload(); //重新渲染
									}
								},
								error: function() {
									ui.error('服务器错误！');
									$(".piDimmer.ui.dimmer").remove();
								}
							});
						}
					}).modal('show');
				}
			}
		});
	});

	function myFindTree(wd) {
		var setting = {
			async: {
				enable: true,
				url: rootPath + '/search/searchaction.htm?m=getTree&wdcode=' + wd,
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
				onClick: diyCheck
			}
		};

		function diyCheck(event, treeId, treeNode) {
			var currentNodeId, node;
			currentNodeId = treeNode.tId + '_a';
			node = $('#' + currentNodeId);
			node.data('value', treeNode.id);
			$('[item=' + wd + ']').attr('level', treeNode.level);
		}

		$.ajax({
			url: rootPath + '/search/searchaction.htm?m=getTree&wdcode=' + wd,
			type: 'post',
			dataType: "json",
			success: function(data) {
				data.unshift({
					"checked": false,
					"isParent": false,
					"name": "使用该维度下所有项目",
					"id": "",
					"pid": ""
				});
				$.fn.zTree.init($("#" + wd + "_findTree"), setting, data);
				$("#" + wd + "_find.dropdown").dropdown({
					preserveHTML: false,
					treeWidget: wd + '_findTree',
					selector: {
						item: '.item > label, #' + wd + '_findTree li a:not(.disabled)'
					}
				});
			}
		});
	}

	//切换到位置信息
	doc.on("click", "#changeToPosi", function() {
		var backHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "切回位置信息面板会清除主宾栏设置！",
			"iconClass": "warning sign",
			"hideClass": ""
		});
		$('#modal2').empty().append(backHtml).modal({
			closable: false,
			onApprove: function() {
				$.ajax({
					url: rootPath + '/marker/minimarker.htm?m=removeMarker&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY + "&sort=" + sortType,
					type: 'post',
					async: false,
					dataType: "json",
					success: function(data) {
						if (data.returncode === 200) {
							$(".weidu-index").removeClass("hidden");
							$(".attr-panel").hide();
							$("#attr-panel-btn input").attr("checked", false);
							$(".excel-panel").css({"bottom": 0, "height": "auto"});
							var h = $(".excel-panel").height();
							$(".excel-panel").css("height", h);
							$('#show-colors-btn input').attr('checked', false);
							$('[data-toolbar=background],[data-toolbar=color]').removeClass('disable'); //解除禁用颜色按钮
							$(".selText0,.selText1,.selText2").val('');
							ss.adaptScreen();
							ss.reload(); //重新渲染
							isSetWeidu = false; //主宾栏变为未设置，点击单元格不发请求（查询属性面板）
							$("#rootPath").data("isSetWeidu", isSetWeidu); //识别区不可以点击
							$(".hasMainAndGuest").hide(); //清除主宾栏后隐藏测试和提交按钮
							$('#selectTreeNodeId').val('');
							$('#selectTreeNodeName').val('');
							$('#selectTreeWd').val('');
						}
					}
				});
			}
		}).modal('show');
	});

	//切换到维度，需要隐藏的面板
	doc.on("click", "#weidu-panel-btn", function() {
		$(".floats").addClass("hidden");
	});

	//显示辅助色
	$("#show-colors-btn.checkbox").checkbox({
		onChange: function() {
			var showcolor = $(this).is(":checked"),
				showcode = $("#show-code-btn input").is(":checked"),
				type = '';
			if (showcode) {
				type += '1';
			} else {
				type += '0';
			}
			if (showcolor) {
				type += '1';
				$('[data-toolbar=background],[data-toolbar=color]').addClass('disable'); //禁用颜色按钮
			} else {
				type += '0';
				$('[data-toolbar=background],[data-toolbar=color]').removeClass('disable'); //解除禁用颜色按钮
			}
			changeColorCode(type);
		}
	});

	//默认隐藏代码
	hideCode();

	//显示、隐藏代码
	$("#show-code-btn.checkbox").checkbox({
		onChange: function() {
			var showcolor = $("#show-colors-btn input").is(":checked"),
				showcode = $(this).is(":checked"),
				type = '';
			if (showcode) {
				type += '1';
				//  showCode();
			} else {
				type += '0';
				//  hideCode();
			}
			if (showcolor) {
				type += '1';
			} else {
				type += '0';
			}
			if (showcode) {
				showCode();
			} else {
				hideCode();
			}
			changeColorCode(type);
		}
	});

	function showCode() {
		ss.setRowHeight("1", "2", 20);
		ss.setRowHeight("1", "1", 20);
		ss.setColWidth("1", "A", 70);
	}

	function hideCode() {
		ss.setRowHeight("1", "2", 0);
		ss.setRowHeight("1", "1", 0);
		ss.setColWidth("1", "A", 0);
	}

	function changeColorCode(type) {
		step = ss.getStep();
		$.ajax({
			url: rootPath + '/marker/minimarker.htm?m=colorChange&step=' + step + '&type=' + type + '&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
			type: 'post',
			async: false,
			timeout: 10000,
			success: function(data) {
				if (data.returncode === 200) {
					
				}else{
					cTips.commonTips(data.returndata);
				}
				ss.reload(); //重新渲染
			}
		});
	}

	//隐藏侧边栏
	$(".show-panel-btn").toggle(function() {
		$(".weidu-panel").css("left", -290);
		$(".main-content").css("left", 10);
		$(".icon", this).removeClass("left").addClass("right");
		ss.adaptScreen();
	}, function() {
		$(".weidu-panel").css("left", 0);
		$(".main-content").css("left", 300);
		$(".icon", this).removeClass("right").addClass("left");
		ss.adaptScreen();
	});

	//显示or隐藏属性面板
	$("#attr-panel-btn.checkbox").checkbox({
		onChange: function() {
			var show = $(this).is(":checked"),
				height;
			if (show) {
				$(".attr-panel").show();
				$(".excel-panel").css({"bottom": 160, "height": "auto"});
				height = $(".excel-panel").height();
				$(".excel-panel").css("height", height);
				ss.adaptScreen();
			} else {
				$(".attr-panel").hide();
				$(".excel-panel").css({"bottom": 0, "height": "auto"});
				height = $(".excel-panel").height();
				$(".excel-panel").css("height", height);
				ss.adaptScreen();
			}
		}
	});

	//删除维度属性
	doc.on("click", ".attr-panel .remove", function() {
		var parent = $(this).parent(),
			cell = parent.data("posi"),
			code = parent.data("code"),
			isSelf = parent.data("isself"),
			wdType = parent.data("dimension"),
			pos = parent.data("pos");
		var delHtml = Handlebars.compile($('#confirm-modal').html())({
			"title": "确认要删除此属性？",
			"iconClass": "trash",
			"hideClass": ""
		});
		$('#modal2').empty().append(delHtml).modal({
			closable: false,
			onApprove: function() {
				$.ajax({
					url: rootPath + '/templatecenter/PropertyPanel.htm?m=minidel&modId=' + modId,
					type: 'post',
					data: {
						"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
						"cell": cell,
						"delWdType": wdType,
						"delCode": code,
						"isSelfCode": isSelf,
						"delPos": pos
					},
					dataType: "json",
					success: function(data) {
						if (data.returncode === 200) {
							var cell = data.returndata.split("==")[0],
								text = data.returndata.split("==")[1],
								showcode = $("#show-code-btn input").is(":checked");
							if (showcode) {
								ss.setCellContent("1", text, cell);
							}
							setTimeout(function() {
								handler(prevClickCell);
							}, 400);
							cTips.commonTips("删除成功！");
						}else{
							cTips.commonTips(data.returndata);
						}
					}
				});
			}
		}).modal('show');
	});

	// dimension tabs list
	$('.custom-menu .custom-item').tab();
	// initialize dimension tree
	/**
	 * @param {array} initialize display node array
	 * @param {string} point position for menu tree mount
	 */
	function initTree(baseNode, posi) {
		var curMenu,
			zTree_Menu,
			setting = {
				edit: {
					drag: {
						prev: false,
						inner: false,
						next: false
					},
					enable: true,
					showRemoveBtn: false,
					showRenameBtn: false
				},
				async: {
					enable: true,
					url: rootPath + '/templatecenter/templatehandel.htm?m=getResultLeftTree&modId=' + modId + '&wd=' + posi,
					contentType: 'application/json',
					type: 'get',
					autoParam: ["id"]
				},
				data: {
					simpleData: {
						enable: true
					},
					key: {
						title: 'path'
					}
				},
				callback: {
					onDrop: posiDrop,
					beforeDrag: beforeDrag,
					onClick:recordNode
				}
			};
		return $.fn.zTree.init($("#tree-" + posi), setting, baseNode);
	}
	function recordNode(event, treeid, treeNode){
		if(treeNode.ifData === "0"){
			$('#selectTreeNodeId').val('');
			$('#selectTreeNodeName').val('');
			$('#selectTreeWd').val('');
		}else{
			$('#selectTreeNodeId').val(treeNode.id);
			$('#selectTreeNodeName').val(treeNode.name);
			$('#selectTreeWd').val(treeNode.p1);
		}
	}

	function beforeDrag(treeId, treeNodes) {
		for (var i = 0, l = treeNodes.length; i < l; i++) {
			if (treeNodes[i].drag === false) {
				return false;
			}
		}
	}

	function posiDrop(event, treeId, treeNodes, targetNode, moveType) {
		var currPoint, coordinate, posiInfo;
		currPoint = ss.getPointByPosi('1', event.clientX, event.clientY);
		coordinate = currPoint.point.col + currPoint.point.row;
		asyncGetPosiInfo(coordinate, setPosi);

		function setPosi(posiInfo) {
			var posi = posiInfo.returndata,
				oldText, newText, wdtype,
				row, col, coordinateList = [],
				current, i;
			switch (posi) {
				case 'main':
					row = '2';
					for (i = 0; i < posiInfo.param2.length; i++) {
						current = posiInfo.param2[i] + row;
						coordinateList.push(current);
					}
					break;
				case 'guest':
					col = 'A';
					for (i = 0; i < posiInfo.param1.length; i++) {
						current = col + posiInfo.param1[i];
						coordinateList.push(current);
					}
					break;
				case 'datapos':
					coordinateList.push(coordinate);
					break;
				case 'normal':
					//coordinateList.push(coordinate);
					break;
				case 'global':
					col = 'A';
					row = '2';
					coordinateList.push(col + row);
					break;
			}
			if (!coordinateList.length) return;
			newText = treeNodes[0].id;
			wdtype = $('#' + treeId).data('id');
			$.ajax({
				url: rootPath + '/marker/minimarker.htm?m=codeDrag&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
				data: {
					cell: coordinateList.toString(),
					code: newText,
					type: wdtype
				},
				type: 'post',
				success: function(data) {
					var showcode = $("#show-code-btn input").is(":checked");
					if (data.returncode === 200 && showcode) {
						for (i = 0; i < coordinateList.length; i++) {
							oldText = ss.getTextByCoordinate("1", coordinateList[i]);
							newText = oldText + $('#' + treeId).data('id') + '_' + treeNodes[0].id + ';';
							ss.setCellContent("1", newText, coordinateList[i]);
						}
					}
				}
			});

		}
	}

	function asyncGetPosiInfo(coordinate, succevent) {
		$.ajax({
			url: rootPath + '/marker/minimarker.htm?m=checkcell&sort=' + sortType + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY + '&cell=' + coordinate,
			type: 'post',
			success: succevent
		});
	}

	var treeList = [];

	function initializeDimensionTree() {
		var returnTree;
		$(".weidu-tree-boxs .ui.tab > div").each(function() {
			var wd = $(this).data("id");
			returnTree = initTree(null, wd);
			treeList.push(returnTree);
		});
	}
	initializeDimensionTree();

	ss.addEventListener('mousedown', function(e) {
		//get currenttarget property
		var cell = e.point.col[0] + e.point.row[0];
		if (!isSetWeidu || e.point.row[0] <= 2 || e.point.col[0] === "A") {
			return false;
		}
		prevClickCell = cell;
		handler(cell);
	});
	ss.addEventListener('mousedown', function(e) {
		//search information
		var cell = e.point.col[0] + e.point.row[0];
		if (!isSetWeidu || e.point.row[0] <= 2 || e.point.col[0] === "A") {
			return false;
		}
		asyncGetPosiInfo(cell, searchInfo);
		$(".attr-panel i.add").attr("cell", cell);

		function searchInfo(posiInfo) {
			var posi = posiInfo.returndata;
			switch (posi) {
				case 'main':
				case 'guest':
					$('.J-dimension-search input').val(e.text);
					$('.J-dimension-search').trigger('submit');
					break;
			}
		}
	});

	function handler(cell) {
		$.ajax({
			url: rootPath + '/templatecenter/PropertyPanel.htm?m=minishow&sort=' + sortType + '&cell=' + cell + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
			type: 'post',
			dateType: 'json',
			success: analyticsData,
			complete: function() {
				$('.J-dimension-search').trigger('submit');
			}
		});

		function analyticsData(data) {
			//var dimensionList = ['zb', 'fl', 'reg', 'tt', 'sj', 'ds', 'unit'],
			var dimensionList = $('#wdlist').val().split(','),
				djson = {},
				len = 0,
				i, j, tempLen,
				tempPropertyArray,
				templateID = '#proplist',
				template,
				currentProperty;
			if (data.returncode == 600) {
				cTips.commonTips(data.returndata);
				return;
			}
			if (data.returncode !== 200) {
				return;
			}
			for (i = 0; i < data.returndata.list.length; i++) {
				djson[data.returndata.list[i].code] = data.returndata.list[i].list;
			}
			len = dimensionList.length;
			for (i = len - 1; i >= 0; i--) {
				tempPropertyArray = djson[dimensionList[i]];
				tempLen = tempPropertyArray.length;
				$('.J-attr-' + dimensionList[i]).empty();
				if (tempLen) {
					for (j = tempLen - 1; j >= 0; j--) {
						template = Handlebars.compile($(templateID).html());
						currentProperty = tempPropertyArray[j];
						currentProperty.alias = dimensionList[i];
						$('.J-attr-' + dimensionList[i]).append(template(currentProperty));
					}
				}
			}
		}
	}
	$('.J-dimension-search').on('submit', function(e) {
		var searchValue;
		e.preventDefault();
		searchValue = $('input', this).val().trim();
		if (searchValue === '') {
			return;
		}
		$.ajax({
			url: rootPath + '/search/searchaction.htm?m=query&modId=' + modId + '&s=' + (searchValue.replace("%", "%25")).replace("#", "") + "&sort=" + sortType,
			type: 'post',
			beforeSend: function() {
				$('.J-input-container > i').remove();
				$('.J-input-container').addClass('loading');
				$('.J-input-container').append('<i class="search icon"></i>');
			},
			success: handleSuccess,
			error: function() {
				return;
			},
			complete: function() {
				$('.J-input-container > i').remove();
				$('.J-input-container').removeClass('loading');
				$('.J-input-container').append('<i class="remove link icon J-remove-search"></i>');
			}
		});


		function handleSuccess(result) {
			var i = -1,
				dataDimension, dataDimensionLen,
				data = result.returndata;
			removeResult();
			/**
			 * display search result nu9mber of every dimension
			 */
			$(".weidu-tree-boxs .ui.tab > div").each(function() {
				i++;
				var wd = $(this).data("id");
				dataDimension = data[wd];
				dataDimensionLen = dataDimension.length;
				if (dataDimension instanceof Array && dataDimensionLen) {
					$('.J-label-' + wd + '> div').text(dataDimensionLen).removeClass('hidden');
					resultTree();
				}
			});
			sstab();

			/**
			 * addtion to every tree by diff dimension tree result
			 */
			function resultTree() {
				var tmpResultList = [{
						name: '推荐结果',
						drag: false,
						id: 'searchresult',
						pid: 0,
						isParent: true,
						open: true
					}],
					j = 0;
				for (; j < dataDimensionLen; j++) {
					tmpResultList.push(dataDimension[j]);
				}
				treeList[i].addNodes(null, 0, tmpResultList);
			}
		}
	});
	doc.on('click', '.J-remove-search', function(e) {
		$(this).siblings('input').val('');
		removeResult();
		sstab();
	});

	function removeResult() {
		$('.J-label-tab .custom-label').addClass('hidden');
		var currentNodes,
			nodeLen,
			x;

		$(".weidu-tree-boxs .ui.tab > div").each(function(i) {
			currentNodes = treeList[i].getNodesByParam('id', 'searchresult');
			nodeLen = currentNodes.length;
			for (x = nodeLen - 1; x >= 0; x--) {
				treeList[i].removeNode(currentNodes[x]);
			}
		});
	}
});
