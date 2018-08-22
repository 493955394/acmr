define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		doc = $(document),
		dropdown = require('dropdown'),
		tree = require('tree'),
		ztreeObj;
	require('synonym');

	doc.on('submit', '.J_edit_form', function(event) {
		event.preventDefault();
		var self = this,
			reqestUrl = $(self).prop('action'),
			checkDelegate;
		//检查ucode
		checkDelegate = new VaildNormal();
		//前端检查
		if (!checkDelegate.checkNormal($('input[name="sj"]'), [{'name': 'required','msg': '汇率编码不能为空'}]) ||
				!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '中文单位不能为空'}]) || 
				!checkDelegate.checkNormal($('input[name="rate"]'), [{'name': 'required','msg': '汇率值不能为空'}])
		){
			return;
		}
		
		//后台数据检查 code
//		if(!checkProcodeAsyncExist($('input[name="procode"]').val(),checkDelegate)){
//			common.commonTips('保存失败');
//			return;
//		}
		
		$.ajax({
			url: reqestUrl,
			type: 'post',
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('修改成功');
					var url=common.rootPath+"metadata/hvmgr.htm?m=query&id="+data.param1;
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
//	$(document).on('blur','input[name="procode"]',function(){
//		var self = this,
//			checkDelegate  = new VaildNormal();
//		if(checkDelegate.checkNormal($('input[name="procode"]'), [{'name': 'ch','msg': '上级代码不能包含中文'},{'name':'maxlength','param':17,'msg':'上级代码长度不能超过17个字符'}])){
//			checkProcodeAsyncExist($(self).val(),checkDelegate);
//		}
//	});

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
			$('input[name="code"]').val(treeNode.id);
		}
		var rootNode = [{"id":"","name":"单位树", "open":"true", "isParent":"true"}];
		$.fn.zTree.init($("#treeUnit"), setting,rootNode);
	}
	getTreeUnit();
	
});