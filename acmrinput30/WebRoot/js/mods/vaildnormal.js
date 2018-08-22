define(function(require, exports, module) {
	'use strict';
	var vaild = require('vaild');

	function VaildNormal() {
		
	}
	VaildNormal.prototype = {
		construtor: VaildNormal,
		init:function(currentObj, checkArray){
			if (typeof currentObj != 'object' || currentObj == null || typeof checkArray == 'Array' || checkArray.length === 0) {
				return;
			}
			this.checkNormal(currentObj, checkArray);
		},
		checkNormal: function(currentObj, checkArray) {
			var self = this,
				ucodeObj = currentObj,
				ucodeTipObj = ucodeObj.parents('.form-group:eq(0)').children('label.tips-error'),
				ucodeTipObjLen = ucodeTipObj.length,
				checkResult;
			checkResult = vaild(ucodeObj.val(), checkArray);
			if (!checkResult.rval) {
				self.viewTip(ucodeObj, checkResult.rmsg, ucodeTipObjLen,ucodeTipObj);
			}
			if (checkResult.rval && ucodeTipObjLen > 0) {
				self.hideTip(ucodeTipObj);
			}
			return checkResult.rval;
		},
		viewTipAjax:function(currentObj,checkResult,rmsg){
			var self = this,
				ucodeObj = currentObj,
				ucodeTipObj = ucodeObj.parent().siblings('label.tips-error'),
				ucodeTipObjLen = ucodeTipObj.length;
			if (!checkResult) {
				self.viewTip(ucodeObj, rmsg, ucodeTipObjLen,ucodeTipObj);
			}
			if (checkResult && ucodeTipObjLen > 0) {
				self.hideTip(ucodeTipObj);
			}
		},
		viewTip: function(currentObj, msg, ucodeTipObjLen,ucodeTipObj) {
			var self = this;
			if (ucodeTipObjLen === 0) {
				//生成提示信息
				currentObj.parents('.form-group:eq(0)').append(self.buildTipHTML(msg));
			}
			if (ucodeTipObjLen > 0) {
				ucodeTipObj.text(msg);
			}
		},
		hideTip: function(ucodeTipObj) {
			ucodeTipObj.remove();
		},
		buildTipHTML: function(msg) {
			var tempHTML = '';
			tempHTML = '<label class="col-sm-2 control-label tips-error">' + msg + '</label>';
			return tempHTML;
		}
	}
	module.exports = VaildNormal;
});