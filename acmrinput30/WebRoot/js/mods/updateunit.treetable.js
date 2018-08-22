define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		PA = require('Packajax'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		PackAjax = new PA(),
		UpdateUnittreetable,
		$doc = $(document);
	UpdateUnittreetable = function(cfg) {
		this.config = {
			btn: cfg.btn || '',
			template: cfg.template || '<select class="form-control input-sm">{content}</select><button class="btn btn-sm btn-primary">保存</button>',
			oprArray: cfg.oprArray || {},
			currentIndex: cfg.currentIndex,
			positonBar: cfg.positionBar,
			partIndex: cfg.partIndex
		}
		this.internal = {
			state: false
		}
		this.init();
	};
	UpdateUnittreetable.prototype = {
		contrustor: UpdateUnittreetable,
		init: function() {
			this.change();
			this.save();
		},
		change: function() {
			var self = this;
			this.config.btn.on('dblclick', function(event) {
				event.preventDefault();
				self.internal.state = true;
				PackAjax({
					url: common.rootPath + common.dragTemplate + '?m=getUnitZhuanhuanList',
					data: 'id=' + $(this).data('unitcode'),
					success: function(data) {
						if (data.returncode == 200) {
							var tempHTML = {
									content: ''
								},
								len, i = 0;
							len = data.returndata.length
							for (; i < len; i++) {
								tempHTML.content += '<option value="' + data.returndata[i].code + '">' + data.returndata[i].key + '</option>'
							};
							self.config.btn.html(common.substitute(self.config.template, tempHTML));
						}
					}
				});
			});
		},
		save: function() {
			var self = this,
				currentObj, currentDOMObj, currentCode, currentText, currentIndex;
			this.config.btn.on('click', 'button', function() {
				self.internal.state = false;
				currentDOMObj = $(this).siblings('select').children('option:selected');
				currentCode = currentDOMObj.val();
				currentText = currentDOMObj.text();
				currentIndex = $(this).parents('tr').index();

				currentObj = self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex].list[currentIndex];
				currentObj['unitCode'] = currentCode;
				currentObj['unitName'] = currentText;
				self.config.btn.html(currentText);
				reviewtreetable({
					requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positonBar,
					sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray, self.config.partIndex))) + '}'
				});
			});
		}
	}
	module.exports = UpdateUnittreetable;
});