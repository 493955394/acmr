define(function(require, exports, module) {
	'use strict';

	var $ = require('jquery');

	function ajaxMods(cfg) {
		var self = this;
		self.config = {
			targetContent: cfg.targetContent || '',
			requireUrl: cfg.requireUrl || '',
			callbackFunc: cfg.callbackFunc || function() {},
			timeout: cfg.timout || 3000,
			type: cfg.type || 'get',
			data: cfg.data || '',
			dataType:cfg.dataType || 'html'
		};
		self.init();
	}
	ajaxMods.prototype = {
		construtor: ajaxMods,
		init: function() {
			var self = this;
			self.getMods();
		},
		getMods: function() {
			var self = this;
			$.ajax({
				url: self.config.requireUrl,
				type: self.config.type,
				dataType:self.config.dataType,
				timeout: self.config.timeout,
				data: self.config.data,
				success: function(data) {
					$(self.config.targetContent).html(data);
					self.config.callbackFunc();
				},
				error: function(e) {
					alert('response error,please refresh page or F5');
				},
				complate: function() {
				}
			})
		}
	}
	module.exports = ajaxMods;
})