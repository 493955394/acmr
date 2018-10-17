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
<style>
    th{
        text-align: center;
        vertical-align: middle!important;
        background-color: #EBECF1;
    }

    td{
        text-align: center;
        vertical-align: middle!important;
    }
</style>
<div>
    <table class="table table-hover J_pastviews_data_table">
        <thead>
        <tr>
            <th>${info.get('col')}</th>
            <c:forEach items="${info.get('head')}" var="head">
                <th>${head}</th>
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