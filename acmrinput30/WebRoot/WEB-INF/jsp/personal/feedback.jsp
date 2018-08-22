<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-我的</title>
		<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid" id="mainpanel">
			<div class="col-md-2" style="height: 570px;overflow:auto;background: #f6f9fe">
			<div class="panel tree-panel">
		        <div class="panel-heading">反馈分类</div>
		    </div>
			    <div style="margin-top:20px;">
				    <div class="list-group">	
				    	<a href="${ctx}/personal/feedback.htm?m=findDimension" class="list-group-item <c:if test="${type =='dimension'}">active</c:if>">维度反馈</a>
				    	<a href="${ctx}/personal/feedback.htm?m=findSynonym" class="list-group-item <c:if test="${type =='synonym'}">active</c:if>">同义词反馈</a>
				    	<a href="${ctx}/personal/feedback.htm?m=finddatafeed" class="list-group-item <c:if test="${type =='datafeed'}">active</c:if>">数据反馈</a>
				    </div>
			    </div>
			</div>
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-body">
					</div>				
					<div class="J_feedback_data_table">
						<c:if test="${type=='dimension'}">
							<jsp:include page="/WEB-INF/jsp/personal/dimensionTabList.jsp" flush="true"/>
						</c:if>
						<c:if test="${type=='synonym'}">
							<jsp:include page="/WEB-INF/jsp/personal/synonymTabList.jsp" flush="true"/>
						</c:if>
						<c:if test="${type=='datafeed'}">
							<jsp:include page="/WEB-INF/jsp/personal/dataTabList.jsp" flush="true"/>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/personal/feedback.js');
		</script>
	</body>
</html>