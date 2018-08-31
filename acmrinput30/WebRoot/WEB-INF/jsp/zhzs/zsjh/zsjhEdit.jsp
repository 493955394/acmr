<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/27
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.acmr.model.security.User" %>
<%@page import="com.acmr.model.security.Menu"%>
<%@page import="acmr.util.ListHashMap"%>
<%
    @SuppressWarnings("unchecked")
    ListHashMap<Menu> menus = (ListHashMap<Menu>) request.getSession().getAttribute("usermenu");
    User user = (User) request.getSession().getAttribute("loginuser");
%>
<html>
<head>
    <title>${projectTitle}-编辑指数计划</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />

<div class="container-fluid" id="mainpanel">

    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1>编辑指数计划</h1>
            </div>
            <div class="panel-body">
                <div>
                        <!-- Nav tabs -->
                        <ul class="nav nav-tabs nav-justified" role="tablist">
                            <li role="presentation" class="active"><a href="#jbxx" aria-controls="jbxx" role="tab" data-toggle="tab">基本信息</a></li>
                            <li role="presentation"><a href="#zssx" aria-controls="zssx" role="tab" data-toggle="tab">指数筛选</a></li>
                            <li role="presentation"><a href="#jsfw" aria-controls="jsfw" role="tab" data-toggle="tab">计算范围</a></li>
                            <li role="presentation"><a href="#mxgh" aria-controls="mxgh" role="tab" data-toggle="tab">模型规划</a></li>
                        </ul>
                </div>
                <!-- Tab panes -->
                <div class="tab-content row" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="jbxx">
                        <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/commonIndex.jsp" flush="true"/>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="zssx">
                        <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/zbEdit.jsp" flush="true"/>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="jsfw">
                        <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/regSelect.jsp" flush="true"/>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="mxgh">
                        <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/modelEdit.jsp" flush="true"/>
                    </div>
                    <!--Tab panes end-->
            </div>
        </div>
    </div>
</div>
</div>
</body>
<script>
    define("listjsp",function (require,exports,module) {
        var indexlist=[];
        <c:forEach items="${indexlist}" var="index">
        if (${index.getIfdata()=='0'}){
        if(${index.getProcode()!=null})
            indexlist.push({id:"${index.getCode()}",pId:"${index.getProcode()}",name:"${index.getCname()}",isPrent:!${index.getIfdata()},sou:!${index.getIfdata()}})
        else indexlist.push({id:"${index.getCode()}",pId:"#1",name:"${index.getCname()}",isParent:!${index.getIfdata()},sou:!${index.getIfdata()}})
    }
        </c:forEach>
        module.exports={
            indexlist:indexlist
        }
    })
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/main');
</script>

</html>

