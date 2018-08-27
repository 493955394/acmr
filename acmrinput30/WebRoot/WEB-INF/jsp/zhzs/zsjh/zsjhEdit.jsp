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
                <div class="tab-content" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="jbxx">
                <form class="form-horizontal" id="myform" action="" method="post">
                <div class="form-group">
                    <label class="col-sm-2 control-label">编码：</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" value="${code}" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label ">计划名称：</label>
                    <div class="col-sm-5">
                        <input id="cname" name="cname" class="form-control" type="text" value="${cname}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">所属目录：</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="procode">
                            <option value="M"  ></option>
                            <option value="M"  ></option>
                            <option value="M"  ></option>
                        </select>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">统计周期：</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="sort" disabled>
                            <option value="Y"  <c:if test="${timesort == 'Y'}"></c:if>年度</option>
                            <option value="M"  <c:if test="${timesort == 'M'}"></c:if>月度</option>
                            <option value="Q"  <c:if test="${timesort == 'Q'}"></c:if>季度</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label talign-center fz13">起始数据期：</label>
                    <div class="col-sm-5">
                        <input id="startpeirod" name="startpeirod" class="form-control" type="text" value="${startpeirod}" placeholder="年度：2015，季度：2015A，月度：201501" />
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label ">数据期满后：</label>
                    <div class="col-sm-5">
                        <input id="delayday" name="delayday" value="${delayday}"  class="form-control"/>
                    </div>
                    <div>
                        <label class="control-label ">自然日</label>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"> 指数个数位数：</label>
                    <div class="col-sm-5">
                        <input id="dacimal" name="dacimal" value="${dacimal}" class="form-control"/>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </form>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="zssx">...</div>
                    <div role="tabpanel" class="tab-pane" id="jsfw">
                        <div class="panel tree-panel">
                            <div class="panel-heading">地区树</div>
                        </div>
                        <ul id="treeDemo" class="ztree ztree-margin"></ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="mxgh">...</div>

                </div><!--Tab panes end-->
            </div>
        </div>
    </div>
</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/main');
</script>

</html>

