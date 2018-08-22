<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
	<table class="table table-striped table-hover J_role_table">
		<thead>
			<tr>
				<th>序号</th>
				<th>代码</th>
				<th>角色名称</th>
				<th>备注</th>
				<th>状态</th>
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
			</tr>
		</c:if>
		<c:forEach items="${page.data}" var="item" varStatus="status">
		  <tr <c:if test="${selId != '' && selId != null && selId == item['code']}"> class="warning"</c:if>>
		  	<td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
		    <td>${item['code']}</td>
		    <td>${item['cname']}</td>
		    <td title="${item['memo']}" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
		    
		    	<div style="max-width:100px">${item['memo']}</div>
		    </td>
			<td>${item['state']}</td>				    
		    
		    <td>
		    <a href="${ctx}/system/role.htm?m=toEdit&code=${item['code']}" class="btn-opr J_opr_edit">编辑</a>
		    <a href="${url}" class="btn-opr J_opr_del" id="${item['code']}">
	    		<c:choose>
	    			<c:when test="${item['state']=='启用'}">停用</c:when>
	    			<c:otherwise>启用</c:otherwise>
	    		</c:choose></a>
	    	 
		    	<c:choose>
	    			<c:when test="${item['state']=='启用'}">
	    			 <a href="#" class="btn-opr J_add_user" id="${item['code']}">添加用户</a> 
	    				<a href="#" class="btn-opr J_write_load_task" id="${item['code']}">设置权限</a>
	    			</c:when>
	    			<c:otherwise>
	   				<label class="btn-opr btn-disabled">添加用户</label> 
	   					<label class="btn-opr btn-disabled">设置权限</label>
	    			</c:otherwise>
	    		</c:choose>
			</td>
		  </tr>  
		</c:forEach>
		</tbody>
	</table>
	<div class="toolbar-right">
		<ul class="pagination J_role_pagination">${page}</ul>  
	</div>
