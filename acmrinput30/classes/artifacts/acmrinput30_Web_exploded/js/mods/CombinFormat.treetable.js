define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		CombinFormat;
	CombinFormat = function(cfg) {
		this.config = {
			btn: cfg.btn,
			oprArray: cfg.oprArray,
			currentIndex: cfg.currentIndex,
			partIndex: cfg.partIndex,
			positionBar: cfg.positionBar,
		}
		this.init();
	}
	CombinFormat.prototype = {
		contrustor: CombinFormat,
		init: function() {
			var self = this;
			self.config.btn.on('click', function(e) {
				var currentStatus = $(this).data('status'),
					currentText = '',
					currentIndex;
				if (currentStatus === 1) {
					$(this).data('status', 0);
					currentText = '未合并';
				} else {
					$(this).data('status', 1);
					currentText = '合并';
				}
				currentIndex = $(this).parents('tr').index();
				self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex].list[currentIndex]['ismerge'] = $(this).data('status');
				self.config.btn.text(currentText);
				reviewtreetable({
					requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positionBar,
					sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray, self.config.partIndex))) + '}'
				});
			})
		}
	};
	module.exports = CombinFormat;
});