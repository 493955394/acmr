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
    #preview-table td{
        white-space: nowrap;
    }
</style>
<input type="hidden" id="result-ifcomplete" value="${flag}">
<div style="max-height:55%;max-width: 100%;overflow: auto;margin-top: 10px">
    <table class="table table-bordered J_jgyl_table" id="preview-table">
        <thead style="background-color: #e4edf6">
        <tr>
            <td rowspan="2" align="center" width="10%">地区</td>
            <td colspan="2" align="center" width="20%">指标</td>
            <c:forEach items="${times}" var="time">
                <td colspan="2">${time}</td>
            </c:forEach>
        </tr>
        <tr>
            <td width="10%">原始指标</td>
            <td width="10%">计算结果</td>
            <c:forEach items="${times}" var="li">
                <td>原始指标</td>
                <td>计算结果</td>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${predata}" var="pre">
            <tr>
                <c:forEach items="${pre}" var="data">
                    <td>${data}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
<div class="ict-footer footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>