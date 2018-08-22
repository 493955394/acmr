define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
	common = require('common'),
	dropdown = require('dropdown'),
	AjaxMods = require('AjaxMods'),
	Pagination = require('pagination');
	
	function formatBackTable(data){
		var list = data.data,
		len = list.length,
		trs = '';
		if(len === 0){
			trs += '<tr><td></td>';
			trs += '<td>没有查询到数据</td>';
			trs += '<td></td>';
			trs += '<td></td></tr>';
			$('.J_zbgmgr_template > tbody').html(trs);
			return;
		}
		for (var i = 0; i < len; i++) {
			var index = (data.pageNum - 1)*data.pageSize+(i+1);
			trs += '<tr><td>'+index+'</td>';
			trs +='<td>'+list[i].code+'</td>';
			trs +='<td>'+list[i].name+'</td>';
			trs +='<td>'+list[i].state+'</td>'; 
			trs +='</tr>';
		}
		$('.J_zbgmgr_template > tbody').html(trs);
	}	
	
	$('.J_template_pagination').delegate('a','click',function(event){
		event.preventDefault();
		var self = this,
		requestUrl = $(self).prop('href');
		$.ajax({
			url:requestUrl,
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.returncode == 200){
					$('.J_zbgmgr_template > tbody').html("");
					formatBackTable(data.returndata);
					new Pagination({
						totalPage:data.returndata.totalPage,
						currentPage:data.returndata.pageNum,
						posePageCount:3,
						currentObj:$('.J_template_pagination'),
						url:data.returndata.url
					});
				}
			}
		})
	});
	
	/**
	 * 选择制度期别类型
	 */
	$(document).on('change','#templateStageType',function(event){
		event.preventDefault();
		var self = this,
		code = $("#code").val(),
		sortVal = $(this).children('option:selected').val(),
		currentUrl = common.rootPath+'metadata/zbgmgr.htm?m=getTemplateData&code='+code+"&type="+sortVal;
		$.ajax({
			url:currentUrl,
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.returncode == 200){
					formatBackTable(data.returndata);
					$("#templateCount").html(data.returndata.totalRecorder);
					new Pagination({
						totalPage:data.returndata.totalPage,
						currentPage:data.returndata.pageNum,
						posePageCount:3,
						currentObj:$('.J_template_pagination'),
						url:data.returndata.url
					});
				}
			}
		})
	});
});