<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/21
  Time: 12:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div>
        <div class="panel panel-body">
    <table class="table table-bordered J_jsjg_table">
        <thead>
        <tr>
        <td rowspan="2" align="center">指标</td>
        <c:forEach items="${regs}" var="reg">
            <td colspan="2">${reg}</td>
        </c:forEach>
        </tr>
        <tr>
            <c:forEach items="${regs}" var="li">
                <td>本期值</td>
                <td>环比</td>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <tr></tr>
        </tbody>
        <c:forEach items="${rsdatas}" var="rsdata">
            <tr>
                <c:forEach items="${rsdata}" var="rsdata1">
                    <td>${rsdata1}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </table>
        </div>
</div>