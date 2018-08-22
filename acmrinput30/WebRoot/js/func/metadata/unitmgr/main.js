define(function(require,exports,module){
	'use strict';
	var $ = require('jquery'),
	dropdown = require('dropdown'),
	Pagination = require('pagination'),
	common = require('common'),
	pjax=require('pjax'),
	tree = require('tree'),
	fileupload = require('fileupload'),
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
	var uuid = "";
	var idColumn="";
	var treeNodeName="计量单位树";
	var isMove = true;
	var searchField = "";
	var isFlush = false;//是否刷新
	var searchPara = $("#searchPara").val();
	if(searchPara.length>0){
		isMove = false;
	}
	
	
	/**
	 * 菜单树
	 */
	var st = new Date().getTime();//时间戳，解决ie9 ajax缓存//2015-7-2 by liaojin
	var setting = {
		async: {
			enable: true,
			url: common.rootPath+'metadata/unit.htm?m=findUnitTree&st='+st,
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
		//queryData(common.rootPath+"metadata/unit.htm?m=findDepTree", common.formatData('id',treeNode.id));
		treeNodeId = treeNode.id;
		treeNodeName = treeNode.name;
		isMove=true;
		
		var str = "";
		if(treeNode.id.length>0){
			str="&";
		}
		$.pjax({
			url: common.rootPath+"metadata/unit.htm?m=findDepTree"+str+ common.formatData('id',treeNode.id),
			container: '.J_unit_data_table'
		});
		$(document).on('pjax:success', function() {
			delIds = [];
		});
	}
	var rootNode = [{"id":"","name":"计量单位树", "open":"true", "isParent":"true"}];
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
		var requestData = common.formatData(key,val);
		if(requestData.length>0){
			requestData="&"+requestData;
		}
		searchField = requestData+str;
		isMove = false;
		$.pjax({
			url: requestUrl+searchField,
			container: '.J_unit_data_table'
		});
		$(document).on('pjax:success', function() {
			delIds = [];
		});
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
			url: common.rootPath+'metadata/unit.htm?m=matchingDetails',
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
		window.location.href=common.rootPath+"metadata/unit.htm?m=mdExport&id="+idColumn+"&uuid="+uuid;
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
		window.location.href = common.rootPath+"metadata/unit.htm?m=templateDownload";
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
	 * 批量导入文件上传
	 */
	function importData(){
		$('#J_fileupload_import',document).fileupload({
			url: common.rootPath+'metadata/unit.htm?m=importData',
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
		window.location.href=common.rootPath+"metadata/unit.htm?m=impErrInfoExport&uuid="+uuid;
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
	$(document).on('click', '#mouldDownload2', function(event) {
		window.location.href = common.rootPath+"metadata/unit.htm?m=templateDownload2";
	});
	
	/**
	 * 删除数据
	 */
	$(document).on('click','.J_opr_del',function(event){
		event.preventDefault();
		var self = this,
		delId = $(self).attr('id');
		$.ajax({
			url:common.rootPath+'metadata/unit.htm?m=delete',
			data:"id=" + delId,
			type:'post',
			dataType:'json',
			timeout:10000,
			success:function(data){	
				if(data.returncode == 200){
					$(self).parent().parent("tr").remove();
					common.commonTips("删除成功");
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
		$.ajax({
			url:common.rootPath+'metadata/unit.htm?m=deleteAll',
			data:"ids=" + delIds,
			type:'post',
			dataType:'json',
			timeout:10000,
			success:function(data){
				if(data.returncode==200){
					$("tbody input:checked").parent().parent("tr").remove();
					$("thead input:checked").removeAttr("checked");
					common.commonTips("删除成功");
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
			url:common.rootPath+'metadata/unit.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
					common.cancalCheck('.J_unit_table input[type="checkbox"]',delIds);
//					var tempHTML = '';
//					tempHTML = '<tr>'+$(self).parents('tr:eq(0)').html()+'</tr>';
//					if(len===2){
//						var spHTML = '',
//						targetHTML = '';
//						spHTML=$(self).parents('tbody').children('tr:eq(0)').html();
//						spHTML=spHTML.replace('<label class="btn-opr btn-disabled J_opr_moveup" id="'+siblingsId+'">上移</label>','<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+siblingsId+'">上移</a>');
//						spHTML=spHTML.replace('<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+siblingsId+'">下移</a>','<label class="btn-opr btn-disabled J_opr_movedown" id="'+siblingsId+'">下移</label>');			
//						$(self).parents('tbody').children('tr:eq(0)').html(spHTML);
//						tempHTML=tempHTML.replace('<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+currentId+'">上移</a>','<label class="btn-opr btn-disabled J_opr_moveup" id="'+currentId+'">上移</label>');
//						tempHTML=tempHTML.replace('<label class="btn-opr btn-disabled J_opr_movedown" id="'+currentId+'">下移</label>','<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+currentId+'">下移</a>');
//						$(self).parents('tbody').children('tr:eq(0)').before(tempHTML);
//					}else if(indexVal === 1){
//						var spHTML = '',
//						HTML='',
//						targetHTML = '';
//						spHTML=$(self).parents('tbody').children('tr:eq(0)').html();
//						HTML=spHTML.replace('<label class="btn-opr btn-disabled J_opr_moveup" id="'+siblingsId+'">上移</label>','<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+siblingsId+'">上移</a>');
//						$(self).parents('tbody').children('tr:eq(0)').html(HTML);
//						tempHTML=tempHTML.replace('<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+currentId+'">上移</a>','<label class="btn-opr btn-disabled J_opr_moveup" id="'+currentId+'">上移</label>');
//						$(self).parents('tbody').children('tr:eq(0)').before(tempHTML);
//					}
//					else if(indexVal > 1 && indexVal< $('.J_unit_table tbody').children('tr').length-1){
//						$(self).parents('tbody').children('tr:eq('+(indexVal-2)+')').after(tempHTML);
//					}
//					else if(indexVal==$('.J_unit_table tbody').children('tr').length-1){
//						var spHTML='',
//						HTML='',
//						targetHTML='';
//						spHTML=$(self).parents('tbody').children('tr:eq('+(indexVal-1)+')').html();
//						HTML=spHTML.replace('<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+siblingsId+'">下移</a>','<label class="btn-opr btn-disabled J_opr_movedown" id="'+siblingsId+'">下移</label>');
//						$(self).parents('tbody').children('tr:eq('+(indexVal-1)+')').html(HTML);
//						tempHTML=tempHTML.replace('<label class="btn-opr btn-disabled J_opr_movedown" id="'+currentId+'">下移</label>','<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+currentId+'">下移</a>');
//						$(self).parents('tbody').children('tr:eq('+(indexVal-1)+')').before(tempHTML);
//					}
//					$(self).parents('tr:eq(0)').remove();
					common.commonTips('上移成功');
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips('上移失败');
				}
			},
			error:function(e){}
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
			url:common.rootPath+'metadata/unit.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
					common.cancalCheck('.J_unit_table input[type="checkbox"]',delIds);
//					var tempHTML = '';
//					tempHTML = '<tr>'+$(self).parents('tr:eq(0)').html()+'</tr>';
//					if(len===2){
//						var spHTML = '',
//						HTML='';
//						spHTML=$(self).parents('tbody').children('tr:eq(0)').html();
//						spHTML=spHTML.replace('<label class="btn-opr btn-disabled J_opr_moveup" id="'+currentId+'">上移</label>','<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+currentId+'">上移</a>');
//						spHTML=spHTML.replace('<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+currentId+'">下移</a>','<label class="btn-opr btn-disabled J_opr_movedown" id="'+currentId+'">下移</label>');	
//						$(self).parents('tbody').children('tr:eq(1)').after('<tr>'+spHTML+'</tr>');
//						HTML=$(self).parents('tbody').children('tr:eq(1)').html();
//						HTML=HTML.replace('<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+siblingsId+'">上移</a>','<label class="btn-opr btn-disabled J_opr_moveup" id="'+siblingsId+'">上移</label>');
//						HTML=HTML.replace('<label class="btn-opr btn-disabled J_opr_movedown" id="'+siblingsId+'">下移</label>','<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+siblingsId+'">下移</a>');
//						$(self).parents('tbody').children('tr:eq(1)').html(HTML);
//					}
//					else if(indexVal === 0){
//						var spHTML = '',
//						HTML='';
//						spHTML=$(self).parents('tbody').children('tr:eq(0)').html();
//						spHTML=spHTML.replace('<label class="btn-opr btn-disabled J_opr_moveup" id="'+currentId+'">上移</label>','<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+currentId+'">上移</a>');					
//						$(self).parents('tbody').children('tr:eq(1)').after('<tr>'+spHTML+'</tr>');
//						HTML=$(self).parents('tbody').children('tr:eq(1)').html();
//						HTML=HTML.replace('<a href="javascript:;" class="btn-opr J_opr_moveup" id="'+siblingsId+'">上移</a>','<label class="btn-opr btn-disabled J_opr_moveup" id="'+siblingsId+'">上移</label>');
//						$(self).parents('tbody').children('tr:eq(1)').html(HTML);
//					}else if(indexVal > 0 && indexVal< len-2){
//						var tempHTML = '';
//						tempHTML = '<tr>'+$(self).parents('tr:eq(0)').html()+'</tr>';
//					$(self).parents('tbody').children('tr:eq('+(indexVal+1)+')').after(tempHTML);
//						$(self).parents('tr:eq(0)').remove();
//					}else if(indexVal===len-2){
//						var spHTML='',
//						HTML='';
//						spHTML=$(self).parents('tbody').children('tr:eq('+(indexVal)+')').html();
//						spHTML=spHTML.replace('<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+currentId+'">下移</a>','<label class="btn-opr btn-disabled J_opr_movedown" id="'+currentId+'">下移</label>');
//						$(self).parents('tbody').children('tr:eq('+(indexVal+1)+')').after('<tr>'+spHTML+'</tr>');
//						HTML=$(self).parents('tbody').children('tr:eq('+(indexVal+1)+')').html();
//						HTML=HTML.replace('<label class="btn-opr btn-disabled J_opr_movedown" id="'+siblingsId+'">下移</label>','<a href="javascript:;" class="btn-opr J_opr_movedown" id="'+siblingsId+'">下移</a>');
//						$(self).parents('tbody').children('tr:eq('+(indexVal+1)+')').html(HTML);
//					}
//					$(self).parents('tr:eq(0)').remove();
					common.commonTips('下移成功');
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips('下移失败');
				}
			},
			error:function(e){}
		});
	});

	function PackAjax(){
		var responseState = true;
		return function(cfg){
			if(!responseState){
				alert('正在进行中');
				return;
			}
			var config ={
				url:cfg.url || '',
				type:cfg.type ||'post',
				dataType:cfg.dataType || 'json',
				timeout:cfg.timeout||5000,
				success:cfg.success || function(){}
			};
			$.ajax({
				url:config.url,
				type:config.type,
				dataType:config.dataType,
				success:config.success,
				error:function(e){},
				complete:function(){
					responseState = true;
				}
			});
		};
	}
	
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
				if(data.returncode==200){
					var returndata=data.returndata;
					formatBackTable(data);
					new Pagination({
						totalPage:returndata.totalPage,
						currentPage:returndata.currentPage,
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
	 * 跳转到新增界面
	 */
	$(document).on('click','.J_add_data',function(event){
		event.preventDefault();
		window.location.href=common.rootPath+"metadata/unit.htm?m=turnToAdd&code=" + treeNodeId + "&cname=" + treeNodeName;
	});
	
	/**
	 *点击code展开子节点 
	 */
	$(document).on('click','.J_opr_code',function(event){
		event.preventDefault();
		var self = this,
		currentCode = $(self).text();
		var flag = false;
		$.ajax({
			url:common.rootPath+"metadata/unit.htm?m=checkHasParent&code="+currentCode,
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
			url: common.rootPath+"metadata/unit.htm?m=findDepTree&id="+currentCode,
			container: '.J_unit_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	});
	
	
	/**
	 *提交选择的checkebox(默认所有)
	 */
	$(document).on('click','.J_unit_table>thead>tr>th>input[type="checkbox"]',function(){
		var self = this;
		delIds = common.checkAll('.J_unit_table>tbody input[type="checkbox"]',$(self).prop('checked'),delIds);
	});
	$(document).on('click','.J_unit_table>tbody input[type="checkbox"]',function(){
		var self = this,
		currentId =  $(self).attr('value'),
		currentState = $(self).prop('checked');
		if(currentState){
			common.oprArray('add',currentId,delIds);
		}else{
			common.oprArray('del',currentId,delIds);
		}
		if(delIds.length === $('.J_unit_table>tbody input[type="checkbox"]').length){
			$('.J_unit_table>thead>tr>th>input[type="checkbox"]').attr('checked',true);
		}else{
			$('.J_unit_table>thead>tr>th>input[type="checkbox"]').attr('checked',false);
		}
	});
	
	/**
	 * 导出
	 */
	$(document).on('click','.J_export_data',function(event){
		event.preventDefault();
		if(delIds.length>0){//有没有被选中的指标
			window.location.href=common.rootPath+"metadata/unit.htm?m=exportAllData&ids="+delIds;
		}else if(isMove){
			window.location.href=common.rootPath+"metadata/unit.htm?m=exportData&id="+treeNodeId+"&procodeName="+treeNodeName;
		}else{
			if(searchField.length>0){
				searchPara = searchField;
			}
			window.location.href=common.rootPath+"metadata/unit.htm?m=exportSearchData&"+searchField+"&procodeName="+treeNodeName+searchPara;
		}
	});
	
	/**
	 * 格式化数据表
	 */
	function formatBackTable(data){
		if(data.returncode == 200){
			common.cancalCheck('.J_unit_table input[type="checkbox"]',delIds);
			var returndata = data.returndata;
			var list=returndata.data;
			var len = list.length,
			trs = '',
			i = 0;
			if(len === 0){
				trs += '<tr><td></td>';
				trs += '<td></td>';
				trs += '<td></td>';
				trs += '<td>没有查询到数据</td>';
				trs += '<td></td>';
				trs += '<td></td></tr>';
				
				$('.J_unit_table > tbody').html(trs);
				return;
			}
			for (i; i < len; i++) {
				var index = (returndata.pageNum - 1)*returndata.pageSize+(i+1);
				trs += '<tr><td><input type="checkbox" value="'+list[i].code+'"/></td>';
				trs +='<td>'+index+'</td>';
				trs +='<td><a href="javascript:;" class="btn-opr J_opr_code">'+list[i].code+'</a></td>';
				trs +='<td><a href="'+common.rootPath+'metadata/unit.htm?m=getById&id='+list[i].code+'" >'+list[i].cname+'</a></td>';
				trs +='<td>'+list[i].rate+'</td>';
				trs +='<td><a href="'+common.rootPath+'metadata/unit.htm?m=turnToEdit&code='+list[i].code+'" class="btn-opr J_opr_edit">编辑</a>';
				/*trs +='<a href="'+common.rootPath+'metadata/unit.htm?m=delete&id='+list[i].code+'" class="btn-opr J_opr_del" id='+list[i].code+'>删除</a>';*/
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
				trs +='</td></tr>';
			}
			$('.J_unit_table > tbody').html(trs);
		}
	}
	/**
	 * 翻页操作
	 */
	$(document).pjax('.J_unit_pagination a', '.J_unit_data_table');
	
	var clip = new ZeroClipboard($("#copy"));
	clip.on("ready", function() {
		this.on('copy', function(event) {
			var str = '';
			var name='unit_';
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