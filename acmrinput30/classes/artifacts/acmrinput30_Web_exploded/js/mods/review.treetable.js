define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		PA = require('Packajax'),
		PackAjax =  new PA(),
		templateId,
		reviewtreetable;
	templateId = $('input[name="modId"]').val().trim();
	reviewtreetable = function(cfg) {
		var config = {
				sendObj:cfg.sendObj,
				requestUrl:common.rootPath + cfg.requestUrl + '&modId='+templateId,
		}
		PackAjax({
			url: config.requestUrl,
			dataType: 'json',
			data: config.sendObj,
			success: cfg.success || function(data){
				if(data.returncode == 200){
					$('.J_preview_table').html(data.returndata);
				}
			}
		})
	}
	module.exports = reviewtreetable;
});