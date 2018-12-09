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
<link rel="stylesheet" type="text/css" href="${ctx}/css/zhzs/zsjh/multiple-select.css" />
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/dropList.js"></script>
<script type="text/javascript" src="${ctx}/js/func/zhzs/zsjhEdit/multipleSelect.js"></script>
<html>
<head>
    <title>${projectTitle}-预览结果</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style>
        table tr th, table tr td { border:1px solid #cecfdf; }
        #preview-table td,th{
            text-align: center;
            vertical-align: middle;
        }
        .red{background-color: #F39801}
    </style>
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="col-xs-12">
    <div>
        <input type="hidden" value="${icode}" id="preview-code">
        <div class="panel panel-default">
            <div class="panel-heading">
                预览结果
            </div>
        </div>
        <div class="toolbar">
            <div class="regselect" style="float: left ;padding-right: 20px;">
                地区：
                <select multiple="multiple" id="ms" size="10">
                    <c:forEach items="${regions}" var="reg">
                        <option value="${reg.regcode}">${reg.regcname}</option>
                    </c:forEach>
                </select>
                <button  type="button" id="select-choose" style="display: none"/>
            </div>
            <div id="mySelect2"></div>
            <input type="hidden" id="timecode" value="">
            <button  type="button" id="timeinput" data-value="" style="display: none"/>
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
                {code:"last3",name:'最近三期'}
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
    $(function() {
        $('#ms').change(function() {
        }).multipleSelect({
            width:'200px',
            selectAllText: "全选", //选择全部的复选框的text值
            allSelected: "全部", //全部选中后显示的
            placeholder: "多选下拉框", //不选择时下拉框显示的内容
        });
    });
    $("#select-choose").click(function () {
        var val = $("#ms").multipleSelect('getSelects', 'text');//是个数组
        $("#preview-table tr").each(function(){
            $(this).removeClass("red")
        })
        for (var i = 0; i <val.length ; i++) {
            $("#preview-table tr").each(function(){
                var text = $(this).children("td:first").text();
                if(text == val[i]){
                    var num = $(this).index();
                    var rowspan = $(this).children("td:first").attr("rowspan");
                    for (var j = num; j <num+rowspan ; j++) {
                        $("#preview-table tbody tr").eq(j).addClass("red")
                    }
                }
            })
        }
    })
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/preview');
</script>