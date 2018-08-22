/**
 * temp cache for copy function 
 * 2015-01-13
 * author by wulei
 */
define(function(require,exports,module){
	'use strict';
	var $ = require('jquery'),
	dropdown = require('dropdown'),
	Pagination = require('pagination'),
	common = require('common'),
	pjax=require('pjax'),
	tree = require('tree'),
	fileupload = require('fileupload'),
	PackAjax = require('Packajax'),
	modal = require('modal'),
	ZeroClipboard = require('ZeroClipboard'),
	dragwidth = require('dragwidth');
	
	$("#mainpanel").dragwidth();
	autodrag();
	$(window).resize(function(){
		autodrag();
	});
	function autodrag(){
		$(".right-panel").css('height','auto');
		var rch = $(window).height() - $('#tops').outerHeight();
		if($(".right-panel").height() <= rch){
			$(".right-panel").height(rch);
			$(".left-panel, .dragline").height(rch);
		}else{
			$(".left-panel, .dragline").height($(".right-panel").height());
		}
	}

	var delIds = [];
	var initTreePara = $("#initTreePara").val();
	var treeNodeId = $("#procode").val(); 
	var treeNodeName = "主体树";
	var isMove = true;
	var searchField = "";
	var currentVal="";
	var uuid = "";
	var idColumn="";
	var isAdvQuery="";
	var advVal="";
	var isFlush = false;//是否刷新
	var searchPara = $("#searchPara").val();
	if(searchPara.length>0){
		isMove = false;
	}
	
	/**
	 * 菜单树
	 */
	var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-1 by liaojin
	var setting = {
		async: {
			enable: true,
			url: common.rootPath+'metadata/company.htm?m=findZbTree&st='+st,
			contentType: 'application/json',
			type: 'get',
			autoParam: ["id"]
		},
		callback: {
			onClick: clickEvent,
			onAsyncSuccess: zTreeOnAsyncSuccess
		}
	};
	function clickEvent(event, treeid, treeNode) {
		//queryData(common.rootPath+"zbmgr.htm?m=query", common.formatData('id',treeNode.id));
		treeNodeId = treeNode.id;
		treeNodeName = treeNode.name;
		isMove = true;
		
		var str = "";
		if(treeNode.id.length>0){
			str="&";
		}
		$.pjax({
			url: common.rootPath+"metadata/company.htm?m=query"+str+ common.formatData('id',treeNode.id),
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	}
	var rootNode = [{"id":"","name":"主体树", "open":"true", "isParent":"true"}];
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, rootNode);
	var treenodes = treeObj.getNodes();
	treeObj.expandNode(treenodes[0], true, true, true);
	
	function zTreeOnAsyncSuccess(event, treeid, treeNode, msg){
		if (initTreePara.length>0){
			var zbs = initTreePara.split("/");
			var nodes;
			var treeObj = $.fn.zTree.getZTreeObj(treeid);
			
			if (treeNode == null){	// 第一层结点    
				nodes = treeObj.getNodes();    
			} else {
				nodes = treeNode.children;
			}
			var isBreak = false;
			for (var i = 0; i < nodes.length; i++){
				var node = nodes[i];
				for (var j = 0; j < zbs.length; j++){
					if (zbs[j] == node.id){
						if (node.isParent){
							treeObj.expandNode(node, true);
							if(node.id== zbs[zbs.length-1]){
								treeObj.selectNode(node);
								treeNodeId = node.id;
								treeNodeName = node.name;
							}
						} else {
							treeObj.selectNode(node);
							treeNodeId = node.id;
							treeNodeName = node.name;
						}
						isBreak = true;
						break;
					}
				}
				if (isBreak){
					break;
				}
			}
		}
    }   
	
	/**
	 * 按条件查找数据
	 */
	$(document).on('submit','.J_search_form',function(event){
		event.preventDefault();
		var self = this,
		requestUrl = $(self).prop('action'),
		key = $('select',self).val(),
		val = $('input',self).val(),
		str = "";
		if(treeNodeId.length>0){
			str="&id="+treeNodeId;
		}
		var requestData = key +"="+ encodeURIComponent(val)
		//common.formatData(key,val);
		if(requestData.length>0){
			requestData="&"+requestData;
		}
		searchField = requestData+str;
		isMove = false;
		$.pjax({
			url: requestUrl+searchField,
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	});
	/**
	 * 弹出高级查找层
	 */
	$(document).on('click','.J_grade_search',function(event){
		event.preventDefault();
		if($('#grade_modal_search').length>0){
			$('#grade_modal_search').remove();
		}
		var modalContent = '<div class="modal-header">';
			modalContent +='<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
			modalContent +='<h4 class="modal-title" id="myModalLabel">高级查询</h4>';
			modalContent +='</div>';
			modalContent +='<div class="modal-body">';
			modalContent +='<div class="form-inline form-margin">';
			modalContent +='<div class="form-group">';
			modalContent +='<select class="form-control input-sm" name="searchCode"><option value="code">代码</option><option value="cname">名称</option><option value="cname_en">英文名称</option>';
			modalContent +='<option value="ccname">中文全称</option><option value="ccname_en">英文全称</option><option value="ifdata">主体、分类</option><option value="unitcode">单位</option><option value="dotcount">小数点位数</option>';
			modalContent +='<option value="cexp">中文解释</option><option value="cexp_en">英文解释</option><option value="cmemo">中文备注</option><option value="cmemo_en">英文备注</option><option value="createtime">生成时间</option>';
			modalContent +='</select>';
			modalContent +='</div>';
			modalContent +='<div class="form-group">';
			modalContent +='<select class="form-control input-sm btn-margin" name="searchOpr"><option value="like">包含</option><option value="=">等于</option><option value=">=">大于等于</option><option value=">">大于</option><option value="<=">小于等于</option><option value="<">小于</option>';
			modalContent +='<option value="not like">不包含</option><option value="<>">不等于</option><option value="起于">起于</option><option value="止于">止于</option><option value="is">is</option>';
			modalContent +='</select>';
			modalContent +='</div>';
			modalContent +='<div class="form-group">';
			modalContent +='<select class="form-control input-sm btn-margin" name="searchCondition"><option value="|">或</option><option value="&">与</option></select>';
			modalContent +='</div>';
			modalContent +='<div class="form-group">';
			modalContent +='<input type="text" class="form-control input-sm btn-margin" name="searchText" placeholder="条件值"/>';
			modalContent +='</div>';
			modalContent +='<div class="form-group">';
			modalContent +='<button type="button" class="btn btn-sm btn-primary btn-margin J_component_sentence">组合语句</button>';
			modalContent +='</div>';
			modalContent +='</div>';
			modalContent +='<div class="form-group">';
			modalContent +='<textarea class="form-control J_priview_sentence" rows="3" readonly></textarea>';
			modalContent +='<textarea class="form-control J_hpriview_sentence hidden" rows="3"></textarea>';
			modalContent +='</div>';
			modalContent +='<div class="form-group" id="msg"></div>';
			modalContent +='<div class="modal-footer">';
			modalContent +='<button type="button" class="btn btn-primary J_btn_clear_condition">清空</button>';
			modalContent +='<button type="button" class="btn btn-primary J_btn_import_condition">代码导入</button>';
			modalContent +='<button type="button" class="btn btn-primary J_btn_check_condition">分析</button>';
			modalContent +='<button type="button" class="btn btn-primary J_btn_save_condition" id="find">查询</button><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
			modalContent +='</div>';
		common.buildModelHTML('grade_modal_search',modalContent);
		$("#msg").hide();
		$('#grade_modal_search').modal('show');
	});
	
	/**
	 * 组合语句
	 */
	$(document).on('click','.J_component_sentence',function(event){
		event.preventDefault();
		$('#msg').empty();
		var searchCode = $('select[name="searchCode"]').val(),
			searchOpr = $('select[name="searchOpr"]').val(),
			searchCondition = $('select[name="searchCondition"]').val(),
			searchText = $('input[name="searchText"]').val(),
			currentVal = $('.J_priview_sentence').val();
		var hcurrentVal=$('.J_hpriview_sentence').val();
		//判断’或‘，’和‘是否起作用
		if($('.J_priview_sentence').val() === ''){
			searchCondition = '';
		}

		//根据条件修改用户输入内容
		if(searchCondition==='|'){
			searchCondition=' or ';
		}
		
		if(searchCondition==='&'){
			searchCondition=' and ';
		}
		
		//-------------
		if(searchText === ''){
			$('#msg').append('<font color="red">请输入条件值</font>');
			$('#msg').show();
//			common.commonTips("请输入条件值");
			return;
		}
		
		if(searchOpr === 'not like'){
			searchText = "'%"+searchText+"%'";
		}
		if(searchOpr === '='){
			searchText = "'"+searchText+"'";
		}
		if(searchOpr === '>='){
			searchText="'"+searchText+"'";
		}
		if(searchOpr === '>'){
			searchText="'"+searchText+"'";
		}
		if(searchOpr === '<'){
			searchText="'"+searchText+"'";
		}
		if(searchOpr === '<='){
			searchText="'"+searchText+"'";
		}
		if(searchOpr === '<>'){
			searchText="'"+searchText+"'";
		}
		if(searchOpr === 'is'){
			searchText=searchText;
		}
		
		if(searchCode === "createtime"){
			if(searchOpr === 'like'){
				searchText = "to_date("+"'"+searchText+"'"+",'yyyy-MM-dd')";
			}
			
			if(searchOpr === '起于'){
				searchText="'"+searchText+"'";
				$('.J_priview_sentence').val(currentVal +' '+ searchCode +' '+'起于'+ ' '+ searchText);
				$('.J_hpriview_sentence').val(hcurrentVal +' '+ searchCondition +' '+ searchCode + ' between '+ searchText.replace(/%/g, "%25"));
			}else if(searchOpr === '止于'){
				searchText="'"+searchText+"'";
				$('.J_priview_sentence').val(currentVal +' '+'止于'+' '+searchText+' ');
				$('.J_hpriview_sentence').val(hcurrentVal +' '+searchCode +' '+'and'+' '+searchText.replace(/%/g, "%25"));
			}else{
				$('.J_priview_sentence').val(currentVal+searchCondition+' '+searchCode+' '+searchOpr+' '+searchText+' ');
				$('.J_hpriview_sentence').val(hcurrentVal+searchCondition+' '+searchCode+' '+searchOpr+' '+searchText.replace(/%/g, "%25")+' ');
			}
		}else{
			if(searchOpr === 'like'){
				searchText = "'%"+searchText+"%'";
			}
			
			if(searchOpr === '起于'){
				searchOpr = "between";
				searchText="'"+searchText+"'";
				$('.J_priview_sentence').val(currentVal +' '+searchCode+' '+'起于'+' '+searchText+' ');
				$('.J_hpriview_sentence').val(hcurrentVal+' '+searchCondition+' '+searchCode+' '+searchOpr+' '+searchText.replace(/%/g, "%25")+' ');
			}else if(searchOpr === '止于'){
				searchOpr = "and";
				searchText="'"+searchText+"'";
				$('.J_priview_sentence').val(currentVal+' '+'止于'+' '+' '+searchText+' ');
				$('.J_hpriview_sentence').val(hcurrentVal+' '+searchCode+' '+'and'+' '+searchText.replace(/%/g, "%25")+' ');
			}else{
				$('.J_priview_sentence').val(currentVal+' '+searchCondition+' '+searchCode+' '+searchOpr+' '+searchText+' ');
				$('.J_hpriview_sentence').val(hcurrentVal+' '+searchCondition+' '+searchCode+' '+searchOpr+' '+searchText.replace(/%/g, "%25")+' ');
			}
		}
	});
	
	/**
	 * 清空
	 */
	
	$(document).on('click','.J_btn_clear_condition',function(event){
		event.preventDefault();
		$(".J_priview_sentence").val("");
		$(".J_hpriview_sentence").val("");
	});
	/**
	 * 分析查找语句 
	 */
	$(document).on('click','.J_btn_check_condition',function(event){
		event.preventDefault();
		$('#msg').empty();
		var currentVal = $(".J_hpriview_sentence").val();
		var length = $(".J_hpriview_sentence").val().length;
		//alert(length);
		if(length>=2000){
			$('#msg').append('<font color="red">组合语句条件过长</font>');
			$('#msg').show();
//			common.commonTips("组合语句条件过长");
			return;
		}
		if(currentVal === ''){
			$('#msg').append('<font color="red">格式错误</font>');
			$('#msg').show();
//			common.commonTips("格式错误");
			return;
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm',
			type:'post',
			dataType:'json',
			data:'m=checkSql&searchSQL='+currentVal,
			timeout:5000,
			success:function(data){
				if(data.returncode == 200){
					$('#msg').append('<font color="green">格式正确</font>');
					$('#msg').show();
//					common.commonTips("格式正确");
					document.getElementById("find").disabled="";
				}else{
					$('#msg').append('<font color="red">格式错误</font>');
					$('#msg').show();
//					common.commonTips("格式错误");
					document.getElementById("find").disabled="disabled";
				}
			}
		});
	});
	/**
	 * 提交保存的查找语句
	 */
	$(document).on('click','.J_btn_save_condition',function(event){
		event.preventDefault();
		$('#msg').empty();
		isMove = false;
		isAdvQuery = true;
		var flag = false;
		if(advClick()){
			$('#msg').append('<font color="green">格式正确</font>');
			$('#msg').show();
//			common.commonTips("格式正确");
		}else{
			$('#msg').append('<font color="red">格式错误</font>');
			$('#msg').show();
//			common.commonTips("格式错误");
			return;
		}
		advVal = $(".J_hpriview_sentence").val();
		if(advVal === ''){
			return;
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm',
			type:'post',
			dataType:'json',
			data:'m=advQuery&searchSQL='+advVal,
			timeout:5000,
			success:function(data){
				if(data.returncode == 200){
					flag=true;
				}else{
					$('#msg').append('<font color="red">格式错误</font>');
					$('#msg').show();
//					common.commonTips("格式错误");
				}
			}
		});
		
		$.pjax({
			url: common.rootPath+"metadata/company.htm?m=advQuery&searchSQL="+advVal,
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
			$('#grade_modal_search').modal('hide');
			delIds = [];
		});
	});
	
	function advClick (){
		var flag = "";
		var hcurrentVal = $(".J_hpriview_sentence").val();
		if(hcurrentVal === ''){
			return;
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm',
			type:'post',
			async: false,
			dataType:'json',
			data:'m=checkSql&searchSQL='+hcurrentVal,
			timeout:5000,
			success:function(data){
				if(data.returncode == 200){
					flag=true;
				}else{
					flag=false;
				}
			}
		});
		return flag;
	}
	
	/**
	 * 代码导入
	 */
	$(document).on('click','.J_btn_import_condition',function(event){
		$('#import').remove();
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">代码导入</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(不能超过5MB)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>代码上传</span><input type="file" name="file" id="J_import_fileupload" multiple></span></div>';
		modalContent += '<div class="form-group"><div id="J_import_progress" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_close_file">关闭</button>';
		modalContent += '<input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('import',modalContent);
		$('#import').modal('show');
		fileUpload();
	});
	
	/**
	 * 关闭代码导入
	 */
	$(document).on('click', '.J_btn_close_file', function(event) {
		event.preventDefault();
		$('#import').modal('hide');
	});
	
	/**
	 * 上传代码
	 */
	function fileUpload(){
		$('#J_import_fileupload',document).fileupload({
			url: common.rootPath+'metadata/company.htm?m=upload',
			type: 'post',
	        dataType: 'json',
	        done: function (e,data) {
	            if(data.result.returncode == 200){
	            	alert("代码导入成功");
	            	$('.J_priview_sentence').val(data.result.returndata);
	            	$('.J_hpriview_sentence').val(data.result.returndata);
	            }
	            else{
	            	alert("代码导入失败");
	            }
	        },
	        progressall: function (e,data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_import_progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }

		});
	}	
	
	/**
	 *提交选择的checkebox(默认所有)
	 */
	$(document).on('click','.J_zbmgr_table>thead>tr>th>input[type="checkbox"]',function(){
		var self = this;
		delIds = common.checkAll('.J_zbmgr_table>tbody input[type="checkbox"]',$(self).prop('checked'),delIds);
	});
	$(document).on('click','.J_zbmgr_table>tbody input[type="checkbox"]',function(){
		var self = this,
		currentId =  $(self).attr('value'),
		currentState = $(self).prop('checked');
		if(currentState){
			common.oprArray('add',currentId,delIds);
		}else{
			common.oprArray('del',currentId,delIds);
		}
		if(delIds.length === $('.J_zbmgr_table>tbody input[type="checkbox"]').length){
			$('.J_zbmgr_table>thead>tr>th>input[type="checkbox"]').attr('checked',true);
		}else{
			$('.J_zbmgr_table>thead>tr>th>input[type="checkbox"]').attr('checked',false);
		}
	});
	
	/**
	 * 删除数据
	 */
	$(document).on('click','.J_opr_del',function(event){
		event.preventDefault();
		var self = this,
		delId = $(self).attr('id');
		if(!confirm("确定要删除选中记录吗？")){
			return;
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm?m=toDelete',
			data:"delId=" + delId,
			type:'post',
			dataType:'json',
			timeout:10000,
			success:function(data){
				if(data.returncode == 200){
					common.commonTips("删除成功");
					window.location.reload(true);
				}else{
					common.commonTips(data.returndata);
				}
			}
		});
	});
	
	/**
	 * 批量删除数据
	 */
	$(document).on('click','.J_All_del',function(event){
		event.preventDefault();	
		if(delIds==[]||delIds.length==0){
			common.commonTips("请至少选中一条记录！");
			return;
		}
		if(!confirm("确定要删除选中记录吗？")){
			return;
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm?m=toDeleteAll',
			data:"ids=" + delIds,
			type:'post',
			dataType:'json',
			timeout:10000,
			success:function(data){
				if(data.returncode==200){
					common.commonTips("删除成功");
					window.location.reload(true);
				}else{
					common.commonTips(data.returndata);
				}
			}
		});
	});
	/**
	 * 上移动操作
	 */
	$(document).on('click','.J_opr_moveup',function(event){
		event.preventDefault();
		var self = this,
			indexVal=parseInt($(self).parents('tr:eq(0)').index()),
			currentId = $(self).attr('id'),
			len = $(self).parents('tbody').children('tr').length,
			siblingsId;
		if($(this).hasClass("btn-disabled")){
			return;
		}
		if(indexVal==0){
			siblingsId = $("#top").val();
		}else{
			siblingsId = $(self).parents('tr').prev('tr').children('td').children('.J_opr_moveup').attr("id");
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
					common.cancalCheck('.J_zbmgr_table input[type="checkbox"]',delIds);
					common.commonTips('上移成功');
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips('上移失败');
				}
			}
		});
	});
	/**
	 * 下移动操作
	 */
	$(document).on('click','.J_opr_movedown',function(event){
		event.preventDefault();
		var self = this,
		indexVal=parseInt($(self).parents('tr:eq(0)').index()),
		len = $(self).parents('tbody').children('tr').length,
		currentId = $(self).attr('id'),
		siblingsId;
		if($(this).hasClass("btn-disabled")){
			return;
		}
		if(indexVal === len-1){
			siblingsId = $("#bottom").val();
		}else{
			siblingsId = $(self).parents('tr').next('tr').children('td').children('.J_opr_movedown').attr("id");
		}
		$.ajax({
			url:common.rootPath+'metadata/company.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
					common.cancalCheck('.J_zbmgr_table input[type="checkbox"]',delIds);
					common.commonTips('下移成功');
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips('下移失败');
				}
			},
			error:function(e){}
		});
	});
	/**
	 *导出数据 
	 */
	$(document).on('click','.J_export_data',function(event){
		event.preventDefault();
		if(delIds.length>0){//有没有被选中的主体
			window.location.href=common.rootPath+"metadata/company.htm?m=exportAllData&ids="+delIds;
		}else if(isMove){//是否为普通的数据查看
			window.location.href=common.rootPath+"metadata/company.htm?m=exportData&id="+treeNodeId+"&procodeName="+treeNodeName;
		}else if(isAdvQuery){//是否为高级查询
			var hcurrentVal = $(".J_hpriview_sentence").val();
			window.location.href=common.rootPath+"metadata/company.htm?m=exportAdvQueryData&searchSQL="+hcurrentVal;
		}else{//表示进入搜索
			if(searchField.length>0){
				searchPara = searchField;
			}
			window.location.href=common.rootPath+"metadata/company.htm?m=exportSearchData&procodeName="+treeNodeName+searchPara;
		}
	});
	
	/**
	 * 批量匹配详情
	 */
	$(document).on('click','.J_Matching_details',function(event) {
		$('#J_modal_data_upLoad_now').remove();
		event.preventDefault();
		var self = this;
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">批量匹配详情页面</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(文件大小不能超过5MB)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload" multiple></span><span class="btn btn-success fileinput-button" style="margin-left:20px;" id="mouldDownload"><span>模板下载</span></span></div>';
		modalContent += '<div class="form-group"><div id="J_progress" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<span id="md_count" class="form-group" style="margin-right:10px;"></span>';
		modalContent += '<button aria-hidden="true" type="button" class="btn hid btn-primary btn-sm J_btn_md_export">导出</button>';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_save_file">关闭</button>';
		modalContent += '<input type="hidden" name="code" value="'+ '" /><input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('J_modal_data_upLoad_now',
				modalContent);
		$('#J_modal_data_upLoad_now').modal('show');
		initFileUpload();
	});
	
	/**
	 * 批量匹配详情文件上传
	 */
	function initFileUpload(){
		$('#J_fileupload',document).fileupload({
			url: common.rootPath+'metadata/company.htm?m=matchingDetails',
	        dataType: 'json',
	        done: function (e, data) {
	        	var result = data.result;
	            if(result.returncode == 200){
	            	uuid = result.param2;
	            	idColumn = result.param3;
            		$("#md_count").html("文件上传成功,共查询到<span style='color:red'>"+result.param1+"条</span>数据");
            		$(".J_btn_md_export").show();
	            }
	            else{
	            	alert("数据不正确,上传失败");
	            }
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    });
	}
	
	/**
	 *导出数据 
	 */
	$(document).on('click','.J_btn_md_export',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/company.htm?m=mdExport&id="+idColumn+"&uuid="+uuid;
	});
	
	/**
	 * 批量匹配详情关闭界面
	 */
	$(document).on('click', '.J_btn_save_file', function(event) {
		event.preventDefault();
		$('#J_modal_data_upLoad_now').modal('hide');
	});
	
	/**
	 * 批量匹配详情模板下载
	 */
	$(document).on('click', '#mouldDownload', function(event) {
		window.location.href = common.rootPath+"metadata/company.htm?m=templateDownload";
	});
	
	/**
	 * 批量导入
	 */
	$(document).on('click','.J_import',function(event) {
		$('#J_import_upLoad').remove();
		event.preventDefault();
		var self = this;
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">批量导入页面</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(文件大小不能超过5MB，数据不能超过10000条)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload_import" multiple></span><span class="btn btn-success fileinput-button" style="margin-left:20px;" id="mouldDownload2"><span>模板下载</span></span></div>';
		modalContent += '<div class="form-group"><div id="J_progress_import" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<span id="import_count" class="form-group" style="margin-right:10px;"></span>';
		modalContent += '<button aria-hidden="true" type="button" class="btn hid btn-primary btn-sm J_btn_id_export">导出错误信息</button>';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_import_close">关闭</button>';
		modalContent += '<input type="hidden" name="code" value="'+ '" /><input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('J_import_upLoad',modalContent);
		$('#J_import_upLoad').modal('show');
		importData();
	});
	
	/**
	 * 批量导入模板下载
	 */
	$(document).on('click', '#mouldDownload2', function(event) {
		window.location.href = common.rootPath+"metadata/company.htm?m=templateDownload2";
	});
	
	/**
	 * 批量导入文件上传
	 */
	function importData(){
		$('#J_fileupload_import',document).fileupload({
			url: common.rootPath+'metadata/company.htm?m=importData',
	        dataType: 'json',
	        done: function (e, data) {
	        	var result = data.result;
	            if(result.returncode == 200){
	            	$(".J_btn_id_export").hide();
	            	isFlush = true;
            		$("#import_count").html("文件上传成功,共导入<span style='color:red'>"+result.param1+"条</span>数据");
	            }else if(result.returncode == 300){
	            	$(".J_btn_id_export").show();
	            	uuid = result.param1;
	            	$("#import_count").html("<span style='color:red'>上传失败！"+result.returndata+"</span>");
	            }else if(result.returncode == 400){
	            	$(".J_btn_id_export").hide();
	            	$("#import_count").html("<span style='color:red'>上传失败！"+result.returndata+"</span>");
	            }else{
	            	alert("数据不正确,上传失败");
	            }
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_progress_import .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    });
	}
	
	
	/**
	 * xls数据选择
	 */
	$(document).on('click','.J_XlsModel',function(event) {
		$('#J_import_upLoad').remove();
		event.preventDefault();
		var self = this;
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">Excel样本数据导入</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(文件大小不能超过5MB)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload_import" multiple></span></div>';
		modalContent += '<div class="form-group"><div id="J_progress_import" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<span id="import_count" class="form-group" style="margin-right:10px;"></span>';
		modalContent += '<button aria-hidden="true" type="button" class="btn hid btn-primary btn-sm J_btn_id_export">导出错误信息</button>';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_import_close">关闭</button>';
		modalContent += '<input type="hidden" name="code" value="'+ '" /><input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('J_import_upLoad',modalContent);
		$('#J_import_upLoad').modal('show');
		importXlsModel();
	});
	
	
	/**
	 * xls数据上传
	 */
	function importXlsModel(){
		$('#J_fileupload_import',document).fileupload({
			url: common.rootPath+'xlsmgr.htm?m=importXlsModel',
	        dataType: 'json',
	        done: function (e, data) {
	        	var result = data.result;
	            if(result.returncode == 200){
	            	$(".J_btn_id_export").hide();
	            	isFlush = true;
            		$("#import_count").html("文件上传成功,共导入<span style='color:red'>"+result.param1+"条</span>数据");
	            }else if(result.returncode == 300){
	            	$(".J_btn_id_export").show();
	            	uuid = result.param1;
	            	$("#import_count").html("<span style='color:red'>上传失败！"+result.returndata+"</span>");
	            }else if(result.returncode == 400){
	            	$(".J_btn_id_export").hide();
	            	$("#import_count").html("<span style='color:red'>上传失败！"+result.returndata+"</span>");
	            }else{
	            	alert("数据不正确,上传失败");
	            }
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_progress_import .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    });
	}
	
	/**
	 *批量导入 导出错误信息 
	 */
	$(document).on('click','.J_btn_id_export',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/company.htm?m=impErrInfoExport&uuid="+uuid;
	});
	
	/**
	 * 批量导入关闭界面
	 */
	$(document).on('click', '.J_btn_import_close', function(event) {
		event.preventDefault();
		$('#J_import_upLoad').modal('hide');
		if(isFlush){
			window.location.reload(true);
		}
	});

	/**
	 * 批量修改
	 */
	$(document).on('click','.J_edit',function(event) {
		$('#J_edit').remove();
		event.preventDefault();
		var self = this;
		var modalContent = '<div class="modal-header">';
		modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		modalContent += '<h4 class="modal-title" id="myModalLabel">批量修改页面</h4>';
		modalContent += '</div>';
		modalContent += '<div class="modal-body">';
		modalContent += '<div class="form-group"> <h5>1. 请选择Excel文件(文件大小不能超过5MB，数据不能超过10000条)</h5></div>';
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload_import" multiple></span><span class="btn btn-success fileinput-button" style="margin-left:20px;" id="mouldDownload3"><span>模板下载</span></span></div>';
		modalContent += '<div class="form-group"><div id="J_progress_import" class="progress"><div class="progress-bar progress-bar-success"></div></div></div>';
		modalContent += '</div>';
		modalContent += '<div class="modal-footer">';
		modalContent += '<span id="import_count" class="form-group" style="margin-right:10px;"></span>';
		modalContent += '<button aria-hidden="true" type="button" class="btn hid btn-primary btn-sm J_btn_edit_export">导出错误信息</button>';
		modalContent += '<button type="button" class="btn btn-primary btn-sm J_btn_edit_close">关闭</button>';
		modalContent += '<input type="hidden" name="code" value="'+ '" /><input type="hidden" name="fileAddress"/>';
		modalContent += '</div>';
		common.buildModelHTML('J_edit',modalContent);
		$('#J_edit').modal('show');
		importNewData();
	});
	
	/**
	 * 批量修改文件上传
	 */
	function importNewData(){
		$('#J_fileupload_import',document).fileupload({
			url: common.rootPath+'metadata/company.htm?m=importEditData',
	        dataType: 'json',
	        done: function (e, data) {
	        	var result = data.result;
	            if(result.returncode == 200){
	            	$(".J_btn_edit_export").hide();
	            	isFlush = true;
            		$("#import_count").html("修改成功,共修改<span style='color:red'>"+result.param1+"条</span>数据");
	            }else if(result.returncode == 300){
	            	$(".J_btn_edit_export").show();
	            	uuid = result.param1;
	            	$("#import_count").html("<span style='color:red'>修改失败！"+result.returndata+"</span>");
	            }else if(result.returncode == 400){
	            	$(".J_btn_edit_export").hide();
	            	$("#import_count").html("<span style='color:red'>修改失败！"+result.returndata+"</span>");
	            }else{
	            	alert("数据不正确,上传失败");
	            }
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#J_progress_import .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    });
	}

	/**
	 *批量修改 导出错误信息 
	 */
	$(document).on('click','.J_btn_edit_export',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/company.htm?m=impErrInfoExport&uuid="+uuid;
	});
	
	/**
	 * 批量修改模板下载
	 */
	$(document).on('click', '#mouldDownload3', function(event) {
		window.location.href = common.rootPath+"metadata/company.htm?m=templateDownload3";
	});

	/**
	 * 批量修改关闭界面
	 */
	$(document).on('click', '.J_btn_edit_close', function(event) {
		event.preventDefault();
		$('#J_edit').modal('hide');
		if(isFlush){
			window.location.reload(true);
		}
	});

	/**
	 * 跳转到新增界面
	 */
	$(document).on('click','.J_Add',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/company.htm?m=toAdd&procodeId=" + treeNodeId + "&procodeName=" + treeNodeName;
	});
	/**
	 *根据code下转 
	 */
	$(document).on('click','.J_opr_code',function(event){
		event.preventDefault();
		var self = this,
		currentCode = $(self).text();
		var flag = false;
		$.ajax({
			url:common.rootPath+"metadata/company.htm?m=checkHasParent&code="+currentCode,
			type:'post',
			timeout:5000,
			dataType:'json',
			async: false,
			success:function(data){
				if(data.returncode == 200){
					flag = true;
				}
			},
			error:function(e){
				common.commonTips('操作失败');	
			}
		});
		if(!flag){
			common.commonTips('没有下级了');
			return;
		}
		$.pjax({
			url: common.rootPath+"metadata/company.htm?m=query&id="+currentCode,
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	});
	/**
	 * 通用查询
	 */
	function queryData(requireUrl, data) {
		$.ajax({
			url:requireUrl,
			data:data,
			type:'post',
			timeout:10000,
			dataType:'json',
			success:function(data){
				if(data.returncode == 200){
					var returndata = data.returndata;
					formatBackTable(data);
					new Pagination({
						totalPage:returndata.totalPage,
						currentPage:returndata.pageNum,
						posePageCount:3,
						currentObj:$('.pagination'),
						url:returndata.url
					});
				}
			},
			error:function(e){
				
			}
		});
	}
	/**
	 * 格式化数据表
	 */
	function formatBackTable(data){
		if(data.returncode == 200){
			common.cancalCheck('.J_zbmgr_table input[type="checkbox"]',delIds);
			var returndata = data.returndata;
			var list = returndata.data,
			len,
			trs = '',
			i = 0;
			len = list.length;
			if(len === 0){
				trs += '<tr><td></td>';
				trs += '<td></td>';
				trs += '<td></td>';
				trs += '<td></td>';
				trs += '<td>没有查询到数据</td>';
				trs += '<td></td>';
				trs += '<td></td>';
				trs += '<td></td></tr>';
				
				$('.J_zbmgr_table > tbody').html(trs);
				return;
			}
			for (i; i < len; i++) {
				var index = (returndata.pageNum - 1)*returndata.pageSize+(i+1);
				trs += '<tr><td><input type="checkbox" value="'+list[i].code+'"/></td>';
				trs +='<td>'+index+'</td>';
				trs +='<td><a href="javascript:;" class="btn-opr J_opr_code">'+list[i].code+'</a></td>';
				trs +='<td><a href="'+common.rootPath+'metadata/company.htm?m=getDataById&id='+list[i].code+'" >'+list[i].cname+'</a></td>';
				trs +='<td>'+list[i].unitcode+'</td>';
				trs +='<td>'+list[i].procode+'</td>';
				trs +='<td>'+list[i].ccname+'</td>';
				trs +='<td><a href="'+common.rootPath+'metadata/company.htm?m=toEdit&id='+list[i].code+'" class="btn-opr J_opr_edit">编辑</a>';
				//trs +='<a href="javascript:;" class="btn-opr">数据详情</a>';
				if(isMove){
					if(i==0){
						trs += '<label class="btn-opr btn-disabled J_opr_moveup" id="'+list[i].code+'">上移</label>';
					}else{
						trs +='<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+list[i].code+'">上移</a>';
					}
					if(i==len-1){
						trs += '<label class="btn-opr btn-disabled J_opr_movedown" id="'+list[i].code+'">下移</label>';
					}else{
						trs +='<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+list[i].code+'">下移</a>';
					}
				}else{
					trs += '<label class="btn-opr btn-disabled">上移</label><label class="btn-opr btn-disabled">下移</label>';
				}
				trs +='<a href="javascript:;" class="btn-opr J_opr_del" id='+list[i].code+'>删除</a>';
				trs +='<a href="javascript:;" class="btn-opr" id='+list[i].code+'>模板详情</a>';
				trs +='</td></tr>';
			}
			$('.J_zbmgr_table > tbody').html(trs);
		}
	}
	
	/**
	 * 翻页操作
	 */
	$(document).pjax('.J_zbmgr_pagination a', '.J_zbmgr_data_table');
	
	var clip = new ZeroClipboard($("#copy"));
	clip.on("ready", function() {
		this.on('copy', function(event) {
			var str = '';
			var name='co_';
			$.each(delIds,function(i){
				if(i!=delIds.length-1){
					str += name + delIds[i]+",";
				}else{
					str += name + delIds[delIds.length-1];
				}
			});
			this.setText(str.replace(/,/g, "\n"));
		});
		this.on("aftercopy", function(event) {
			if(delIds==""){
				common.commonTips("请至少选中一条记录!");
			}else{
				common.commonTips("复制成功！");
			}
		});
	});
});