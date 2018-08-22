define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		ztreeObj;
	
	require('synonym');

	doc.on('submit', '.J_edit_form', function(event) {
		event.preventDefault();
		var self = this,
			reqestUrl = $(self).prop('action'),
			checkDelegate;
		//检查ucode
		checkDelegate = new VaildNormal();
		if (!(checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'}])) ||
			!(checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'maxlength','msg': '中文名称长度不能超过50','param':51}]))||
			!(checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name': 'maxlength','msg': '英文名称长度不能超过50','param':51}]))||
			!(checkDelegate.checkNormal($('input[name="rate"]'), [{'name': 'maxlength','msg': '换算率长度不能超过38','param':39}]))||
			!(checkDelegate.checkNormal($('input[name="cmemo"]'), [{'name': 'maxlength','msg': '中文备注长度不能超过4000','param':4001}]))||
			!(checkDelegate.checkNormal($('input[name="cmemo_en"]'), [{'name': 'maxlength','msg': '英文备注长度不能超过2000','param':2001}]))
		){
			return;
		}
		
		//后台数据检查 code
		if(!checkProcodeAsyncExist($('input[name="procode"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}
		
		var next = false;
		$.ajax({
			url: common.rootPath+"metadata/zbmgr.htm?m=querySysnName&synonym="+$('[name=synonym]').val()+"&code="+$('input[name="code"]').val(),
			type: 'get',
			async: false,
			dataType: 'json',
			success: function(data) {
				if(data.returncode != 200){
					common.commonTips(data.returndata);
					next = true;
				}
			},
			error: function(e) {
				common.commonTips('操作失败');
				next = true;
			}
		});
		if(next){
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
					var url=common.rootPath+"metadata/unit.htm?m=findDepTree&id="+data.param1;
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
	 * 校验指标代码
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
			url: common.rootPath+'metadata/unit.htm?m=checkProCode',
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
		//清空中文备注、英文备注
		$('[name=cmemo], [name=cmemo_en]').val('');
		//清空同义词
		$('.J_synonym_table tbody').empty();
		$('[name=synonym]').val('');
	}
});