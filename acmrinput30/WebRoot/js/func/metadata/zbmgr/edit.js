define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		modal = require('modal'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		ztreeObj,
		treeUnit,
		//保存用户列表的数组
		hidUserArray = [];		
	require('synonym');

	doc.on('submit', '.J_edit_form', function(event) {
		event.preventDefault();
		var self = this,
			requestUrl = $(self).prop('action'),
			checkDelegate;
		//检查ucode
		checkDelegate = new VaildNormal();
		//前端检查
		if (!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '中文名称不能为空'},{'name':'maxlength','param':101,'msg':'中文名称长度不能超过100个字符'}]) || 
			!checkDelegate.checkNormal($('input[name="ccname"]'), [{'name':'maxlength','param':101,'msg':'中文全称长度不能超过100个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="cname_en"]'), [{'name':'maxlength','param':251,'msg':'英文名称长度不能超过250个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="ccname_en"]'), [{'name':'maxlength','param':251,'msg':'英文全称长度不能超过250个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp"]'), [{'name':'maxlength','param':2001,'msg':'中文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cexp_en"]'), [{'name':'maxlength','param':2001,'msg':'英文解释长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo"]'), [{'name':'maxlength','param':2001,'msg':'中文备注长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($(':input[name="cmemo_en"]'), [{'name':'maxlength','param':2001,'msg':'英文备注长度不能超过2000个字符'}]) ||
			!checkDelegate.checkNormal($('input[name="nbscode"]'), [{'name': 'ch','msg': 'NBS代码不能包含中文'},{'name':'maxlength','param':11,'msg':'NBS代码长度不能超过10个字符'}])
		){
			return;
		}
		
		//后台数据检查 code
		if(!checkProcodeAsyncExist($('input[name="procode"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}

//		//后台数据检查 nbscode
//		if ($('input[name="nbscode"]').val().replace(/(^s*)|(s*$)/g, "").length !=0 && $('input[name="crrentnbscode"]').val()!=$('input[name="nbscode"]').val() ) 
//		{
//			if(!checkNbsAsyncExist($('input[name="nbscode"]').val(),checkDelegate)){
//				common.commonTips('保存失败');
//				return;
//			}
//		} 
//		
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
			url: requestUrl,
			type: 'post',
			data: $(self).serialize()+'&classprocodes='+hidUserArray.toString(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					common.commonTips('修改成功');
					var url=common.rootPath+"metadata/zbmgr.htm?m=query&id="+data.param1;
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
	 * 查看已存在的分组代码
	 */
	$(document).on('click','.J_group_code',function(event){
		event.preventDefault();
		var currentId = $(this).attr('id'),
			len = $('#J_group_modal').length,
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
				getCheckedList(currentId);
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
			$('.timely-search').hide();
			return;
		}
		$.ajax({
			url: common.rootPath+'metadata/zbgmgr.htm',
			timeout: 5000,
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
					$('.timely-search').show();
				}
			},
			error: function(e) {}
		});
	});
	doc.on('focus','.J_zbinfo',function(){
		$('.timely-search').show();
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
			$('.timely-search').hide();
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
				url: common.rootPath+'metadata/zbgmgr.htm?m=findZbTree',
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
	function getCheckedList(currentId){
		$.ajax({
			url:common.rootPath+'metadata/zbgmgr.htm',
			type:'post',
			timeout:10000,
			data:'m=findCodeNames&id='+currentId,
			dataType:'json',
			success:function(data){
				if(data.returncode == 200){
					var i = 0,
						list = data.returndata,
						len = list.length,
						templen = hidUserArray.length,
						trs = '';
					for(;i<len;i++){
						hidUserArray = common.oprArray('add',list[i].code,hidUserArray);
						//成功增加数组以后才进行页面新增
						if(hidUserArray.length > templen){
							templen = hidUserArray.length;
							trs +='<a id="'+list[i].code+'" href="javascript:;" class="list-group-item J_del_user">'+list[i].cname+'</a>';
						}
					}
					$('.J_user_list').append(trs);
				}
			},
			error:function(e){
				
			}
		});
	}
//	/**
//	 * 校验nbs代码
//	 */
//	$(document).on('blur','input[name="nbscode"]',function(){
//		var self = this,
//			checkDelegate  = new VaildNormal();
//		if ($(self).val().replace(/(^s*)|(s*$)/g, "").length !=0 && $('input[name="crrentnbscode"]').val()!=$(self).val()){ 
//			checkNbsAsyncExist($(self).val(),checkDelegate);
//		}
//	});
	/**
	 * 后台检验nbs码是否重复
	 */
	function checkNbsAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/zbmgr.htm?m=checkCode',
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
	 * 校验指标代码
	 */
	$(document).on('blur','input[name="procode"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($('input[name="procode"]'), [{'name': 'ch','msg': '上级代码不能包含中文'},{'name':'maxlength','param':21,'msg':'上级代码长度不能超过20个字符'}])){
			checkProcodeAsyncExist($(self).val(),checkDelegate);
		}
	});
	/**
	 * 后台检验procode是否存在
	 */
	function checkProcodeAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'metadata/zbmgr.htm?m=checkProCode',
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
		//清空NBS代码、中文解释、英文解释、中文备注、英文备注
		$('[name=cexp], [name=cexp_en], [name=cmemo], [name=cmemo_en], [name=nbscode]').val('');
		//清空小数点位数、时间类型
		$('[name=zbsort], [name=dotcount]').val('');
		//清空同义词
		$('.J_synonym_table tbody').empty();
		$('[name=synonym]').val('');
		//清空添加查看
		if(ztreeObj){
			$('[name=J_zbinfo]').val('');
			hidUserArray = [];
			$('.J_user_list').empty();
			var nodes = ztreeObj.getCheckedNodes(true),
				i,
				l;
			for (i=0, l=nodes.length; i < l; i++) {
				nodes[i].checked = false;
				ztreeObj.removeNode(nodes[i]);
			}
		}
		//清空中文单位
		$('[name=unitName], [name=unitcode]').val('');
		treeUnit.cancelSelectedNode();
		treeUnit.expandAll(false);
	}
});