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
	var treeNodeName = "全部时间";
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
			url: common.rootPath+'metadata/mdate.htm?m=findMdateTree&st='+st,
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
		treeNodeId = treeNode.id;
		treeNodeName = treeNode.name;
		isMove = true;
		
		var str = "";
		if(treeNode.id.length>0){
			str="&";
		}
		$.pjax({
			url: common.rootPath+"metadata/hvmgr.htm?m=query"+str+ common.formatData('id',treeNode.id)+"&unitcode="+$("#unitcode").val()+"&sjcode="+$("#sjcode").val(),
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	}
	var rootNode = [{"id":"all","name":"默认汇率", "open":"true", "isParent":"false"},{"id":"","name":"全部时间", "open":"true", "isParent":"true"}];
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, rootNode);
	var treenodes = treeObj.getNodes();
	treeObj.expandNode(treenodes[1], true, true, true);
	
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
			url:common.rootPath+'metadata/hvmgr.htm?m=toDelete',
			data:"delId=" + delId,
			type:'post',
			dataType:'json',
			timeout:10000,
			success:function(data){
				if(data.returncode == 200){
					common.commonTips("删除成功");
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips(data.returndata);
				}
			}
		});
	});
	
	
	/**
	 * 跳转到新增界面
	 */
	$(document).on('click','.J_Add',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/hvmgr.htm?m=toAdd&procodeId=" + treeNodeId + "&procodeName=" + treeNodeName;
	});
	
	/**
	 * 翻页操作
	 */
	//$(document).pjax('.J_zbmgr_pagination a', '.J_zbmgr_data_table');
	
	//折叠
	$(document).on('change', '#unitcode,#sjcode', function(event){
		window.location.href=common.rootPath+"metadata/hvmgr.htm?m=query&id="+treeNodeId+"&unitcode="+$("#unitcode").val()+"&sjcode="+$("#sjcode").val();
	});
	
	//全选和反选
	$(document).on('click', '.J_all_checkbox', function() {
		var self = this;
		$("input[name='reportcode']").prop("checked",$(this).prop("checked"));
	});
	
	//批量导出
	$(document).on('click','.J_export_data',function(event){
		var currentId = '';
		$("[name='reportcode']:checked").each(function(i){
			if(i != 0){
				currentId += "," + $(this).val();
			}else{
				currentId += $(this).val();
			}
		});
		
		if(currentId.length==0){
			//下载全部
			window.location.href=common.rootPath+"metadata/hvmgr.htm?m=export&id="+treeNodeId+"&unitcode="+$("#unitcode").val()+"&sjcode="+$("#sjcode").val();
		}else{
			//下载选中的
			window.location.href=common.rootPath+"metadata/hvmgr.htm?m=exportData&ids="+currentId;
		}
	})
	
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
		modalContent += '<div class="form-group"><span class="btn btn-success fileinput-button"><span>文件上传</span><input type="file" name="file" id="J_fileupload_import" multiple></span><span class="btn btn-success fileinput-button" style="margin-left:20px;" id="mouldDownload"><span>模板下载</span></span></div>';
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
	 * 批量导入模板下载
	 */
	$(document).on('click', '#mouldDownload', function(event) {
		window.location.href = common.rootPath+"metadata/hvmgr.htm?m=templateDownload";
	});
	
	/**
	 * 批量导入文件上传
	 */
	function importData(){
		$('#J_fileupload_import',document).fileupload({
			url: common.rootPath+'metadata/hvmgr.htm?m=importData',
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

});