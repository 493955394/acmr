define(function(require,exports,module){
	'use strict';
	var $ = require('jquery'),
	dropdown = require('dropdown'),
	Pagination = require('pagination'),
	common = require('common'),
	pjax=require('pjax'),
	tree = require('tree'),
	fileupload = require('fileupload'),
	PackAjax = require('Packajax'),
	modal = require('modal'),
	ZeroClipboard = require('ZeroClipboard'),
	dragwidth = require('dragwidth');
	
	$(document).on('click','#generate_table',function(event){
		$.ajax({
			url:common.rootPath+'metadata/mulindex.htm?m=createCustomTable',
			type:'get',
			timeout:5000,
			success:function(data){
				if(data.returncode == 200){
					$("#generate_table").text("生成中");
					$("#generate_table").addClass("disabled");
					$("#generate_table").attr("disabled", true);
				}
			}
		});
	});
	
});