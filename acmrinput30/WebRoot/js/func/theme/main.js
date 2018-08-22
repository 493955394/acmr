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
	VaildNormal = require('vaildnormal'),
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
			url: common.rootPath+'theme/theme.htm?m=findTree&st='+st,
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
			url: common.rootPath+"theme/theme.htm?m=query"+str+ common.formatData('id',treeNode.id),
			container: '.J_zbmgr_data_table'
		});
		$(document).on('pjax:success', function() {
		  delIds = [];
		});
	}
	var rootNode = [{"id":"","name":"分类查询", "open":"true", "isParent":"true"}];
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
		if(!confirm("确定要删除该菜单及所有子集菜单吗？")){
			return;
		}
		$.ajax({
			url:common.rootPath+'theme/theme.htm?m=toDelete',
			data:"code=" + delId,
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
		window.location.href=common.rootPath+"theme/theme.htm?m=toAdd&procodeId=" + treeNodeId;
	});
	
	/**
	 * 翻页操作
	 */
	//$(document).pjax('.J_zbmgr_pagination a', '.J_zbmgr_data_table');
	
	//全选和反选
	$(document).on('click', '.J_all_checkbox', function() {
		var self = this;
		$("input[name='reportcode']").prop("checked",$(this).prop("checked"));
	});

	
	/**
	 * 添加分类弹窗
	 */
    $(document).on('click', '.J_temp_task', function(event) {
        event.preventDefault();
        if ($('#tempTaskTemplate').length > 0) {
            $('#tempTaskTemplate').remove();
        }
        var modalContent = '<div class="modal-header">';
        modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
        modalContent += '<h4 class="modal-title">新建分类</h4>';
        modalContent += '</div>';

        modalContent += '<div class="modal-body">';
        modalContent += '<div class="form-horizontal">';
        modalContent += '<div class="form-group">';
        modalContent += '<label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>栏目ID</label>';
        modalContent += '<div class="col-sm-7">';
        modalContent += '<input type="text" class="form-control" name="code" maxlength="10"><span></span>';
        modalContent += '</div>';
        modalContent += '</div>';

        modalContent += '<div class="form-group">';
        modalContent += '<label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>栏目名称</label>';
        modalContent += '<div class="col-sm-7">';
        modalContent += '<input type="text" class="form-control" name="cname" maxlength="50">';
        modalContent += '</div>';
        modalContent += '</div>';

        modalContent += '<div class="modal-footer">';
        modalContent += '<button type="button" class="btn btn-primary J_add_task_btn">保存</button>';
        modalContent += '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
        modalContent += '</div>';
        common.buildModelHTML('tempTaskTemplate', modalContent);
        $('#tempTaskTemplate').modal({ backdrop: 'static', keyboard: false }).modal('show');
        $("#tempTaskTemplate").on("hidden.bs.modal", function() {
            $(this).remove();
        });
    });

    
    function editfl(ecode,cname){
    	 if ($('#tempTaskTemplate').length > 0) {
             $('#tempTaskTemplate').remove();
         }
         var modalContent = '<div class="modal-header">';
         modalContent += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
         modalContent += '<h4 class="modal-title">编辑分类</h4>';
         modalContent += '</div>';

         modalContent += '<div class="modal-body">';
         modalContent += '<div class="form-horizontal">';
         modalContent += '<div class="form-group">';
         modalContent += '<label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>栏目ID</label>';
         modalContent += '<div class="col-sm-7">';
         modalContent += '<input type="text" class="form-control" name="ecode" value='+ecode+' maxlength="10" readonly="true"><span></span>';
         modalContent += '</div>';
         modalContent += '</div>';

         modalContent += '<div class="form-group">';
         modalContent += '<label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>栏目名称</label>';
         modalContent += '<div class="col-sm-7">';
         modalContent += '<input type="text" class="form-control" name="cname"  value='+cname+' maxlength="50">';
         modalContent += '</div>';
         modalContent += '</div>';

         modalContent += '<div class="modal-footer">';
         modalContent += '<button type="button" class="btn btn-primary J_edit_task_btn">保存</button>';
         modalContent += '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
         modalContent += '</div>';
         common.buildModelHTML('tempTaskTemplate', modalContent);
         $('#tempTaskTemplate').modal({ backdrop: 'static', keyboard: false }).modal('show');
         $("#tempTaskTemplate").on("hidden.bs.modal", function() {
             $(this).remove();
         });
    }
    
    /**
     * 新增分类提交
     */
    $(document).on('click', '.J_add_task_btn', function(event) {
        event.preventDefault();
        var self = this,
            cname,
        	code,
        	checkDelegate;

		checkDelegate = new VaildNormal();
        code = $("[name='code']").val();
        cname = $("[name='cname']").val();
		if(!checkDelegate.checkNormal($("[name='code']"), [{'name': 'required','msg': '栏目ID不能为空'}])){
			return;
		}
		
		if(!checkDelegate.checkNormal($("[name='cname']"), [{'name': 'required','msg': '栏目名称不能为空'}])){
			return;
		}
		
		//后台数据检查
		if(!checkAsyncExist($('input[name="code"]').val(),checkDelegate)){
			common.commonTips('保存失败');
			return;
		}
		
        $.ajax({
            url: common.rootPath + 'theme/theme.htm?m=savefl',
            type: 'get',
            data: {
            	"code": code,
            	"cname": cname
            },
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    common.commonTips('添加成功');
					setTimeout("window.location.reload(true)",1000);
                }else{
                	common.commonTips('添加失败');
                }
            }
        });
    });
    
    /**
     * 修改分类提交
     */
    $(document).on('click', '.J_edit_task_btn', function(event) {
        event.preventDefault();
        var self = this,
            cname,
        	code,
        	checkDelegate;

		checkDelegate = new VaildNormal();
        code = $("[name='ecode']").val();
        cname = $("[name='cname']").val();
		if(!checkDelegate.checkNormal($("[name='ecode']"), [{'name': 'required','msg': '栏目ID不能为空'}])){
			return;
		}
		
		if(!checkDelegate.checkNormal($("[name='cname']"), [{'name': 'required','msg': '栏目名称不能为空'}])){
			return;
		}
		
        $.ajax({
            url: common.rootPath + 'theme/theme.htm?m=updatefl',
            type: 'post',
            data: {
            	"code": code,
            	"cname": cname
            },
            dataType: 'json',
            success: function(data) {
                if (data.returncode == 200) {
                    common.commonTips('修改成功');
					setTimeout("window.location.reload(true)",1000);
                }else{
                	common.commonTips('修改失败');
                }
            }
        });
    });

    $(document).on('blur','input[name="code"]',function(){
		var self = this,
			checkDelegate  = new VaildNormal();
		if(checkDelegate.checkNormal($(self), [{'name': 'required','msg': '栏目ID不能为空'}])){
			checkAsyncExist($(self).val(),checkDelegate);
		}
	});
	/**
	 * 后台检验是否重复
	 */
	function checkAsyncExist(currentVal,checkDelegate){
		var flag = false;
		/*if (!checkDelegate.checkNormal($('input[name="code"]'))){
			return;
		}*/
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
	
	/**
	 * 跳转到编辑界面
	 */
	$(document).on('click','.J_opr_edit',function(event){
		event.preventDefault();
		var self = this,
		code = $(self).attr('id'),
		procode = $(self).attr('procode'),
		cname;
		if(procode==''){
			// 弹窗编辑
			$.ajax({
				url: common.rootPath+'theme/theme.htm?m=getByCode',
				timeout: 5000,
				type: 'get',
				async: false,
				data: "code="+code,
				dataType: 'json',
				success: function(data) {
					if(data.returncode == 200){
						cname = data.returndata.cname;
					}else{
	                	common.commonTips('修改失败');
					}
				},
				error: function(e) {}
			});
			editfl(code, cname);
		}else{
			// 跳转编辑
			window.location.href=common.rootPath+"theme/theme.htm?m=toEdit&code="+code+"&procodeId=" + treeNodeId;
		}
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
			url:common.rootPath+'theme/theme.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
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
			url:common.rootPath+'theme/theme.htm',
			type:'get',
			dataType:'json',
			data:'m=move&currentId='+currentId+'&siblingsId='+siblingsId,
			success:function(data){
				if(data.returncode == 200){
					common.commonTips('下移成功');
					setTimeout("window.location.reload(true)",1000);
				}else{
					common.commonTips('下移失败');
				}
			},
			error:function(e){}
		});
	});
});