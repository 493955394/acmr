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
		treeUnit,
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
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标分组代码不能为空'},{'name': 'ch','msg': '指标分组代码不能包含中文'},{'name':'maxlength','param':(parseInt(codeLen)+1),'msg':'指标分组代码长度不能超过'+ codeLen +'个字符'},{'name':'minlength','param':4,'msg':'指标分组代码长度不能少于5个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'},{'name':'maxlength','param':501,'msg':'中文名称长度不能超过500个字符'}]) || 
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name':'maxlength','param':501,'msg':'中文全称长度不能超过500个字符'}]) ||
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
			async: false,
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('保存成功');
					var url=common.rootPath+"metadata/zbgmgr.htm?m=query&id="+data.param1;
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
		if(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标分组代码不能为空'},{'name': 'ch','msg': '指标分组代码不能包含中文'},{'name':'maxlength','param':(parseInt(codeLen)+1),'msg':'指标分组代码长度不能超过'+ codeLen +'个字符'},{'name':'minlength','param':4,'msg':'指标分组代码长度不能少于5个字符'}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		var procode = $('input[name="procode"]').val();
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标分组代码不能为空'},{'name': 'ch','msg': '指标分组代码不能包含中文'},{'name':'maxlength','param':21,'msg':'指标分组代码长度不能超过20个字符'},{'name':'minlength','param':4,'msg':'指标分组代码长度不能少于5个字符'}])){
			return;
		}
		$.ajax({
			url: common.rootPath+'metadata/zbgmgr.htm?m=checkCode',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "code="+currentVal+"&procode="+procode,
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
	/**
	 * 获取单位数
	 */
	function getTreeUnit(){
		//菜单树
		var setting = {
			async: {
				enable: true,
				url: common.rootPath+'metadata/unit.htm?m=findUnitTree',
				contentType: 'application/json',
				type: 'get',
				autoParam: ["id"]
			},
			callback: {
				onClick: clickEvent
			}
		}
		
		function clickEvent(event, treeid, treeNode) {
			$('input[name="unitName"]').val(treeNode.name);
			$('input[name="unitcode"]').val(treeNode.id);
		}
		var rootNode = [{"id":"","name":"单位树", "open":"true", "isParent":"true"}];
		treeUnit = $.fn.zTree.init($("#treeUnit"), setting,rootNode);
	}
	getTreeUnit();

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
		//清空小数点位数
		$('[name=dotcount]').val('');
		//清空同义词
		$('.J_synonym_table tbody').empty();
		$('[name=synonym]').val('');
		//清空中文单位
		$('[name=unitName], [name=unitcode]').val('');
		treeUnit.cancelSelectedNode();
		treeUnit.expandAll(false);
	}
});