var rootPath = 'http://localhost/template/';
var codeId = 'i000000793';
var uncodeId = '0';
var codeIdList = 'i000000793,i000000794,i000000795';
var bookcode = '178dcd41-87ec-4635-ba4b-551b156bb364';
var filterstr = 'f2008:20083';
var rangestr = 'f2008:20083,f2009:20085';
//获取公共的filter
$.mockjax({
    url: rootPath + 'reportPage.htm?m=getGolbalFilter&code=' + codeIdList,
    type: 'get',
    response: function() {
        this.responseText = {
            "returncode": 200,
            "returndata": [{
                "defaultCode": "2016",
                "defaultName": "2016年",
                "filterName": "filter_time",
                "ifmul": false,
                "treelist": [{
                    "checked": false,
                    "childOuter": false,
                    "chkDisabled": false,
                    "drag": false,
                    "id": "2016",
                    "isParent": true,
                    "name": "2016年"
                }],
                "wdtype": "sj"
            }]
        };
    }
});
// 获取单个表的filter
// 有filter
$.mockjax({
    url: rootPath + 'reportPage.htm?m=getFilter&code=' + codeId,
    type: 'get',
    response: function() {
        this.responseText = {
            "returncode": 200,
            "returndata": [{
                "ifmul": false,
                "defaultCode": "201606",
                "defaultName": "2016年6月",
                "filterName": "fName2016841559",
                "treelist": [{
                    "checked": false,
                    "childOuter": false,
                    "chkDisabled": false,
                    "drag": false,
                    "id": "2016",
                    "isParent": true,
                    "name": "2016年"
                }],
                "wdtype": "sj"
            }]
        }
    }
});
//没有filter
$.mockjax({
    url: rootPath + 'reportPage.htm?m=getFilter&code=' + uncodeId,
    type: 'get',
    response: function() {
        this.responseText = {
            "returncode": 200,
            "returndata": []
        }
    }
});
// 获取单个表的chart数据
$.mockjax({
    url: rootPath + 'report.htm?m=getDrawData',
    data: {
        'code': codeId,
        'filterstr': filterstr,
        response: function() {
            this.reponseText = {
                "returncode": 200,
                "returndata": {
                    "code": "i000000731",
                    "createtime": 1470301999000,
                    "createuser": "admin",
                    "exps": "",
                    "property": "{\"axisType\":\"0\",\"tuType\":2,\"title\":{\"show\":false,\"textStyle\":{\"fontWeight\":\"bold\",\"fontFamuly\":\"Calibri\"},\"x\":\"center\"},\"tooltip\":{\"trigger\":\"axis\"},\"grid\":{\"right\":\"30%\",\"containLabel\":true},\"backgroundColor\":\"#fff\",\"legend\":{\"pos\":\"4\",\"y\":\"center\",\"left\":\"75%\",\"data\":[\"客运量万人\",\"旅客周转量万人公里\",\"货邮运输量万吨\",\"货邮周转量万吨公里\",\"运输总周转量万吨公里\"]},\"xAxis\":{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},\"axisLabel\":{\"show\":true},\"data\":[\"本年本月止累计本年本月止累计\",\"本月本月\",\"比上年同期增长(%)累计\",\"比上年同期增长(%)本月\"]},\"yAxis\":{\"min\":\"auto\",\"axisLabel\":{\"show\":true},\"type\":\"value\"},\"series\":[{\"name\":\"客运量万人\",\"type\":\"line\",\"stack\":\"总量\",\"areaStyle\":{\"normal\":{}},\"data\":[\"546.0\",\"45.0\",\"0.0\",\"45.0\"]},{\"name\":\"旅客周转量万人公里\",\"type\":\"line\",\"stack\":\"总量\",\"areaStyle\":{\"normal\":{}},\"data\":[\"465.0\",\"456.0\",\"0.0\",\"456.0\"]},{\"name\":\"货邮运输量万吨\",\"type\":\"line\",\"stack\":\"总量\",\"areaStyle\":{\"normal\":{}},\"data\":[\"1211.0\",\"97.0\",\"0.0\",\"97.0\"]},{\"name\":\"货邮周转量万吨公里\",\"type\":\"line\",\"stack\":\"总量\",\"areaStyle\":{\"normal\":{}},\"data\":[\"45.0\",\"9.0\",\"0.0\",\"9.0\"]},{\"name\":\"运输总周转量万吨公里\",\"type\":\"line\",\"stack\":\"总量\",\"areaStyle\":{\"normal\":{}},\"data\":[\"9.0\",\"785.0\",\"0.0\",\"785.0\"]}]}",
                    "reportchartsort": "31",
                    "sort": "2"
                }
            }
        }
    }
});
// 获取单个表的表格数据
// 有filter
$.mockjax({
    url: rootPath + 'bookshow.htm?m=catalogPage',
    type: 'post',
    data: {
        'code': codeId,
        'filterstr': filterstr
    },
    response: function() {
        this.responseText = {
            returncode: 200,
            returndata: '<div>有filter的html结构</div>'
        }
    }
});
//无filter
$.mockjax({
    url: rootPath + 'bookshow.htm?m=catalogPage',
    type: 'post',
    data: {
        'code': codeId
    },
    response: function() {
        this.responseText = {
            returncode: 200,
            returndata: '<div>无filter的htm</div>'
        }
    }
});
// 加载页面时，返回的对象
$.mockjax({
    url: rootPath + 'reportPage.htm?m=findById&code=' + codeId,
    type: 'get',
    response: function() {
        this.responseText = {
            pageCollection: [{
                "id": 6,
                "content": "这是默认内容，请输入新标题",
                "bg": "#fff"
            }, {
                "id": 7,
                "content": "这是默认内容，请输入新文本",
                "bg": "#fff"
            }, {
                "id": 8,
                "url": ""
            }, {
                "id": 9,
                "bg": "#fff",
                "scale": ["1", "1", "1", "1"],
                "enlarge": 3,
                "row": "1",
                "col": "4"
            }, {
                "id": 10,
                'limith': '',
                'transId': codeId,
                'html': '',
                'type': 'report'
            }],
            pageHTML: '<div class="item ui-draggable" type="title" style="display:block"><div class="title"><div class="render 6"><h3 style="background:#fff">这是默认内容，请输入新标题</h3></div><div class="opr"><a class="edit item glyphicon glyphicon-edit" data-toggle="modal" data-target="#modal" data-id="6"></a> <a class="del item glyphicon glyphicon-trash" data-id="6"></a></div></div></div><div class="item ui-draggable" type="paragraph" style="display:block"><div class="title"><div class="render 7"><h3 style="background:#fff">这是默认内容，请输入新文本</h3></div><div class="opr"><a class="edit item glyphicon glyphicon-edit" data-toggle="modal" data-target="#modal" data-id="7"></a> <a class="del item glyphicon glyphicon-trash" data-id="7"></a></div></div></div><div class="item ui-draggable" type="image" style="display:block"><div class="title"><div class="render 8"><span class="btn btn-success fileinput-button"><i class="glyphicon glyphicon-plus"></i> <span>选择图片</span> <input class="fileupload" data-id="8" type="file" name="files[]" multiple></span></div><div class="opr"><a class="edit item glyphicon glyphicon-edit" data-toggle="modal" data-target="#modal" data-id="8"></a> <a class="del item glyphicon glyphicon-trash" data-id="8"></a></div></div></div><div class="item ui-draggable" type="layout" style="display:block"><div class="title"><div class="render 9"><div class="row"><div class="col-md-3 demo ui-sortable" placeholder="组件拖放处"></div><div class="col-md-3 demo ui-sortable" placeholder="组件拖放处"></div><div class="col-md-3 demo ui-sortable" placeholder="组件拖放处"></div><div class="col-md-3 demo ui-sortable" placeholder="组件拖放处"></div></div></div><div class="opr"><a class="edit item glyphicon glyphicon-edit" data-toggle="modal" data-target="#modal" data-id="9"></a> <a class="del item glyphicon glyphicon-trash" data-id="9"></a></div></div></div><div class="item ui-draggable" type="report" style="display:block"><div class="title"><div class="render 10"><div>this is report</div></div><div class="opr"><a class="edit item glyphicon glyphicon-edit" data-toggle="modal" data-target="#modal" data-id="10"></a> <a class="del item glyphicon glyphicon-trash" data-id="10"></a><button data-id="10" class="refresh">refresh</button></div></div></div>',
            maxIdent: 10
        };
    }
});
// 获取报表list集合
$.mockjax({
    url: rootPath + 'report.htm?m=findTree&bookcode=' + bookcode + '&id=' + codeId,
    type: 'get',
    response: function() {
        this.responseText = [{
            "checked": false,
            "childOuter": false,
            "chkDisabled": false,
            "drag": false,
            "id": "i000000793",
            "isParent": true,
            "name": "21212",
            "pId": "i000000784"
        }]
    }
});