<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/14
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
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
    <table class="table table-bordered table-hover J_zsrw_table">
        <thead>
        <tr>
            <th>任务时间期</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="list_body my_received">
        <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
            <tr>
                <td colspan="4">没有查询到数据</td>
            </tr>
        </c:if>
        <c:forEach items="${page.data}" var="vo">
            <tr>
                <td>${vo.getAyearmon()}</td>
                <td><fmt:formatDate value="${vo.getCreatetime()}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><fmt:formatDate value="${vo.getUpdatetime()}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <input type="hidden" value="${vo.getCode()}">
                <c:if test="${right!='0'}">
                    <td>
                        <a href="#" class="zs_calculate">计算</a>
                        <a href="#" class="zs_delete">删除</a>
                    </td>
                </c:if>
                <c:if test="${right=='0'}">
                    <td>
                        <a href="#" class="zs_read">查看</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="toolbar-right">
    <ul class="pagination J_task_pagination">${page}</ul>
</div>