define(function(require,exports,module){
	'use strict';
	var $ = require('jquery'),
		dropdown = require('dropdown'),
		common = require('common'),
		modal = require('modal'),
		fileupload = require('fileupload'), 
		PackAjax = require('Packajax'),
		VaildNormal = require('vaildnormal'),
		pjax=require('pjax'),
		tab = require('tab');
	
	/**
	 * 单条数据上传
	 */
	$(document).on('click','.J_data_upLoad_now',function(event) {
		event.preventDefault();
		var self = this,


		currentId;
		if ($(self).attr('id') != undefined) {// id有值，就是单个设置审批
			currentId = $(self).attr('id');
		} else {
			currentId = hidObj.hidWriteArray.toString();
		}
		;
		$('input[name=code]').val(currentId);
		if ($('#J_modal_data_upLoad_now').length > 0) {
			$('#J_modal_data_upLoad_now').remove();
		}
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">数据单条上传选择页面</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(不能超过5MB)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload" multiple></span></div>';
		modalContent += '<div class="form-group"><div id="J_progress" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '<div id="errordiv" style="display:none"><a id="errorlink" target="_blank">查看详细错误信息</a></div></div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_save_file">关闭</button>';
		modalContent += '<input type="hidden" name="code" value="'
				+ currentId
				+ '" /><input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('J_modal_data_upLoad_now',
				modalContent);
		$('#J_modal_data_upLoad_now').modal('show');
		initFileUpload();
	});
	/**
	 * 关联模板文件上传
	 */

	function initFileUpload(){
		$('#J_fileupload',document).fileupload({
	        url: common.rootPath + 'dataload/datamanageload.htm?m=upload&taskCode='+$('input[name="code"]').val(),
	        dataType: 'json',
	        done: function (e, data) {
	            if(data.result.returncode == 200){
	            	var code=$('input[name="code"]').val();
	            	var t=$('a[name='+code+']').parent().next().next().next().next().next().next().html('已上传');
	            	window.location.href=common.rootPath+'dataload/datamanageload.htm?m=checkTableCss&id='+$('input[name="code"]').val();
	            }
	            else if(data.result.returncode == 300){
	            	$('#errorlink').attr("href", common.rootPath + "error.htm?type=excelerror&id="+data.result.returndata);
	            	$('#errordiv').show();
	            }
	            else{
	              	$('#errordiv').hide();
	            	alert("数据上传失败");
	            }
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    });

	}
	/**
	 * 关闭按钮
	 */
	$(document).on('click', '.J_btn_save_file', function(event) {
		event.preventDefault();
		$('#J_modal_data_upLoad_now').modal('hide');
		location.reload();
	});
	/**
	 * 查看数据审批结果
	 */
	$(document).on('click','.J_single_data_overview',function(event) {
						event.preventDefault();
						var self = this, currentId = $(self).prop('id'), requestUrl = $(
								self).prop('href');
						window.location.href = requestUrl + "&id=" + currentId;
					});
	/**
	 * 通过
	 */
	$(document).on('click','.J_batch_submit_checkbox_data',function(event){
		event.preventDefault();
		var self = this,
		currentId;
		currentId = $(self).attr('id');

		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button><h4 class="modal-title" id="myModalLabel">请填写审批意见并确认通过（只影响有审批权限的待审批项）</h4>';
		modalContent += '</div>';
		modalContent += '<form class="J_batch_submit_checkbox_approve">';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> ';
		modalContent += '<textarea class="form-control" name="data" id="approveReason" rows="5" ></textarea>';
		modalContent += '</div>';
		modalContent += '<div class="form-group"> ';
		modalContent +='<label class="col-sm-12 control-label" id="errorShow"></label>';
		modalContent += '</div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<button type="submit" class="btn btn-primary">确定</button><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
		modalContent += '<input type="hidden" name="ids" value="">';
		modalContent += '</div>';
		modalContent += '</form>'
		
		 common.buildModelHTML('J_write_modal_approve',modalContent);
		 $('#J_write_modal_approve').modal('show');
		  $('input[name="ids"]').val(currentId);
	});
	/**
	 * 点击通过
	 */
	$(document).on('submit','.J_batch_submit_checkbox_approve',function(event){
		event.preventDefault();
		var currentId=$('input[name=ids]').val();
		
		var self = this,
		approveReason,
		executAjax;
		approveReason = $('#approveReason').val();
		executAjax = new PackAjax();
			executAjax({
				url:common.rootPath + 'dataapprove/dataapprove.htm?m=toPassMoreOrOne',
				data:$(self).serialize(),
				success:function(data){
						$('#J_write_modal_approve').modal('hide');
						$('#'+currentId).parents('tr').remove();
						common.commonTips('审批成功');
						$.pjax({
							url: common.rootPath+ "index.htm",
							container: '.J_template_data_table'
						})
				}		
				
			});		
	});
	
	/**
	 * 退回
	 */
	$(document).on('click','.J_write_bach_back',function(event){
		event.preventDefault();
		var self = this,
		currentId;
		currentId = $(self).attr('id');   
		var modalContent = '<div class="modal-header">';
			modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button><h4 class="modal-title" id="myModalLabel">请填写审批意见并确认退回（只影响有审批权限的待审批项）</h4>';
			modalContent += '</div>';
			modalContent += '<form class="J_batch_submit_checkbox_back">';
			modalContent += '<div class="modal-body">';
			modalContent += '<div class="form-group"> ';
			modalContent += '<textarea class="form-control" name="data" id="backReason" rows="5" ></textarea>';
			modalContent += '</div>';
			modalContent += '<div class="form-group"> ';
			modalContent +='<label class="col-sm-12 control-label" id="errorShow"></label>';
			modalContent += '</div>';
			modalContent += '</div>';
			modalContent += '<div class="modal-footer">';
			modalContent += '<button type="submit" class="btn btn-primary">确定</button><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
			modalContent += '<input type="hidden" name="ids" value="">';
			modalContent += '</div>';
			modalContent += '</form>'
		    common.buildModelHTML('J_write_modal_back',modalContent);
		   $('#J_write_modal_back').modal('show');
		   $('input[name="ids"]').val(currentId);
	});
	
	/**
	 * 退回
	 */
	$(document).on('submit','.J_batch_submit_checkbox_back',function(event){
		event.preventDefault();
		var self = this,
		backReason,
		executAjax;
		var currentId=$('input[name=ids]').val();
		backReason = $('#backReason').val();
		if(backReason==''|| backReason==null){
			$('#errorShow').empty();
			$('#errorShow').append("<font color='red'>退回原因不能为空！</font>");
			return;
		}
		var checkDelegate = new VaildNormal();
		executAjax = new PackAjax();
			executAjax({
				url:common.rootPath + 'dataapprove/dataapprove.htm?m=toRebackMoreOrOne',
				data:$(self).serialize(),
				success:function(data){
					if(data.returncode == 200){
						$('#J_write_modal_back').modal('hide');
						$('#'+currentId).parents('tr').remove();
						common.commonTips('退回成功');
						$.pjax({
							url: common.rootPath+ "index.htm",
							container: '.J_template_data_table'
						})
					}		
				}
			});		
	});
	/**
	 * 导出方式:模板导出
	 */
	$(document).on('click','.J_single_template_export_data',function(event) {
			event.preventDefault();
			var self = this, currentId = $(self).prop('id'), requestUrl = $(
					self).prop('href');
			if ($(self).attr("id") == undefined) {
				currentId = hidObj.hidAllArray.toString();
			}
			if (currentId === "") {
				common.commonTips('请选择导出项');
				return;
			}
			window.location.href = requestUrl + "&id=" + currentId;
		});
	
	//自适应高级查询
	$(window).resize(function(){
		autoMarginTop();
	});
	autoMarginTop();
	function autoMarginTop(){
		var marginTop = $(".chaxun_r").height()- $(".chaxun_r_title").height() - $(".highs").height() - 10;
		$(".highs ul").css({"margin-top": parseInt(marginTop/2), "margin-bottom": parseInt(marginTop/2)});
	}
});