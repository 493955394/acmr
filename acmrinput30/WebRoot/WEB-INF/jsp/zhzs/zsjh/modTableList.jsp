<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div>
    <div class="panel panel-default">
        <div class="panel panel-body">
            <table class="table table-striped table-hover">
                <thead>
                <tr id="mod_list_head">
                    <td>编号</td>
                    <td>名称</td>
                    <td>类型</td>
                    <td>权重</td>
                    <td>详情</td>
                    <td>小数位数</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody id="mod_list_body">
                <c:forEach items="${mods}" var="module">
                    <td>${module.getCode()}</td>
                    <td>${module.getCname()}</td>
                    <td>${module.getIfzs()}</td>
                    <td>${module.getWeight()}</td>
                    <td>${module.getFormula()}</td>
                    <td>${module.getDacimal()}</td>
                    <td>
                        操作
                    </td>
                </c:forEach>

<%--
                <c:forEach items="${rows}" var="row">
                    <tr>
                        <c:forEach items="${row}" var="data">
                            <td>${data}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>--%>

                </tbody>
            </table>
        </div>
    </div>
</div>