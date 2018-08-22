define(function(require,exports,module){
	'use strict';
	/**
	 * ajax包装，防止重复提交
	 */
	var $ = require('jquery');
	function PackAjax(){
		var responseState = true;
		return function(cfg){
			if(!responseState){
				return;
			}
			responseState = false;
			var config ={
				url:cfg.url || '',
				type:cfg.type ||'post',
				dataType:cfg.dataType || 'json',
				data:cfg.data || '',
				timeout:cfg.timeout||5000,
				success:cfg.success || function(){},
				error:cfg.error || function(){},
				complete:cfg.complete || function(){}
			}
			$.ajax({
				url:config.url,
				type:config.type,
				dataType:config.dataType,
				data:config.data,
				timeout:config.timeout,
				success:config.success,
				error:config.error,
				complete:function(){
					config.complete();
					responseState = true;
				}
			});
		}
	}
	module.exports = PackAjax;
})