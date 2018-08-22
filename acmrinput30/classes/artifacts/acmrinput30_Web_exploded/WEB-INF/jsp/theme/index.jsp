<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-栏目管理</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<input type="hidden" id="initTreePara" value="${initTreePara}" />
		<input type="hidden" id="procode" value="${procode}" />
		<input type="hidden" id="searchPara" value="${searchPara}" />
		<div class="container-fluid" id="mainpanel">
			<div class="col-md-2 left-panel">
			    <div class="panel tree-panel">
			        <div class="panel-heading">栏目管理</div>
			    </div>
				<ul id="treeDemo" class="ztree ztree-margin"></ul>
			</div>
			<div class="col-md-10 right-panel">
				<div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="toolbar-right">
							<div class="toolbar-group" style="position: relative;">
								<a href="javascript:;" class="btn btn-default btn-sm J_temp_task">新建分类</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_Add">新建查询模板</a>
							</div>
						</div>
					</div>
					<div class="J_zbmgr_data_table">
						<jsp:include page="/WEB-INF/jsp/theme/tableList.jsp" flush="true"/>
					</div>
				</div>
			</div>
		</div> 
		<div class="sidebar"></div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/theme/main');
		</script>
	</body>
</html>