define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		reviewtreetable = require('reviewtreetable'),
		common = require('common'),
		Deltreetable;
	/**
	 * batch del treetable data
	 * @param {[type]} cfg [description]
	 */
	Deltreetable = function(cfg) {
		this.config = {
			delBtn: cfg.delBtn, //btn selector
			table: cfg.table, //table selector
			tempArray: cfg.tempArray, // example ['id':'{id}','posi':'{posi]']
			oprArray: cfg.oprArray,
			currentIndex: cfg.currentIndex,
			oprArrayList: cfg.oprArray[cfg.partIndex]['part_regin'][cfg.currentIndex].list || {}, // dragObj[currentindex].list
			randomTableArray: cfg.randomTableArray,
			closeBtn: cfg.closeBtn || '',
			symbolBtn: cfg.symbolBtn || '',
			positonBar: cfg.positionBar
		}
		this.bindEvent();
	};
	Deltreetable.prototype = {
		constructor: Deltreetable,
		bindEvent: function() {
			var self = this,
				i, j,
				len,
				oprArrayLen;
			this.config.delBtn.on('click', function() {
				len = self.config.tempArray.length;
				oprArrayLen = self.config.oprArrayList.length;
				if (len === 0 || oprArrayLen === 0) {
					return;
				}
				for (i = len - 1; i >= 0; i--) {
					j = oprArrayLen - 1;
					for (; j >= 0; j--) {
						if (self.config.tempArray[i]["id"] == self.config.oprArrayList[j]["id"]) {
							self.config.oprArrayList.splice(j, 1);
							$('tbody tr:eq(' + self.config.tempArray[i]["posi"] + ')', self.config.table).remove();
							break;
						}
					}
					oprArrayLen = self.config.oprArrayList.length;
				}
				//完成临时数组操作，清空
				self.config.tempArray.length = 0;
				//如果该表下面的内容已经为空，需要删除整个panel并且删除随机表数组该内容
				if (oprArrayLen === 0) {
					self.config.oprArray.splice(self.config.currentIndex, 1);
					len = self.config.randomTableArray.length;
					for (i = len - 1; i >= 0; i--) {
						if (self.config.randomTableArray[i]['id'] == self.config.table.substring(1)) {
							self.config.randomTableArray.splice(i, 1);
							break;
						}
					};
					if (!self.isFristIndex()) {
						self.config.symbolBtn.remove();
					} else {
						self.config.closeBtn.siblings('span:eq(0)').remove();
					}
					self.config.closeBtn.remove();
					$(self.config.table).parents('div.panel').remove();
				}
				reviewtreetable({
					requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positonBar,
					sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray))) + '}'
				});
			});
		},
		isFristIndex: function() {
			return this.config.currentIndex === 0;
		}
	}
	module.exports = Deltreetable;
});