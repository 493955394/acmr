<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${projectTitle}-系统管理</title>
<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
<style type="text/css">
.cont {
	background: #cfe7ff;
	padding: 8px;
}

.tit {
	background: #a7d1ff;
	padding: 8px;
	color: #000;
	font-weight: 900;
	font-family: 'microsoft yahei';
	font-size: 16px;
}

.box {
	border: 1px dashed #ccc;
}

.boxmargin {
	margin-bottom: 8px;
}

.btn sys-btn {
	font-family: 'microsoft yahei';
	font-size: 16px;
}

.btn-primary {
	border-radius: 0;
	margin-top: 6px;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="container-fluid">
		${menulist}
	</div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/management/menu/main');
	</script>
</body>
</html>