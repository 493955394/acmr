<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table class="table table-striped table-hover">
	<thead>
		<tr>			
			<th>标题</th>
			<th>状态</th>
			<th>时间</th>
			<th>审批意见</th>
			<th width="280">操作</th>
		</tr>
	</thead>
	<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
		<tr>
			<td></td>
			<td></td>
			<td>没有查询到数据</td>
			<td></td>
		</tr>
	</c:if>
	<c:forEach items="${page.data}" var="item" varStatus="status">
	  <tr>
		    <td>${item.name}</td>
		   	<td>
		    	<c:if test="${item.state == '1'}">待审批</c:if>
		    	<c:if test="${item.state == '2'}">已审批</c:if>
		    </td>
			<td>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${item['createTime']}" type="both"/>
			</td>
			<td title="${item.replyMemo}">
				<c:choose> 
			     <c:when test="${fn:length(item.replyMemo) > 10}"> 
			      <c:out value="${fn:substring(item.replyMemo, 0, 10)}..." /> 
			     </c:when> 
			     <c:otherwise> 
			      <c:out value="${item.replyMemo}" /> 
			     </c:otherwise>
			    </c:choose>
			</td>
		    <td>
		    	<a href="${ctx}/personal/feedback.htm?m=findDimensionInfo&id=${item['id']}"  class="btn btn-default btn-sm J_dimension_info">查看</a>
		    	<c:if test="${item.state == '1'}">
		    		<a href="${url}" id="${item['id']}" class="btn btn-default btn-sm J_dimension_cancel">撤销</a>
		    	</c:if>
		    	<c:if test="${item.state == '2'}">
		    		<label class="btn-opr btn-disabled">撤销</label>	
		    	</c:if>
		   	</td>
	  </tr>  
	</c:forEach>
</table>
<div class="toolbar-right">
	<ul class="pagination J_dimension_pagination">${page}</ul>  	
</div>

