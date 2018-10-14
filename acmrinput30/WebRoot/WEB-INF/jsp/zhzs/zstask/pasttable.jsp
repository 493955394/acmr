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
                <th>${time}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody class="list_body my_datas">
        <c:forEach items="${showdata}" var="mo">
            <tr>
                <c:forEach items="${mo}" var="td">
                    <td>${td}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>