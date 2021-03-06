<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
<table class="table table-striped table-hover J_zbgmgr_table">
	<colgroup>
		<col width="2%"/>
		<col width="5%"/>
		<col width="10%"/>
		<col width="17%"/>
		<col width="13%"/>
		<col width="10%"/>
		<col width="18%"/>
		<col width="25%"/>
	</colgroup>
	<thead>
		<tr>
			<th><input autocomplete="off" type="checkbox"></th>
			<th>序号</th>
			<th>指标分组代码</th>
			<th>指标分组名称</th>
			<th>计量单位</th>
			<th>上一级代码</th>
			<th>指标分组全称</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
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
				<td><a href="${ctx}/metadata/zbgmgr.htm?m=getDataById&id=${list.code}" >${list.cname}</a></td>
				<td>${list.unitcode}</td>
				<td>${list.procode}</td>
				<td>${list.ccname}</td>
				<td>
					<a href="${ctx}/metadata/zbgmgr.htm?m=toEdit&id=${list.code}" class="btn-opr J_opr_edit">编辑</a>
					<c:if test="${ismove==0}"><label class="btn-opr btn-disabled J_opr_moveup" id="${list.code}">上移</label><label class="btn-opr btn-disabled J_opr_movedown" id="${list.code}">下移</label></c:if>
					<c:if test="${ismove==1}">
						<c:choose>
							<c:when test="${(page.pageNum-1)*(page.pageSize)+(status.index) == 0}">
		            			<label class="btn-opr btn-disabled J_opr_moveup" id="${list.code}">上移</label>
			            	</c:when>
			            	<c:otherwise>
								<a href="javascript:;" class="btn-opr J_opr_moveup" id="${list.code}">上移</a>
			            	</c:otherwise>
	       				</c:choose>
	      				<c:choose>
		            		<c:when test="${(page.pageNum-1)*(page.pageSize)+(status.index+1) == page.totalRecorder}">
		            			<label class="btn-opr btn-disabled J_opr_movedown" id="${list.code}">下移</label>
		            		</c:when>
		            		<c:otherwise>
								<a href="javascript:;" class="btn-opr J_opr_movedown" id="${list.code}">下移</a>
		            		</c:otherwise>
	          			</c:choose>
					</c:if>
				 	<a href="javascript:;" class="btn-opr J_opr_del" id="${list.code}">删除</a>
				 	<a href="zbgmgr.htm?m=datadetails&code=${list.code}&cname=${list.cname}" class="btn-opr" id="${list.code}">数据详情</a>
				 	<a href="zbgmgr.htm?m=template&code=${list.code}" class="btn-opr" id="${list.code}">模板详情</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="toolbar-right">
	<ul class="pagination J_zbgmgr_pagination">${page}</ul>  	
</div>