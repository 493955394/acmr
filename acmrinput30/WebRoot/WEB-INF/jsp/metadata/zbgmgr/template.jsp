<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-分组管理-模板详情</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading">模板详情</div>
			<div class="panel-body">
				<div class="toolbar-left">
					<div class="form-inline">
						<div class="form-group" style="display: none">
							<input type="hidden" id="code" value="${code}"/>
							<input type="hidden" id="type" value="${type}"/>
							<!-- 
							<select id="templateStageType" class="form-control input-sm btn-margin">
								<option value="">请选择模板类型</option>
								<option value="D">日</option>
								<option value="W">周</option>
								<option value="X">旬</option>
								<option value="M">月</option>
								<option value="Q">季</option>
								<option value="Y">年</option>
							</select>
							 -->
						</div>
						&nbsp;&nbsp;共有 <span id="templateCount" style="color:red">${page.totalRecorder}</span> 条数据
					</div>
				</div>
				<div class="toolbar-right">
					<div class="toolbar-group">
						<a href="javascript:;" onclick="javascript:history.go(-1);" class="btn btn-default btn-sm">返回</a>
					</div>
				</div>
			</div>
			<table class="table table-striped table-hover J_zbgmgr_template">
				<thead>
					<tr>
						<th>序号</th>
						<th>模板代码</th>
						<th>模板名称</th>
						<th>模板状态</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
						<tr>
							<td></td>
							<td>没有查询到数据</td>
							<td></td>
							<td></td>
						</tr>
					</c:if>
					<c:forEach items="${page.data}" var="list" varStatus="status">
						<tr>
							<td>${(page.pageNum-1)*(page.pageSize)+(status.index+1)}</td>
							<td>${list.code}</td>
							<td>${list.name}</td>
							<td>${list.state}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="toolbar-right">
	            <ul class="pagination J_template_pagination">
	            	<li class="disabled"><span>首页</span></li>
	            	<li class="disabled"><span>上一页</span></li>
	            	<li class="active"><a href="">1</a></li>
	            	<c:forEach begin="2" end="4" varStatus="i">
		            	<c:if test="${page.pageNum+i.index-1 <= page.totalPage}">
		            		<li><a href="${ctx}/metadata/zbgmgr.htm?m=getTemplateData&code=${code}&pageNum=${page.pageNum+i.index-1}">${i.index}</a></li>
						</c:if>
	            	</c:forEach>
	            	<c:choose>
	            		<c:when test="${page.totalPage==1 or page.totalRecorder==0}">
			            	<li class="disabled"><a href="">下一页</a></li>
			            	<li class="disabled"><a href="">末页</a></li>
	            		</c:when>
	            		<c:otherwise>
			            	<li><a href="${ctx}/metadata/zbgmgr.htm?m=getTemplateData&code=${code}&pageNum=${page.pageNum+1}">下一页</a></li>
			            	<li><a href="${ctx}/metadata/zbgmgr.htm?m=getTemplateData&code=${code}&pageNum=${page.totalPage}">末页</a></li>
	            		</c:otherwise>
	            	</c:choose>
	            </ul>
			</div>
		</div>
	</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/zbgmgr/template');
	</script>
</body>
</html>