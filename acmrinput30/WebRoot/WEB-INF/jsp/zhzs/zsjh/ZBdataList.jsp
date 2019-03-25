<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<div class="table_head_cont" style="position:relative; overflow: hidden">
    <table class="table table-bordered table-hover table_head" style="width: 100%;position:absolute;">
        <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
        <tr id="zb_data_head" class="ict-table-header">
            <td class="ict-th" id="thead_column1" style="font-weight: bold;font-family: 'Microsoft YaHei';min-width: 80px">地区(${regsize}个)</td>
            <c:forEach items="${sjs}" var="sj" varStatus="i">
                <td class="ict-th" id="thead_column${i.count+1}" style=" font-weight: bold;font-family: 'Microsoft YaHei';min-width: 80px">${sj}</td>
            </c:forEach>
        </tr>
        </thead>
    </table>

</div>
<div class="table_body_cont" style="overflow: auto;width: 100%;max-height: 90%; border: 1px solid #EAECF1;font-size: 14px;text-align: center;">
        <table class="table table-bordered table-hover table_body" style="width: 100%;height: 100%;">
            <tbody id="zb_data_body">
            <c:forEach items="${rows}" var="row">
                <tr>
                    <c:forEach items="${row}" var="data" varStatus="i">
                        <td style="min-width: 80px;height: 39px;" class="column${i.count}">${data}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
            <tr></tr>
            </tbody>
        </table>
    <p class="table_nodata" style="color: orangered">${nodata}</p>
</div>


