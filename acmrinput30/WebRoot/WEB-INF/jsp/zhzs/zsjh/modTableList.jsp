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
                <c:forEach items="${mods}" var="module" varStatus="stat">
                    <tr>
                        <td>${module.getCode()}</td>
                        <td>${module.getCname()}</td>
                        <td>
                            <c:if test="${module.getIfzs()==1}" >指数</c:if>
                                <c:if test="${module.getIfzs()==0}" >指标</c:if>
                        </td>
                        <td>${module.getWeight()}</td>
                        <td>${module.getFormula()}</td>
                        <td>${module.getDacimal()}</td>
                        <input type="hidden" name="thisprocode" value="${module.getProcode()}">
                        <td>
                            <a href="#" class="mod_edit">编辑</a>
                            <a href="#" class="mod_delete">删除</a>
                            <c:if test="${stat.first}">
                                <label  class="btn-disabled mod_up_noclick" >上移</label>
                            </c:if>
                            <c:if test="${!stat.first}">
                                <a href="#" class="mod_up">上移</a>
                            </c:if>
                            <c:if test="${stat.last}">
                                <label  class="btn-disabled mod_down_noclick" >下移</label>
                            </c:if>
                            <c:if test="${!stat.last}">
                                <a href="#" class="mod_down">下移</a>
                            </c:if>
                        </td>
                    </tr>
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