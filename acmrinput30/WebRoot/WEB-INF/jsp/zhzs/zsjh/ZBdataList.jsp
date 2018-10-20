<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div>
    <div style="height: 500px">
        <table class="table table-bordered table-hover">
            <thead style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
            <tr id="zb_data_head">
                <td>地区</td>
                <c:forEach items="${sjs}" var="sj">
                    <td>${sj}</td>
                </c:forEach>

            </tr>
            </thead>
            <p>${nodata}</p>
            <tbody id="zb_data_body">

            <c:forEach items="${rows}" var="row">
                <tr>
                    <c:forEach items="${row}" var="data">
                        <td>${data}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>