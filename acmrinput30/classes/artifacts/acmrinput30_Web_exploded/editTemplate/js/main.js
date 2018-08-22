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
	require('fileupload');
	require('semantic');
	require('area'); //识别区
	require('figures'); //计算
	require('check'); //校验
	require('memo'); //备注
	require('tools');
	require('dimension');
	require('synonym');
	pub.initDTC();
	pub.refreshFilter();

	var isSetWeidu = false, //主宾栏未设置之前，点击单元格不发请求（查询属性面板）
		prevClickCell; //保存上次点击的单元格位置

	if ($("#updataTemplate").length && $("#updataTemplate").val() === '1') {
		isSetWeidu = true;
		$("#rootPath").data("isSetWeidu", true);
	}
	if ($("#hasTag").val()) {
		isSetWeidu = true;
		$("#rootPath").data("isSetWeidu", true);
	}

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

	//导入报表
	doc.on("click", "#import", function() {
		var uploadHtml = Handlebars.compile($('#uploadModal').html())({});
		$('#modal').empty().append(uploadHtml).modal({
			closable: false,
			onShow: function() {
				afterUpload();
				$('#templated.checkbox').checkbox({
					onChange: function() {
						var isTag = $(this).is(":checked") ? '1' : '0';
						$('#sheetList').attr('isTag', isTag);
					}
				});
			},
			onApprove: function() {
				$.ajax({
					url: rootPath + "/templatecenter/TemplateHandel.htm?m=uploadTemplate",
					data: {
						modId: modId,
						sortType: sortType,
						index: $('#sheetList').val(),
						cache: $('#sheetList').attr('cache'),
						isTag: $('#sheetList').attr('isTag')
					},
					dataType: 'json',
					type: 'post',
					success: function(data) {
						if (data.returncode === 200) {
							var isTag = $('#sheetList').attr('isTag');
							if (isTag === '1') {
								window.location = rootPath + "/templatecenter/TemplateHandel.htm?excelId=" + data.returndata + "&modId=" + data.param1 + "&sort=" + sortType + "&hastag=1";
							} else {
								window.location = rootPath + "/templatecenter/TemplateHandel.htm?excelId=" + data.returndata + "&modId=" + data.param1 + "&sort=" + sortType;
							}
						}
						if (data.returncode === 500 || data.returncode === 600) {
							ui.error(data.returndata);
						}
					},
					error: function() {
						ui.error('服务器错误！');
					}
				});
			}
		}).modal('show');
	});

	function afterUpload() {
		$('#J_fileupload').fileupload({
			url: rootPath + "/templatecenter/TemplateHandel.htm?m=uploadExcel&modId=" + modId + "&sortType=" + sortType,
			dataType: 'json',
			singleFileUploads: false,
			maxFileSize: 999000,
			done: function(e, data) {
				if (data.returncode === 200) {
					$('.ui.progress .bar').css('width', '100%');
				}
			},
			change: function(e, data) {
				$('.ui.progress .bar').removeAttr('style');
				$('.ui.progress .progress').html('0%');
			},
			success: function(data) {
				if (data.returncode === 200) {
					$('.ui.progress .bar').css('width', '100%');
					$('.ui.progress .progress').html('100%');
					$('#sheetList').attr('cache', data.param2).empty();
					$.each(data.param1, function(i) {
						$('#sheetList').append('<option value="' + data.param1[i].index + '">' + data.param1[i].name + '</option>');
					});
					$('#sheetList').parent().show();
					$('#modal').modal("refresh");
					//window.location = rootPath + "/excelindex.htm?excelId=" + data.returndata + "&modId=" + data.param1 + "&sort=" + sortType;
				}
			},
			progressall: function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('.ui.progress .bar').css('width', progress + '%');
				$('.ui.progress .progress').html(progress + '%');
			}
		});
	}

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
		pub.selRange($(".excel-select3"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".selText3").val(range).data("posi", e.point);
		});
		pub.selRange($(".excel-select4"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + e.point.row[0] + ":" + e.point.col[colLen - 1] + e.point.row[rowLen - 1];
			$(".selText4").val(range).data("posi", e.point);
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
	//识别区行标记
	doc.on("click", "#rowSignBtn2", function() {
		pub.selRange($("#rowNumber2"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.row[0] + ":" + e.point.row[rowLen - 1];
			$("#rowindex").val(range).data("posi", e.point);
		});
	});
	//识别区列标记
	doc.on("click", "#colSignBtn2", function() {
		pub.selRange($("#colNumber2"), function(e) {
			var colLen = e.point.col.length,
				rowLen = e.point.row.length,
				range = e.point.col[0] + ":" + e.point.col[colLen - 1];
			$("#colindex").val(range).data("posi", e.point);
		});
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
			url: rootPath + "/templatecenter/identifyArea.htm?m=add&modId=" + modId+"&step="+ss.getStep(),
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
								url: rootPath + '/templatecenter/IdentifyArea.htm?m=autod&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY + "&sort=" + sortType,
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
				url: rootPath + '/templatecenter/templatehandel.htm?m=getTree&wdcode=' + wd,
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
			url: rootPath + '/templatecenter/templatehandel.htm?m=getTree&wdcode=' + wd,
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
					url: rootPath + '/templatecenter/IdentifyArea.htm?m=removeMarker&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
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
							$(".selText0,.selText1,.selText2,.selText3,.selText4").val('');
							ss.adaptScreen();
							ss.reload(); //重新渲染
							isSetWeidu = false; //主宾栏变为未设置，点击单元格不发请求（查询属性面板）
							$("#rootPath").data("isSetWeidu", isSetWeidu); //识别区不可以点击
							pub.refreshFilter();
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
			url: rootPath + '/templatecenter/IdentifyArea.htm?m=colorChange&step=' + step + '&type=' + type + '&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
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

	//编辑维度属性
	doc.on("click", ".attr-panel a.label", function() {
		var parent = $(this).parent(),
			wdType = parent.data("dimension"),
			cell = parent.data("posi"),
			code = parent.data("code"),
			cname = $(this).attr("cname"),
			ifEdit = parent.data("ifedit"),
			isself = parent.data("isself"),
			isf = parent.data("isf"),
			data = {},
			template = {},
			dd = new Date();
		if (!ifEdit) {
			return false;
		}
		//如果是filter就发送请求
		if (isf) {
			$.ajax({
				url: rootPath + '/templatecenter/PropertyPanel.htm?m=findById&modId=' + modId,
				type: "post",
				timeout: 10000,
				data: {
					"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
					"wdtype": wdType,
					"code": code,
					"cell": cell,
					"fname": cname
				},
				async: false,
				dataType: "json",
				success: succ
			});
		} else {
			data.attrType = '0';
			template = {
				selAttrType0: "checked",
				weidu: wdType,
				attrFarea0: "checked",
				inputArg1: "checked",
				inputVal1: "sheetname[]",
				outArg0: "checked",
				fixedValName: cname,
				fixedVal: code,
				attrFName: "fName" + dd.getFullYear() + (dd.getMonth() + 1) + dd.getDate() + dd.getHours() + dd.getMinutes()
			};
		}

		function succ(d) {
			data = d.returndata;
			if (d.returncode === 500) {
				//defaultText
				template = {
					selAttrType0: "checked",
					weidu: wdType,
					attrFarea0: "checked",
					inputArg1: "checked",
					inputVal1: "sheetname[]",
					outArg0: "checked",
					fixedValName: cname,
					fixedVal: code,
					attrFName: "fName" + dd.getFullYear() + (dd.getMonth() + 1) + dd.getDate() + dd.getHours() + dd.getMinutes()
				};
			} else {
				//回写
				//if (data.attrType === '1') {
				template.weidu = wdType;
				template.attrFName = data.filterInfo.fname;
				template.oldFName = data.filterInfo.fname;
				template.sjy = data.filterInfo.sjy;
				template.sjq = data.filterInfo.sjq;
				template.sjm = data.filterInfo.sjm;
				template.selAttrType1 = "checked";
				template.showFilterBox = "active";
				template["attrFarea" + data.filterInfo.farea] = "checked";
				template["inputArg" + data.filterInfo.itype] = "checked";
				template["outArg" + data.filterInfo.otype] = "checked";
				template["inputVal" + data.filterInfo.itype] = data.filterInfo.ival;
				template["outVal" + data.filterInfo.otype] = data.filterInfo.oval;
				template.oname = data.filterInfo.oname;
				if (data.filterInfo.farea === "1") {
					template.showBox = "active";
					template.fwd = data.filterInfo.fwd;
				}
				//} 
				/*else {
					template = {
						selAttrType0: "checked",
						weidu: wdType,
						attrFarea0: "checked",
						inputArg1: "checked",
						inputVal1: "sheetname[]",
						outArg0: "checked",
						fixedValName: cname,
						fixedVal: data.fixedName,
						attrFName: "fName" + dd.getFullYear() + (dd.getMonth() + 1) + dd.getDate() + dd.getHours() + dd.getMinutes()
					};
				}*/
			}
		}

		var weiduAttrHtml = Handlebars.compile($('#weiduAttr').html())(template);
		$('#modal').empty().append(weiduAttrHtml).modal({
			closable: false,
			onShow: function() {
				pub.initArgTree(wdType);
			},
			onApprove: function() {
				var isFilter = $("[name='selAttrType']:checked").val(),
					FixedVal = $("[name='attrFixedVal']").attr('code'),
					fType = $("[name='attrWdType']:checked").val();
				if (isFilter === "0") {
					if ($.trim(FixedVal) === "") {
						ui.alert("固定值不能为空！！");
						return false;
					} else {
						attrArea = {
							"attrType": isFilter,
							"fixedName": FixedVal
						};
					}
				} else {
					var filterName = $("[name='attrFName']").val(),
						oldFilterName = $("[name='attrFName']").attr("oldName"),
						itype = $("[name='inputArg']:checked").val(),
						ival = $("#inputArg" + itype).val(),
						otype = $("[name='outArg']:checked").val(),
						oval = $("#outArg" + otype).val(),
						oname = $("#wdOutArgSelect.dropdown .text").html(),
						fwd = [],
						isNext = true,
						sjtype = '';
					$('[name=mqy]:checked').each(function(){
						sjtype += $(this).val();
					});
					if ($.trim(filterName) === "") {
						ui.alert("筛选名称不能为空！！");
						return false;
					}
					if (oval === "") {
						ui.alert("输出参数不能为空！！");
						return false;
					}
					if (filterName !== oldFilterName) {
						$.ajax({
							url: rootPath + "/templatecenter/DistinguishArea.htm?m=checkFname&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + filterName,
							type: "post",
							async: false,
							timeout: 10000,
							dataType: "json",
							success: function(data) {
								if (data.returncode === 500) {
									ui.error(data.returndata);
									isNext = false;
								}
							}
						});
					}
					if (!isNext) {
						return false;
					}
					if (fType === "1") {
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
					}
					attrArea = {
						"attrType": isFilter,
						"filterInfo": {
							"sjtype": sjtype,
							"fname": filterName,
							"farea": fType,
							"fwd": fwd,
							"itype": itype,
							"ival": ival,
							"otype": otype,
							"oval": oval,
							"oname": oname
						}
					};
				}

				$.ajax({
					url: rootPath + '/templatecenter/PropertyPanel.htm?m=edit&modId=' + modId,
					type: "post",
					timeout: 10000,
					data: {
						"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
						"wdtype": wdType,
						"code": code,
						"cell": cell,
						"datas": JSON.stringify(attrArea)
					},
					dataType: "json",
					success: function(d) {
						if (d.returncode === 200) {
							var cell = d.returndata.split("==")[0];
							var text = d.returndata.split("==")[1];
							ss.setCellContent("1", text, cell);
							//刷新筛选区
							if (attrArea.filterInfo) {
								pub.refreshFilter();
							}
							setTimeout(function() {
								handler(prevClickCell);
							}, 400);
						}
					}
				});
			}
		}).modal('show');

		pub.initDTC(".modal");

		$(".attrSelType .checkbox").checkbox({
			onChange: function() {
				if (this.value === '1') {
					$(".filter-box").addClass("active");
				} else {
					$(".filter-box").removeClass("active");
				}
				//outArg
				getOutArg();
				$('#modal').modal("refresh");
			}
		});

		$("#attrWdType .checkbox").checkbox({
			onChange: function() {
				if(wdType === 'sj' && this.value === '0'){
					$("#mqy").removeClass("hidden");
				}else{
					$("#mqy").addClass("hidden");
				}
				$(".weidu-box-two").toggleClass("active");
				$('#modal').modal("refresh");
				//outArg
				getOutArg();
			}
		});

		$("[name='attrFixedVal']").click(function(e) {
			$(".fixed-tree").show();
			e.stopPropagation();
		});
		$(".fixed-tree").click(function(e) {
			e.stopPropagation();
		});
		$(".modal").click(function() {
			$(".fixed-tree").hide();
		});

		$("#onceItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("attrWdTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0]);
			//outArg
			getOutArg();
		});
		$("#procodeAllItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("attrWdTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0], true);
			//outArg
			getOutArg();
		});

		//remove
		$("#filterTreeList .remove").live("click", function() {
			$(this).parents("tr").remove();
			//outArg
			getOutArg();
		});
		getOutArg();
		if (data.attrType === '1') {
			$('#wdOutArgSelect .text').html(data.filterInfo.oname);
			$('#outArg0').val(data.filterInfo.oval);
		}
	});

	//新增维度属性
	doc.on("click", ".attr-panel i.add", function() {
		var wdType = $(this).attr("wdtype"),
			cell = $(this).attr("cell"),
			d = new Date();
		if (!cell) {
			return false;
		}
		var template = {
			weidu: wdType,
			selAttrType0: "checked",
			attrFarea0: "checked",
			inputArg1: "checked",
			inputVal1: "sheetname[]",
			outArg0: "checked",
			attrFName: "fName" + d.getFullYear() + (d.getMonth() + 1) + d.getDate() + d.getHours() + d.getMinutes(),
			fixedValName: "",
			fixedVal: ""
		};

		var weiduAttrHtml = Handlebars.compile($('#weiduAttr').html())(template);
		$('#modal').empty().append(weiduAttrHtml).modal({
			closable: false,
			onShow: function() {
				pub.initArgTree(wdType);
			},
			onApprove: function() {
				var isFilter = $("[name='selAttrType']:checked").val(),
					FixedVal = $("[name='attrFixedVal']").attr('code'),
					fType = $("[name='attrWdType']:checked").val();
				if (isFilter === "0") {
					if ($.trim(FixedVal) === "") {
						ui.alert("固定值不能为空！！");
						return false;
					} else {
						attrArea = {
							"attrType": isFilter,
							"fixedName": FixedVal
						};
					}
				} else {
					var filterName = $("[name='attrFName']").val(),
						oldFilterName = $("[name='attrFName']").attr("oldName"),
						itype = $("[name='inputArg']:checked").val(),
						ival = $("#inputArg" + itype).val(),
						otype = $("[name='outArg']:checked").val(),
						oval = $("#outArg" + otype).val(),
						oname = $("#wdOutArgSelect.dropdown .text").html(),
						fwd = [],
						isNext = true,
						sjtype = '';
					$('[name=mqy]:checked').each(function(){
						sjtype += $(this).val();
					});
					if ($.trim(filterName) === "") {
						ui.alert("筛选名称不能为空！！");
						return false;
					}
					if (oval === "") {
						ui.alert("输出参数不能为空！！");
						return false;
					}

					$.ajax({
						url: rootPath + "/templatecenter/DistinguishArea.htm?m=checkFname&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY + "&name=" + filterName,
						type: "post",
						timeout: 10000,
						async: false,
						dataType: "json",
						success: function(data) {
							if (data.returncode === 500) {
								ui.error(data.returndata);
								isNext = false;
							}
						}
					});

					if (!isNext) {
						return false;
					}
					if (fType === "1") {
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
					}
					attrArea = {
						"attrType": isFilter,
						"filterInfo": {
							"sjtype": sjtype,
							"fname": filterName,
							"farea": fType,
							"fwd": fwd,
							"itype": itype,
							"ival": ival,
							"otype": otype,
							"oval": oval,
							"oname": oname
						}
					};
				}

				$.ajax({
					url: rootPath + '/templatecenter/PropertyPanel.htm?m=add&modId=' + modId,
					type: "post",
					timeout: 10000,
					data: {
						"templateCode": window.SPREADSHEET_AUTHENTIC_KEY,
						"wdtype": wdType,
						"code": "",
						"cell": cell,
						"datas": JSON.stringify(attrArea)
					},
					dataType: "json",
					success: function(d) {
						if (d.returncode === 200) {
							var cell = d.returndata.split("==")[0];
							var text = d.returndata.split("==")[1];
							ss.setCellContent("1", text, cell);
							//刷新筛选区
							if (attrArea.filterInfo) {
								pub.refreshFilter();
							}
							setTimeout(function() {
								handler(prevClickCell);
							}, 400);
						}
					}
				});
			}
		}).modal('show');

		pub.initDTC(".modal");

		$(".attrSelType .checkbox").checkbox({
			onChange: function() {
				if (this.value === '1') {
					$(".filter-box").addClass("active");
				} else {
					$(".filter-box").removeClass("active");
				}
				//outArg
				getOutArg();
				$('#modal').modal("refresh");
			}
		});

		$("#attrWdType .checkbox").checkbox({
			onChange: function() {
				if(wdType === 'sj' && this.value === '0'){
					$("#mqy").removeClass("hidden");
				}else{
					$("#mqy").addClass("hidden");
				}
				$(".weidu-box-two").toggleClass("active");
				$('#modal').modal("refresh");
				//outArg
				getOutArg();
			}
		});

		$("[name='attrFixedVal']").click(function(e) {
			$(".fixed-tree").show();
			e.stopPropagation();
		});
		$(".fixed-tree").click(function(e) {
			e.stopPropagation();
		});
		$(".modal").click(function() {
			$(".fixed-tree").hide();
		});

		$("#onceItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("attrWdTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0]);
			//outArg
			getOutArg();
		});
		$("#procodeAllItem2").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("attrWdTree");
			var nodes = treeObj.getSelectedNodes();
			pub.addAreaItem($("#filterTreeList"), nodes[0], true);
			//outArg
			getOutArg();
		});

		//remove
		$("#filterTreeList .remove").live("click", function() {
			$(this).parents("tr").remove();
			//outArg
			getOutArg();
		});
	});

	//输出参数下拉列表实时刷新
	function getOutArg() {
		var isFilter = $("[name='selAttrType']:checked").val(),
			wd = $("[name='wdType']").val(),
			fType = $("[name='attrWdType']:checked").val(),
			nodes = [];
		$("#wdOutArgSelect.dropdown .text").html('');
		$("#outArg0").val('');
		if (isFilter === "1") {
			if (fType === "1") {
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
			} else {
				nodes = $("#weiduTreeFixed").data("itree");
			}
		}

		var setting = {
			async: {
				enable: true,
				url: rootPath + '/templatecenter/templatehandel.htm?m=getResultLeftTree&modId=' + modId + '&wd=' + wd,
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
			$.fn.zTree.init($("#wdOutArgTree"), setting, nodes);
		} else {
			$("#wdOutArgTree").empty();
		}

		$("#wdOutArgSelect.dropdown").dropdown({
			preserveHTML: false,
			treeWidget: 'wdOutArgTree',
			selector: {
				item: '.item > label, #wdOutArgTree li a:not(.disabled)'
			}
		});
		$("> .label > .delete.icon", $("#wdOutArgSelect")).trigger("click.dropdown");
	}

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
					url: rootPath + '/templatecenter/PropertyPanel.htm?m=del&modId=' + modId,
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
							if (data.returndata) {
								var cell = data.returndata.split("==")[0];
								var text = data.returndata.split("==")[1];
								var showcode = $("#show-code-btn input").is(":checked");
								if (showcode) {
									ss.setCellContent("1", text, cell);
								}
								//刷新筛选区
								pub.refreshFilter();
								setTimeout(function() {
									handler(prevClickCell);
								}, 400);
							}
							cTips.commonTips("删除成功！");
						}else{
							cTips.commonTips(data.returndata);
						}
					}
				});
			}
		}).modal('show');
	});

	//暂存模板
	doc.on("click", "#tempSave", function() {
		step = ss.getStep();
		$.ajax({
			url: rootPath + "/templatecenter/TemplateHandel.htm?m=saveTempModel&step=" + step + "&modId=" + modId + "&id=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			timeout: 10000,
			dataType: "json",
			beforeSend: function() {
				$("body").append('<div class="ui active dimmer piDimmer"><div class="ui loader"></div></div>');
			},
			success: function(d) {
				$(".piDimmer.ui.dimmer").remove();
				if (d.returncode === 200) {
					ui.success("保存成功");
				} else {
					ui.success("保存失败");
				}
			}
		});
	});

	//保存模板
	doc.on("click", "#saveTemplate", function() {
		step = ss.getStep();
		$.ajax({
			url: rootPath + "/templatecenter/TemplateHandel.htm?m=saveTemplateContent&step=" + step + "&modId=" + modId + "&templateCode=" + window.SPREADSHEET_AUTHENTIC_KEY,
			type: "post",
			timeout: 100000,
			dataType: "json",
			async: false,
			beforeSend: function() {
				var delHtml = Handlebars.compile($('#confirm-modal').html())({
					"title": "正在保存....",
					"iconClass": "save",
					"hideClass": "hide"
				});
				$('#modal2').empty().append(delHtml).modal({
					closable: false
				}).modal('show');
			},
			success: function(d) {
				if (d.returncode === 200) {
					ui.success('保存成功');
					$('#modal2').modal('hide');
					$('#modal2').removeClass('active');
				}
				if (d.returncode === 500 || d.returncode == 600) {
					$('#modal2 .header').text('保存失败，原因：' + d.returndata);
					$('#modal2 .actions').removeClass('hide');
				}
				ss.reload();
			},
			error: function() {
				$('#modal2 .header').text('保存失败，原因：远程连接错误');
				$('#modal2 .actions').removeClass('hide');
			}
		});
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
				url: rootPath + '/templatecenter/IdentifyArea.htm?m=codeDrag&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
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
			url: rootPath + '/templatecenter/IdentifyArea.htm?m=checkcell&modId=' + modId + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY + '&cell=' + coordinate,
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
		//search information
		var cell = e.point.col[0] + e.point.row[0];
		if (!isSetWeidu || e.point.row[0] <= 2 || e.point.col[0] === "A") {
			return false;
		}
		asyncGetPosiInfo(cell, searchInfo);
		$(".attr-panel i.add").attr("cell", cell).hide();

		function searchInfo(posiInfo) {
			var posi = posiInfo.returndata;
			switch (posi) {
				case 'global':
					$(".attr-panel i.add").show();
					break;
				case 'main':
				case 'guest':
					$('.J-dimension-search input').val(e.text);
					break;
			}
			prevClickCell = cell;
			handler(cell);
		}
	});

	function handler(cell) {
		$.ajax({
			url: rootPath + '/templatecenter/PropertyPanel.htm?m=show&modId=' + modId + '&cell=' + cell + '&templateCode=' + window.SPREADSHEET_AUTHENTIC_KEY,
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
