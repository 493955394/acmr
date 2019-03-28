<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ page import="acmr.util.PubInfo" %>
<%@ page import="com.acmr.model.zhzs.IndexMoudle" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/5
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="${ctx}/css/pastreview.css" />
<html>
<head>
    <title>权重设置</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<div id="container">
    <div class="ict-header">
        <jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true"/>
    </div>
    <div class="ict-page">
        <div class="container-fluid">

<input class="indexCode" type="hidden" value="${indexcode}">
<input type="hidden" class="scheme_code" value="${schemecode}">

<div class="panel-body J_weight_table">
    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/weighttable.jsp" flush="true"/>
</div>
        </div>
        <jsp:include page="/WEB-INF/jsp/common/footer.jsp" flush="true" />
    </div>
</div>
</body>

<script>
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/weight');
</script>


</html>

