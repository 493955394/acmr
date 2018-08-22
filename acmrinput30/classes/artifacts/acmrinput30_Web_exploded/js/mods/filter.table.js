define(function(require,exports,module){
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		filterTable,
		$doc = $(document);
	filterTable = function(cfg){
		var self = this;
		self.config = {
			dragTargetObj:cfg.dragTargetObj,
			targetBtnDel:cfg.targetBtnDel,
			targetObj:cfg.targetObj
		};
		$doc.on('click', self.config.targetBtnDel, function(event) {
			event.preventDefault();
			var myself = this,
				currentType = $(this).data('type'),
				currentID = $(this).data('id'),
				len, i;
			len = self.config.dragTargetObj.length;
			for (i = len - 1; i >= 0; i--) {
				if (self.config.dragTargetObj[i]['type'] === currentType && self.config.dragTargetObj[i]['id'] === currentID.toString()) {
					self.config.dragTargetObj.splice(i, 1);
					reviewtreetable({
						requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.targetObj.pId,
						sendObj: 'data={"filterData":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.dragTargetObj))) + '}',
						success:function(data){
							if(data.returncode == 200){
								$(myself).parents('tr').remove();
							}
						}
					});
					break;
				}
			};
		});
	}
	module.exports = filterTable;
});