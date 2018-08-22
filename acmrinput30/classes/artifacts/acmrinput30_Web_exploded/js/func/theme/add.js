define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		VaildNormal = require('vaildnormal'),
		common = require('common'),
		modal = require('modal'),
		dropdown = require('dropdown'),
		tree = require('tree'),
		doc = $(document),
		ztreeObj;
	require('uidropdown');
	
	doc.on('submit', '.J_add_form', function(event) {
		//event.preventDefault();
		var self = this,
			isEmpty = true,
			requestUrl = $(self).prop('action'),
			checkDelegate;
		//检查code
		checkDelegate = new VaildNormal();
		
		//前端检查
		if (!checkDelegate.checkNormal($('input[name="code"]'), [{'name': 'required','msg': '模块ID不能为空'}]) || 
			!checkDelegate.checkNormal($('input[name="cname"]'), [{'name': 'required','msg': '模块名称不能为空'}])
		){
			event.preventDefault();
			return;
		}
		
		//后台数据检查
		if(!checkAsyncExist($('input[name="code"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			event.preventDefault();
			return;
		}

		/*模块权限*/
		if( $('[name=visible]:checked').val() === '1' ){
			var isEmptyPower = false,
				arrPower = [];

			$('.J_powerItem').each(function(){
				var rols = $('select', this).val(),
					usersText = $('.ui.multiple.dropdown input', this).val(),
					powerTtem = {'rols': rols,'user':'','dept':''};
				if( usersText === '' && rols === ''){
					isEmptyPower = true;
					return false;
				}

				$('.ui.multiple.dropdown > a', this).each(function(){
					if($(this).attr('data-type') === '1'){
						powerTtem.user += $(this).data('value') + ',';
					}else{
						powerTtem.dept += $(this).data('value') + ',';
					}
				});

				arrPower.push(powerTtem);
			});

			if(isEmptyPower || $('.J_powerList').html() === ''){
				common.commonTips('模块权限不能为空！');
				event.preventDefault();
				return;
			}

			$('[name=modePower]').val(JSON.stringify(arrPower));
		}else{
			$('[name=modePower]').val('');
		}
		/*模块权限end*/

		/*添加子模块*/
		if(!$("#table tbody tr").length){
			isEmpty = false;
		}else{
			$("#table tbody tr input").each(function(){
				if($.trim($(this).val()) === ''){
					isEmpty = false;
				}
			});
		}
		
		if(!isEmpty){
			common.commonTips('子模块不能为空！');
			event.preventDefault();
			return;
		}
		
		// 校验子模块
		var flag =  false;
		$('[name="spans"]').each(function(i){
			if($(this).html().length>0){
				flag = true;
			}
		});
		
		if(flag){
			common.commonTips('保存失败');
			event.preventDefault();
			return;
		}
		
		$('[name=childMode]').val(getMods());
		/*添加子模块end*/
	});
	
	$(document).on('blur','input[name="code"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($(self), [{'name': 'required','msg': '模块ID不能为空'}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
	  
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'theme/theme.htm?m=checkCode',
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
	
	$(document).on('click','.J_addModel',function(){
		var str = ""; 
		str += '<tr>';
		str += '<td><input type="text" flag="flag" readonly><span style="color:red;font-size:10px;" name="spans"></span></td>';
		str += '<td><input type="text" ></td>';
		str += '<td><input type="radio" name="aa"> 　<img name="delimg" src="'+common.rootPath+'/images/del.png"></td>';
		str += '</tr>';
		$("#table tbody").append(str);
		setChecked();
	});
	
	 $(document).on('click','[name="delimg"]',function(){
		$(this).parent().parent().remove();
		setChecked();
	 });
	 
	 $(document).on('blur','[flag="flag"]',function(){
			var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($(self), [{'name': 'required','msg': 'ID不能为空'}])){
			checkExist(self,checkDelegate);
		}
	 });
	 
	 /**
	 * 后台检验是否重复
	 */
	function checkExist(self,checkDelegate){
		var flag = false;
		$.ajax({
			url: common.rootPath+'theme/theme.htm?m=checkLanmu',
			timeout: 5000,
			type: 'post',
			async: false,
			data: "code="+$(self).val(),
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					$(self).next("span").html("");
					flag = true;
				}else{
					$(self).next("span").html(data.returndata);
				}
			},
			error: function(e) {}
		});
		return flag;
	}
	 
	 function getMods(){
		 var arr = [];
		 $("#table tbody tr").each(function(){
			 var def = 0;
			 if ($('td:eq(2) input', this).attr('checked')){
				 def = 1;
			 }
			 arr.push({'id': $('td:eq(0) input', this).val(), 'name': $('td:eq(1) input', this).val(), 'def': def});
		 });
		 return JSON.stringify(arr);
	 }
	 
	 // 第一个设为选中
	 function setChecked(){
		 var flag = true;
		 $("#table tbody tr").each(function(){
			 if ($('td:eq(2) input', this).attr('checked')){
				 flag = false;
			 }
		 });
		 if(flag){
			 $('#table tbody tr td:eq(2) input').eq(0).attr('checked','checked');
		 }
	 }

	 
	doc.on('change', '[name=visible]', function(event) {
		if($(this).val() === "1"){
			$(".J_addPower").removeClass('hide');
			$(".J_power").removeClass('hide');
		}else{
			$(".J_addPower").addClass('hide');
			$(".J_power").addClass('hide');
		}
	});
	 
	doc.on('click', '.J_addPower', function(event) {
		var id = new Date().getTime();
		var html = '<div class="J_powerItem"><label class="control-label vtop">组织与用户：</label>';
			html +=	'<div id="dropdown'+ id +'" class="ui selection multiple dropdown mini" style="width:200px;height:34px;overflow:hidden;">';
			html +=		'<input type="hidden" autocomplete="off">';
			html +=		'<i class="dropdown icon"></i>';
			html +=		'<div class="text"></div>';
			html +=		'<div class="menu">';
			html +=			'<div class="divider" style="margin:0;"></div>';
			html +=			'<ul class="ztree select-tree hid-top" id="tree'+ id +'"></ul>';
			html +=			'<div class="scrolling menu" style="max-height:none;"></div>';
			html +=		'</div>';
			html +=	'</div>';
			html +=	'&nbsp;&nbsp;&nbsp;&nbsp;<label class="control-label vtop">角色：</label>';
			html +=	'<select class="form-control vtop roles'+ id +'" autocomplete="off" style="width:200px;">';
			html +=		'<option value=""> - 请选择 - </option>';

			html +=	'</select>&nbsp;&nbsp;';
			html +=	'<label class="control-label vtop J_delPower"><i class="delete icon"></i></label>';
			html +=	'</div>';

		$(".J_powerList").append(html);
		$(".roles" + id).append($(".publicSelect").html());

		initTrees("dropdown"+id, "tree"+id);
	});

	//删除一行
	doc.on('click', '.J_delPower', function(event) {
		$(this).parent().remove();
	});

	function initTrees(el, treeId) {
		var curMenu,
			zTree_Menu,
			setting = {
				async: {
					enable: true,
					url: common.rootPath + 'sysindex/sys.htm?m=findPersonTree',
					contentType: 'application/json',
					type: 'get',
					autoParam: ["id"]
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: cEvent
				}
			};

		function cEvent(event, treeid, treeNode) {
			var type = 0;
			if (treeNode.canselect === true) {
				type = 1;
			}
			var currentNodeId, node;
			currentNodeId = treeNode.tId + '_a';
			node = $('#' + currentNodeId);
			node.data('value', treeNode.id);
			node.attr('data-type', type);
		}
		$.fn.zTree.init($("#" + treeId), setting, []);

		$("#" + el + ".dropdown").uidropdown({
			preserveHTML: false,
			treeWidget: treeId,
			selector: {
				item: '.item > label, #' + treeId + ' li a:not(.disabled)'
			}
		});
	}
	

	//
	var obj;
	doc.on('click', '[flag=flag]', function(ev) {
		obj = $(this);
		var l = $(this).offset().left;
		var t = $(this).offset().top + $(this).outerHeight() + 5;
		
		$('.did').css({left: l, top: t}).show();
		ev.stopPropagation();
	});
	//
	doc.on('click', '.did li', function(ev) {
		obj.val($(this).attr("val"));
		$('.did').hide();
		obj.blur();
		ev.stopPropagation();
	});
	doc.on('click', function() {
		$('.did').hide();
		$('.did, .searchBox').hide();
	});

	doc.on('click', '.didsearch', function(ev) {
		ev.stopPropagation();
	});

	doc.on('click', '.didhead span', function(ev) {
		$('.searchBox').show();
		ev.stopPropagation();
		var key = $('.didsearch').val();
		if($.trim(key) === ''){
			alert('搜索内容不能为空！');
			return false;
		}
		
		$.ajax({
			url: common.rootPath+'theme/theme.htm?m=search',
			timeout: 5000,
			type: 'post',
			async: false,
			data: {key: key},
			dataType: 'json',
			success: function(data) {
				if(data.returncode == 200){
					$('.searchBox ul').empty();
					if(!data.returndata.length){
						$('.searchBox ul').append('没有搜索到结果！');
					}
					$.each(data.returndata, function(i){
						$('.searchBox ul').append('<li val="'+ data.returndata[i].code +'">'+ data.returndata[i].name +'</li>');
					});
				}
			},
			error: function(e) {}
		});
	});//theme/theme.htm?m=search&key=欧洲统计局

	//
	doc.on('click', '.searchBox li', function(ev) {
		obj.val($(this).attr("val"));
		$('.did, .searchBox').hide();
		obj.blur();
		ev.stopPropagation();
	});
});