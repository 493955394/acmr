define(function(require, exports, module) {
	//维度提交
	var $ = require('$'),
	rootPath = require('rootPath'),
	ui = require('alert'),
	Handlebars = require('handlebars');
	require('fileupload');
	
	//指标提交
	$(document).on('click', '#addDimension', function(e) {
		var modalId = randomNumber();
		var zbHtml = Handlebars.compile($('#submitDimension').html())({'modalId':modalId});
		$('#modal').empty().append(zbHtml).modal({
			closable: false,
			onShow: function() {
				onUpload();
			}
		}).modal('show');
	});
	function randomNumber(){
		var str = '';
		for(var i = 0; i < 6; i ++){
			str += Math.floor(Math.random() * 10);
		}
		return str;
	}
	function onUpload(){
		$('#J_dimension_fileupload').fileupload({
			url: rootPath + "/templatecenter/TemplateHandel.htm?m=uploadDimension&pageId=" +$('#dimensionModelId').val(),
			dataType: 'json',
			singleFileUploads: true,
			maxFileSize: 999000,
			success: function(data) {
				if (data.returncode === 200) {
					$('.ui .progress .bar').css('width', '100%');
				}
			},
			progressall: function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('.ui.progress .bar').css('width', progress + '%');
				$('.ui.progress .progress').html(progress + '%');
			}
		});
	}
	/**
	 * 添加提交维度列表
	 */
	$(document).on('click', '#insertDimension', function(){
		var dimensionHTML =  Handlebars.compile($('#dimensionItem').html())();
		$('#dimensionTab tbody').append(dimensionHTML);
	});
	/**
	 * 删除列表项
	 */
	$(document).on('click', '.J_dimension_delete',function(e){
		var self = this,
			parent = $(self).parent().parent();
		console.log(parent);
		parent.remove();
	})
	/**
	 * 编辑列表
	 */
	$(document).on('click', '#dimensionTab' ,function(event){
		var self = $(event.target);
		if(self[0].nodeName.toUpperCase() === "TD" && self.parent()[0].nodeName.toUpperCase() === "TR" &&
				self.children('a').length===0 && self.children('select').length===0){
			if(self.children('input').length ===0){
				var inputObj = $('<input type="text" class="J_dimension_input"/>');
				$(self).children('.J_input_error_info').remove();
				inputObj.height(self.height());
				inputObj.width(self.width());
				inputObj.val(self.html());
				self.html('');
				self.append(inputObj);
				inputObj[0].focus();
			}
		}
	});
	$(document).on('blur','.J_dimension_input',function(){
		var self = this;
		$(self).parent().html($(self).val());
		self.remove();
	});
	$(document).on('click','.J_submit_dimension',function(){
		submitDimension()
	})
	function submitDimension(){
		var name,
			memo,
			excelId,
			dimensionList=[],
			elementlist,
			len,i,
			sendData;
		elementlist = $('#dimensionTab').find('tr');
		len=elementlist.length;
		$('.J_input_error_info').remove();
		if($.trim($('#dimensionName').val())===''){
			$('#dimensionName').after('<div class="J_input_error_info ui pointing label red">不能为空</div>');
			return;
		}
		if($.trim($('#dimensionName').val()).length > 25){
			$('#dimensionName').after('<div class="J_input_error_info ui pointing label red">不能超过25字符</div>');
			return;
		}
		if($.trim($('#dimensionMemo').val()).length > 300){
			$('#dimensionMemo').after('<div class="J_input_error_info ui pointing label red">不能超过300字符</div>');
			return;
		}
		if(len>20){
			$('#dimensionTab').after('<div class="J_input_error_info ui pointing label red">不能超过20条</div>');
			return;
		}
		
		for(i=0;i<len;i++){
			if($.trim($(elementlist[i]).children('td:eq(1)').html()) !== ''){
				dimensionList.push({
					type: $(elementlist[i]).find('td:eq(0) select option:selected').val(),
					name: $(elementlist[i]).children('td:eq(1)').html(),
					memo: $(elementlist[i]).children('td:eq(2)').html()
				});
				if($(elementlist[i]).children('td:eq(1)').html().length >50){
					$(elementlist[i]).children('td:eq(1)').append('<div class="J_input_error_info ui pointing label red">不能超过50字符</div>');
					return;
				}
				if($(elementlist[i]).children('td:eq(2)').html().length >50){
					$(elementlist[i]).children('td:eq(2)').append('<div class="J_input_error_info ui pointing label red">不能超过50字符</div>');
					return;
				}
			}
		}
		$.ajax({
			url: rootPath+'/templatecenter/TemplateHandel.htm?m=submitAddDimension',
			dataType: 'json',
			data:{
				name : $('#dimensionName').val(),
				memo : $('#dimensionMemo').val(),
				fileId : $('#dimensionModelId').val(),
				dimensionList: JSON.stringify(dimensionList),
			},
			type:'post',
			success:function(data){
				ui.alert("保存成功");
				$("#modal").modal("hide");
//				$("#modal").remove();
			}
		});
	}
});