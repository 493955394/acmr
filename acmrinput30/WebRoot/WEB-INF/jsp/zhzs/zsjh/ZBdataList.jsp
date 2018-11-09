<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table class="table table-bordered table-hover table_head" style="width: 100%;position: absolute;">
    <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
    <tr id="zb_data_head" class="ict-table-header">
        <td class="ict-th" id="thead_column1" style="font-weight: bold;font-family: 'Microsoft YaHei'">地区</td>
        <c:forEach items="${sjs}" var="sj" varStatus="i">
            <td class="ict-th" id="thead_column${i.count+1}" style=" font-weight: bold;font-family: 'Microsoft YaHei'">${sj}</td>
        </c:forEach>
    </tr>
    </thead>
</table>

<table class="table table-bordered table-hover table_body" style="width: 100%">
    <tbody id="zb_data_body">
    <c:forEach items="${rows}" var="row">
        <tr>
            <c:forEach items="${row}" var="data" varStatus="i">
                <td class="column${i.count}">${data}</td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>

</table>
<p class="table_nodata" style="color: orangered">${nodata}</p>


