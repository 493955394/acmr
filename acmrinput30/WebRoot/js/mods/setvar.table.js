define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		setvarTable,
		$doc = $(document);
	setvarTable = function(cfg) {
		var self = this;
		self.config = {
			dragTargetObj: cfg.dragTargetObj,
			targetBtnVar: cfg.targetBtnVar,
			targetObj: cfg.targetObj,
			targetBtnSelect: cfg.targetBtnSelect
		};
		$doc.on('click', self.config.targetBtnVar, function(e) {
			e.preventDefault();
			var myself = this,
				currentType = $(myself).data('type'),
				currentID = $(myself).data('id'),
				targetStats = $(myself).data('stats'),
				len, i;

			len = self.config.dragTargetObj.length;
			for (i = len - 1; i >= 0; i--) {
				var currentObj = self.config.dragTargetObj[i];
				if (currentObj['type'] === currentType && currentObj['id'] === currentID.toString()) {
					currentObj.isvar = targetStats;
					//targetStats = targetStats === 1 ? 0 : 1;
					reviewtreetable({
						requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.targetObj.pId,
						sendObj: 'data={"filterData":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.dragTargetObj))) + '}',
						success: function(data) {
							if (data.returncode == 200) {
								if (!currentObj.isvar) {
									$(myself).text('使用变量');
									$(myself).attr('data-stats', "1");
									$(myself).data('stats', 1);
									$(myself).parents('tr').children('td:eq(0)').html(currentObj.name);
								} else {
									$(myself).text('使用默认');
									$(myself).attr('data-stats', "0");
									$(myself).data('stats', 0);
									$(myself).parents('tr').children('td:eq(0)').html('<select data-type="' + currentType + '" data-id="' + currentID + '" class="form-control J_select_var"><option value="1" selected>sheet名称</option><option value="2">文件名称</option></select>')
								}

							}
						}
					});
					break;
				}
			};
		});
		$doc.on('change', self.config.targetBtnSelect, function() {
			var myself = this,
				currentType = $(myself).data('type'),
				currentID = $(myself).data('id'),
				len, i;

			len = self.config.dragTargetObj.length;
			for (i = len - 1; i >= 0; i--) {
				var currentObj = self.config.dragTargetObj[i];
				if (currentObj['type'] === currentType && currentObj['id'] === currentID.toString()) {
					currentObj.varopt = $(myself).val();
					reviewtreetable({
						requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.targetObj.pId,
						sendObj: 'data={"filterData":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.dragTargetObj))) + '}',
						success: function(data) {
							if (data.returncode != 200) {
								common.commonTips(data.msg);
							}
						}
					});
					break;
				}
			};
		});
	}
	module.exports = setvarTable;
});