define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		UpdateNametreetable;
	UpdateNametreetable = function(cfg) {
		this.config = {
			btn: cfg.btn || '',
			template: cfg.template || '<input type="text" class="input-sm form-control" value="{currentText}"/><button type="button" class="btn btn-sm btn-primary">保存</button>',
			oprArray: cfg.oprArray || {},
			currentIndex: cfg.currentIndex,
			positionBar: cfg.positionBar,
			partIndex: cfg.partIndex
		}
		this.internal = {
			state: false
		}
		this.init();
	}
	UpdateNametreetable.prototype = {
		constructor: UpdateNametreetable,
		init: function() {
			this.touch();
			this.save();
		},
		save: function() {
			var self = this;
			this.config.btn.on('click', 'button', function() {
				self.internal.state = false;
				var currentText = $(this).siblings('input').val();
				var currentIndex = $(this).parents('tr').index();
				self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex].list[currentIndex]['name'] = currentText;
				self.config.btn.html(currentText);
				reviewtreetable({
					requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positionBar,
					sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray, self.config.partIndex))) + '}'
				});
			})
		},
		touch: function() {
			var self = this;
			this.config.btn.on('dblclick', function() {
				if (!self.internal.state) {
					self.internal.state = true;
					var currentObj = {
						currentText: $(this).text()
					};
					$(this).html(common.substitute(self.config.template, currentObj));
					$(this).append(self.internal.saveBtn);
				}
			});
		}
	}
	module.exports = UpdateNametreetable;
});