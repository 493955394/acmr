<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-受理</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid" id="mainpanel">
			<div class="col-md-2" style="height: 570px;overflow:auto;background: #f6f9fe">
			<div class="panel tree-panel">
			        <div class="panel-heading">反馈分类</div>
			    </div>
				<ul>
					<li><a href="${ctx}/system/handle.htm?m=findDimension">维度反馈</a></li>
					<li><a href="${ctx}/system/handle.htm?m=findSynonym">同义词反馈</a></li>
				</ul>
			</div>
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
					</div>				
					<div class="J_feedback_data_table">
						<c:if test="${type=='dimension'}">
							<jsp:include page="/WEB-INF/jsp/management/handle/dimensionTabList.jsp" flush="true"/>
						</c:if>
						<c:if test="${type=='synonym'}">
							<jsp:include page="/WEB-INF/jsp/management/handle/synonymTabList.jsp" flush="true"/>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/metadata/handle/main.js');
		</script>
	</body>
</html>