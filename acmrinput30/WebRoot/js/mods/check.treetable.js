define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		checktreetable;
	checktreetable = {
		oprAll: function(targetCKs, checkState, oprArray) {
			var i, len,
				currentCheckObj,
				tempObj;
			oprArray.length = 0;
			if (checkState) {
				currentCheckObj = $(targetCKs);
				len = currentCheckObj.length;
				for (i = 0; i < len; i++) {
					tempObj = {
						id: currentCheckObj.eq(i).attr('value'),
						posi: i
					};
					oprArray.push(tempObj);
				}
			}
			$(targetCKs).prop('checked', checkState);
		},
		oprOnce: function(targetCheckbox, checkState, oprArray) {
			var len, i = 0,
				$targetCheckbox = $(targetCheckbox),
				tempObj = {
					id: $targetCheckbox.attr('value'),
					posi: $targetCheckbox.parents('tr').index()
				},
				result = false;
			if (checkState) {
				this._sortArray(oprArray, tempObj);
				result = true;
			} else {
				len = oprArray.length;
				for (; i < len; i++) {
					if (tempObj.id == oprArray[i].id) {
						oprArray.splice(i, 1);
						result = true;
						break;
					}
				}
			}
			$targetCheckbox.prop('checked',checkState);
			return result;
		},
		//按顺序大小进行排序插入
		_sortArray: function(oprArray, currentVal) {
			var len = oprArray.length,
				i;
			if (len === 0) {
				oprArray.push(currentVal);
				return;
			}
			i = len - 1;

			function m() {
				//在中间位置插入
				if (oprArray[i]['posi'] < currentVal['posi']) {
					oprArray.splice(i + 1, 0, currentVal);
					return;
				}
				i--;
				//在最前面插入
				if (i >= 0) {
					m();
				} else {
					oprArray.splice(0, 0, currentVal);
				}
			}
			m();
		}
	}
	module.exports = checktreetable;
});