Handlebars.registerHelper("compare", function(v1, v2, options) {
    if (v1 === v2) {
        //满足添加继续执行
        return options.fn(this);
    } else {
        //不满足条件执行{{else}}部分
        return options.inverse(this);
    }
});
Handlebars.registerHelper("rowlist", function(num, options) {
    var out = '',
        currentNum, i;
    currentNum = parseInt(num);
    i = 0;
    for (; i < num; i++) {
        out += options.fn(this);
    }
    return out;
});
Handlebars.registerHelper('multiplicative', function(v1, v2, options) {
    return v1 * v2;
});
Handlebars.registerHelper('reporttype', function(report,strand, options) {
    if (report === strand) {
        return options.fn(this);
    }else{
        return options.inverse(this);
    }
});