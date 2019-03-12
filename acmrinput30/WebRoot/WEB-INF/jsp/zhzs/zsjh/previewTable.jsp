<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/31
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    #preview-table {
        table-layout: fixed;
    }
    .table{
        width: auto;
    }
</style>
<div style="max-height:55%;max-width: 100%;overflow: auto;margin-top: 10px">
    <table class="table table-bordered J_jgyl_table" id="preview-table">
        <thead style="background-color: #e4edf6">
        <tr>
        <td rowspan="2">地区</td>
        <c:forEach items="${date}" var="arr">
            <td colspan="${schemename.size()}">${arr}</td>
        </c:forEach>
        </tr>
        <tr>
       <c:forEach items="${date}" var="arr">
            <c:forEach items="${schemename}" var="sname">
                <td>${sname}</td>
            </c:forEach>
       </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${val}" var="vall">
            <tr>
                <c:forEach items="${vall}" var="list">
                    <td>${list}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
