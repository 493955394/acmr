<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table class="table table-striped table-hover J_zbgmgr_table">
	<colgroup>
		<col width="25%" />
		<col width="25%" />
		<col width="25%" />
		<col width="25%" />
	</colgroup>
	<thead>
		<tr>
			<th>模块ID</th>
			<th>模块名称</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="list" varStatus="status">
			<tr>
				<td>${list.code}</td>
				<td>${list.cname}</td>
				<td>${list.exp}</td>
				<td>
					<c:choose>
						<c:when test="${list.first==0}">
							<label class="btn-opr btn-disabled J_opr_moveup"  id="${list.code}">上移</label>
						</c:when>
						<c:otherwise>
							<a href="javascript:;" class="btn-opr J_opr_moveup" id="${list.code}">上移</a>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${list.last==0}">
							<label class="btn-opr btn-disabled J_opr_movedown"  id="${list.code}">下移</label>
						</c:when>
						<c:otherwise>
							<a href="javascript:;" class="btn-opr J_opr_movedown" id="${list.code}">下移</a>
						</c:otherwise>
					</c:choose>
					<a href="javascript:;" class="btn-opr J_opr_edit" id="${list.code}" procode="${list.procode}">编辑</a> 
					<a href="javascript:;" class="btn-opr J_opr_del" id="${list.code}">删除</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="toolbar-right">
	<!-- 
	 <ul class="pagination J_zbmgr_pagination">${page}</ul>
	 -->
</div>