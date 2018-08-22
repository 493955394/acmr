/********* 弹出层 **********
********* liaojin *********
******** 2014-06-05 *******
******* QQ:150334055 ******
**http://www.acmr.com.cn***
**************************/
define(function(require, exports, module) {
	var jQuery = require('jquery');
	
	(function($){
		
		$.extend({
			tips: function(options) {
				var defaults = {
					title : '',			//标题
					content : '',		//内容
					width : 600,		//默认宽度
					height : 350,		//默认高度
					date : '',			//发布时间
					drag : true,		//弹出层是否可以拖拽
					swfUrl :'',
					isCopy : true
				};
				var options = $.extend(defaults,options);
				
				var w = $(window).width();
				var _h = $(window).height();
				var h = $(document).height();
				var t = parseInt(( _h - options.height - 62 ) / 2) + $(window).scrollTop();
				var l = parseInt(( w - options.width - 28 ) / 2);
				
				var content = "<div style='padding:5px'>" + options.content + "</div>";
							
				var html = "<div class='shade' style='height:" + h + "px;'></div>" + 
					"<div class='tipsBox' style='top:" + t + "px;left:" + l + "px;width:" + (options.width + 20) +"px'>" +
						"<div class='tips-title'><span class='titleTxt'>" + options.title + "</span><span class='closeBtn'></span></div>" +
						"<div class='tips-content' style='height:" + options.height + "px;width:" + options.width + "px'>" + content + "</div>" +
						"<div class='tips-copy'>复制到粘贴板</div>" +
					"</div>";
					
				$('body').append(html);
				$('.closeBtn').live('click',function(e){
					$('.shade, .tipsBox').remove();
					e.stopPropagation();
				});
				if(!options.isCopy){
					$('.tips-copy').hide();
				}else{
					$('.tips-copy').zclip({
						path: options.swfUrl,
						copy: function(){
							var data = "";
							$('.tips-content ul li,.tips-content > div').each(function(){
								//data += ($(this).text().replace(/,/g, '\r'));
								data += ($(this).html().replace(/<[^<>]+>/g, '\r'));
							});
							return data;
						},
						afterCopy:function(){
							alert("复制成功！");
						}
					});
				}
				$(window).resize(function(){
					auto();
				}).scroll(function(){
					auto()
				});
				
				function auto(){
					var t = parseInt(( $(window).height() - options.height - 62 ) / 2) + $(window).scrollTop();
					var l = parseInt(( $(window).width() - options.width - 28 ) / 2);
					$('.tipsBox').css({left:l,top:t});
					$('.shade').css({width: $(window).width(),height:$(window).height() + $(window).scrollTop()});
				}
				
				if(options.drag){
					$('.tips-title').live('mousedown',function(e){
						var that = $(this);
						that.parent().css({opacity:0.6});
						var e = e || window.event;
						var disX = e.clientX - that.parent().offset().left;
						var disY = e.clientY - that.parent().offset().top;
						
						if(that[0].setCapture){
							that[0].onmousemove=fnMove;
							that[0].onmouseup=fnUp;
							that[0].setCapture();
						}
						else{
							document.onmousemove=fnMove;
							document.onmouseup=fnUp;
						}
						
						function fnMove(e){
							var e = e || window.event;
							var l = e.clientX - disX;
							var t = e.clientY - disY;
							(l < 0) && (l = 0);
							(l > $(document).width() - that.parent().outerWidth()) && (l = $(document).width()-that.parent().outerWidth());
							(t < 0) && (t = 0);
							(t > $(document).height() - that.parent().outerHeight()) && (t = $(document).height() - that.parent().outerHeight());
							
							that.parent().css({left: l});
							that.parent().css({top: t});
						}
						
						function fnUp(){
							that.parent().css({opacity:1});
							this.onmousemove=null;
							this.onmouseup=null;
							
							that[0].releaseCapture && that[0].releaseCapture();
						}
						return false;
						
					});
				}
			}
		});
	    
	})(jQuery);
});