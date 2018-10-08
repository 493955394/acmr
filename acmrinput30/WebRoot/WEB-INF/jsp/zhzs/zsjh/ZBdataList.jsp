<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div class="panel panel-default">
    <div class="panel panel-body"  style="height: 300px">
        <table class="table table-hover">
            <thead>
            <tr id="zb_data_head">
                <td>地区</td>
                <c:forEach items="${sjs}" var="sj">
                    <td>${sj}</td>
                </c:forEach>
<%--
                <td>2010</td>
                <td>2011</td>
                <td>2012</td>
                <td>2013</td>
--%>
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
<%--            <tr>
                <td>test</td>
                <td>test</td>
                <td>test</td>
                <td>test</td>
                <td>test</td>
            </tr>--%>
            </tbody>
        </table>
    </div>
</div>