define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		RP = require('RenderPage'),
		translate = require('translate'),
		RenderPage = new RP(),
		controlCacheArray;
	controlCacheArray = function() {

	}
	controlCacheArray.prototype = {
		contrustor: controlCacheArray,
		pushData: function() {

		},
		delData: function() {

		},
		buildRandomID: function() {
			return 'treetable' + parseInt(Math.random(0, 1000) * 1000) + parseInt(new Date().getTime() / 1000);
		},
		renderUI: function(randomTableArray, dragTargetObj, currentIndex, treeId, targetObj, setSymbol, partIndex, targetPartIndex) {
			//生成html数据，已经在random Array 中存在该表
			var i = 0,
				len = randomTableArray.length,
				list = dragTargetObj[partIndex]['part_regin'][currentIndex].list,
				randomID = '',
				randomTable;
			for (; i < len; i++) {
				if (randomTableArray[i].posi === targetObj.pId && randomTableArray[i].dimension === treeId && randomTableArray[i].targetPartIndex === targetPartIndex) {
					randomID = randomTableArray[i].id;
					RenderPage.buildTbody(RenderPage.buildTreetableHTML(list), randomID);
				}
			}
			//返回数据加入cache random Array,生成html数据 ：不存在当前随机表
			if (randomID == '') {
				randomID = this.buildRandomID();
				randomTable = {
					posi: targetObj.pId,
					dimension: treeId,
					id: randomID,
					targetPartIndex: targetPartIndex
				};
				randomTableArray.push(randomTable);
				RenderPage.buildPanel(randomID, targetObj.pId, RenderPage.buildTable(randomID, {
					tbody: RenderPage.buildTreetableHTML(list)
				}), translate(treeId), dragTargetObj, currentIndex, randomTableArray, setSymbol, partIndex, targetPartIndex);

			}
			return randomID;
		},
		renderNormalUI: function(currentObj, targetObj, dragTargetObj) {
			//不存在表的情况
			RenderPage.renderTable('#' + targetObj.pId + '>table>tbody', RenderPage.buildOneTr(currentObj), dragTargetObj, targetObj);
		}
	}
	module.exports = controlCacheArray;
});