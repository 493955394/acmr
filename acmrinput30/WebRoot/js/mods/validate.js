define(function(require, exports, module) {
	var $ = require('jquery');

	function vaildate(elems, allrules) {
		var i = 0,
			rebool, remsg, defrules, elem, rules, defmsgs;
		if (elems == undefined) {
			return;
		}
		if (allrules == undefined) {
			return;
		}
		elem = elems;
		rules = allrules;
		defrules = {
			required: function(value, param) {
				return $.trim(value).length > 0;
			},
			email: function(argv) {
				return /^([a-zA-Z0-9]+[_|\_|\.]?)*([a-zA-Z0-9]{4,})+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(argv);
			},
			mobile: function(argv) {
				return /^(13|15|18|14)\d{9}$/gi.test(argv);
			},
			tele: function(argv) {
				return /\(?0\d{2,3\)?[- ]?\d{7,8}$|0\d{2,3}[- ]?\d{7,8}$/.test(argv);
			},
			maxlength: function(value, param) {
				return value.length < param;
			},
			minlength: function(value, param) {
				return value.length > param;
			},
			equallength: function(value, param) {
				return value.length === param;
			},
			date: function(argv) {
				return !/Invalid|NaN/.test(new Date(argv));
			},
			digits: function(argv) {
				//return /^\d+(\.\d+)?$/.test(argv);
				return !(isNaN(argv));
			},
			money: function(argv) {
				return /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(argv);
			},
			en: function(argv) {},
			ch: function(argv) {
				return !(/([\u4e00-\u9fa5])/.test(argv));
			},
			oppCh:function(argv){
				return /([\u4e00-\u9fa5])/.test(argv);
			},
			postcode: function(argv) {
				return /^[1-9][0-9]{5}$/.test(argv);
			},
			equal: function(argv, param) {
				if (param instanceof Array) {
					var j = 0;
					for (; j < param.length; j++) {
						if (param[j] == argv) {
							return false;
						}
					}
					return true;
				} else {
					return argv === $.trim(param);
				}
			},
			greater: function(argv, param) {
				return argv >= param;
			},
			lesser: function(argv, param) {
				return argv <= param;
			}
		};
		defmsgs = {
			required: "\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a",
			email: "\u90ae\u7bb1\u683c\u5f0f\u4e0d\u6b63\u786e",
			mobile: "\u624b\u673a\u683c\u5f0f\u4e0d\u6b63\u786e",
			tele: "\u56fa\u5b9a\u7535\u8bdd\u683c\u5f0f\u4e0d\u6b63\u786e",
			maxlength: "\u8d85\u8fc7\u6700\u5927\u957f\u5ea6",
			minlength: "\u81f3\u5c11\u4e3a6\u4f4d",
			equallength: "\u4f4d\u6570\u4e0d\u76f8\u7b49",
			date: "\u65e5\u671f\u683c\u5f0f\u4e0d\u6b63\u786e",
			digits: "\u4e0d\u662f\u6570\u5b57\u7c7b\u578b",
			money: "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u91d1\u989d",
			en: "",
			ch: "",
			postcode: "\u90ae\u7f16\u683c\u5f0f\u4e0d\u6b63\u786e",
			equal: "\u4e24\u6b21\u8f93\u5165\u4e0d\u5339\u914d",
			greater: "\u8f93\u5165\u503c\u5c0f\u4e8e\u6bd4\u8f83\u503c"
		};
		for (; i < rules.length; i++) {
			rebool = defrules[rules[i].name](elem, rules[i].param) ? true : false;
			remsg = rules[i].msg != undefined ? rules[i].msg : defmsgs[rules[i].name];
			if (!rebool) {
				return {
					"rval": rebool,
					"rmsg": remsg
				};
				break;
			}
		}
		return {
			"rval": true,
			"rmsg": "success"
		};
	}
	module.exports = vaildate;
})
