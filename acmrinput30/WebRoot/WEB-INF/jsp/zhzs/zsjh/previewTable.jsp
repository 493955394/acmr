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
<div style="max-height:60%;width: 100%;overflow: auto;margin-top: 30px; border: 1px solid #EAECF1;font-size: 14px;text-align: center;">
    <input type="hidden" value="${zbtitle}" id="modname"/>
    <table class="table table-bordered table-hover" style="width: 100%;font-size: 14px;text-align: center;" class="table table-bordered J_jgyl_table" >
        <thead style="background-color: #e4edf6">
        <tr>
        <td style="text-align: center;vertical-align:middle;width:8%;min-width: 80px;" rowspan="2">地区</td>
        <c:forEach items="${date}" var="arr">
            <td style="min-width: 80px;" colspan="${schemename.size()}">${arr}</td>
        </c:forEach>
            <td class="null_td" style="min-width: 80px"></td>
        </tr>
        <tr>
       <c:forEach items="${date}" var="arr">
            <c:forEach items="${schemename}" var="sname">
                <td style="min-width: 80px;">${sname}</td>
            </c:forEach>
       </c:forEach>
            <td class="null_td" style="min-width: 80px"></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${val}" var="vall">
            <tr>
                <c:forEach items="${vall}" var="list">
                    <td style="min-width: 80px;">${list}</td>
                </c:forEach>
                <td class="null_td" style="min-width: 80px"></td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
