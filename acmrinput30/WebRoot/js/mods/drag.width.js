/**
 * 左右拖拽插件
 */
define(function(require, exports, module) {
	var jQuery = require('jquery');
	(function($) {
		var oLine = null;
		var oBox = null;
		var oTop = null;
		var oBottom = null;
		$.fn.dragwidth = function(options) {
			var defaults = {
				height : 570,
				background : '#f6f9fe'
			};
			var opts = $.extend(defaults, options);

			oLine = document.createElement('div');
			oLine.setAttribute('class', 'dragline');
			oBox = $(this)[0];
			if(oBox ==undefined)
			{
				return;
			}
			oBox.style.position='relative';
			var childdren = oBox.childNodes;
			oTop = null;
			oBottom = null;
			var i = 0;
			for (i = 0; i < childdren.length; i++) {
				var child = childdren[i];
				if (child.nodeName != 'DIV') {
					continue;
				}
				if (oTop == null) {
					oTop = child;
				} else if (oBottom == null) {
					oBottom = child;
				} else {
					break;
				}
			}
			oBox.appendChild(oLine);
			oLine.style.height = opts.height + 'px';
			oTop.style.background = opts.background;
			oLine.onmousedown = function(e) {
				var disX = (e || event).clientX;
				oLine.left = oLine.offsetLeft;
				document.onmousemove = function(e) {
					var iT = oLine.left + ((e || event).clientX - disX);
					var e = e || window.event, tarnameb = e.target
							|| e.srcElement;
					var min = 100;
					var maxT = oBox.clientWidth - oLine.offsetWidth - 4*min;
					if (options.maxwidth) {
						maxT=options.maxwidth
					}
					//oLine.style.margin = 0;

					iT < min && (iT = min);
					iT > maxT && (iT = maxT);
					oLine.style.left = oTop.style.width = (iT-2)*100/oBox.clientWidth + "%";//多减2pt：防止计算误差
					oBottom.style.width = (oBox.clientWidth - iT-2)*100/oBox.clientWidth + "%";//多减2pt：防止计算误差
					return false
				};
				document.onmouseup = function() {
					document.onmousemove = null;
					document.onmouseup = null;
					oLine.releaseCapture && oLine.releaseCapture()
				};
				oLine.setCapture && oLine.setCapture();
				return false
			};
		};
	})(jQuery);
});