define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		contorlCacheList;
	contorlCacheList = {
		orientatePosi: function(posiArray, targetObj, cacheListObj) {
			var i = 0;
			for (; i < posiArray.length; i++) {
				if (posiArray[i] == targetObj.pId) {
					return cacheListObj[i];
				}
			}
		},
		orientateDim: function(dragTargetObj, treeId, partIndex) {
			var i = 0,
				len, tempObj, cloneTargetObj;
			cloneTargetObj = partIndex !== undefined ? dragTargetObj[partIndex]['part_regin'] : dragTargetObj;
			len = cloneTargetObj.length;
			for (; i < len; i++) {
				if (cloneTargetObj[i] !== null && cloneTargetObj[i]['type'] === treeId) {
					return i;
				}
			}
			return -1;
		},
		orientatePart: function(dragTargetObj, targetPartIndex) {
			var i = 0,
				len = dragTargetObj.length,
				tempObj;
			for (; i < len; i++) {
				if (dragTargetObj[i]['part_index'] != null && dragTargetObj[i]['part_index'] == targetPartIndex) {
					return i;
				}
			}
			return -1;
		},
		pushPart: function(currentObj, oprArray) {
			oprArray.push(currentObj);
			return oprArray.length;
		},
		pushDim: function(currentObj, oprArray, partIndex) {
			oprArray[partIndex]['part_regin'].push(currentObj);
			return oprArray[partIndex]['part_regin'].length;
		},
		pushCacheList: function(returndata, dragTargetObj, currentIndex, partIndex) {
			var i = 0,
				currentList = dragTargetObj[partIndex]['part_regin'][currentIndex].list,
				len = returndata.length;
			for (; i < len; i++) {
				currentList.push(returndata[i]);
			}
		},
		updateCacheList: function() {

		},
		//清理已经没有数据内容的数组
		clearCacheList: function(dragTargetObj) {
			var len = dragTargetObj.length,
				i, j, items, itemsLen;
			for (i = len - 1; i >= 0; i--) {
				var isExist = false;
				items = dragTargetObj[i]['part_regin'] || [];
				itemsLen = items.length || 0;
				for (j = 0; j < itemsLen; j++) {
					if (items[j] !== null) {
						isExist = true;
						break;
					}
				}
				if (!isExist) {
					dragTargetObj[i] = null;
					--dragTargetObj.length;
				}
			};
		},
		isExistBackData: function(currentData, dragTargetObj, currentIndex) {
			var len = 0,
				tempArray = [],
				currentList = dragTargetObj[currentIndex].list,
				currentListLen = currentList.length,
				i = 0,
				j,
				isExist = false,
				x;
			for (x in currentList[0]) {
				tempArray.push(x);
				len++;
			}
			for (; i < currentListLen; i++) {
				isExist = true;
				for (j = 0; j < len; j++) {
					if (currentData[tempArray[j]] != currentList[i][tempArray[j]]) {
						isExist = false;
					}
				}
				if (isExist) {
					return isExist;
				}
			}
			return isExist;
		},
		isExistReplaceData: function(currentData, dragTargetObj, currentIndex, partIndex) {
			var i = 0,
				currentObj = dragTargetObj[partIndex]['part_regin'][currentIndex],
				currentList = currentObj.list,
				len = currentList.length,
				isExist = false;
			for (i = len - 1; i >= 0; i--) {
				if (currentData.treeParentId == currentObj.type && currentData.treeId == currentList[i].id) {
					isExist = true;
					return isExist;
				}
			};
			return isExist;
		}
	}
	module.exports = contorlCacheList;
});