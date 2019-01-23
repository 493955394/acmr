<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<div class="range_table_container">
    <table class="table table-bordered table-hover range_table">
        <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
        <tr id="range_data_head">
            <td class="null_td" style="width: 8%;min-width: 60px"></td>
            <c:forEach var="zb" items="${zbrow}">
                <td colspan="${sjrow.size()/zbrow.size()}}" style="min-width: 60px">
                    <input type="checkbox" class="zb_checkbox" id="${zb.get("code")}">
                        ${zb.get("name")}
                </td>
            </c:forEach>
        </tr>
        <tr>
            <td class="null_td"  style="width: 8%;min-width: 60px"></td>
            <c:forEach var="sj" items="${sjrow}" varStatus="i">
                <td class="sj_${i.count}" style="min-width: 60px">${sj}</td>
            </c:forEach>
        </tr>
        </thead>
        <tbody id="range_data_body">
        <c:forEach var="data" items="${datarow}">
            <tr>
                <td class="reg_td" style="width: 8%;min-width: 60px" >
                    <input type="checkbox" class="reg_checkbox" id="${data.get("code")}">
                        ${data.get("name")}
                </td>
                <c:forEach items="${data.get('value')}" var="value" varStatus="i">
                    <td class="value_col" id="value_col_${i.count}" style="min-width: 60px">${value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<%--
<div class="range_table_head_cont" style="position:relative;overflow: hidden;width: 100%;height: 10%;">
    <table class="table table-bordered table-hover range_table_head" style="width: 100%;position:absolute;">
        <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
        <tr id="range_data_head">
            <td class="null_td" style="width: 8%;min-width: 60px"></td>
            <c:forEach var="zb" items="${zbrow}">
                <td colspan="${sjrow.size()/zbrow.size()}}" style="min-width: 60px">
                    <input type="checkbox" class="zb_checkbox" id="${zb.get("code")}">
                        ${zb.get("name")}
                </td>
            </c:forEach>
        </tr>
        <tr>
            <td class="null_td"  style="width: 8%;min-width: 60px"></td>
            <c:forEach var="sj" items="${sjrow}" varStatus="i">
                <td class="sj_${i.count}" style="min-width: 60px">${sj}</td>
            </c:forEach>
        </tr>
        </thead>
    </table>
</div>
<div class="range_table_body_cont" style="overflow: auto;width: 100%;height: 75%;">
    <table class="table table-bordered table-hover range_table_body" style="width: 100%;height: 100%;">
        <tbody id="range_data_body">
        <c:forEach var="data" items="${datarow}">
            <tr>
                <td class="reg_td" style="width: 8%;min-width: 60px" >
                    <input type="checkbox" class="reg_checkbox" id="${data.get("code")}">
                        ${data.get("name")}
                </td>
                <c:forEach items="${data.get('value')}" var="value" varStatus="i">
                    <td class="value_col" id="value_col_${i.count}" style="min-width: 60px">${value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
--%>



