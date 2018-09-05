<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<html>
<head>
    <title>权重设置</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<h2>权重设置</h2>
<button type="button" class="btn btn-primary btn-sm save_weight" style="float: right">保存设置</button>


<table class="table table-striped table-hover">
    <c:forEach items="${mods}" var="module">
        <c:if test="${module.getProcode()==''}">
            <tr>
                <td rowspan="3">${module.getCname()}</td>
            </tr>
            <tr>c</tr>
            <tr>c</tr>
        </c:if>
    </c:forEach>


<%--    <tr>
        <c:forEach items="${mods}" var="module">
            <td>${module.getCname()}</td>
        </c:forEach>
    </tr>
    <tr>
        <c:forEach items="${mods}" var="module">
            <td>${module.getProcode()}</td>
            <c:if test="${module.getProcode()==''}">
                <td>测试</td>
            </c:if>
        </c:forEach>
    </tr>--%>
<%--    <tr>
        <td rowspan="3">总指数</td>
        <td>分指数1</td>
    </tr>
    <tr>
        <td>分指数2</td>
    </tr>
    <tr>
        <td>分指数3</td>
    </tr>--%>
</table>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/weight');
</script>
</html>
