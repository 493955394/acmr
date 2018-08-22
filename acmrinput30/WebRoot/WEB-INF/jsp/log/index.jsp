<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${projectTitle}-首页</title>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />	
    <iframe src="${url}" width="100%" frameborder="0"  id="win" name="win"
        height="850" scrolling="auto" ></iframe>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/log/main');
	</script>
</body>
</html>