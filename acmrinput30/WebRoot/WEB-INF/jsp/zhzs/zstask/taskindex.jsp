<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/13
  Time: 10:03
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.acmr.model.security.User" %>
<%@page import="com.acmr.model.security.Menu"%>
<%@page import="acmr.util.ListHashMap"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    @SuppressWarnings("unchecked")
    ListHashMap<Menu> menus = (ListHashMap<Menu>) request.getSession().getAttribute("usermenu");
    User user = (User) request.getSession().getAttribute("loginuser");
%>
<html>
<head>
    <title>${projectTitle}-指数任务</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="panel panel-default">
    <div class="panel-heading"></div>
    <div class="panel-body">
<div class="toolbar-left">
    <form class="form-inline J_search_form" action="${ctx}/zbdata/indexlist.htm?m=find">
        <div class="form-group">
            <span>任务时间期：</span>
            <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-sm">查询</button>
        </div>
    </form>
</div>
<table class="table table-striped table-hover J_regmgr_table">
    <thead>
    <tr>
        <th>任务时间期</th>
        <th>创建时间</th>
        <th>更新时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody class="list_body my_received">
    <c:forEach items="${tasklist}" var="vo">
    <tr>
        <td>${vo.getAyearmon()}</td>
        <td>${vo.getCreatetime()}</td>
        <td>${vo.getUpdatetime()}</td>
        <td>
            <a>计算</a>
            <a>删除</a>
        </td>
    </tr>
    </c:forEach>
    </tbody>
</table>
    </div>
</div>
</body>
</html>
