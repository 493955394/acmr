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

<div style="max-height:60%;max-width: 100%;overflow: auto;margin-top: 30px; border: 1px solid #EAECF1;font-size: 14px;text-align: center;">
    <table  style="font-size: 14px;text-align: center;" class="table table-bordered" id="previewzb-table">
        <thead style="background-color: #e4edf6">
        <th>地区</th>
        <c:forEach items="${sj}" var="list">
            <th>${list}</th>
        </c:forEach>
        </thead>
        <tbody>
        <c:forEach items="${datas}" var="data">
        <tr>
            <c:forEach items="${data}" var="var">
                <td><div  style="min-width: 80px;">${var}</div></td>
            </c:forEach>
        </tr>
        </c:forEach>
        </tbody>
    </table>
</div>