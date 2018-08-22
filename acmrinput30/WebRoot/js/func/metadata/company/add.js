define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		modal = require('modal'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		treeUnit,
		ztreeObj,
		//保存用户列表的数组
		hidUserArray = [];

	require('synonym');
	doc.on('click','.Reset',function(event){
		hidUserArray = [];
		$('.J_user_list').empty();
		var nodes = ztreeObj.getCheckedNodes(true);
		for (var i=0, l=nodes.length; i < l; i++) {
			nodes[i].checked = false;
			ztreeObj.removeNode(nodes[i]);
		}
	});
	doc.on('submit', '.J_add_form', function(event) {
		event.preventDefault();
		var self = this,
			requestUrl = $(self).prop('action'),
			checkDelegate;
		//检查code
		checkDelegate = new VaildNormal();
		//前端检查
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标代码不能为空'},{'name': 'ch','msg': '指标代码不能包含中文'},{'name':'maxlength','param':21,'msg':'指标代码长度不能超过20个字符'},{'name':'minlength','param':2,'msg':'指标代码长度不能少于1个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'},{'name':'maxlength','param':101,'msg':'中文名称长度不能超过100个字符'}]) || 
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name':'maxlength','param':101,'msg':'中文全称长度不能超过100个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name':'maxlength','param':251,'msg':'英文名称长度不能超过250个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="ccname_en"]'), [{'name':'maxlength','param':251,'msg':'英文全称长度不能超过250个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp"]'), [{'name':'maxlength','param':2001,'msg':'中文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp_en"]'), [{'name':'maxlength','param':2001,'msg':'英文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo"]'), [{'name':'maxlength','param':2001,'msg':'中文备注长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo_en"]'), [{'name':'maxlength','param':2001,'msg':'英文备注长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gpdm"]'), [{'name':'maxlength','param':2001,'msg':'股票代码长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="qyfl1"]'), [{'name':'maxlength','param':2001,'msg':'企业分类1长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="qyfl2"]'), [{'name':'maxlength','param':2001,'msg':'企业分类2长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gpjc"]'), [{'name':'maxlength','param':2001,'msg':'股票简称长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="hy"]'), [{'name':'maxlength','param':2001,'msg':'股票简称长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gsxx"]'), [{'name':'maxlength','param':2001,'msg':'上市板块长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gswz"]'), [{'name':'maxlength','param':2001,'msg':'公司网址长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gsdz"]'), [{'name':'maxlength','param':2001,'msg':'公司地址长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="gssx"]'), [{'name':'maxlength','param':2001,'msg':'公司地址长度不能超过2000个字符'}])
		){
			return;
		}
		//后台数据检查 code
		if(!checkAsyncExist($('input[name="code"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}
//		//后台数据检查 nbscode
//		if ($('input[name="nbscode"]').val().replace(/(^s*)|(s*$)/g, "").length !=0) 
//		{
//			if(!checkNbsAsyncExist($('input[name="nbscode"]').val(),checkDelegate)){
//				common.commonTips('保存失败');
//				return;
//			}
//		}
		var next = false;
		$.ajax({
			url: common.rootPath+"metadata/company.htm?m=querySysnName&synonym="+$('[name=synonym]').val(),
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
			data: $(self).serialize()+'&classprocodes='+hidUserArray.toString(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('保存成功');
					var url=common.rootPath+"metadata/company.htm?m=query&id="+data.param1;
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
	doc.on('click','.J_group_code',function(event){
		event.preventDefault();
		var len = $('#J_group_modal').length,
			modalContent = '<div class="modal-header">';
			modalContent +='<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
			modalContent +='<h4 class="modal-title" id="myModalLabel">优先分组代码</h4>';
			modalContent +='</div>';
			modalContent +='<div class="modal-body">';
			modalContent +='<div class="panel panel-default">';
			modalContent +='<div class="panel-heading">';
			modalContent +='<div class="form-inline">';
			modalContent +='<div class="form-group reset-form-group" style="position:relative">';
			modalContent +='<input type="text" class="form-control input-sm J_zbinfo" style="width:200px" placeholder="输入搜索内容">';
			modalContent +='<div class="timely-search"><ul class="timely-search-content J_search_result"></ul></div>'
			modalContent +='</div>';
			modalContent +='</div>';
			modalContent +='</div>';
			modalContent +='<div class="panel-body reset-filter-group-body">';
			modalContent +='<div class="filter-group"><ul class="ztree" id="treeGroup"></ul></div>';
			modalContent +='<div class="filter-group-func">';
			modalContent +='<div class="btn-group-func btn-add">';
			modalContent +='<button class="btn btn-sm btn-default J_btn_add_group" type="button">添加</button>';
			modalContent +='</div>';
			modalContent +='<div class="btn-group-func btn-del">';
			modalContent +='<button class="btn btn-sm btn-default J_btn_del_group" type="button">删除</button>';
			modalContent +='</div>';
			modalContent +='</div>';	
			modalContent +='<div class="filter-group"><div class="list-group J_user_list"></div></div>';
			modalContent +='</div>';
			modalContent +='</div>';
			modalContent +='</div>';
			modalContent +='<div class="modal-footer">';
			modalContent +='<button type="button" class="btn btn-primary J_btn_save_group_users" data-dismiss="modal">保存</button>';
			modalContent +='</div>';
		common.buildModelHTML('J_group_modal',modalContent);
		$('#J_group_modal').modal('show');
		if(len === 0){
			getTree();
		}
	});
	/**
	 * 添加优先分组代码
	 */
	doc.on('click','.J_btn_add_group',function(event){
		event.preventDefault();
		var self = this,
			currentObj = ztreeObj.getCheckedNodes(),
			len = currentObj.length,
			i = 0,
			trs = '',
			templen = hidUserArray.length;
		for(;i<len;i++){
			hidUserArray = common.oprArray('add',currentObj[i].id,hidUserArray);
			//成功增加数组以后才进行页面新增
			if(hidUserArray.length > templen){
				templen = hidUserArray.length;
				trs +='<a id="'+currentObj[i].id+'" href="javascript:;" class="list-group-item J_del_user">'+currentObj[i].name+'</a>';
			}
		}
		$('.J_user_list').append(trs);
	});
	/**
	 * 选中要删除分组代码用户
	 */
	doc.on('click','.J_del_user',function(event){
		var self = this;
		$('.J_user_list a').removeClass('active');
		$(self).addClass('active');
	});
	doc.on('click','.J_btn_save_group_users',function(event){
		event.preventDefault();
	});
	/**
	 * 搜索
	 */
	doc.on('input propertychange','.J_zbinfo',function(){
		var self = this,
		currentText = $(self).val().trim();
		if(currentText == ''){
			$('.J_search_result').hide();
			return;
		}
		$.ajax({
			url: common.rootPath+'company.htm',
			type: 'post',
			data: "m=findGroup&cname="+currentText,
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					var returndata = data.returndata;
					var list = returndata.data,
						len = list.length,
						trs = '',
						i = 0;
					for (; i < len; i++) {
						trs+='<li class="J_search_item" id="'+list[i].code+'">'+list[i].cname+"</li>";
					}
					$('.J_search_result').html(trs);
					$('.J_search_result').show();
				}
			},
			error: function(e) {}
		});
	});
	doc.on('focus','.J_zbinfo',function(){
		$('.J_search_result').show();
	});
	/**
	 * 搜索结果选项添加
	 */
	doc.on('click','.J_search_item',function(event){
		var self = this,
		currentId = $(self).attr('id'),
		currentText = $(self).text();
		
		var self = this,
			currentObj = ztreeObj.getCheckedNodes(), 
			len = currentObj.length,
			i = 0,
			trs = '',
			templen = hidUserArray.length;
			hidUserArray = common.oprArray('add',currentId,hidUserArray);
			//成功增加数组以后才进行页面新增
			if(hidUserArray.length > templen){
				templen = hidUserArray.length;
				trs +='<a id="'+currentId+'" href="javascript:;" class="list-group-item J_del_user">'+currentText+'</a>';
			}
		$('.J_user_list').append(trs);
	});
	
	$('body').on('click',function(event){
		var target = event.target;
		if(!$(target).hasClass('J_search_item') && !$(target).hasClass('J_zbinfo')){
			$('.J_search_result').hide();
		}
	});
	/**
	 * 删除已选择的优先分组代码
	 */
	doc.on('click','.J_btn_del_group',function(event){
		event.preventDefault();
		var selectedObj = $('.J_user_list a.active').eq(0),
			selectedId = selectedObj.attr('id'),
			templen = hidUserArray.length;
		hidUserArray = common.oprArray('del',selectedId,hidUserArray);
		if(hidUserArray.length < templen){
			templen = hidUserArray.length;
			selectedObj.remove();
		}
	});
	function getTree(){
		var setting = {
			async: {
				enable: true,
				url: common.rootPath+'company.htm?m=findZbTree',
				contentType: 'application/json',
				type: 'get',
				autoParam: ["id"]
			},
			view:{
				selectMulti:false
			},
			check:{
				enable:true,
				chkboxType:{ "Y" : "", "N" : "" }
			}
		}
		ztreeObj =  $.fn.zTree.init($("#treeGroup"), setting);
	}
	/**
	 * 校验指标代码
	 */
	$(document).on('blur','input[name="code"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标代码不能为空'},{'name': 'ch','msg': '指标代码不能包含中文'},{'name':'maxlength','param':21,'msg':'指标代码长度不能超过20个字符'},{'name':'minlength','param':2,'msg':'指标代码长度不能少于1个字符'}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
//	/**
//	 * 校验nbs代码
//	 */
//	$(document).on('blur','input[name="nbscode"]',function(){
//		var self = this,
//			checkDelegate  = new VaildNormal();
//		if ($(self).val().replace(/(^s*)|(s*$)/g, "").length !=0) 
//		{
//			checkNbsAsyncExist($(self).val(),checkDelegate);
//		}
//	});
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		var procode = $('input[name="procode"]').val();
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '指标代码不能为空'},{'name': 'ch','msg': '指标代码不能包含中文'},{'name':'maxlength','param':21,'msg':'指标代码长度不能超过20个字符'},{'name':'minlength','param':2,'msg':'指标代码长度不能少于1个字符'}])){
			return;
		}
		$.ajax({
			url: common.rootPath+'metadata/company.htm?m=checkCode',
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
	 * 后台检验nbs码是否重复
	 */
	function checkNbsAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/company.htm?m=checkCode',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "field=nbscode&code="+currentVal,
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					checkDelegate.viewTipAjax($('input[name="nbscode"]'),true);
					flag = true;
				}else{
					checkDelegate.viewTipAjax($('input[name="nbscode"]'),false,data.returndata);
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
		//清空企业分类1、企业分类2、股票简称、股票代码、所属行业、上市板块、公司属性、公司地址、公司网址
		$('[name=qyfl1], [name=qyfl2], [name=gpjc], [name=gpdm], [name=hy], [name=gsxx], [name=gssx], [name=gsdz], [name=gswz]').val('');
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