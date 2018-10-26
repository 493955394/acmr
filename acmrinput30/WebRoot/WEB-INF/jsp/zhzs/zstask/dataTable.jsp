<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/zhzs/zstask/dataTable.css" />

<div>
    <div>
        <div>
            <table class="table table-bordered table-hover" id="origin-data">
                <thead>
                <tr id="mod_list_head">
                    <td>指标</td>
                    <c:forEach items="${regs}" var="reg">
                        <td>${reg}</td>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${data}" var="row">
                    <tr>
                        <c:forEach items="${row}" var="td">
                            <td>${td}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>