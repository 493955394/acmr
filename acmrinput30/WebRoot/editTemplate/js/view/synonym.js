define(function(require, exports, module) {
	//维度提交
	var $ = require('$'),
	rootPath = require('rootPath'),
	ui = require('alert'),
	Handlebars = require('handlebars');
	
	//指标提交
	$(document).on('click', '#addSynonym', function(e) {
		var synonymHtml,
			selectId =$('#selectTreeNodeId').val(),
			selectName = $('#selectTreeNodeName').val();
		if(selectId===''){
			ui.alert('未选中有效节点');
			return;
		}
		var synonymHtml = Handlebars.compile($('#submitSynonym').html())({
			code:selectId,
			name:selectName,
		});

		$('#modal').empty().append(synonymHtml).modal({
			closable: false
		}).modal('show');
	});
	/**
	 * 编辑同义词信息
	 */
	$(document).on('click', '.J_edit_text' ,function(event){
		var self = $(this);
		if(self.children('input').length ===0){
			$(self).children('.J_input_error_info').remove();
			var inputObj = $('<input type="text" class="J_synonym_input"/>');
			inputObj.height(self.height()+ self.css('padding-top')+self.css('padding-bottom'));
			inputObj.width(self.width()+self.css('padding-left')+self.css('padding-right'));
			inputObj.val(self.html());
			self.html('');
			self.append(inputObj);
			inputObj[0].focus();
		}
	});
	$(document).on('blur','.J_synonym_input',function(){
		var self = this;
		$(self).parent().html($(self).val());
		self.remove();
	});
	
	$(document).on('click','.J_submit_synonym',function(){
		var code,
			name,
			synonym,
			memo,
			data = $('#synonymTab').find('tr')[1];
		
		code = $(data).children('td:eq(0)').html();
		name = $(data).children('td:eq(1)').html();
		synonym = $(data).children('td:eq(2)').html();
		memo = $(data).children('td:eq(3)').html();
		
		$('.J_input_error_info').remove();
		if($.trim(synonym)===''){
			$(data).children('td:eq(2)').append('<div class="J_input_error_info ui pointing label red">不能为空</div>');
			return;
		}
		if($.trim(synonym).length > 25){
			$(data).children('td:eq(2)').append('<div class="J_input_error_info ui pointing label red">不能超过25字符</div>');
			return;
		}
		if($.trim(memo).length > 50){
			$(data).children('td:eq(3)').append('<div class="J_input_error_info ui pointing label red">不能超过50字符</div>');
			return;
		}
		
		$.ajax({
			url: rootPath+'/templatecenter/TemplateHandel.htm?m=submitAddSynonym',
			dataType: 'json',
			data:{
				code : code,
				name : name,
				synonym : synonym,
				memo: memo,
				dimension: $('#selectTreeWd').val()
			},
			type:'post',
			success:function(data){
				ui.alert("保存成功");
				$("#modal").modal("hide");
			}
		});
	});

});