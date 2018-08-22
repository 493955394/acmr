define(function(require, exports, module) {
	'use strict';
	var translate;
	translate = function(cfg) {
		var i, len,
			config = {
				cnName: ['指标', '分组', '时间类型', '地区', '时间', '来源'],
				enName: ['index', 'group', 'dateType', 'area', 'time', 'source']
			}
		len = config.enName.length;
		for (i = 0; i < len; i++) {
			if (cfg == config.enName[i]) {
				return config.cnName[i];
			}
		}
	}
	module.exports = translate;
});