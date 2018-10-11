<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div>
    <table class="table table-striped table-hover J_pastviews_data_table">
        <thead>
        <tr>
            <th>指标</th>
            <c:forEach items="${last5}" var="time">
                <th>${time.get(0)}</th>
                <th>${time.get(1)}</th>
                <th>${time.get(2)}</th>
                <th>${time.get(3)}</th>
                <th>${time.get(4)}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody class="list_body my_datas">
        <c:forEach items="${moddata}" var="mo">
            <tr>
                <td>${mo.getCname()}</td>
                <td>${mo.getData()}</td>
                <td>${mo.getData()}</td>
                <td>${mo.getData()}</td>
                <td>${mo.getData()}</td>
                <td>${mo.getData()}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>