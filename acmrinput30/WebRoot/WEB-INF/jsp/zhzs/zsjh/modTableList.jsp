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
    <div>
        <div style="overflow-x: auto;
    border: 1px solid #EAECF1;max-height:100%;font-size: 14px;text-align: center;">
            <table class="table table-bordered table-hover">
                <thead  style="font-size: 15px;background-color: #F5F5F5;font-weight: bold;font-family: 'Microsoft YaHei';">
                <tr id="mod_list_head">
                    <td>名称</td>
                    <td>类型</td>
                    <td>小数位数</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody id="mod_list_body">
                <c:if test="${fn:length(mods)==0}">
                    <tr>
                        <td colspan="4">没有查询到数据</td>
                    </tr>
                </c:if>
                <c:forEach items="${mods}" var="module" varStatus="stat">
                    <tr>
                        <td style="display: none">${module.getCode()}</td>
                        <td>${module.getCname()}</td>
                        <td>
                            <c:if test="${module.getIfzs()==1}" >指数</c:if>
                                <c:if test="${module.getIfzs()==0}" >指标</c:if>
                        </td>
                        <td>${module.getDacimal()}</td>
                        <input type="hidden" name="thisprocode" value="${module.getProcode()}">
                        <td>
                            <a href="#" class="mod_edit btn-margin">编辑</a>
                            <a href="#" class="mod_delete btn-margin">删除</a>
                            <c:if test="${stat.first}">
                                <label  class="btn-disabled btn-margin mod_up_noclick" >上移</label>
                            </c:if>
                            <c:if test="${!stat.first}">
                                <a href="#" class="mod_up btn-margin">上移</a>
                            </c:if>
                            <c:if test="${stat.last}">
                                <label  class="btn-disabled btn-margin mod_down_noclick" >下移</label>
                            </c:if>
                            <c:if test="${!stat.last}">
                                <a href="#" class="mod_down btn-margin">下移</a>
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