<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2019/3/11
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div style="max-height:55%;max-width: 100%;overflow: auto;margin-top: 10px">
    <table class="table table-bordered J_orgZb_table" id="previewzb-table">
        <thead style="background-color: #e4edf6">
        <th></th>
        <th>地区</th>
        <c:forEach items="${sj}" var="list">
            <th>${list}</th>
        </c:forEach>
        </thead>
        <tbody>
        <c:forEach items="${datas}" var="data">
            <td>${data}</td>
        </c:forEach>
        </tbody>
    </table>
</div>