define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		controlCacheList = require('controlcachelist'),
		CCA = require('controlcachearray'),
		reviewtreetable = require('reviewtreetable'),
		common = require('common'),
		Reduction,
		ControlCacheArray;
	Reduction = function(cfg) {
		this.config = {
			url: cfg.url || '',
			cacheListObj: cfg.cacheListObj || {},
			posiIndex: cfg.posiIndex || 0,
			partIndex: cfg.partIndex || 0,
			posiArray: cfg.posiArray || 0,
			templateObj: cfg.templateObj || '',
			partTemplate: cfg.partTemplate || '',
			randomTableArray: cfg.randomTableArray || [],
			bindEvent: cfg.bindEvent || function() {},
			partTable: cfg.partTable,
			completeEvent: cfg.completeEvent || function() {}
		}
	}
	Reduction.prototype = {
		contrustor: Reduction,
		viewDataList: function() {
			$.ajax({
				url: this.config.url,
				success: this.viewDataSucess(this.config)
			});
		},
		itemViewData: function(data, currentIndex, partIndex, targetPartIndex) {
			var randomID,
				ControlCacheArray,
				targetObj;
			//返回数据加入cache data json列表
			controlCacheList.pushCacheList(data.list, this.config.cacheListObj[this.config.posiIndex], currentIndex, partIndex);
			ControlCacheArray = new CCA();
			targetObj = {
				pId: this.config.posiArray[this.config.posiIndex]
			}
			randomID = ControlCacheArray.renderUI(this.config.randomTableArray, this.config.cacheListObj[this.config.posiIndex], currentIndex, data.type, targetObj, data.symobol, partIndex, targetPartIndex);
			//主动绑定事件
			this.config.bindEvent('#' + randomID, this.config.cacheListObj[this.config.posiIndex], currentIndex, targetObj.pId, partIndex);
		},
		viewDataSucess: function(config) {
			var self = this;
			return function(data) {
				var i, j, len, cloneTemplateObj, clonePartTemplateObj, partIndex;
				if (data.returncode == 500 || data.returncode != 200 || !data.returndata) {
					return;
				}
				len = data.returndata.length || 0;
				for (i = 0; i < len; i++) {
					//reduction right sider status
					var dataPartIndex = parseInt(data.returndata[i]['part_index']);
					config.partTable.push(dataPartIndex);
					//reduction main content
					clonePartTemplateObj = $.extend(true, {}, config.partTemplate);
					var targetPartIndex = clonePartTemplateObj.part_index = dataPartIndex;
					clonePartTemplateObj.part_regin = [];
					partIndex = controlCacheList.pushPart(clonePartTemplateObj, config.cacheListObj[config.posiIndex]) - 1;
					var items = data.returndata[partIndex]['part_regin'];
					var itemsLen = items.length || 0;
					for (j = 0; j < itemsLen; j++) {
						cloneTemplateObj = $.extend(true, {}, config.templateObj);
						cloneTemplateObj.type = items[j].type;
						cloneTemplateObj.symobol = items[j].symobol;
						cloneTemplateObj.list = [];
						controlCacheList.pushDim(cloneTemplateObj, config.cacheListObj[config.posiIndex], partIndex);
						self.itemViewData(items[j], j, partIndex, targetPartIndex);
					}
				};
				config.completeEvent();
			}
		},
		filterDataList: function() {
			$.ajax({
				url: this.config.url,
				success: this.filterDataSucess(this.config)
			})
		},
		filterDataSucess: function(config) {
			var self = this;
			return function(data) {
				var i, len, currentObj, targetObj, item;
				if (data.returncode == 500) {
					alert(data.returndata);
				}
				if (data.returncode != 200 || !data.returndata || !data.returndata.length) {
					return;
				}
				len = data.returndata.length;
				for (i = 0; i < len; i++) {
					item = data.returndata[i];
					currentObj = {
						type: item.type,
						typename: item.typename,
						id: item.id,
						name: item.name,
						unitcode: item.unitcode,
						unitname: item.unitname,
						isvar: item.isvar,
						paraIndex: item.paraIndex,
						varopt: item.varopt
					}
					targetObj = {
						pId: self.config.posiArray[self.config.posiIndex]
					}
					self.config.cacheListObj[self.config.posiIndex].push(currentObj);
					ControlCacheArray = new CCA();
					ControlCacheArray.renderNormalUI(currentObj, targetObj, self.config.cacheListObj[self.config.posiIndex]);
				}
			}
		}
	}
	module.exports = Reduction;
});