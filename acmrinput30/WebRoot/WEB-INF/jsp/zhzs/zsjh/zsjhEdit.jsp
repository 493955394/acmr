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
                <form class="form-horizontal" id="myform" action="" method="post">
                    <input type="hidden" name="procode" value="${procode}">
                <div class="form-group">
                    <label class="col-sm-2 control-label">编码：</label>
                    <div class="col-sm-5">
                        <input type="text" id="index_code" class="form-control" value="${list.getCode()}" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label ">计划名称：</label>
                    <div class="col-sm-5">
                        <input id="cname" name="cname" class="form-control" type="text" value="${list.getCname()}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">所属目录：</label>
                    <div class="col-sm-5">
                        <input type="text"  class="form-control hid-bottom" name="proname" value="${proname}" readonly>
                        <ul id="tree" class="ztree select-tree hid-top"></ul>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">统计周期：</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="sort" disabled>
                            <c:if test="${list.getSort() == 'm'}"><option value="m" >月度</option></c:if>
                            <c:if test="${list.getSort() == 'y'}"><option value="y" >年度</option></c:if>
                            <c:if test="${list.getSort() == 'q'}"><option value="q" >季度</option></c:if>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label talign-center fz13">起始数据期：</label>
                    <div class="col-sm-5">
                        <input id="startpeirod" name="startpeirod" class="form-control" type="text" value="${list.getStartperiod()}" placeholder="年度：2015，季度：2015A，月度：201501" />
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label ">数据期满后：</label>
                    <div class="col-sm-5">
                        <input id="delayday" name="delayday" value="${list.getDelayday()}"  class="form-control"/>
                    </div>
                    <div>
                        <label class="control-label ">自然日</label>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </form>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="zssx">...</div>
                    <div role="tabpanel" class="tab-pane" id="jsfw">
                        <div class="col-xs-3" style="padding-top:50px">
                        <input type="hidden" id="initTreePara" value="${initTreePara}" />
                        <input type="hidden" id="procode" value="" />
                        <div class="panel tree-panel">
                            <div class="panel-heading" style="text-align:center">地区树</div>
                        </div>
                        <ul id="treeDemo" class="ztree ztree-margin"></ul>
                            <input type="hidden" name="regcode" value="" />
                            <input type="hidden" name="regname" value="" />
                        </div>
                        <div class="col-xs-1 btn-group-vertical" role="group" style="padding-top:100px">
                            <button class="btn btn-default btn-lg" id="sigglechoose">></button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default btn-lg" id="chooseall">>></button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default btn-lg" id="delsiggle"><</button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default btn-lg" id="delall"><<</button>
                        </div>
                        <div class="col-xs-3" style="padding-top:50px">
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">地区列表</div>
                            </div>
                            <div class="panle-body" >
                                <ul id="selectreg"></ul>
                            </div>
                        </div>

                        <div class="col-xs-5" style="padding-left: 20px;padding-right: 20px">
                            <div class="panel panel-default">
                                <div class="panel-heading" style="text-align:center">数据检查区</div>
                                <div class="panel-body">
                                <span>时间检查</span>
                                <input name="begintime"/>
                                    ~
                                    <input name="endtime">
                                    <button>数据检查</button>
                                    <button>数据下载</button>
                                </div>
                            </div>
                        </div>

                </div><!--Tab panes end-->
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

