define(function(require, exports, module) {
	'use strict';
	var jQuery = require('jquery');
	
	(function($){
		$.fn.extend({
			drawback: function(options,callback) {
				var defaults = {
						width:7,height:100,top:150
					},
					options = $.extend(defaults,options),
					that	= $(this),
					lObj	= that.children().eq(0),
					rObj	= that.children().eq(1),
					l	 	= rObj.offset().left - options.width,
					lclass  = lObj.attr("class"),
					rclass  = rObj.attr("class");
					//padding = rObj.css("padding-left") + rObj.css("padding-right");
					
				$("<div />",{
					"class": "drawBackBtn",
					"text": "",
					click: function(){
						if( lObj.width() == 0 ){
							lObj.removeAttr("style").addClass(lclass);
							
							rObj.removeAttr("style").addClass(rclass);
							
							$(this).html("").css({"left": l});
						}else{
							lObj.css({
								"width": 0,
								"overflow": "hidden"
							});
							
							rObj.css({
								//"width": "100%",
								"margin-left": options.width
							});
							
							$(this).html("").css({"left":0});
						}
						
						callback && callback();//回调
					}
				}).css({
					"position":"absolute",
					"width": options.width,
					"height": options.height,
					"line-height": options.height + "px",
					"top": options.top,
					"left":l,
					"background": "#000",
					"opacity": 0.5,
					"font-family": "sunsim",
					"cursor":"pointer",
					"font-size": "26px",
					"font-weight": "bold",
					"text-align": "center",
					"color": "#fff"
				}).appendTo( that.css({"position":"relative"}) );
			}
		});
	})(jQuery);
	
});