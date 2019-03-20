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
    <div class="rangData_ing" style="display:none;position: fixed; width: 100%;height: 100%;z-index: 2;background-color: gray;filter:alpha(opacity=60);;opacity: 0.4;">
        <div style="background-color: white;position: fixed;top: 50%;left: 50%;border-radius: 10%">
            <p style="color: orangered;font-size: 30px;padding: 10% 0;">加载中……</p>
        </div>
    </div>
    <table  style="width: 100%;font-size: 14px;text-align: center;" class="table table-bordered" id="previewzb-table">
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
                <td style="min-width: 80px;">${var}</td>
            </c:forEach>
        </tr>
        </c:forEach>
        </tbody>
    </table>
</div>