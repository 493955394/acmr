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

<div style="height:63%;overflow: auto;margin-top: 20px; border: 1px solid #EAECF1;font-size: 14px;text-align: center;vertical-align:middle;">
    <table  style="font-size: 14px;text-align: center;" class="table table-bordered" id="previewzb-table">
        <thead style="background-color: #e4edf6">
        <th style="text-align: center;vertical-align:middle;min-width: 80px;width: 80px;">地区</th>
        <c:forEach items="${sj}" var="list">
            <th style="text-align: center;vertical-align:middle;min-width: 80px;width: 80px;">${list}</th>
        </c:forEach>
        </thead>
        <tbody>
        <c:forEach items="${datas}" var="data">
        <tr>
            <c:forEach items="${data}" var="var">
                <td style="text-align: center;vertical-align:middle;min-width: 80px;width: 80px;">${var}</td>
            </c:forEach>
        </tr>
        </c:forEach>
        </tbody>
    </table>
</div>