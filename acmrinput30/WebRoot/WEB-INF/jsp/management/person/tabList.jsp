<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%> 
<table class="table table-striped table-hover J_person_table">
	<thead>
		<tr>
			<th>序号</th>			
			<th>代码</th>
			<th>用户姓名</th>
			<!-- <th>电子邮件</th> -->
			<!-- <th>联系电话</th> -->
			<th width="420">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
			<tr>
				<td></td>
				<td></td>
				<!-- <td></td> -->
				<td>没有查询到数据</td>
				<!-- <td></td> -->
				<td></td>
			</tr>
		</c:if>
	<c:forEach items="${page.data}" var="item" varStatus="status">
	  <tr <c:if test="${selId != '' && selId != null && selId == item['userid']}">class="warning"</c:if>>
	  	<td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
	    <td>${item['userid']}</td>
	    <td>${item['username']}</td>
	    <!-- <td>${item['email']}</td> -->
	    <!-- <td>${item['tel']}</td> -->
	    <td>
			<a href="${ctx}/system/person.htm?m=toEdit&userid=${item['userid']}" class="btn-opr J_opr_edit">编辑</a>
			<a href="${url}" class="btn-opr J_opr_del" id="${item['userid']}">删除</a>
			<a href="javascript:;" class="btn-opr J_opr_pwd" id="${item['userid']}">重置密码</a>
			<a href="javascript:;" class="btn-opr J_add_role" id="${item['userid']}">设置角色</a>
		</td>
	  </tr>  
	</c:forEach>
	</tbody>
</table>
<div class="toolbar-right">
	<ul class="pagination J_person_pagination">${page}</ul>  	
</div>
<input type="hidden" name="depcode" value="${depcode}"/>
