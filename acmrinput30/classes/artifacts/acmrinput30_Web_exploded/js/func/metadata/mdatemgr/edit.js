define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		modal = require('modal'),
		datepicker = require('datepicker'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		ztreeObj;
	require('synonym');

	/**
	 * 初始化时间控件
	 */
	$(".J_datepicker").datepicker({
    	changeMonth:true,
    	changeYear:true,
    	dateFormat:'yy-mm-dd'
    });
	
	doc.on('submit', '.J_edit_form', function(event) {
		event.preventDefault();
		var self = this,
			reqestUrl = $(self).prop('action'),
			checkDelegate;
		//检查ucode
		checkDelegate = new VaildNormal();
		if (!(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '时间代码不能为空'}])) || 
				!(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'maxlength','msg': '代码长度不能超过21','param':21}])) || 
			!(checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '时间名不能为空'}])) || 
			!(checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'maxlength','msg': '中文名称长度不能超过50','param':51}]))||
			!(checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name': 'maxlength','msg': '英文名称长度不能超过50','param':51}]))
		){
			common.commonTips('修改失败');
			return;
		}
		
		//后台数据检查 code
		if(!checkProcodeAsyncExist($('input[name="procode"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}
		
		$.ajax({
			url: reqestUrl,
			type: 'post',
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('修改成功');
					var url=common.rootPath+"metadata/mdate.htm?m=find&id="+data.param1;
					url="window.location.href='"+url+"'";
					setTimeout(url,"1000");
				}else{
					common.commonTips('修改失败');
				}
			},
			error: function(e) {
				common.commonTips('修改失败');
			}
		});
	});
	
	/**
	 * 校验代码
	 */
	$(document).on('blur','input[name="procode"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($('input[name="procode"]'), [{'name': 'ch','msg': '上级代码不能包含中文'},{'name':'maxlength','param':17,'msg':'上级代码长度不能超过17个字符'}])){
			checkProcodeAsyncExist($(self).val(),checkDelegate);
		}
	});
	/**
	 * 后台检验procode是否存在
	 */
	function checkProcodeAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/mdate.htm?m=checkProCode',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "procode="+currentVal+"&code="+$('input[name="code"]').val(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					checkDelegate.viewTipAjax($('input[name="procode"]'),true);
					$('#procodeName').val(data.param1);
					flag = true;
				}else{
					$('#procodeName').val("");
					checkDelegate.viewTipAjax($('input[name="procode"]'),false,data.returndata);
				}
			},
			error: function(e) {}
		});
		return flag;
	}

	//折叠
	doc.on('change', '[name=ifdata]', function(event){
		var isGroup = $(this).val();
		if(isGroup === '0'){
			clearContent();
			$('.hidden-group').hide();
		}else{
			$('.hidden-group').show();
		}
	});

	function clearContent(){
		//清空开始时间、结束时间
		$('[name=btime], [name=etime]').val('');
	}
});