<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
<table class="table table-striped table-hover J_zbgmgr_table">
	<colgroup>
		<col width="4%"/>
		<col width="15%"/>
		<col width="15%"/>
		<col width="15%"/>
		<col width="15%"/>
		<col width="15%"/>
	</colgroup>
	<thead>
		<tr>
			<th><input class="J_all_checkbox" type="checkbox"></th>
			<th>序号</th>
			<th>货币代码</th>
			<th>货币名称</th>
			<th>时间代码</th>
			<th>时间名称</th>
			<th>美元汇率</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td>没有查询到数据</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</c:if>
		<c:forEach items="${page.data}" var="list" varStatus="status">
			<tr>
				<td><input name="reportcode" type="checkbox" value="${list.id}"/></td>
				<td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
				<td>${list.code}</td>
				<td>${list.unitname}</td>
				<td>${list.sj}</td>
				<td>${list.sjname}</td>
				<td>${list.rate}</td>
				<td>
					<a href="${ctx}/metadata/hvmgr.htm?m=toEdit&id=${list.id}" class="btn-opr J_opr_edit">编辑</a>
				 	<a href="javascript:;" class="btn-opr J_opr_del" id="${list.id}">删除</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="toolbar-right">
	<ul class="pagination J_zbmgr_pagination">${page}</ul>  	
</div>