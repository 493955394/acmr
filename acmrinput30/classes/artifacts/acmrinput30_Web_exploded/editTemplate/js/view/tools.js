define(function(require, exports, module) {
	var $ = require('$'),
		ss = require('excel'),
		pub = require('publicfn'),
		unfrozenBtn = $('li[data-frozen="unfrozen"]'),
		customBtn = $('li[data-frozen="custom"]'),
		frozenState;

	frozenState = ss.getFrozenState();
	if (frozenState) {
		unfrozenBtn.show();
		customBtn.hide();
	} else {
		unfrozenBtn.hide();
		customBtn.show();
	}
	$(document).on('click', '#contentFontContainer span', function(event) {
		event.preventDefault();
		var tool = $(event.currentTarget).data('toolbar');
		switch (tool) {
			case 'bold':
				ss.setFontWeight('sheetId');
				break;
			case 'italic':
				ss.setFontStyle('sheetId');
				break;
			default:
				return;
		}
	});
	$(document).on('click', '#font li', function(event) {
		var displayText,
			fontFamily,
			$currentTarget;
		$currentTarget = $(event.currentTarget);
		fontFamily = $currentTarget.data('family');
		displayText = $currentTarget.text();
		$("#fontShow").text(displayText);
		ss.setFontFamily('1', fontFamily);
	});
	$(document).on('click', '#fontSize li', function(event) {
		var fontSize,
			text,
			$currentTarget;
		$currentTarget = $(event.currentTarget);
		fontSize = $currentTarget.data('size');
		text = $currentTarget.text();
		$("#fontSizeShow").text(text);
		ss.setFontSize('1', fontSize);
	});
	$(document).on('click', '#contentAlignContainer span', function(event) {
		event.preventDefault();
		alignType = $(event.currentTarget).data('align');
		ss.setAlign('sheetId', alignType);
	});
	$(document).on('click', '#mergeCellContainer div[data-toolbar]', function(event) {
		event.preventDefault();
		var merge = $(event.currentTarget).data('toolbar');
		switch (merge) {
			case 'merge':
				ss.mergeCell('sheetId');
				break;
			case 'split':
				ss.splitCell('sheetId');
				break;
		}
	});

	$(document).on('click', '#funcBorder li', function(event) {
		event.preventDefault();
		var borderPositon = $(event.currentTarget).data('border');
		ss.setCellBorder('1', borderPositon);
	});
	$(document).on('click', '#fontColor .color-body', function(event) {
		event.preventDefault();
		var color = $(event.currentTarget).css('background-color');
		ss.setFontColor('1', color);
	});
	$(document).on('click', '#fillColor .color-body', function(event) {
		event.preventDefault();
		var color = $(event.currentTarget).css('background-color');
		ss.setFillColor('1', color);
	});
	$(document).on('click', '#contentFormat li', function(event) {
		event.preventDefault();
		var formatPosition = $(event.currentTarget).data('format');
		// setTextType('1',formatPosition);
		switch (formatPosition) {
			case 'normal':
				ss.setNormalType('1');
				break;
			case 'text':
				ss.setTextType('1');
				break;
			case 'number-1':
				ss.setNumType('1', false, 0);
				break;
			case 'number-2':
				ss.setNumType('1', false, 1);
				break;
			case 'number-3':
				ss.setNumType('1', false, 2);
				break;
			case 'number-4':
				ss.setNumType('1', false, 3);
				break;
			case 'number-5':
				ss.setNumType('1', false, 4);
				break;
			case 'date-1':
				ss.setDateType('1', "yyyy/MM/dd");
				break;
			case 'date-2':
				ss.setDateType('1', "yyyy年MM月dd日");
				break;
			case 'date-3':
				ss.setDateType('1', "yyyy年MM月");
				break;
			case 'percent':
				ss.setPercentType('1', 2);
				break;
			case 'coin-1':
				ss.setCoinType('1', 2, '$');
				break;
			case 'coin-2':
				ss.setCoinType('1', 2, '¥');
				break;
			default:
				ss.setTextType('1');
				break;
			}
	});

	$(document).on('click', '#frozen li', function(event) {
		event.preventDefault();
		var frozenPositon = $(event.currentTarget).data('frozen');

		switch (frozenPositon) {
			case 'unfrozen':
				ss.unFrozen();
				break;
			case 'custom':
				ss.frozen();
				break;
			case 'row':
				ss.rowFrozen();
				break;
			case 'col':
				ss.colFrozen();
				break;
			default:
				break;
		}

		frozenState = ss.getFrozenState();
		if (frozenState) {
			unfrozenBtn.show();
			customBtn.hide();
		} else {
			unfrozenBtn.hide();
			customBtn.show();
		}
		if ($("#rootPath").attr("range")) {
			var startBin = $("#rootPath").attr("range").split("-")[0],
				endBin = $("#rootPath").attr("range").split("-")[1];
			pub.scroll(startBin, endBin);
		}
	});
	$(document).on('click', '#wordWrapContainer', function(event) {
		ss.setWordWrap('1');
	});
	$(document).on('click', '#reviewContainer .fui-section', function(event) {
		var action;
		action = $(event.currentTarget).data('toolbar');
		switch (action) {
			case 'addComment':
				ss.createAddCommentView();
				break;
			case 'editComment':
				ss.createEditCommentView();
				break;
			case 'deleteComment':
				ss.deleteComment();
				break;
			default:
				break;
		}
	});
	
	$(document).on('click', function() {
		$(".widget").removeClass('active');
	});

	$(document).on('click','#insert li', function(e) {
		var operate = $(e.currentTarget).data('type');
		if(operate === 'column'){
			ss.addCol();
		}else{
			ss.addRow();
		}		
	});
	$(document).on('click','#delete li', function(e) {
		var operate = $(e.currentTarget).data('type');
		if(operate === 'column'){
			ss.deleteCol();
		}else{
			ss.deleteRow();
		}		
	});
	//隐藏列
	$(document).on('click', '#hideContainer .fui-section', function(event) {
		var action;
		action = $(event.currentTarget).data('toolbar');
		switch (action) {
			case 'hide':
				ss.colHide();
				break;
			case 'cancelHide':
				ss.colCancelHide();
				break;
			default:
				break;
		}
	});
	$(document).on('click', '#toolBar', function(e) {
		var currentBar,
			widgetList,
			len,
			i = 0,
			$target,
			targetLen;

		if ($(e.target).length) {
			$target = $(e.target);
		}
		if ($(e.target).parents('[data-toolbar]').length) {
			$target = $(e.target).parents('[data-toolbar]');
		}
		if ($target.hasClass('disable')) {
			return false;
		}
		targetLen = $target.length;
		widgetList = $('.widget-list > div');
		widgetList.removeClass('active');
		$('#toolBar .fui-section,#toolBar .section,#toolBar .ico-section').removeClass('active');
		if (targetLen === 0) {
			return;
		}
		len = widgetList.length;
		currentBar = $target.data('toolbar');
		for (; i < len; i++) {
			var currentWidget = widgetList.eq(i);
			if (currentBar === currentWidget.data('widget')) {
				var excuHeight = 0;
				if ($target[0].offsetHeight > $target[0].clientHeight) {
					excuHeight = parseInt(($target[0].offsetHeight - $target[0].clientHeight) / 2, 0) + $target[0].clientHeight;
				} else {
					excuHeight = $target.outerHeight();
				}
				currentWidget.css({
					left: $target.offset().left,
					top: $target.offset().top + excuHeight
				}).addClass('active');
			}
		}
		$target.addClass('active');
		e.stopPropagation();
	});
});
