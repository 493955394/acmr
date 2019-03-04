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
    <table class="table table-bordered table-hover">
        <thead  id="scheme_list_head" style="font-size: 15px;background-color: #F5F5F5;font-family: '黑体';">
        <tr>
            <td>  </td>
            <td>方案名称</td>
            <td>备注</td>
            <td>状态</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody id="scheme_list_body">
        <c:forEach var="scheme" items="${schemes}">
            <tr>
                <td>
                    <input autocomplete="off" type="checkbox"/>
                </td>
                <td>${scheme.getCname()}</td>
                <td>${scheme.getRemark()}</td>
                <td>
                    <c:if test="${scheme.getState().equals('0')}">
                        待用
                    </c:if>
                    <c:if test="${scheme.getState().equals('1')}">
                        选用
                    </c:if>
                </td>
                <td>
                    <c:if test="${scheme.getState().equals('0')}">
                        <a href="javascript:;" class="btn-margin J_start" id="${scheme.getCode()}">选用</a>
                        <a href="javascript:;" class="btn-margin J_edit" id="${scheme.getCode()}">编辑</a>
                        <a href="javascript:;" class="btn-margin J_del" id="${scheme.getCode()}">删除</a>
                    </c:if>
                    <c:if test="${scheme.getState().equals('1')}">
                        <a href="javascript:;" class="btn-margin J_stop" id="${scheme.getCode()}">停用</a>
                        <a href="javascript:;" class="btn-margin J_edit" id="${scheme.getCode()}">编辑</a>
                        <span class="btn-disabled btn-margin">删除</span>
                    </c:if>

                    <a href="javascript:;" class="btn-margin J_clone" id="${scheme.getCode()}">克隆</a>
                    <a href="#" class="btn-margin single_weight_set" scheme_code="${scheme.getCode()}" scheme_name="${scheme.getCname()}">公式/权重设置</a>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>