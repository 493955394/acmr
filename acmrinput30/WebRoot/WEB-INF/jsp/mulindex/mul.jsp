<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${projectTitle}-生成数据</title>
<style type="text/css">
body{
}
.createTable {
	background: url("${ctx}/images/mul.jpg") no-repeat center;
	width: 944px;
	height: 459px;
	position: relative;
	margin: 0 auto;
}
.createTable button{
	position: absolute;
	right: 184px;
	bottom: 165px;
	background: #ff9900;
	border-color: #ec8f03;
	padding-left: 50px;
	padding-right: 50px;
}
.createTable .btn-primary:hover,
.createTable .btn-primary:focus,
.createTable .btn-primary:active {
	color: #ffffff;
	background-color: #ec8f03;
	border-color: #ff9900;
}
.createTable .btn-primary.disabled {
	background-color: #ccc;
	border-color: #bbb;
}

</style>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="createTable">
		<c:if test="${status == 0 || status == 2}">
			<button class="btn btn-primary" id="generate_table">生成表</button>
		</c:if>
		<c:if test="${status == 1}">
			<button class="btn btn-primary disabled" id="generate_table" disabled="disabled">生成中</button>
		</c:if>
		
	</div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/mulindex/main');
	</script>
</body>
</html>