define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		Deltreetable = require('Deltreetable'),
		Selecttreetable = require('Selecttreetable'),
		UpdateNametreetable = require('UpdateNametreetable'),
		UpdateUnittreetable = require('UpdateUnittreetable'),
		filterTable = require('filterTable'),
		setvarTable = require('setvarTable'),
		Relation = require('Relation'),
		common = require('common'),
		CombinFormattreetable = require('CombinFormattreetable'),
		RenderPage;

	function RenderPage() {
		this.tempArray = [];
		this.config = {};
		this.filterConfig = {};
	}
	RenderPage.prototype = {
		contrustor: RenderPage,
		buildTreetableHTML: function(contentArray) {
			var i = 0,
				len = 0,
				tempHTML = '',
				fileType,
				mergeName;
			if (contentArray instanceof Array) {
				len = contentArray.length;
			}
			if (len === 0) {
				return false;
			}
			for (; i < len; i++) {
				tempHTML += '<tr ';
				if (contentArray[i].id !== '') {
					tempHTML += 'data-tt-id="' + contentArray[i].id + '"';
				}
				if (contentArray[i].pid !== '') {
					tempHTML += ' data-tt-parent-id="' + contentArray[i].pid + '"';
				}
				tempHTML += '>';
				tempHTML += '<td><input type="checkbox"';
				if (contentArray[i].id !== '') {
					tempHTML += ' value="' + contentArray[i].id + '"';
				}
				tempHTML += '/>';
				if (!parseInt(contentArray[i].ifData)) {
					fileType = 'folder';
				} else {
					fileType = 'file';
				}
				if (contentArray[i].ismerge) {
					mergeName = '合并';
				} else {
					mergeName = '未合并';
				}
				tempHTML += '</td><td><span data-name="t" class="editor ' + fileType + '">' + contentArray[i].name + '</span></td>'
				tempHTML += '<td><span class="editor" data-name="u" data-unitcode="' + contentArray[i].unitCode + '">' + contentArray[i].unitName + '</span></td>';
				tempHTML += '<td><a data-name="m" class="btn btn-sm btn-link" href="javascript:;" data-status="' + contentArray[i].ismerge + '">' + mergeName + '</a></td></tr>';
			}
			return tempHTML;
		},
		buildTbody: function(rhtml, randomID) {
			$('#' + randomID + " tbody").html(rhtml);
			this.bindEvent(this.config);
		},
		renderTable: function(postionBar, rhtml, dragTargetObj, targetObj) {
			this.filterConfig = {
				dragTargetObj: dragTargetObj,
				targetBtnDel: postionBar + ' td a.J_btn_filter_del',
				targetBtnVar: postionBar + ' td a.J_btn_filter_var',
				targetBtnSelect: postionBar + ' .J_select_var',
				targetObj: targetObj
			}
			$(postionBar).append(rhtml);
			new filterTable(this.filterConfig);
			new setvarTable(this.filterConfig);
		},
		buildTable: function(randomID, cfg) {
			var config = cfg || {},
				randomID = randomID || '',
				colgroup = config.colgroup || '<colgroup><col width="5%"></col><col width="35%"></col><col width="35%"></col><col width="20%"></col></colgroup>',
				thead = config.thead || '<thead><tr><th><input type="checkbox"/></th><th>内容名称</th><th>计量单位</th><th>是否合并</th></tr></thead>',
				tbody = config.tbody,
				tableClass = config.tableClass || '',
				tempTable = '<table';
			if (randomID != '') {
				tempTable += ' id=' + randomID
			}
			if (tableClass != '') {
				tempTable += ' class=' + tableClass;
			}
			tempTable += '>';
			tempTable += colgroup + thead + tbody;
			tempTable += '</table>';
			return tempTable;
		},
		buildPanel: function(randomID, postionBar, rhtml, treeName, oprArray, currentIndex, randomTableArray, setSymbol, partIndex, targetPartIndex) {
			var tempHTML,
				delBtn = $('<a/>').addClass('btn btn-default btn-sm').prop('href', 'javascript:;').text('批量删除');
			tempHTML = '<div class="panel panel-default reset-panel" data-part="' + targetPartIndex + '">';
			tempHTML += '<div class="panel-heading">';
			tempHTML += '<div class="toolbar">';
			tempHTML += '<div class="toolbar-left"><label class="control-label">' + treeName + '</label></div>';
			tempHTML += '<div class="toolbar-right" mapp="' + randomID + '">';
			tempHTML += '</div>';
			tempHTML += '</div>';
			tempHTML += '</div>';
			tempHTML += '<div class="panel-body" style="padding:0;">';
			tempHTML += rhtml;
			tempHTML += '</div>';
			tempHTML += '</div>';
			$('#' + postionBar).append(tempHTML);
			$('div[mapp="' + randomID + '"]').append(delBtn);
			var treeObj = {
				dimesionTitle: treeName
			}
			if (setSymbol === 'mutli') {
				setSymbol = 'remove'
			}
			if (setSymbol === 'add') {
				setSymbol = 'plus'
			}
			this.config = {
				delBtn: delBtn,
				table: '#' + randomID,
				randomID: randomID,
				tempArray: this.tempArray,
				oprArray: oprArray,
				singleCheckbox: '#' + randomID + '>thead input[type="checkbox"]',
				allCheckbox: '#' + randomID + '>tbody input[type="checkbox"]',
				nameSpan: '#' + randomID + '>tbody span[data-name="t"]',
				unitSpan: '#' + randomID + '>tbody span[data-name="u"]',
				mergeSpan: '#' + randomID + '>tbody a[data-name="m"]',
				randomTableArray: randomTableArray,
				currentIndex: currentIndex,
				dimesionTitle: treeName,
				targetPanel: postionBar == 'posiindex' ? '.J_symbol_panel_main' : '.J_symbol_panel_guest',
				symbolBtn: $('<span>').addClass('glyphicon glyphicon-' + setSymbol + ' relation-symbol').attr('data-part', targetPartIndex),
				closeBtn: $('<button>').addClass('btn btn-danger btn-sm').html(common.substitute('{dimesionTitle}<span class="badge">×</span>', treeObj)).attr('data-part', targetPartIndex),
				positionBar: postionBar,
				partIndex: partIndex,
				targetPartIndex: targetPartIndex
			}
			this.bulidTag();
			this.bindEvent(this.config);
		},
		bulidTag: function() {
			if (this.config.currentIndex !== 0) {
				$(this.config.targetPanel).append(this.config.symbolBtn);
			}
			$(this.config.targetPanel).append(this.config.closeBtn);
			new Relation(this.config)
		},
		buildOneTr: function(contentObj) {
			var tempHTML = '<tr>';
			if (common.dragTemplate === 'templatedrag.htm' && parseInt(contentObj.isvar)) {
				tempHTML += '<td><select data-type="' + contentObj.type + '" data-id="' + contentObj.id + '" class="form-control J_select_var">';
				tempHTML += '<option value="1" ';
				if (contentObj.varopt == "1") {
					tempHTML += 'selected';
				}
				tempHTML += '>sheet名称</option><option value="2"';
				if (contentObj.varopt == "2") {
					tempHTML += 'selected';
				}
				tempHTML += '>文件名称</option></select>';
				tempHTML += '</td>';
			} else {
				tempHTML += '<td>' + contentObj.name + '</td>';
			}
			tempHTML += '<td data-unitcode="' + contentObj.unitcode + '">' + contentObj.unitname + '</td>';
			tempHTML += '<td>' + contentObj.typename + '</td>';
			tempHTML += '<td>';
			tempHTML += '<a class="btn btn-sm btn-link J_btn_filter_del" data-type="' + contentObj.type + '" data-id="' + contentObj.id + '">删除</a>';
			if (common.dragTemplate === 'templatedrag.htm') {
				tempHTML += '<a class="btn btn-sm btn-link J_btn_filter_var" data-type="' + contentObj.type + '" data-id="' + contentObj.id + '"';
				contentObj.isvar === 1 ? tempHTML += ' data-stats="0"' : tempHTML += ' data-stats="1"';
				tempHTML += '>'
				contentObj.isvar === 1 ? tempHTML += '使用默认' : tempHTML += '使用变量';
				tempHTML += '</a>';
			}
			tempHTML += '</td>'
			tempHTML += '</tr>';
			return tempHTML;
		},
		bindEvent: function(cfg) {
			var i, len, $currentNameSpan, $unitSpan, $mergeSpan;
			new Deltreetable(cfg);
			new Selecttreetable({
				singleBtn: cfg.singleCheckbox,
				allBtn: cfg.allCheckbox,
				oprArray: cfg.tempArray,
				randomID:cfg.randomID
			});
			$currentNameSpan = $(cfg.nameSpan);
			$unitSpan = $(cfg.unitSpan);
			$mergeSpan = $(cfg.mergeSpan);
			len = $currentNameSpan.length;
			for (i = len - 1; i >= 0; i--) {
				new UpdateNametreetable({
					btn: $currentNameSpan.eq(i),
					oprArray: cfg.oprArray,
					currentIndex: cfg.currentIndex,
					positionBar: cfg.positionBar,
					partIndex: cfg.partIndex
				});
				new CombinFormattreetable({
					btn: $mergeSpan.eq(i),
					oprArray: cfg.oprArray,
					currentIndex: cfg.currentIndex,
					positionBar: cfg.positionBar,
					partIndex: cfg.partIndex
				});
				new UpdateUnittreetable({
					btn: $unitSpan.eq(i),
					oprArray: cfg.oprArray,
					currentIndex: cfg.currentIndex,
					positionBar: cfg.positionBar,
					partIndex: cfg.partIndex
				});
			};
		},
		bindFilterEvent: function(cfg) {

		}
	}
	module.exports = RenderPage;
});