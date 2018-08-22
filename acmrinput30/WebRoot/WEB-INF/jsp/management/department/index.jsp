<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-组织管理</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid" id="mainpanel">
		<div id="mainpanel" class="col-md-2" style="height: 570px;overflow:auto">
			    <div class="panel tree-panel">
			        <div class="panel-heading">组织树</div>
			    </div>
				<ul id="treeDemo" class="ztree ztree-margin"></ul>
			</div>
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="toolbar-left">
							<form class="form-inline J_search_form" action="${ctx}/system/dep.htm?m=find">
								<div class="form-group">
									<input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${findvalue!= ''}">${findvalue}</c:if>">
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary btn-sm">查询</button>
								</div>
							</form>
						</div>
					
					<div class="toolbar-right">
							<div class="toolbar-group">
							<r:right context="" id="2502">
							<a href="${ctx}/system/dep.htm?m=toExcel" class="btn btn-default btn-sm J_department_excel">导出</a>	
							</r:right>
							<r:right context="" id="2501">
							<a href="${ctx}/system/dep.htm?m=toAdd" id="addBtn" class="btn btn-default btn-sm">新增组织</a>
							</r:right>
							</div>
						</div>
					</div>
				
					<div class="J_template_data_table">
						<jsp:include page="/WEB-INF/jsp/management/department/tabList.jsp" flush="true"/>
					</div>				 
				</div>
			</div>
		</div>
		<div class="sidebar"></div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/management/department/main');
		</script>
	</body>
</html>