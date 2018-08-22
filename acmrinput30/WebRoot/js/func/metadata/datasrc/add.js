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
			reqestUrl = $(self).prop('action'),
			checkDelegate;
		//检查code
		checkDelegate = new VaildNormal();
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '数据来源代码不能为空'},{'name': 'ch','msg': '数据来源代码不能包含中文'},{'name': 'maxlength','msg': '代码长度不能超过'+codeLen+'个字符','param':(parseInt(codeLen)+1)}]) ||
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'}]) ||
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'maxlength','msg': '中文名称长度不能超过50','param':51}])||
			!checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name': 'maxlength','msg': '英文名称长度不能超过50','param':51}])||
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name': 'required','msg': '中文全称不能为空'}]) ||
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name': 'maxlength','msg': '中文全称长度不能超过100','param':101}]) ||
			!checkDelegate.checkNormal($('input[name="ccname_en"]'), [{'name': 'maxlength','msg': '英文全称长度不能超过250','param':251}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp"]'), [{'name': 'maxlength','msg': '中文解释长度不能超过4000','param':4001}])||
			!checkDelegate.checkNormal($(':input[name="cexp_en"]'), [{'name': 'maxlength','msg': '英文解释长度不能超过4000','param':4001}])||
			!checkDelegate.checkNormal($(':input[name="cmemo"]'), [{'name': 'maxlength','msg': '中文备注长度不能超过4000','param':4001}])||
			!checkDelegate.checkNormal($(':input[name="cmemo_en"]'), [{'name': 'maxlength','msg': '英文备注长度不能超过4000','param':4001}])
		){
			common.commonTips('保存失败');
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
			url: reqestUrl,
			type: 'post',
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('保存成功');
					var url=common.rootPath+"metadata/datasrc.htm?m=find&id="+data.param1;
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
		if(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '数据来源代码不能为空'},{'name': 'ch','msg': '数据来源代码不能包含中文'},{'name': 'maxlength','msg': '代码长度不能超过'+codeLen+'个字符','param':(parseInt(codeLen)+1)}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
	
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/datasrc.htm?m=checkCode',
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