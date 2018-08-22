<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table class="table table-striped table-hover">
	<thead>
		<tr>			
			<th>标题</th>
			<th>发件人</th>
			<th>状态</th>
			<th>时间</th>
			<th width="280">操作</th>
		</tr>
	</thead>
	<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
		<tr>
			<td></td>
			<td></td>
			<td>没有查询到数据</td>
			<td></td>
			<td></td>
		</tr>
	</c:if>
	<c:forEach items="${page.data}" var="item" varStatus="status">
	  <tr>
		    <td>${item.name}</td>
		    <td>${item.userName}</td>
		   	<td>
		    	<c:if test="${item.state == '1'}">待确认</c:if>
		    	<c:if test="${item.state == '2'}">已确认</c:if>
		    </td>
		    
			<td>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${item['createTime']}" type="both"/>
			</td>
		    <td>
		    	<a href="${ctx}/system/handle.htm?m=findDimensionInfo&id=${item['id']}" target="_blank"  class="btn btn-default btn-sm J_dimension_info">查看</a>
		   	</td>
	  </tr>  
	</c:forEach>
</table>
<div class="toolbar-right">
	<ul class="pagination J_dimension_pagination">${page}</ul>  	
</div>
