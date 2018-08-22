// Set configuration
var rPath = document.getElementById("rootPath").value;
seajs.config({
	base: rPath+ "/editTemplate/js/",
	alias: {
		"$": "libs/jquery.js",
		"cTips": "libs/cTips.js",
		"underscore": "libs/underscore.js",
		"handlebars": "libs/handlebars.js",
		"echarts": "libs/echarts.min.js",
		"semantic": "widget/components.js",
		"widget": "libs/widget.js",
		"fileupload": "libs/fileupload.js",
		"alert": "widget/alert.js",
		"ztree": "widget/jquery.ztree.core-3.5.js",
		'uidropdown':'libs/semantic.components.uidropdown.js',
		"rootPath": "common/rootpath.js",
		"publicfn": "common/public.fn.js",
		"area": "view/area.js",
		"figures": "view/figures.js",
		"check": "view/check.js",
		"memo": "view/memo.js",
		"chart": "view/chart.js",
		"area.chart": "view/area.chart.js",
		"figures.chart": "view/figures.chart.js",
		"check.chart": "view/check.chart.js",
		"memo.chart": "view/memo.chart.js",
		"tools": "view/tools.js",
		"excel": "view/excel.js",
		"dimension": "view/dimension.js",
		"synonym": "view/synonym.js"
	}
});
