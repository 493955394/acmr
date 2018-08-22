	<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-人员管理</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid" id="mainpanel">
			<div class="col-md-2" style="height: 570px;overflow:auto;background: #f6f9fe">
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
							<form class="form-inline J_search_form" action="${ctx}/system/person.htm?m=find">
								<div class="form-group">
									<input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${user.userid != ''}">${user.userid}</c:if><c:if test="${user.username != ''}">${user.username}</c:if>">
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary btn-sm">查询</button>
								</div>
							</form>
						</div>
						<div class="toolbar-right">
							<div class="toolbar-group">
								<a href="${ctx}/system/person.htm?m=toExcel" class="btn btn-default btn-sm J_person_excel">导出</a>	
								<a href="javascript:;" class="btn btn-default btn-sm" id="addBtn">新增用户</a>	
							</div>
						</div>
					</div>				
					<div class="J_template_data_table">
						<jsp:include page="/WEB-INF/jsp/management/person/tabList.jsp" flush="true"/>
					</div>
				</div>
			</div>
		</div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/management/person/main');
		</script>
	</body>
</html>