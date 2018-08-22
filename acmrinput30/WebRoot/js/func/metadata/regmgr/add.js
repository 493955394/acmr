define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		modal = require('modal'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		codeLen = $("#codeLen").val(),
		ztreeObj;
	require('synonym');

	doc.on('submit', '.J_add_form', function(event) {
		event.preventDefault();
		var self = this,
			requestUrl = $(self).prop('action'),
			checkDelegate;
		//检查code
		checkDelegate = new VaildNormal();
		//前端检查
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '地区代码不能为空'},{'name': 'ch','msg': '地区代码不能包含中文'},{'name':'maxlength','param':(parseInt(codeLen)+1),'msg':'地区代码长度不能超过'+codeLen+'个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'},{'name':'maxlength','param':51,'msg':'中文名称长度不能超过50个字符'}]) || 
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name':'maxlength','param':101,'msg':'中文全称长度不能超过100个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name':'maxlength','param':101,'msg':'英文名称长度不能超过100个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="ccname_en"]'), [{'name':'maxlength','param':251,'msg':'英文全称长度不能超过250个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp"]'), [{'name':'maxlength','param':2001,'msg':'中文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp_en"]'), [{'name':'maxlength','param':2001,'msg':'英文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo"]'), [{'name':'maxlength','param':2001,'msg':'中文备注长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo_en"]'), [{'name':'maxlength','param':2001,'msg':'英文备注长度不能超过2000个字符'}]) 
		){
			return;
		}
		//后台数据检查
		if(!checkAsyncExist($('input[name="code"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}
		
		var next = false;
		$.ajax({
			url: common.rootPath+"metadata/zbmgr.htm?m=querySysnName&synonym="+$('[name=synonym]').val(),
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
			url: requestUrl,
			type: 'post',
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('保存成功');
					var url=common.rootPath+"metadata/regmgr.htm?m=query&id="+data.param1;
					url="window.location.href='"+url+"'";
					setTimeout(url,"1000");
				}else{
					common.commonTips('保存失败');
				}
			},
			error: function(e) {
				common.commonTips('保存失败');
			}
		});
	});
	
	$(document).on('blur','input[name="code"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '地区代码不能为空'},{'name': 'ch','msg': '地区代码不能包含中文'},{'name':'maxlength','param':(parseInt(codeLen)+1),'msg':'地区代码长度不能超过'+codeLen+'个字符'}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/regmgr.htm?m=checkCode',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "code="+currentVal,
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					checkDelegate.viewTipAjax($('input[name="code"]'),true);
					flag = true;
				}else{
					checkDelegate.viewTipAjax($('input[name="code"]'),false,data.returndata);
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
		//清空中文解释、英文解释、中文备注、英文备注
		$('[name=cexp], [name=cexp_en], [name=cmemo], [name=cmemo_en]').val('');
		//清空同义词
		$('.J_synonym_table tbody').empty();
		$('[name=synonym]').val('');
	}
});