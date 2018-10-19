<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    thead{
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
    <div class="panel panel-default">
        <div class="panel panel-body">
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