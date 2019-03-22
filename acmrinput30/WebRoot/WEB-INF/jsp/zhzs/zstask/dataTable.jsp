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
        white-space: nowrap;
        text-align: center;
        vertical-align: middle!important;
    }
</style>

        <div style="width: 100%;max-height: 100%;overflow: auto">
            <table class="table table-bordered" id="origin-data">
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