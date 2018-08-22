<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table class="table table-striped table-hover">
	<thead>
		<tr>			
			<th>所属维度</th>
			<th>代码</th>
			<th>名称</th>
			<th>同义词</th>
			<th>状态</th>
			<th>备注</th>
			<th width="280px">操作</th>
		</tr>
	</thead>
	<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td>没有查询到数据</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</c:if>
	<c:forEach items="${page.data}" var="item" varStatus="status">
	  <tr>
		    <td>
		   		<c:if test="${item.dimension == 'zb'}">指标</c:if>
		   		<c:if test="${item.dimension == 'fl'}">分组</c:if>
		   		<c:if test="${item.dimension == 'reg'}">地区</c:if>
		   		<c:if test="${item.dimension == 'tt'}">时间类型</c:if>
		   		<c:if test="${item.dimension == 'sj'}">时间</c:if>
		   		<c:if test="${item.dimension == 'ds'}">数据来源</c:if>
		   		<c:if test="${item.dimension == 'unit'}">单位</c:if>
		    </td>
		    <td>${item.code}</td>
		   	<td>${item.name}</td>
		   	<td>${item.synonym}</td>
		   	<td>
		   		<c:if test="${item.state == '1'}">待审批</c:if>
		   		<c:if test="${item.state == '2'}">通过</c:if>
		   		<c:if test="${item.state == '3'}">退回</c:if>
		   	</td>
		   	<td>${item.memo}</td>
			<td>
				<c:if test="${item.state == '1'}">
		    		<a href="${url}" id="${item['id']}" class="btn btn-default btn-sm J_synonym_allow">通过</a>
		    		<a href="${url}" id="${item['id']}" class="btn btn-default btn-sm J_synonym_reject">退回</a>
		    	</c:if>
		    	<c:if test="${item.state == '2'}">
		    		<label class="btn-opr btn-disabled">通过</label>	
		    		<label class="btn-opr btn-disabled">退回</label>	
		    	</c:if>
		    	<c:if test="${item.state == '3'}">
		    		<label class="btn-opr btn-disabled">通过</label>	
		    		<label class="btn-opr btn-disabled">退回</label>	
		    	</c:if>
			</td>
	  </tr>  
	</c:forEach>
</table>
<div class="toolbar-right">
	<ul class="pagination J_synonym_pagination">${page}</ul>  	
</div>
