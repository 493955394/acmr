<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/27
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
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
<style type="text/css">
    #jbxx {
        margin-left: 20%;
    }

    .glyphicon{
        color:#FF7F19;
    }
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />

<div class="container-fluid" id="mainpanel">

    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4>编辑指数计划</h4>
            </div>
            <div class="panel-body">
                <div>
                        <!-- Nav tabs -->
                        <ul class="nav nav-pills nav-justified" role="tablist">
                            <li role="presentation" class="active"><a href="#jbxx" aria-controls="jbxx" role="tab" data-toggle="tab">基本信息</a></li>
                            <li role="presentation"><a href="#zssx" aria-controls="zssx" role="tab" data-toggle="tab">指数筛选</a></li>
                            <li role="presentation"><a href="#jsfw" aria-controls="jsfw" role="tab" data-toggle="tab">计算范围</a></li>
                            <li role="presentation"><a href="#mxgh" aria-controls="mxgh" role="tab" data-toggle="tab">模型规划</a></li>
                        </ul>
                </div>
                <!-- Tab panes -->
                <div class="tab-content row" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="jbxx">
                        <form class="form-horizontal" id="indexForm" action="" method="post">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">编码：</label>
                                <div class="col-sm-5">
                                    <input name="index_code" id="index_code" class="form-control" value="${list.getCode()}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label ">计划名称：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="index_cname" name="index_cname" class="form-control" value="${list.getCname()}" />
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="index_cname" name="index_cname" class="form-control" value="${list.getCname()}" readonly />
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">所属目录：</label>
                                <div class="col-sm-5">
                                    <input type="text"  class="form-control hid-bottom" name="proname" value="${proname}" readonly>
                                    <input type="hidden" name="index_procode">
                                    <c:if test="${right!='1'}">
                                        <ul id="tree" class="ztree select-tree hid-top"></ul>
                                    </c:if>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">统计周期：</label>
                                <div class="col-sm-5">
                                    <select class="form-control" name="index_sort" id="index_sort" disabled>
                                        <c:if test="${list.getSort() == 'm'}"><option value="m" >月度</option></c:if>
                                        <c:if test="${list.getSort() == 'y'}"><option value="y" >年度</option></c:if>
                                        <c:if test="${list.getSort() == 'q'}"><option value="q" >季度</option></c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label talign-center fz13">起始数据期：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="startpeirod" name="startpeirod" class="form-control" type="text" value="${list.getStartperiod()}" placeholder="年度：2015，季度：2015A，月度：201501" />
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="startpeirod" name="startpeirod" class="form-control" type="text" value="${list.getStartperiod()}" placeholder="年度：2015，季度：2015A，月度：201501" readonly />
                                    </c:if>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label ">数据期满后：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="delayday" name="delayday" value="${list.getDelayday()}"  class="form-control"/>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="delayday" name="delayday" value="${list.getDelayday()}"  class="form-control" readonly/>
                                    </c:if>
                                </div>
                                <div>
                                    <label class="control-label ">自然日</label>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <input type="reset" style="display:none;" />
                        </form>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="zssx">
                        <div id="tree_and_find" class="col-md-2 left-panel">
                            <div class="form-group">
                                <input id="queryValue" type="text" class="form-control input-xm" placeholder="输入搜索内容" >
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
                                                <div class="form-group">
                                                    <div class="col-md-5">
                                                        <span style="font-size: 10px">数据来源：</span>
                                                    </div>
                                                    <div class="col-md-7">
                                                        <select id="ds_select" class="form-control input-sm zb_select">
                                                            <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                            <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <div class="col-md-5">
                                                        <span style="font-size: 10px">主体：</span>
                                                    </div>
                                                    <div class="col-md-7">
                                                        <select id="co_select" class="form-control input-sm zb_select">
                                                            <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                            <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="col-md-5">
                                                    <span style="font-size: 10px">单位：</span>
                                                </div>
                                                <div class="col-md-7">
                                                    <select id="unit_select"
                                                            class="form-control input-sm zb_select">
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
                                <div class="data_table panel-body J_zsjh_zbdata_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBdataList.jsp" flush="true"/>
                                </div>
                            </div>
                            <div class="col-md-3 right-panel">
                                <div class="panel_container">
                                    <c:forEach items="${zbs.zbchoose}" var="zb">
                                        <div class="panel panel-default zb_panel">
                                            <div class="panel-body">
                                                <input type="hidden" class="input_code" value="${zb.code}">
                                                <input type="hidden" class="input_zbcode" value="${zb.zbcode}">
                                                <input type="hidden" class="input_cocode" value="${zb.cocode}">
                                                <input type="hidden" class="input_dscode" value="${zb.dscode}">
                                                <input type="hidden" class="input_unitcode" value="${zb.unitcode}">
                                                <h5>${zb.zbname}</h5>
                                                <h6>主体：${zb.coname}</h6>
                                                <h6>数据来源：${zb.dsname}</h6>
                                                <h6>计量单位：${zb.unitname}</h6>
                                                <button type='button' class='btn btn-primary btn-sm zb_save' style='float: left;display: none'>保存</button>
                                                <button type='button' class='btn btn-primary btn-sm zb_delete' style='float: right;display: none;'>删除</button>
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
                        <div class="col-xs-3" style="padding-top:30px">
                            <input type="hidden" id="initTreePara" value="${initTreePara}" />
                            <input type="hidden" id="procode" value="" />
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">地区树</div>
                            </div>
                            <div class="panel-body" style="height: 500px;width: 310px;overflow:auto ">
                                <ul id="treeDemo" class="ztree ztree-margin">
                                </ul></div>
                            <input type="hidden" name="regcode" value="" />
                            <input type="hidden" name="regname" value="" />
                        </div>
                        <div class="col-xs-1 btn-group-vertical" role="group" style="border:1px solid white;padding-top:100px">
                            <button class="btn btn-default btn-lg" id="sigglechoose" style="border-style:solid;border-width:1px;border-color:#FF7F19;"><span style="color: #FF7F19;">></span></button>
                            <div class="clearfix" style="height: 30px"></div>
                            <button class="btn btn-default btn-lg" id="chooseall" style="border-style:solid;border-width:1px;border-color:#FF7F19;background-color: #FF7F19;"><span style="color: white">>></span></button>
                            <div class="clearfix" style="height: 30px"></div>
                            <button class="btn btn-default btn-lg" id="delsiggle" style="border-style:solid;border-width:1px;border-color:#FF7F19;"><span style="color: #FF7F19;"><</span></button>
                            <div class="clearfix" style="height: 30px"></div>
                            <button class="btn btn-default btn-lg" id="delall" style="border-style:solid;border-width:1px;border-color:#FF7F19;background-color: #FF7F19;"><span style="color: white"><<</span></button>
                        </div>
                        <div class="col-xs-3" style="padding-top:30px">
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">地区列表</div>
                            </div>
                            <div  style="height: 500px;width:100%;overflow:auto ">
                                <ul class="list-group regul" id="selectreg">
                                    <c:forEach items="${regs}" var="reg">
                                        <li class="list-group-item selectedli"  id="${reg.regcode}">${reg.regcname}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <div class="col-xs-5" style="border:#E4EDF6;padding-left: 20px;padding-right: 20px;padding-top:30px">
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">数据检查区</div>
                            </div>
                            <div class="panel-body">
                                <span class="col-sm-2" style="font-size: 10px">时间选择</span>
                                <input class="col-sm-2" name="begintime"/>
                                <span class="col-sm-1" style="font-size: 10px">至</span>
                                <input class="col-sm-2" name="endtime"/>
                                <span class="col-sm-1"></span>
                                <button id="datachecks" class="col-sm-2" style="font-size: 5px"><i class="glyphicon glyphicon-check"></i>数据检查</button>
                                <button class="J_plan_excel col-sm-2" style="font-size: 5px"><i class="glyphicon glyphicon-save"></i>数据下载</button>
                                <div class="data_check_show">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/regSelect.jsp" flush="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="mxgh">
                        <div class="col-md-2 left-panel" id="module_tree_container">
                            <div id="module_tree">
                                <ul id="moduleTree" class="ztree ztree-margin"></ul>
                            </div>
                        </div>
                        <div class="col-md-10 right-panel" id="module_container">
                            <div class="panel-default panel">
                                <div class="panel-body">
                                    <div class="toolbar-left">
                                        <form class="form-inline J_search_form" action="${ctx}/zbdata/zsjhedit.htm?m=searchFind">
                                            <div class="form-group">
                                                <select id="querykey" class="form-control input-sm">
                                                  <option value="zs_cname" <c:if test="${zs_code != '' && zs_code!= null}">selected</c:if>>名称</option>
                                                    <option value="zs_code" <c:if test="${zs_cname != '' && zs_cname != null}">selected</c:if>>编码</option>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <input id="moduleQuery" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="">
                                            </div>
                                            <div class="form-group">
                                                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="toolbar-right">
                                        <div class="toolbar-group" style="position: relative;">
                                            <button class="btn btn-default btn-sm J_Add_ZS" type="button"><i class="glyphicon glyphicon-plus"></i>新增</button>
                                            &nbsp;
                                            <button class="btn btn-default btn-sm J_Add weight_set" type="button">
                                                <i class="glyphicon glyphicon-pencil"></i>新增权重设置
                                            </button>
                                            &nbsp;
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-body J_zsjh_module_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/modTableList.jsp" flush="true"/>
                                </div>
                            </div>

                        </div>
                    </div>
                    <!--Tab panes end-->
            </div>
        </div>
    </div>
        <div class="col-sm-offset-5 col-sm-7">
        <button type="button" class="btn btn-primary tosaveall">保存</button>
        <button type="reset" class="btn btn-primary resetindex">取消</button>
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
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/module')
</script>

</html>

