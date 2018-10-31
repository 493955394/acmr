<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-角色管理</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading"></div>
				<div class="panel-body">
					<div class="toolbar">
						<div class="toolbar-left">
							<form class="form-inline J_search_form" action="${ctx}/system/role.htm?m=find">
								<div class="form-group">
									<input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" autocomplete="off">
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary btn-sm">查询</button>
								</div>
							</form>
						</div>
						<div class="toolbar-right">
							<div class="toolbar-group">
								<a href="${ctx}/system/role.htm?m=toExcel" class="btn btn-default btn-sm J_role_excel">导出</a>		
								<a href="${ctx}/system/role.htm?m=toAdd" class="btn btn-default btn-sm">新增角色</a>		
							</div>
						</div>
					</div>
				</div>
					<div class="J_template_data_table">
						<jsp:include page="/WEB-INF/jsp/management/role/tabList.jsp" flush="true"/>
					</div>
		</div>
		</div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/management/role/main');
		</script>
	</body>

</html>