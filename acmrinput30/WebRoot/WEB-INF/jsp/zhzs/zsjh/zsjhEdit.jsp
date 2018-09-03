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
                    <div role="tabpanel" class="tab-pane" id="zssx">
                        <div id="tree_and_find" class="col-md-2 left-panel">
                            <div class="form-group">
                                <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" >
                                <%--
                                                            <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${code != '' && code!= null}">${code}</c:if><c:if test="${cname != '' && cname != null}">${cname}</c:if>">
                                --%>
                            </div>
                            <div class="form-group">
                                <button type="button" class="btn btn-primary btn-sm btn_search">搜索</button>
                            </div>
                            <%-- <div id="find_panel" class="panel panel-default" style="display: none;"></div>--%>
                            <ul id="treeDemo1" class="ztree ztree-margin"></ul>
                            <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/zbtree.jsp" flush="true"/>--%>
                        </div>
                        <div class="col-md-10 right-panel">
                            <div class="col-md-9 left-panel">
                                <div>
                                    <div class="panel panel-default">
                                        <div class="panel_zbname panel-heading" code="">请选择指标</div>
                                        <div class="ds_choose panel-body">
                                            <div class="col-md-4">
                                                <h5>数据来源：</h5>
                                                <div class="form-group">
                                                    <select id="ds_select" class="form-control input-sm zb_select">
                                                        <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                        <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <h5>主体：</h5>
                                                <div class="form-group">
                                                    <select id="co_select" class="form-control input-sm zb_select">
                                                        <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                        <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <h5>单位：</h5>
                                                <div class="form-group">
                                                    <select id="unit_select" class="form-control input-sm zb_select">
                                                        <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                        <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                    </select>
                                                </div>
                                            </div>
                                            <button type="button" class="btn btn-primary btn-sm zb_add" style="display: none;float: right">添加指标</button>
                                        </div>
                                        <div class="panel-footer"></div>
                                        <%--    <div class="co_choose panel-body">
                                                <div class="col-md-2">
                                                    <h4>主体：</h4>
                                                </div>
                                                <div class="col-md-10">
                                                    <p class="hidden_choose" style="color: gray;">请先选择指标</p>
                                                    <div class="show_choose co_show" style="display: none;">
                                                        test
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel-footer"></div>
                                            <div class="dw_choose panel-body">
                                                <div class="col-md-2">
                                                    <h4>单位：</h4>
                                                </div>
                                                <div class="col-md-10">
                                                    <p class="hidden_choose" style="color: gray;display: inline">请先选择指标</p>
                                                    <div class="show_choose co_show" style="display: none;">
                                                        test
                                                    </div>
                                                    <button type="button" class="btn btn-primary btn-sm zb_add" style="display: none">添加指标</button>
                                                </div>
                                            </div>
                                            <div class="panel-footer"></div>--%>
                                        <%--    <div class="data_table panel-body J_zsjc_zbdata_table">
                                                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBdataList.jsp" flush="true"/>
                                            </div>--%>

                                    </div>
                                    <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/addZB.jsp" flush="true"/>--%>
                                </div>
                                <form class="form-inline J_search_form" action="${ctx}/zbdata/my.htm?m=getDataTest">
                                    <div class="form-group">
                                        <button type="button" class="btn btn-primary btn-sm">测试</button>
                                    </div>
                                </form>
                                <div class="data_table panel-body J_zsjh_zbdata_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBdataList.jsp" flush="true"/>
                                </div>
                            </div>
                            <div class="col-md-3 right-panel">
                                <div class="panel_container">
                                    <c:forEach items="${zbs.zbchoose}" var="zb">
                                        <div class="panel panel-default">
                                            <div class="panel-body">
                                                <input type="hidden" value="${zb.code}">
                                                <h5>${zb.zbname}</h5>
                                                <h6>主体：${zb.coname}</h6>
                                                <h6>数据来源：${zb.dsname}</h6>
                                                <h6>计量单位：${zb.unitname}</h6>
                                                <button type='button' class='btn btn-primary btn-sm' style='float: left;'>保存</button>
                                                <button type='button' class='btn btn-primary btn-sm' style='float: right;'>删除</button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <%--   <div class="panel panel-default">
                                           <div class="panel-body">
                                               指标1
                                           </div>
                                       </div>
                                       <div class="panel panel-default">
                                           <div class="panel-body">
                                               指标1
                                           </div>
                                       </div>
                                   --%>
                                </div>
                                <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBPanel.jsp" flush="true"/>--%>
                            </div>
                        </div>
                    </div>
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
                                <select size="10" style="width: 100%" class="regul" id="selectreg"></select>
                            </div>
                        </div>

                        <div class="col-xs-5" style="padding-left: 20px;padding-right: 20px">
                            <div class="panel panel-default">
                                <div class="panel-heading" style="text-align:center">数据检查区</div>
                                <div class="panel-body">
                                    <span>时间选择</span>
                                    <input name="begintime"/>
                                    ~
                                    <input name="endtime"/>
                                    <button id="datachecks">数据检查</button>
                                    <button>数据下载</button>
                                    <div id="data_check_show"></div>
                                </div>
                            </div>
                        </div>
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
    define("editjsp",function (require,exports,module) {
        var indexlist=[];
        <c:forEach items="${indexlist}" var="index">
        if (${index.getIfdata()=='0'}){
        if(${index.getProcode()!=null})
            indexlist.push({id:"${index.getCode()}",pId:"${index.getProcode()}",name:"${index.getCname()}",isPrent:!${index.getIfdata()},sou:!${index.getIfdata()}})
        else indexlist.push({id:"${index.getCode()}",pId:"#1",name:"${index.getCname()}",isParent:!${index.getIfdata()},sou:!${index.getIfdata()}})
    }
        </c:forEach>
        var zbs=[];
        <c:forEach items="${zbs.zbchoose}" var="zb">
        zbs.push({
            code:"${zb.code}",
            zbcode:"${zb.zbcode}",
            zbname:"${zb.zbname}",
            dscode:"${zb.dscode}",
            dsname:"${zb.dsname}",
            cocode:"${zb.cocode}",
            coname:"${zb.coname}",
            unitcode:"${zb.unitcode}",
            unitname:"${zb.unitname}"
        })
        </c:forEach>
        module.exports={
            indexlist:indexlist,
            zbs:zbs
        }
    })
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/main');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/zbAdd');

</script>

</html>

