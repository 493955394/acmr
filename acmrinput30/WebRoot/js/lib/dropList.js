/******* dropList *********
********* liaojin *********
******** 2014-07-03 *******
******* QQ:150334055 ******
**http://www.acmr.com.cn***
********基于jQuery*********/

(function($){
	
	$.fn.extend({
		dropList: function(json,options,callback) {
			var defaults = {
				setIndex : 0,
				setItem : '',
				isText : false
			};
			var options = $.extend(defaults,options);
			
			var that = $(this);
            var create = function (e, className) {
                return $(document.createElement(e)).addClass(className);
            };
			
			var n = options.setIndex;
			n < 0 && (n = 0);
            that.append(
				create('div', 'wdName').html(json.wdname+' ： '),
				create('div', 'dtHtml').append(
					create('div', 'dtHead').attr({ 'node':JSON.stringify(json.nodes[n]),'title':json.nodes[n].name }).html(json.nodes[n].name),
					create('div', 'dtBody').append(
						create('div', 'dtDrag'),
						create('div', 'dtList').append(
							create('ul')
						)
					)
				)
            ).attr({'wdcode': json.wdcode, 'wdname': json.wdname});

			if(options.isText){
				$('.dtBody',that).append(
					create('div', 'dtFoot').append(
						create('input', 'dtText').attr('type','text').val('时间').attr('maxlength',50),
						create('div', 'dtTextBtn').html('确定'),
						create('div', 'nodeErrorInfo').html('<p class="first">例：输入格式如下</p><p>月：201201,201205</p><p>季：2012A,2012B</p><p class="nbsp">2012C,2012D</p></p><p>年：2012,2013</p><p>其他：2013-,last10</p>'),
						create('div', 'nodeArrow')
					)
				);
			}
			if(options.setItem != ''){
				$('.dtHead',that).attr({'node': '{"code":"'+options.setItem+'","name":"'+options.setItem+'","sort":'+4+'}', 'title':options.setItem }).html( options.setItem );
			}
			
			$.each(json.nodes, function(i){
				$('.dtBody ul',that).append(
					create('li').attr({'node': JSON.stringify(json.nodes[i]), 'code':json.nodes[i].code, 'title':json.nodes[i].name}).html(json.nodes[i].name)
				)
			});
			$('.dtBody ul li:last',that).css({'border-bottom': 'none'});
			
			$(that).on('click','.dtHead',function(){
				if($('.dtBody ul',that).is(':empty')){
					alert('列表为空');
					return false;
				}
				$('.dtBody').hide();
				$('.dtBody',that).show();
			});
			
			$(that).on('click','.dtBody ul li',function(){
				var $this = $(this);
				$('.dtHead',that).attr({ 'node':$this.attr('node'), 'title': $this.html()}).html( $this.html() );
				$('.dtBody',that).hide();
				
				//返回回调函数(事件处理)
				callback && callback(that);
			});
			
			//失去焦点隐藏列表
			$(document).click(function(){
				$('.dtBody').hide();
			});
			$(that).on('click','.dtHtml',function(e){
				e.stopPropagation();
			});
			
			//手动插入
			$(that).on('focus','.dtText',function(){
				$('.nodeErrorInfo, .nodeArrow').show();
				if($(this).val() == '时间'){
					$(this).val('');
				}
			});
			
			$(that).on('blur','.dtText',function(){
				$('.nodeErrorInfo, .nodeArrow').hide();
				if($(this).val().replace(/(^\s*)|(\s*$)/g, '') == ''){
					$(this).val('时间');
				}
			});
			$(that).on('click','.dtTextBtn',function(){
				//把上次时间点缓存起来
				sjwdNode=$('.dtHead',that).attr('node');
				sjwdName=$('.dtHead',that).text();	
				
				var value = $('.dtText',that).val();
				//时间：年正则
				var datereg1 =/^\d{4}\-*(\d{4})*\,*(\d{4})*$/;
				//时间：月正则
				var datereg2 =/^\d{4}\d{2}\-*(\d{4}\d{2})*$/;
				//时间：last*格式
				var datereg3 =/^\last|lastnum|first|firstnum\d/;
				//时间：季度正则 eg：2021a，2013D
				var datereg4 =/^\d{4}\a|b|c|d|A|B|C|D$/;
				if(value.replace(/(^\s*)|(\s*$)/g, '') != '' && value != '时间'){
					var arrays = value.split(",");
					var flag =true;
					for (var i=0 ; i< arrays.length ; i++)
					{
						var arrs = arrays[i].split("-");
						for( var j=0;j<arrs.length;j++){
							if(arrs[j] !=''){
								if(!(datereg1.exec(arrs[j])||datereg2.exec(arrs[j])||datereg3.exec(arrs[j])||datereg4.exec(arrs[j]))){
									flag = false;
									break;
								}
							}
						}
						
					}
					
					if(flag){
						$('.dtHead',that).attr({ 'node': '{"code":"'+value+'","name":"'+value+'","sort":'+4+'}', 'title':value}).html( value );
						$('.dtText',that).val('时间');
						$('.dtBody',that).hide();
					}
					else{
						
						alert("格式不正确，请输入例如：例：\"2012\", \"last10\", \"2010-2014\", \"2011-\", \"2012,2013,2014\"");
						return;
						
					}
//					$('.dtHead',that).attr( 'node', '{"code":"'+value+'","name":"'+value+'","sort":'+4+'}' ).html( value );
//					$('.dtText',that).val('时间');
//					$('.dtBody',that).hide();
					
					//返回回调函数(事件处理)
					callback && callback(that);
				}else{
					alert('输入不能为空');
					$('.dtText',that).val('时间');
				}
			});
			
			//拖拽
			$(document).on('mousedown', '.dtDrag', function(){
				var self = $(this);
				
				if(self[0].setCapture){
					self[0].onmousemove=fnMove;
					self[0].onmouseup=fnUp;
					self[0].setCapture();
				}
				else{
					document.onmousemove=fnMove;
					document.onmouseup=fnUp;
				}
				
				function fnMove(e){
					var e = e || window.event;
					var w = e.clientX - self.parent().offset().left - 1;//边框1
					
					(w < 120) && (w = 120);
					self.parent().css({width: w});
				}
				
				function fnUp(){
					this.onmousemove=null;
					this.onmouseup=null;
					
					self[0].releaseCapture && self[0].releaseCapture();
				}
				return false;
				
			});
			
		},
		//获取item总数
		getSize: function(){
			return $('.dtList ul li',this).length;
		},
		//获取选中item
		getItem: function(){
			return JSON.parse( $('.dtHead', this).attr('node') );
		},
		//获取选中index
		getSelIndex: function(){
			var node = $('.dtHead', this).attr('node');
			console.log(node);
			var index = $('.dtBody ul li[node=\''+ node +'\']', this).index();
			return index;
		},
		//获取选中index
		setSelIndex: function(index){
			$('.dtBody ul li', this).eq(index).trigger("click");
		},
		//增加item
		addItem: function(index,node){
			if(arguments.length > 1){
				$("<li node='" + JSON.stringify(arguments[1]) + "'>"+arguments[1].name+"</li>").insertBefore( $('.dtList ul li', this).eq(arguments[0]) );
			}else{
				$('.dtList ul', this).append("<li node='" + JSON.stringify(arguments[0]) + "'>"+arguments[0].name+"</li>");
			}
			if($('.dtBody ul li', this).length ==1){
				$('.dtHead', this).attr({'node': $('.dtBody ul li:eq(0)', this).attr('node'), 'title': $('.dtBody ul li:eq(0)', this).html()}).html($('.dtBody ul li:eq(0)', this).html());
			}
			$('.dtBody ul li:last', this).css({'border-bottom': 'none'}).siblings().css({'border-bottom': '1px solid #aaa'});
		},
		//清除所有item
		clear: function(){
			$('.dtList ul', this).empty();
			$('.dtHead', this).attr({'node': '', 'title': ''}).empty();
		},
		//获取维度
		getWd: function(){
			return {wdcode: $(this).attr('wdcode'), wdname : $(this).attr('wdname') };
		},
		//清除上次事件
		delClick: function(){
			$('.dtBody ul li',this).off('click');
			$('.dtTextBtn',this).off('click');
			$('.dtHead',this).off('click');
		}
	});
    
})(jQuery);