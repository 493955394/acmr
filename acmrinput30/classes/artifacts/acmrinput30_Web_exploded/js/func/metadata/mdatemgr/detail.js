define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
	common = require('common'),
	datepicker = require('datepicker'),
	dropdown = require('dropdown');
	
	/**
	 * 初始化时间控件
	 */
	$(".J_datepicker").datepicker({
    	changeMonth:true,
    	changeYear:true,
    	dateFormat:'yy-mm-dd'
    });
	
	$(":input").not($(":input").last()).attr("disabled",true);
});