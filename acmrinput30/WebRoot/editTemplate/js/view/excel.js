define(function(require, exports, module) {
	//初始化excel
	var ss = new SpreadSheet('spreadSheet');
	module.exports = ss;
});
