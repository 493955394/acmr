<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%> 
	<table class="table table-striped table-hover J_department_table">
		<thead>
			<tr>
				<th>序号</th>
				<th>代码</th>
				<th>组织名称</th>
				<!-- <th>备注</th> -->
				<th width="350">操作</th>
			</tr>
		</thead>

		<tbody>
			<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
			<tr>
				<td></td>
				<td></td>
				<td>没有查询到数据</td>
				<!-- <td></td> -->
				<td></td>
			</tr>
			</c:if>
			<c:forEach items="${page.data}" var="item" varStatus="status">
			
			<tr <c:if test="${selId != '' && selId != null && selId == item['code']}">class="warning"</c:if>>
			  	<td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
			    <td>${item['code']}</td>
			    <td>${item['cname']}</td>
			    <!-- <td title="${item['memo']}" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${item['memo']}</td> -->
			    <td>			  
			    <c:choose>
			    	<c:when test="${page.data.size()==1}"></c:when>
			    	<c:when test="${status.index==0 }">
			    	<label class="btn-opr btn-disabled">上移</label><a href="${url}" id="${item['code']}" class="btn-opr J_opr_movedown">下移</a>
			    	</c:when>
			    	<c:when test="${status.index==page.data.size()-1}">
			    	<a href="${url}" id="${item['code']}" class="btn-opr J_opr_moveup">上移</a><label class="btn-opr btn-disabled">下移</label>
			    	</c:when>
			    	<c:otherwise>
			    	<a href="${url}" id="${item['code']}" class="btn-opr J_opr_moveup">上移</a><a href="${moveUrl}" id="${item['code']}" class="btn-opr J_opr_movedown">下移</a>
			    	</c:otherwise>
			    </c:choose> 
			    <a href="${ctx}/system/dep.htm?m=toEdit&code=${item['code']}" class="btn-opr J_opr_edit">编辑</a>
			    <a href="${url}" class="btn-opr J_opr_del" id="${item['code']}">删除</a>
			    </td>
			  </tr>  
			</c:forEach>
		</tbody>

	</table>
 
	<div class="toolbar-right">
	<ul class="pagination J_department_pagination">${page}</ul>  	
	</div>
	<input type="hidden" name="depcode" value="${depcode}"/>