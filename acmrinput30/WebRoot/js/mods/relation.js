define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		reviewtreetable = require('reviewtreetable'),
		Relation;
	Relation = function(cfg) {
		this.config = {
			closeBtn: cfg.closeBtn || '',
			symbolBtn: cfg.symbolBtn || '',
			dimesionTitle: cfg.dimesionTitle,
			oprArray: cfg.oprArray,
			currentIndex: cfg.currentIndex,
			randomTableArray: cfg.randomTableArray,
			table: cfg.table,
			targetPanel: cfg.targetPanel,
			positionBar: cfg.positionBar,
			partIndex: cfg.partIndex
		}
		this.internal = {
			MUTLI: 'remove',
			ADD: 'plus',
			aliasAdd: 'add',
			aliasMutli: 'mutli'
		}
		this.init();
	}
	Relation.prototype = {
		contrustor: Relation,
		init: function() {
			this.bindEvent();
		},
		bindEvent: function() {
			var self = this;
			this.config.closeBtn.on('click', function(event) {
				event.preventDefault();
				var i, len;
				//清空当前内容数据的对应维度的对象
				self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex] = null;
				//移除随机数组里的table id数据
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
				reviewtreetable({
					requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positionBar,
					sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray,self.config.partIndex))) + '}'
				});
			});
			//如果是第一个元素，不绑定点击事件
			if (!this.isFristIndex()) {
				this.config.symbolBtn.off().on('click', function() {
					var $this = $(this),currentSymobol;
					currentSymobol = self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex]['symobol'];
					if (currentSymobol === self.internal.aliasMutli) {
						currentSymobol = self.internal.aliasAdd;
						$this.removeClass('glyphicon-' + self.internal.MUTLI);
						$this.addClass('glyphicon-' + self.internal.ADD)
					} else if (currentSymobol === self.internal.aliasAdd) {
						currentSymobol = self.internal.aliasMutli;
						$this.removeClass('glyphicon-' + self.internal.ADD)
						$this.addClass('glyphicon-' + self.internal.MUTLI);
					}
					self.config.oprArray[self.config.partIndex]['part_regin'][self.config.currentIndex]['symobol'] = currentSymobol
					reviewtreetable({
						requestUrl: common.dragTemplate + '?m=changeExcel&type=' + self.config.positionBar,
						sendObj: 'data={"wdlist":' + encodeURIComponent(JSON.stringify(common.clearNullData(self.config.oprArray))) + '}'
					});
				});
			}
		},
		isFristIndex: function() {
			return this.config.currentIndex === 0 || $(this.config.targetPanel).children('button:first-child')[0] === this.config.closeBtn[0];
		}
	};
	module.exports = Relation;
});