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
		if (!checkDelegate.checkNormal($('input[name="sj"]'), [{'name': 'required','msg': '时间不能为空'}]) ||
			!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '单位不能为空'}]) || 
			!checkDelegate.checkNormal($('input[name="rate"]'), [{'name': 'required','msg': '美元汇率不能为空'}])
		){
			return;
		}
		//后台数据检查
//		if(!checkAsyncExist($('input[name="code"]').val(),checkDelegate)){
//			common.commonTips('保存失败');
//			return;
//		}
		
		$.ajax({
			url: requestUrl,
			type: 'post',
			async: false,
			data: $(self).serialize(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('保存成功');
					var url=common.rootPath+"metadata/hvmgr.htm?m=query&id="+data.param1;
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
			checkExist();
		}
		var rootNode = [{"id":"","name":"单位树", "open":"false", "isParent":"true"}];
		$.fn.zTree.init($("#treeUnit"), setting,rootNode);
	}
	getTreeUnit();
	
	/**
	 * 获取时间
	 */
	function getSjTree(){
		//菜单树
		var setting = {
			async: {
				enable: true,
				url: common.rootPath+'metadata/mdate.htm?m=findMdateTree',
				contentType: 'application/json',
				type: 'get',
				autoParam: ["id"]
			},
			callback: {
				onClick: clickEvent
			}
		}
		
		function clickEvent(event, treeid, treeNode) {
			$('input[name="sjName"]').val(treeNode.name);
			$('input[name="sj"]').val(treeNode.id);
			checkExist();
		}
		var rootNode = [{"id":"","name":"全部时间", "open":"false", "isParent":"true"}];
		$.fn.zTree.init($("#treeSj"), setting,rootNode);
	}
	getSjTree();
	
	
	function checkExist(){
		var sj = $('input[name="sj"]').val();
		var code = $('input[name="code"]').val(); 
		$.ajax({
			url: common.rootPath+'metadata/hvmgr.htm?m=checkExist',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "code="+code+"&sj="+sj,
			dataType: 'json',
			success: function(data) {
				$('input[name="rate"]').val(data.returndata);
			},
			error: function(e) {}
		});
	}

});