<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
<table class="table table-striped table-hover J_regmgr_table">
    <colgroup>
        <col width="3%"/>
        <col width="5%"/>
        <col width="10%"/>
        <col width="27%"/>
        <col width="10%"/>
        <col width="27%"/>
        <col width="18%"/>
    </colgroup>
    <thead>
    <tr>
        <th><input autocomplete="off" type="checkbox"></th>
        <th>编码</th>
        <th>名称</th>
        <th>类型</th>
        <th>周期</th>
        <th>最新数据期</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
<%--    <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>没有查询到数据</td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </c:if>
    <c:forEach items="${page.data}" var="list" varStatus="status">
        <tr>
            <td><input autocomplete="off" type="checkbox" value="${list.code}"/></td>
            <td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
            <td><a href="javascript:;" class="btn-opr J_opr_code">${list.code}</a></td>
            <td><a href="${ctx}/metadata/regmgr.htm?m=getDataById&id=${list.code}" >${list.cname}</a></td>
            <td>${list.procode}</td>
            <td>${list.ccname}</td>
        </tr>
    </c:forEach>--%>
    </tbody>
</table>

<div class="toolbar-right">
    <ul class="pagination J_regmgr_pagination">${page}</ul>
</div>