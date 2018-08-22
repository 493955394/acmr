define(function(require, exports, module) {
	'use strict';
	var jQuery = require('jquery');
	
	(function($){
		$.fn.extend({
			fullbox: function($btn, options) {
				var defaults = {
					},
					options = $.extend(defaults,options),
					that	= $(this),
					parent	= that.parent(),
					l 		= that.offset().left,
					t 		= that.offset().top,
					w 		= that.width();
					
				$btn.click(function(){
					var OriHeight = that.height();
					var maxWidth = $(document).width();
					if( maxWidth < $("table",that).width() ){
						maxWidth = $("table",that).width();
					}
					
					that.appendTo( $("body") ).css({
						"position":"absolute",
						"z-index": 999,
						"background":"#fff",
						"left": l,
						"top": t,	
						"width": maxWidth,
						"height": $(document).height(),
						"max-height": "none",
						"margin": 0,
						"padding": 0
					}).animate({
						"left": 0,
						"top": 0,
						"width": "auto"
					});
					
					$("<button/>",{
						title: "退出全屏",
						text:"退出全屏",
						class:"btn btn-sm",
						click: function(){
							that.removeAttr("style").css("height", OriHeight).appendTo( parent );
							$(this).remove();
						}
					}).css({"position":"fixed", "z-index": 10000,'right':'0','top':'0'}).appendTo( $("body") );
					
				});
			}
		});
	})(jQuery);
	
});