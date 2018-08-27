<%@ page import="com.acmr.model.security.User" %>
<%@page import="com.acmr.model.security.Menu"%>
<%@page import="acmr.util.ListHashMap"%><%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/24
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    @SuppressWarnings("unchecked")
    ListHashMap<Menu> menus = (ListHashMap<Menu>) request.getSession().getAttribute("usermenu");
    User user = (User) request.getSession().getAttribute("loginuser");
%>
<html>
<head>
    <title>${projectTitle}-指数计划</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />

<div class="container-fluid" id="mainpanel">
    <div class="col-md-2 left-panel">
        <div class="panel tree-panel">
            <div class="panel-heading">地区树</div>
        </div>
        <ul id="treeDemo" class="ztree ztree-margin"></ul>
    </div>
    <div class="col-md-10 right-panel">
        <div class="panel panel-default">
            <div class="panel-heading"></div>
            <div class="panel-body">
                <div class="toolbar-left">
                    <form class="form-inline J_search_form" action="${ctx}/metadata/regmgr.htm?m=find">
                        <div class="form-group">
                            <select id="querykey" class="form-control input-sm">
<%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                            </select>
                        </div>
                        <div class="form-group">
                            <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容">
<%--
                            <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${code != '' && code!= null}">${code}</c:if><c:if test="${cname != '' && cname != null}">${cname}</c:if>">
--%>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary btn-sm">查询</button>
                        </div>
                    </form>
                </div>
                <div class="toolbar-right">
                    <div class="toolbar-group" style="position: relative;">
                        <a href="/" class="btn btn-default btn-sm J_Add">新增目录</a>
                        <a href="/" class="btn btn-default btn-sm J_Add">新增计划</a>
                        <a href="/" class="btn btn-default btn-sm J_copy_data">复制到</a>
                    </div>
                </div>
            </div>
            <div class="J_regmgr_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/indextable.jsp" flush="true"/>
            </div>
        </div>
    </div>

</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
</script>
</html>
