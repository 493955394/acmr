<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<div style="height: 40%;width: 100%;overflow:auto;">
    <table class="table-content-tbody table-bordered" style="width: 100%">
        <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
        <tr id="zb_data_head" class="ict-table-header">
            <td class="ict-th" style="font-weight: bold;font-family: 'Microsoft YaHei'">地区</td>
            <c:forEach items="${sjs}" var="sj">
                <td style=" font-weight: bold;font-family: 'Microsoft YaHei'">${sj}</td>
            </c:forEach>

        </tr>
        </thead>
        <p>${nodata}</p>
        <tbody id="zb_data_body">

        <c:forEach items="${rows}" var="row">
            <tr>
                <c:forEach items="${row}" var="data">
                    <td>${data}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
