define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		common = require('common'),
		doc = $(document);

	doc.on('click','.glyphicon-trash',function(event){
		var tr = $(this).parent().parent();
		tr.remove();
		$("[name=synonym]").val(getSynonymList());
	});
	
	doc.on('click','.J_add_synonym',function(event){
		var tbody = $(".J_synonym_table tbody"),
			tr = $('<tr/>'),
			td = '<td>'+
					'<span class="item"></span>' +
					'<input type="text" class="synonym_text">' +
					'<span class="glyphicon glyphicon-pencil btn-sm"></span>' +
					'<span class="glyphicon glyphicon-trash btn-sm"></span>' +
				'</td>';
		tr.append(td);
		tbody.append(tr);
		$(".item", tr).hide();
		$('.synonym_text', tr).show().select();
		$("[name=synonym]").val(getSynonymList());
	});
	
	doc.on('click','.glyphicon-pencil',function(event){
		var td = $(this).parent(),
			text = $(".item", td).html();
		$(".synonym_text", td).val(text).show().select();
		$(".item", td).hide();
	});

	doc.on('blur','.synonym_text',function(event){
		var td = $(this).parent(),
			text = $(this).val(),
			next = false;
		$(".item", td).html('');
		if( $.trim(text) === '' ){
			td.parent().remove();
		}else{
			$(".J_synonym_table tbody tr").each(function(){
				var oldText = $('.item', this).text();
				if(text == oldText){
					next = true;
				}
			});
			if(next){
				$(this).focus();
				common.commonTips('同义名不能重复');
				return false;
			}
			$(".item", td).html(text).show();
			$(this).hide();
		}
		$("[name=synonym]").val(getSynonymList());
	});

	function getSynonymList(){
		var html = "";
		$(".J_synonym_table tr").each(function(i){
			if(i !== 0){
				html += "__";
			}
			html += $(".item", this).html();
		});
		return html;
	}
});