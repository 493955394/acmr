<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/27
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ page import="com.acmr.model.security.User" %>
<%@page import="com.acmr.model.security.Menu" %>
<%@page import="acmr.util.ListHashMap" %>
<%
    @SuppressWarnings("unchecked")
    ListHashMap<Menu> menus = (ListHashMap<Menu>) request.getSession().getAttribute("usermenu");
    User user = (User) request.getSession().getAttribute("loginuser");
%>
<html>
<head>
    <title>${projectTitle}-编辑指数计划</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true"/>
</head>
<body>
<style type="text/css">
   span.glyphicon{
        color: red;
    }
    /*.glyphicon {
        color: #F39801;
    }*/
    .tree-panel{
        margin: 0.0px;
    }

</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true"/>

<div class="container-fluid" id="mainpanel">

        <div class="panel panel-default dragpannel" style="height: 70%;overflow: auto;">
            <div class="panel-heading">
               编辑指数计划
            </div>
            <div class="panel-body">
                <div id="top_div">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist" id="bjjhTab">
                        <li role="presentation" class="active"><a href="#jbxx" aria-controls="jbxx" role="tab"
                                                                  data-toggle="tab">基本信息</a></li>
                        <li role="presentation"><a href="#zssx" aria-controls="zssx" role="tab"
                                                   data-toggle="tab">指数筛选</a></li>
                        <li role="presentation"><a href="#jsfw" aria-controls="jsfw" role="tab"
                                                   data-toggle="tab">计算范围</a></li>
                        <li role="presentation"><a href="#mxgh" aria-controls="mxgh" role="tab"
                                                   data-toggle="tab">模型规划</a></li>
                    </ul>
                </div>
                <!-- Tab panes -->
                <div class="col-xs-12 tab-content row" style="padding-top: 10px;">
                    <div role="tabpanel" class="tab-pane active" id="jbxx" style="height: 80%;overflow: auto">
                        <form class="form-horizontal" id="indexForm" action="" method="post">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                                <div class="col-sm-5">
                                    <input name="index_code" id="index_code" class="form-control"
                                           value="${list.getCode()}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>计划名称：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="index_cname" name="index_cname" class="form-control"
                                               value="${list.getCname()}"/>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="index_cname" name="index_cname" class="form-control"
                                               value="${list.getCname()}" readonly/>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                                <div class="col-sm-5">
                                    <input type="text" class="form-control hid-bottom" name="proname" value="${proname}"
                                           readonly>
                                    <input type="hidden" name="index_procode" value="${list.getProcode()}">
                                    <c:if test="${right!='1'&&right!='2'}">
                                        <ul id="tree" class="ztree select-tree hid-top"></ul>
                                    </c:if>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>统计周期：</label>
                                <div class="col-sm-5">
                                    <select class="form-control" name="index_sort" id="index_sort" disabled>
                                        <c:if test="${list.getSort() == 'm'}">
                                            <option value="m">月度</option>
                                        </c:if>
                                        <c:if test="${list.getSort() == 'y'}">
                                            <option value="y">年度</option>
                                        </c:if>
                                        <c:if test="${list.getSort() == 'q'}">
                                            <option value="q">季度</option>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label talign-center fz13"><span class="glyphicon glyphicon-asterisk required_ico"></span>起始数据期：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="startpeirod" name="startpeirod" class="form-control" type="text"
                                               value="${list.getStartperiod()}"
                                               placeholder="年度：2015，季度：2015A，月度：201501"/>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="startpeirod" name="startpeirod" class="form-control" type="text"
                                               value="${list.getStartperiod()}" placeholder="年度：2015，季度：2015A，月度：201501"
                                               readonly/>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label "><span class="glyphicon glyphicon-asterisk required_ico"></span>数据期满后：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="delayday" name="delayday" value="${list.getDelayday()}"
                                               class="form-control"/>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="delayday" name="delayday" value="${list.getDelayday()}"
                                               class="form-control" readonly/>
                                    </c:if>
                                </div>
                                <label class="control-label">自然日</label>
                                <span class="normalday"></span>
                            </div>
                            <input type="reset" style="display:none;"/>
                        </form>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="zssx">
                        <div id="tree_and_find" class="col-md-2 left-panel" style="height: 100%">
                            <form class="form-inline J_search_form">

                                <div class="form-group" style="width: 60%;">
                                    <input id="queryValue" type="text" class="form-control input-xm" placeholder="搜索内容" style="width: 100%;">
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-primary btn-sm btn_search">搜索</button>
                                </div>
                            </form>

                            <ul id="treeDemo1" class="ztree ztree-margin"></ul>
                            <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/zbtree.jsp" flush="true"/>--%>
                        </div>
                        <div class="col-md-10 right-panel">
                            <div class="col-md-9">
                                <div>
                                    <div class="panel panel-default">
                                        <div class="panel_zbname panel-heading" code="">请选择指标</div>
                                        <div class="ds_choose panel-heading" style="height: 60px">
                                            <div class="col-md-4">
                                                <span style="font-size: 14px">数据来源：</span>
                                                <select id="ds_select" class="input-sm zb_select">
                                                    <option>请选择</option>
                                                    <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                    <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                </select>
                                            </div>
                                            <div class="col-md-4">
                                                <span style="font-size: 14px">主体：</span>
                                                <select id="co_select" class="input-sm zb_select">
                                                    <option>请选择</option>

                                                <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                    <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                </select>
                                            </div>
                                            <div class="col-md-4">


                                                <span style="font-size: 14px">单位：</span>
                                                <select id="unit_select"
                                                        class="input-sm zb_select">
                                                    <option>请选择</option>
                                                </select>

                                            </div>

                                        </div>

                                        <button type="button" class="btn btn-default btn-sm zb_add"
                                                style="display: none;float: right;margin-top: 10px;margin-bottom: 0"><i
                                                class="glyphicon glyphicon-plus"></i><span
                                                class="word">&nbsp;添加指标</span></button>
                                        <%--    <button type="button" class="btn btn-default" style="display: none;float: right"><i class="glyphicon glyphicon-plus"></i><span class="word">&nbsp;添加指标</span></button>--%>

                                    </div>

                                    <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/addZB.jsp" flush="true"/>--%>
                                </div>

                                <div class="data_table J_zsjh_zbdata_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBdataList.jsp" flush="true"/>
                                </div>
                            </div>
                            <div class="col-md-3 right-panel">
                                <div class="panel_container" style="border: 1px solid #dddddd;height: 100%;width: 100%;overflow:auto;">
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
                                                <button type='button' class='btn btn-primary btn-sm zb_save'
                                                        style='float: left;display: none'>保存
                                                </button>
                                                <button type='button' class='btn btn-primary btn-sm zb_delete'
                                                        style='float: right;display: none;'>删除
                                                </button>
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
                        <div class="col-xs-2">
                            <input type="hidden" id="initTreePara" value="${initTreePara}"/>
                            <input type="hidden" id="procode" value=""/>
                            <div class="panel tree-panel" >
                                <div class="panel-heading" style="text-align:center">地区树</div>
                            </div>
                            <div class="panel-body col-xs-12" style="height: 60%;overflow:auto;background-color: #F4F5F9">
                                <ul id="treeDemo" class="ztree ztree-margin">
                                </ul>
                            </div>
                            <input type="hidden" name="regcode" value=""/>
                            <input type="hidden" name="regname" value=""/>
                        </div>
                        <div class="col-xs-1 col-lg-1 col-md-1 col-sm-1 btn-group-vertical" role="group" style="border:1px solid white;padding-top:50px">
                            <div id="sigglechoose" style="border-style:solid;border-width:1px;border-color:#F39801;height: 30px;width: 50px;" class="col-md-offset-2 col-md-4">
                                <span style="color: #F39801;text-align: center;font-size: 15px;">></span>
                            </div>

                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="chooseall" style="border-style:solid;border-width:1px;border-color:#F39801;background-color: #F39801;height: 30px;width: 50px;" class="col-md-offset-2 col-md-4">
                                <span style="color: white;text-align: center;font-size: 15px;">>></span>
                            </div>
                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="delsiggle" style="border-style:solid;border-width:1px;border-color:#F39801;height: 30px;width: 50px;" class="col-md-offset-2 col-md-4">
                                <span style="color:#F39801;text-align: center;font-size: 15px;"><</span>
                            </div>
                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="delall"
                                    style="border-style:solid;border-width:1px;border-color:#F39801;background-color: #F39801;height: 30px;width: 50px;"  class="col-md-offset-2 col-md-4">
                                <span style="color: white;text-align: center;font-size: 15px;"><<</span></div>
                        </div>
                        <div class="col-xs-2">
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">地区列表</div>
                            </div>
                            <div style="height: 60%;width:100%;overflow:auto;border: 1px solid #dddddd">
                                <ul class="list-group regul" id="selectreg">
                                    <c:forEach items="${regs}" var="reg">
                                        <li class="list-group-item selectedli" id="${reg.regcode}">${reg.regcname}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <div class="col-xs-7"
                             style="border:#E4EDF6;padding-left: 20px;padding-right: 20px;">
                            <div class="panel tree-panel">
                                <div class="panel-heading" style="text-align:center">数据检查区</div>
                            </div>
                            <div style="margin-top: 10px">
                                <span class="col-md-2" style="font-size: 15px">时间选择</span>
                                <input class="col-md-2" name="begintime"/>
                                <span class="col-md-1" style="font-size: 15px">至</span>
                                <input class="col-md-2" name="endtime"/>
                                <span class="col-md-1"></span>
                                <div style="float: right">
                                    <button id="datachecks" class="btn btn-default btn-sm" style="font-size: 10px"><i class="data-check"></i>数据检查</button>
                                    <button id="down_data" class="J_plan_excel btn btn-default btn-sm" style="font-size: 10px;margin-left: 10px;display:none"><i class="glyphicon glyphicon-save"></i>数据下载</button>
                                </div>
                                <input type="hidden" id="single_reg" value="">

                                <div class="data_check_show">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/regSelect.jsp" flush="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="mxgh">
                        <div class="col-md-2 left-panel" id="module_tree_container" style="background-color: #F4F5F9">
                            <div id="module_tree">
                                <ul id="moduleTree" class="ztree ztree-margin"></ul>
                            </div>
                        </div>
                        <div class="col-md-10" id="module_container">
                            <div class="panel panel-body" style="height: 70%;border-color: #dddddd">
                                <div>
                                    <div class="toolbar-left">
                                        <form class="form-inline J_search_form"
                                              action="${ctx}/zbdata/zsjhedit.htm?m=searchFind">
                                            <div class="form-group">
                                                <select id="querykey" class="form-control input-sm">
                                                    <option value="zs_cname"
                                                            <c:if test="${zs_code != '' && zs_code!= null}">selected</c:if>>
                                                        名称
                                                    </option>
                                                    <option value="zs_code"
                                                            <c:if test="${zs_cname != '' && zs_cname != null}">selected</c:if>>
                                                        编码
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <input id="moduleQuery" type="text" class="form-control input-sm"
                                                       placeholder="输入搜索内容" value="">
                                            </div>
                                            <div class="form-group">
                                                <button type="submit" class="btn btn-sm" style="background-color: #dddddd;"><span style="color: white">查询</span></button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="toolbar-right">
                                        <div class="toolbar-group" style="position: relative;">
                                            <button class="btn btn-default btn-sm J_Add_ZS" type="button"><i
                                                    class="glyphicon glyphicon-plus"></i>新增
                                            </button>
                                            &nbsp;
                                            <button class="btn btn-default btn-sm J_Add weight_set" type="button">
                                                <i class="weight-edit"></i>权重设置
                                            </button>
                                            &nbsp;
                                        </div>
                                    </div>
                                </div>
                                <div class="J_zsjh_module_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/modTableList.jsp" flush="true"/>
                                </div>
                            </div>

                        </div>
                    </div>
                    <!--Tab panes end-->
                </div>
            </div>
        </div>
        <div class="col-sm-offset-5 col-sm-7 savediv">
            <button type="button" class="btn btn-primary tosaveall">保存</button>
            <button type="reset" class="btn btn-primary resetindex">取消</button>
        </div>
    </div>
<div class="teset" style="padding-bottom: 10px"></div>
<div class="ict-footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>
</body>
<script>
    define("editjsp", function (require, exports, module) {
        var zbs = [];
        <c:forEach items="${zbs.zbchoose}" var="zb">
        zbs.push({
            code: "${zb.code}",
            zbcode: "${zb.zbcode}",
            zbname: "${zb.zbname}",
            dscode: "${zb.dscode}",
            dsname: "${zb.dsname}",
            cocode: "${zb.cocode}",
            coname: "${zb.coname}",
            unitcode: "${zb.unitcode}",
            unitname: "${zb.unitname}"
        })
        </c:forEach>
        module.exports = {
            zbs: zbs
        }
    })
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/main');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/zbAdd');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/module')
</script>

</html>

