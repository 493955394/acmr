define(function(require, exports, module) {
	//维度提交
	var $ = require('jquery'),
	 	VaildNormal = require('vaildnormal'),
		common = require('common'),
		pjax = require('pjax'),
		dropdown = require('dropdown'),
		Handlebars = require('handlebars');
		require('fileupload');
	
	//指标提交
	$(document).on('click', '.J_submit', function(e) {
		var name,
		memo,
		id=$(this).attr('id'),
		dimensionList=[],
		elementlist,
		len,i,
		sendData;
        checkDelegate = new VaildNormal();
        if($('.danger').length>0){
        	return;
        }
        if (!checkDelegate.checkNormal($('#replymemo'), [{
            'name': 'required',
            'msg': '审批意见不能为空'
        },{
            'name': 'maxlength',
            'msg': '审批意见最多为300个字符',
            'param': 301
        }])){
        	return;
        }
		elementlist = $('#J_edit_table').find('tr');
		len=elementlist.length;
	
		for(i=0;i<len;i++){
			if($.trim($(elementlist[i]).children('td:eq(2)').html()) !== ''){
				dimensionList.push({
					type: $(elementlist[i]).find('td:eq(1) select option:selected').val(),
					memo: $(elementlist[i]).children('td:eq(2)').html(),
					name: $(elementlist[i]).children('td:eq(3)').html()
				});
			}
		}
		$.ajax({
			url: common.rootPath+'/system/Handle.htm?m=submit',
			dataType: 'json',
			data:{
				id:id,
				memo : $('#replymemo').val(),
				dimensionList: JSON.stringify(dimensionList),
			},
			type:'post',
			success:function(data){
				history.go(-1);
			}
		});
	});
	
	
	function randomNumber(){
		var str = '';
		for(var i = 0; i < 6; i ++){
			str += Math.floor(Math.random() * 10);
		}
		return str;
	}
	/**
	 * 添加提交维度列表
	 */
	$(document).on('click', '.add_list_item', function(){
		var strHTML='',
			length = $('#J_edit_table tbody tr').length;
		strHTML+='<tr><td>'+ (length+1) +'</td><td><select><option value ="指标">指标</option><option value ="分组">分组</option><option value="计量单位">计量单位</option>';
		strHTML+='<option value="时间类型">时间类型</option><option value="时间">时间</option><option value="数据来源">数据来源</option>';
		strHTML+='<option value="地区管理">地区管理</option></select></td><td></td><td></td>';
		strHTML+='<td><a href="#" class="J_dimension_delete">删除</a></td></tr>';
		$('#J_edit_table tbody').append(strHTML);
	});
	/**
	 * 删除列表项
	 */
//	$(document).on('click', '.J_dimension_delete',function(e){
//		var self = this,
//			parent = $(self).parent().parent();
//		parent.remove();
//	})
	/**
	 * 编辑列表
	 */
	$(document).on('click', '#J_edit_table' ,function(event){
		var self = $(event.target);
		if(self[0].nodeName.toUpperCase() === "TD" && self.parent()[0].nodeName.toUpperCase() === "TR" &&
				self.children('a').length===0 && self.children('select').length===0){
			if(self.children('input').length ===0){
				var inputObj = $('<input type="text" class="J_editing_input"/>');
				inputObj.height(self.height());
				inputObj.width(self.width());
				inputObj.val(self.html());
				self.html('');
				self.append(inputObj);
				inputObj[0].focus();
			}
		}
	});
	$(document).on('blur','.J_editing_input',function(){
		var self = this,
			value;
		$(self).parent().removeClass('danger');
		value=$(self).val();
		if(value.length > 50){
			$(self).parent().addClass('danger');
		}
		$(self).parent().html(value);
		self.remove();
	});
	$(document).on('click','.J_dimension_delete',function(){
		var self = this,
			tr =$('#J_edit_table tr'),
			parent = $(self).parent().parent(),
			strHtml;
			parent.remove();
	});
	$(document).on('click','.J_excel_download',function(){
		var self = this,
			id = $(self).attr('id'),
			href = common.rootPath+'/system/Handle.htm?m=download&id='+id;
			window.location.href =href;
	});
	
//	
	 /**
     * 通过
     */
    $(document).on('click', '.J_synonym_allow', function(event) {
        event.preventDefault();
        var self = $(this),
        	id = $(self).attr('id'),
            currentUrl = self.attr('href');
        if (!confirm("确定通过？")) return;
        $.ajax({
            url: common.rootPath+ '/system/handle.htm?m=allowApplySynonymState',
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            data: 'id=' +id,
            success: function(data) {
            	if(data.returncode === 200){
                    $.pjax({
                        url: currentUrl,
                        container: '.J_feedback_data_table'
                    })
            	}else{
            		common.commonTips('通过失败');
            	}
            }
        });
    });
    /**
     * 退回
     */
    $(document).on('click', '.J_synonym_reject', function(event) {
        event.preventDefault();
        var self = $(this),
        	id = $(self).attr('id'),
            currentUrl = self.attr('href');
        if (!confirm("确定退回？")) return;
        $.ajax({
            url: common.rootPath+ '/system/handle.htm?m=rejectApplySynonymState',
            type: 'post',
            timeout: 10000,
            dataType: 'json',
            data: 'id=' +id,
            success: function(data) {
            	if(data.returncode === 200){
                    common.commonTips('退回成功');
                    $.pjax({
                        url: currentUrl,
                        container: '.J_feedback_data_table'
                    })
            	}else{
            		common.commonTips('退回失败');
            	}
            }
        });
    });
    /**
     * 翻页操作
     */
    $(document).pjax('.J_dimension_pagination a', '.J_feedback_data_table');
    $(document).pjax('.J_synonym_pagination a', '.J_feedback_data_table');
});