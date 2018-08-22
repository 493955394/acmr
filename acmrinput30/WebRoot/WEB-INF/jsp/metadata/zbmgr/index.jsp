<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-指标管理</title>
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
			        <div class="panel-heading">指标树</div>
			    </div>
				<ul id="treeDemo" class="ztree ztree-margin"></ul>
			</div>
			<div class="col-md-10 right-panel">
				<div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="toolbar-left">
							<form class="form-inline J_search_form" action="${ctx}/metadata/zbmgr.htm?m=find">
								<div class="form-group">
									<select id="querykey" class="form-control input-sm">
										<option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>指标代码</option>
										<option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>指标名称</option>
									</select>
								</div>
								<div class="form-group">
									<input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${code != '' && code!= null}">${code}</c:if><c:if test="${cname != '' && cname != null}">${cname}</c:if>">
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary btn-sm">查询</button>
								</div>
							</form>
						</div>
						<div class="toolbar-right">
							<div class="toolbar-group" style="position: relative;">
								<a href="javascript:;" class="btn btn-default btn-sm J_copy_data" id="copy">复制</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_grade_search">高级查询</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_Matching_details">批量匹配</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_import">批量导入</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_edit">批量修改</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_All_del">批量删除</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_Add">新增</a>
								<a href="javascript:;" class="btn btn-default btn-sm J_export_data">导出</a>
							</div>
						</div>
					</div>
					<div class="J_zbmgr_data_table">
						<jsp:include page="/WEB-INF/jsp/metadata/zbmgr/tableList.jsp" flush="true"/>
					</div>
				</div>
			</div>
		</div> 
		<div class="sidebar"></div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/metadata/zbmgr/main');
		</script>
	</body>
</html>