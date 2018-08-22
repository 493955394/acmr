<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-汇率管理</title>
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
			        <div class="panel-heading">汇率管理</div>
			    </div>
				<ul id="treeDemo" class="ztree ztree-margin"></ul>
			</div>
			<div class="col-md-10 right-panel">
				<div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="toolbar-left">
							<form class="form-inline J_search_form">
								<div class="form-group">
									<select id="unitcode" class="form-control input-sm">
										<option value="">请选择货币</option>
										<c:forEach items="${allunit}" var="unit">
											<option value="${unit.unitcode}" <c:if test="${unitcode eq unit.unitcode}">selected</c:if>>${unit.unitname}</option>
										</c:forEach>
									</select>
									<select id="sjcode" class="form-control input-sm">
										<option value="">请选择时间</option>
										<option value="M" <c:if test="${sjcode eq 'M'}">selected</c:if>>月度</option>
										<option value="Q" <c:if test="${sjcode eq 'Q'}">selected</c:if>>季度</option>
										<option value="Y" <c:if test="${sjcode eq 'Y'}">selected</c:if>>年度</option>
									</select>
								</div>
							</form>
						</div>
						<div class="toolbar-right">
							<div class="toolbar-group" style="position: relative;">
								<a href="javascript:;" class="btn btn-default btn-sm J_Add">新增</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_import">批量导入</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_export_data">导出</a>
							</div>
						</div>
					</div>
					<div class="J_zbmgr_data_table">
						<jsp:include page="/WEB-INF/jsp/metadata/hvmgr/tableList.jsp" flush="true"/>
					</div>
				</div>
			</div>
		</div> 
		<div class="sidebar"></div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/metadata/hvmgr/main');
		</script>
	</body>
</html>