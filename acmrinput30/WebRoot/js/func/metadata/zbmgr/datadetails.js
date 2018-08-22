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
			trs += '<td></td>';
			trs += '<td></td>';
			trs += '<td></td>';
			trs += '<td></td></tr>';
			$('.J_zbmgr_template > tbody').html(trs);
			return;
		}
		for (var i = 0; i < len; i++) {
			trs +='<tr>';
			trs +='<td>'+list[i].flName+"【"+list[i].fl+"】"+'</td>';
			trs +='<td>'+list[i].regName+"【"+list[i].reg+"】"+'</td>';
			trs +='<td>'+list[i].beginTime+'</td>';
			trs +='<td>'+list[i].endTime+'</td>';
			trs +='<td>'+list[i].updateTime+'</td>';
			trs +='<td>'+list[i].count+'</td>';
			trs +='<td>'+list[i].datasourceName+"【"+list[i].datasource+"】"+'</td>';
			trs +='</tr>';
		}
		$('.J_zbmgr_template > tbody').html(trs);
	}	
	
	$('.J_template_pagination').delegate('a','click',function(event){
		event.preventDefault();
		var self = this,
		requestUrl = $(self).prop('href');
		$.ajax({
			url:requestUrl,
			type:'post',
			dataType:'json',
			beforeSend: function() {
				$("body").append('<div class="ui dimmer ciDimmer"><div class="ui loader"></div></div>');
			},
			success:function(data){
				$(".ciDimmer.ui.dimmer").remove();
				if(data.returncode == 200){
					$('.J_zbmgr_template > tbody').html("");
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
		});
	});
	
	/**
	 * 导出数据
	 */
	$(document).on('click','.J_export',function(event){
		event.preventDefault();
		var code = $("#code").val(),
		codename = $("#cname").val(),
		dbcode = $("#database").children('option:selected').val(),
		dbcodename = $("#database").children('option:selected').text(), 
		flcode = $("#zbclass").children('option:selected').val(),
		flcodename = $("#zbclass").children('option:selected').text(),
		regcode = $("#region").children('option:selected').val(),
		regcodename = $("#region").children('option:selected').text(),
		datasource = $("#datasource").children('option:selected').val(),
		datasourcename = $("#datasource").children('option:selected').text(),
		currentUrl = common.rootPath+'metadata/zbmgr.htm?m=exportDatadetails&code='+code+"&dbcode="+dbcode+"&flcode="+flcode+"&regcode="+regcode+"&datasource="+datasource+'&dbcodename='+dbcodename +"&flcodename="+flcodename +"&regcodename="+regcodename+"&datasourcename="+datasourcename+"&codename="+codename;
		window.location.href = currentUrl;
	});
	
	/**
	 * 数据库
	 */
	$(document).on('change','#database',function(event){
		event.preventDefault();
		var self = this,
		code = $("#code").val(),
		sortVal = $(this).children('option:selected').val(),
		reqUrl = common.rootPath+'metadata/zbmgr.htm?m=getOtherSel&code='+code+"&dbcode="+sortVal+"&flag=1";
		var flag = false;
		$.ajax({
			url:reqUrl,
			type:'post',
			dataType:'json',
			async:false,
			success:function(data){
				if(data.returncode == 200){
					var fldata = data.returndata.fldata;
					$("#zbclass").html("<option value=''>全部</option>");
					for (var i = 0; i< fldata.length; i++) {
						var str = "<option value='"+fldata[i].code+"'>"+fldata[i].cname+"【"+fldata[i].code+"】</option>";
						$("#zbclass").append(str);
					}
					
					var regdata = data.returndata.regdata;
					$("#region").html("<option value=''>全部</option>");
					for (var i = 0; i< regdata.length; i++) {
						var str = "<option value='"+regdata[i].code+"'>"+regdata[i].cname+"【"+regdata[i].code+"】</option>";
						$("#region").append(str);
					}
					
					var dsdata = data.returndata.dsdata;
					$("#datasource").html("<option value=''>全部</option>");
					for (var i = 0; i< dsdata.length; i++) {
						var str = "<option value='"+dsdata[i].code+"'>"+dsdata[i].cname+"【"+dsdata[i].code+"】</option>";
						$("#datasource").append(str);
					}
					flag = true;
				}
			}
		});
		if(flag){
			var flcode = $("#zbclass").children('option:selected').val(),
			regcode = $("#region").children('option:selected').val(),
			datasource = $("#datasource").children('option:selected').val(),
			currentUrl = common.rootPath+'metadata/zbmgr.htm?m=toDatadetails&code='+code+"&dbcode="+sortVal+"&flcode="+flcode+"&regcode="+regcode+"&datasource="+datasource;
			$.ajax({
				url:currentUrl,
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.returncode == 200){
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
			});
		}
	});
	
	/**
	 * 分组
	 */
	$(document).on('change','#zbclass',function(event){
		event.preventDefault();
		var self = this,
		code = $("#code").val(),
		sortVal = $(this).children('option:selected').val(),
		dbcode = $("#database").children('option:selected').val(),
		reqUrl = common.rootPath+'metadata/zbmgr.htm?m=getOtherSel&code='+code+"&dbcode="+dbcode+"&flcode="+sortVal+"&flag=2";
		var flag = false;
		$.ajax({
			url:reqUrl,
			type:'post',
			dataType:'json',
			async:false,
			success:function(data){
				if(data.returncode == 200){
					var regdata = data.returndata.regdata;
					$("#region").html("<option value=''>全部</option>");
					for (var i = 0; i< regdata.length; i++) {
						var str = "<option value='"+regdata[i].code+"'>"+regdata[i].cname+"【"+regdata[i].code+"】</option>";
						$("#region").append(str);
					}
					
					var dsdata = data.returndata.dsdata;
					$("#datasource").html("<option value=''>全部</option>");
					for (var i = 0; i< dsdata.length; i++) {
						var str = "<option value='"+dsdata[i].code+"'>"+dsdata[i].cname+"【"+dsdata[i].code+"】</option>";
						$("#datasource").append(str);
					}
					flag = true;
				}
			}
		});
		
		if(flag){
			var regcode = $("#region").children('option:selected').val(),
			datasource = $("#datasource").children('option:selected').val(),
			currentUrl = common.rootPath+'metadata/zbmgr.htm?m=toDatadetails&code='+code+"&dbcode="+dbcode+"&flcode="+sortVal+"&regcode="+regcode+"&datasource="+datasource;
			$.ajax({
				url:currentUrl,
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.returncode == 200){
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
			});
		}
	});
	
	/**
	 * 地区
	 */
	$(document).on('change','#region',function(event){
		event.preventDefault();
		var self = this,
		code = $("#code").val(),
		dbcode = $("#database").children('option:selected').val(),
		flcode = $("#zbclass").children('option:selected').val(),
		sortVal = $(this).children('option:selected').val(),
		reqUrl = common.rootPath+'metadata/zbmgr.htm?m=getOtherSel&code='+code+"&dbcode="+dbcode+"&flcode="+flcode+"&regcode="+sortVal+"&flag=3";
		var flag = false;
		$.ajax({
			url:reqUrl,
			type:'post',
			dataType:'json',
			async:false,
			success:function(data){
				if(data.returncode == 200){
					var dsdata = data.returndata.dsdata;
					$("#datasource").html("<option value=''>全部</option>");
					for (var i = 0; i< dsdata.length; i++) {
						var str = "<option value='"+dsdata[i].code+"'>"+dsdata[i].cname+"【"+dsdata[i].code+"】</option>";
						$("#datasource").append(str);
					}
					flag = true;
				}
			}
		});
		
		if(flag){
			var datasource = $("#datasource").children('option:selected').val(),
			currentUrl = common.rootPath+'metadata/zbmgr.htm?m=toDatadetails&code='+code+"&dbcode="+dbcode+"&flcode="+flcode+"&regcode="+sortVal+"&datasource="+datasource;
			$.ajax({
				url:currentUrl,
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.returncode == 200){
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
			});
		}
	});
	
	/**
	 * 数据来源
	 */
	$(document).on('change','#datasource',function(event){
		event.preventDefault();
		var self = this,
		code = $("#code").val(),
		dbcode = $("#database").children('option:selected').val(),
		flcode = $("#zbclass").children('option:selected').val(),
		regcode = $("#region").children('option:selected').val(),
		sortVal = $(this).children('option:selected').val(),
		currentUrl = common.rootPath+'metadata/zbmgr.htm?m=toDatadetails&code='+code+"&dbcode="+dbcode+"&flcode="+flcode+"&regcode="+regcode+"&datasource="+sortVal;
		$.ajax({
			url:currentUrl,
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.returncode == 200){
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
		});
	});
	
});