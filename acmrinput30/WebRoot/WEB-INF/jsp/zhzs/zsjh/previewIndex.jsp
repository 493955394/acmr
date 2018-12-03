<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/pastreview.css" />
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/dropList.js"></script>
<html>
<head>
    <title>-预览结果</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style>
        table tr th, table tr td { border:1px solid #cecfdf; }
    </style>
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="col-xs-12">
    <div>
        <div class="panel panel-default">
            <div class="panel-heading">
                预览结果
            </div>
        </div>
        <div class="toolbar">
            <div class="regselect" style="float: left ;padding-right: 20px;">
                地区：
                <select>
                    <option >多选下拉框</option>
                </select>
            </div>
            <div id="mySelect2"></div>
        </div>


            <div class="J_preview_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/previewTable.jsp" flush="true"/>
            </div>
    </div>
</div>
</body>
</html>

<script type="text/javascript">
    //查询的时间
    $(function(){
        var json2 = {
            wdcode:'sj',
            wdname:'时间',
            nodes:[
                {code:"last5",name:'最近五期'}
            ]
        };
        var dt2 = $('#mySelect2');
        //dt2.dropList(json2,{isText:true});	//实例化2(带底部输入框)、默认选中第一个item
        //dt2.dropList(json2,{isText:true,setIndex: 2});	//实例化2(带底部输入框)、选中指定位置item
        dt2.dropList(json2,{isText:true},function(o){		//事件处理
            $("#timecode").val(o.getItem().code)
            $("#timeinput").click();
        });
    });

</script>