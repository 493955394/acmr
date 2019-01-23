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
    <link rel="stylesheet" type="text/css" href="${ctx}/css/pastreview.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/css/zhzsTreeStyle.css" />
    <style type="text/css">
        span.glyphicon{
            color: red;
        }
        /*.glyphicon {
            color: #F39801;
        }*/
        .tree-panel{
            margin: 0px;
        }
        #module_tree_container{
            height: 100%;
        }
        #btn-fullscreen{
            cursor: pointer;
        }
        .fullscreen{
            position: fixed !important;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: white;
            padding-bottom: 0;
        }
        .rightfull{
            height: 100% !important;
        }
        .leftfull{
            height: 100% !important;
        }
        .container_full{
            height: 90% !important;
        }
        .savedivfull{
            display:none;
        }
        .add-or-del{
            border:1px solid white;padding-top: 50px;
        }
        .add-padding{
            padding-top: 200px;
        }
        #set_scheme_weight_formula{
            float: right;
            margin: 5px;
        }
        #add_scheme{
            float: right;
            margin: 5px;
        }
        #scheme_time_select{
            float: right;
            margin: 5px;
        }

    </style>

    <script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/lib/dropList.js"></script>
</head>
<body>
<div id="container">
<div class="ict-header">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true"/>
</div>
<div class="ict-page">
    <div class="container-fluid">
        <div class="panel panel-default reset-panel" style="padding-bottom: 10px;">
            <div class="panel-heading" id="ict-header">
                [${list.getCname()}]指数计划编辑,类型：
                <c:if test="${list.getSort() == 'm'}">月度</c:if>
                <c:if test="${list.getSort() == 'y'}">年度</c:if>
                <c:if test="${list.getSort() == 'q'}">季度</c:if>
            </div>
            <div class="panel-body panel-height" id="edit_container" style="padding-left: 0;padding-right: 0;">

                <button type="reset" class="btn btn-primary resetindex btn-sm" style="float: right;margin-left:10px;">返回</button>
                <button type="button" class="btn btn-primary tosaveall btn-sm" style="float: right;margin-left:10px;">保存</button>
                <span id="btn-fullscreen" class="btn-fullscreen" style="float: right;padding:10px;margin-left:10px;margin-top:5px;"></span>
                <div id="top_div">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist" id="bjjhTab">
                        <li  class="active"><a href="#zssx" aria-controls="zssx" role="tab" data-toggle="tab">指标初选</a></li>
                        <li ><a href="#dqcx" aria-controls="dqcx" role="tab" data-toggle="tab">地区初选</a></li>
                        <li ><a href="#jsfw" aria-controls="jsfw" role="tab" data-toggle="tab">计算范围</a></li>
                        <li ><a href="#mxgh" aria-controls="mxgh" role="tab" data-toggle="tab">模型构建</a></li>
                        <li ><a href="#jsfa" aria-controls="jsfa" role="tab" data-toggle="tab">计算方案</a></li>
                        <li ><a href="#jbxx" aria-controls="jbxx" role="tab" data-toggle="tab">项目信息</a></li>
                    </ul>
                </div>
                <!-- Tab panes -->
                <div class="col-xs-12 tab-content row" style="padding-top: 10px;padding-right: 0;overflow: auto;">
                    <div role="tabpanel" class="edit_tab tab-pane" id="jbxx">
                        <form class="form-horizontal" id="indexForm" action="" method="post">
                            <div class="form-group" style="display: none">
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
                                <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指数时间类型：</label>
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
                                <label class="col-sm-2 control-label "><span class="glyphicon glyphicon-asterisk required_ico"></span>更新周期：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <input id="delayday" name="delayday" value="<c:if test="${list.getDelayday()==''}">30</c:if><c:if test="${list.getDelayday()!=''}">${list.getDelayday()}</c:if>"
                                               class="form-control"/>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <input id="delayday" name="delayday" value="<c:if test="${list.getDelayday()==''}">30</c:if><c:if test="${list.getDelayday()!=''}">${list.getDelayday()}</c:if>"
                                               class="form-control" readonly/>
                                    </c:if>
                                </div>
                                <label class="control-label">自然日</label>
                                <span class="normalday"></span>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">备注：</label>
                                <div class="col-sm-5">
                                    <c:if test="${right!='1'}">
                                        <textarea class="form-control " name="remark">${list.getRemark()}</textarea>
                                    </c:if>
                                    <c:if test="${right=='1'}">
                                        <textarea class="form-control" name="remark" readonly>${list.getRemark()}</textarea>
                                    </c:if>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <input type="reset" style="display:none;"/>
                        </form>
                    </div>
                    <div role="tabpanel" class="edit_tab tab-pane active" id="zssx">
                        <div id="tree_and_find" class="col-md-2  left-panel" style="overflow: auto;min-height: inherit">
                            <form class="form-inline J_search_form" onkeydown="if(event.keyCode==13){return false;}">

                                <div class="form-group" style="width: 60%;">
                                    <input id="queryValue" type="text" class="form-control input-xm" placeholder="搜索内容" style="width: 100%;" autocomplete="off">
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-primary btn-sm btn_search">搜索</button>
                                </div>
                            </form>

                            <ul id="treeDemo1" class="ztree ztree-margin"></ul>
                            <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/zbtree.jsp" flush="true"/>--%>
                        </div>
                        <div class="col-md-10 zssx-right" style="overflow: auto;border: 1px solid #dddddd">
                            <div class="col-md-9 col-sm-9">
                                <div>
                                    <div class="panel panel-default">
                                        <div class="panel_zbname panel-heading" code="">请选择指标</div>
                                        <div class="ds_choose panel-heading" style="height: 60px">
                                            <div class="col-md-4 col-sm-4">
                                                <span style="font-size: 14px;">数据来源：</span>
                                                <select id="ds_select" class="input-sm zb_select" style="max-width: 150px;overflow: scroll;width: 40%">
                                                    <option>请选择</option>
                                                    <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                    <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                </select>
                                            </div>
                                            <div class="col-md-4 col-sm-4">
                                                <span style="font-size: 14px">主体：</span>
                                                <select id="co_select" class="input-sm zb_select" style="max-width: 150px;overflow: scroll;width: 60%">
                                                    <option>请选择</option>

                                                    <%--                                <option value="code" <c:if test="${code != '' && code!= null}">selected</c:if>>地区代码</option>
                                                                                        <option value="cname" <c:if test="${cname != '' && cname != null}">selected</c:if>>地区名称</option>--%>
                                                </select>
                                            </div>
                                            <div class="col-md-4 col-sm-4">
                                                <span style="font-size: 14px">单位：</span>
                                                <select id="unit_select" class="input-sm zb_select" style="max-width: 150px;overflow: scroll;width: 60%">
                                                    <option>请选择</option>
                                                </select>

                                            </div>

                                        </div>
                                        <input  type="hidden" id="zbtimeinput"/>
                                        <input type="hidden" id="timecode" value="last5">
                                        <div id="mySelectTime" style="margin-top: 10px;margin-bottom: 10px;float: left"></div>
                                        <button type="button" class="btn btn-default btn-sm zb_add"
                                                style="display: none;float: right;margin-top: 10px;margin-bottom: 10px"><i
                                                class="glyphicon glyphicon-plus"></i><span
                                                class="word">&nbsp;添加指标</span></button>
                                        <%--    <button type="button" class="btn btn-default" style="display: none;float: right"><i class="glyphicon glyphicon-plus"></i><span class="word">&nbsp;添加指标</span></button>--%>

                                    </div>

                                    <%-- <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/addZB.jsp" flush="true"/>--%>
                                </div>

                                <div class="data_table J_zsjh_zbdata_table" style="height: 72%;width: 100%;"  id="zb_data_table">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/ZBdataList.jsp" flush="true"/>
                                </div>
                            </div>
                            <div class="col-md-3 col-sm-3 right-panel">
                                <div class="panel_container" style="border: 1px solid #dddddd;width: 100%;overflow:auto;">
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
                    <div role="tabpanel" class="edit_tab tab-pane" id="dqcx">
                        <div class="jsfw_col col-xs-2 col-xs-offset-3">
                            <input type="hidden" id="initTreePara" value="${initTreePara}"/>
                            <input type="hidden" id="procode" value=""/>
                            <div class="panel tree-panel" >
                                <div class="panel-heading" style="text-align:center">地区树</div>
                            </div>
                            <div class="panel-body col-xs-12" id="dqs" style="overflow:auto;background-color: #F4F5F9">
                                <form class="form-inline R_search_form" onkeydown="if(event.keyCode==13){return false;}">

                                    <div class="form-group" style="width: 60%;">
                                        <input id="regQueryData" type="text" class="form-control input-xm" placeholder="搜索内容" style="width: 100%;" autocomplete="off">
                                    </div>
                                    <div class="form-group">
                                        <button type="button" class="btn btn-primary btn-sm reg_search">搜索</button>
                                    </div>
                                </form>
                                <ul id="treeDemo" class="ztree ztree-margin">
                                </ul>
                            </div>
                            <input type="hidden" name="regcode" value=""/>
                            <input type="hidden" name="regname" value=""/>
                        </div>
                        <div class="jsfw_col col-xs-1 col-md-1 col-sm-1 btn-group-vertical add-or-del" role="group" >
                            <div id="sigglechoose" class="col-md-offset-3 col-md-4">
                                <span>></span>
                            </div>

                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="chooseall"  class="col-md-offset-3 col-md-4">
                                <span>>></span>
                            </div>
                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="delsiggle"  class="col-md-offset-3 col-md-4">
                                <span><</span>
                            </div>
                            <div class="clearfix" style="height: 30px;margin-top: 20px"></div>
                            <div id="delall" class="col-md-offset-3 col-md-4">
                                <span><<</span></div>
                        </div>
                        <div class="jsfw_col col-xs-2">
                            <div class="panel tree-panel">
                                <div class="panel-heading regs-title" style="text-align:center">地区列表</div>
                            </div>
                            <div id="dqlb" style="width:100%;overflow:auto;border: 1px solid #dddddd">
                                <ul class="list-group regul" id="selectreg">
                                    <c:forEach items="${regs}" var="reg">
                                        <li class="list-group-item selectedli" id="${reg.regcode}">${reg.regcname}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="edit_tab tab-pane" id="jsfw">
                        <div class="col-xs-12">
                            <div style="height: 6%;">
                                <input  type="hidden" id="fwtimeinput"/>
                                <input type="hidden" id="timeval" value="last3">
                                <div id="mySelectTime1" style="margin-top: 10px;margin-bottom: 10px;float: left"></div>
                                <div style="float: right">计划起始时间：
                                    <input id="startpeirod" name="startpeirod"/>
                                    <button type="button" class="btn btn-primary btn-sm" id="rangeConfirm">范围确认</button>
                                    <button type="button" class="btn btn-primary btn-sm J_plan_excel">数据下载</button>
                                </div>
                            </div>

                            <div class="J_zsjh_rangedata_table" id="range_data_table">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/rangeDataTable.jsp" flush="true"/>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="edit_tab tab-pane" id="mxgh">
                        <div class="module_tab_container col-md-2" id="module_tree_container" style="background-color: #F4F5F9;overflow: auto;">
                            <div id="module_tree">
                                <ul id="moduleTree" class="ztree ztree-margin"></ul>
                            </div>
                        </div>
                        <div class="module_tab_container col-md-10" id="module_container">
                            <div class="panel panel-body" id="module_tree_container_body" style="border-color: #dddddd">
                                <div>
                                    <div class="toolbar-left" style="margin-bottom: 0px">
                                        <form class="form-inline J_search_form" action="${ctx}/zbdata/zsjhedit.htm?m=searchFind">
                                            <div class="form-group">
                                                    <div class="form-group">
                                                            <span id="querykey" class="form-control input-sm">
                                                                名称
                                                            </span>
                                                    </div>

                                            </div>
                                            <div class="form-group">
                                                <input id="moduleQuery" type="text" class="form-control input-sm"
                                                       placeholder="输入搜索内容" value="">
                                            </div>
                                            <div class="form-group">
                                                <button type="submit" class="btn btn-sm" style="background-color: #ff7f19;"><span style="color: white">查询</span></button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="toolbar-right">
                                        <div class="toolbar-group" style="position: relative;">
                                            <button class="btn btn-default btn-sm J_Add_ZS" type="button"><i
                                                    class="glyphicon glyphicon-plus"></i>新增节点
                                            </button>
                                            &nbsp;
                                            <button class="btn btn-default btn-sm J_Add weight_set" type="button">
                                                <i class="weight-edit"></i>权重设置
                                            </button>
                                            &nbsp;
                                        </div>
                                    </div>
                                </div>
                                <div class="J_zsjh_module_table"  style="width: 100%;overflow: auto">
                                    <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/modTableList.jsp" flush="true"/>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div role="tabpanel" class="edit_tab tab-pane" id="jsfa">
                        <div id="scheme_main_container">
                            <div id="scheme_select_button">
                                <button class="btn btn-default btn-sm" id="set_scheme_weight_formula">公式/权重设置</button>
                                <button class="btn btn-default btn-sm" id="add_scheme">新增方案</button>
                                <input  type="hidden" id="scheme_timeinput"/>
                                <input type="hidden" id="scheme_timeval" value="">
                                <div id="scheme_time_select" style="margin-bottom: 10px;"></div>
                            </div>
                            <div class="J_zsjh_scheme_table"  style="width: 100%;overflow: auto">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/schemeTable.jsp" flush="true"/>
                            </div>
                        </div>
                    </div>
                    <!--Tab panes end-->
                </div>
            </div>
        </div>
        <div class="col-sm-12 savediv" style="text-align:center;padding: 10px 0;">

        </div>
    </div>
</div>
    <jsp:include page="/WEB-INF/jsp/common/footer.jsp" flush="true" />
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
    });
    //查询的时间搜索框
    $(function(){
        var json1 = {
            wdcode:'sj',
            wdname:'预览时间',
            nodes:[
                {code:"last3",name:'最近三期'}
            ]
        };
        var json2 = {
            wdcode:'sj',
            wdname:'时间',
            nodes:[
                {code:"last5",name:'最近五期'}
            ]
        };
        var json3 = {
            wdcode:'sj',
            wdname:'预览结果时间选择',
            nodes:[
                {code:null,name:'请选择'},
                {code:"last3",name:'最近三期'}
            ]
        };
        var dt1 = $('#mySelectTime1');
        var dt2 = $('#mySelectTime');
        var dt3 = $('#scheme_time_select');
        dt1.dropList(json1,{isText:true},function(o){     //指标初选事件处理
            $("#timeval").val(o.getItem().code)
            $("#fwtimeinput").click();
        });
        dt2.dropList(json2,{isText:true},function(o){     //地区初选事件处理
            $("#timecode").val(o.getItem().code)
            $("#zbtimeinput").click();
        });
        dt3.dropList(json3,{isText:true},function(o){     //方案事件处理
            if(o.getItem().code != null){
                $("#scheme_timeval").val(o.getItem().code)
                $("#scheme_timeinput").click();
            }
        });
    });
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/main');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/zbAdd');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/module');
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/scheme');
</script>
</html>

