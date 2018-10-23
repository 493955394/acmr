<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.acmr.model.security.User" %>
<%@page import="com.acmr.model.security.Menu"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
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
<style>
    .fr{
        display: inline-block;
        float: right;
    }
</style>
<html>
<head>
    <title>${projectTitle}-指数计划</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />

<div class="container-fluid" id="mainpanel">
    <div class="col-md-2 left-panel">
        <%--<div class="panel tree-panel">
            <div class="panel-heading">我的指数</div>
        </div>--%>
        <ul id="treeDemo" class="ztree ztree-margin"></ul>
    </div>
    <div class="col-md-10 right-panel">
        <div class="panel panel-default">
            <div class="J_zsjh_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/indextable.jsp" flush="true"/>
            </div>
        </div>
    </div>
    <!-- 新增目录 -->
    <div class="modal" id="mymodal-data" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form class="form-horizontal J_add_catalogue"  action="${ctx}/zbdata/indexlist.htm?m=insertcate">
                            <input type="hidden" class="form-control" name="ifdata" value="0" >
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">新增/编辑目录</h4>
                            </div>
                            <div class="modal-body">

                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="cocode" >
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="cocname" >
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="cataname" value="" disabled>
                                        <input type="hidden" class="form-control" name="idcata" value="" >
                                       <%-- <input type="hidden" class="form-control" name="getoldplan"  value="">--%>
                                        <ul id="treeCata" class="ztree select-tree hid-top"></ul>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">确定</button>
                                <button type="reset" class="btn btn-default resetcata" data-dismiss="modal">取消</button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
    <!-- 新增计划 -->
    <div class="modal" id="mymodal-data1" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form class="form-horizontal J_add_plan"  action="${ctx}/zbdata/indexlist.htm?m=insertplan">
                            <input type="hidden" class="form-control" name="ifdata" value="1" >
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">新增计划</h4>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="plancode">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="plancname">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="planname" value="" disabled>
                                        <input type="hidden" class="form-control" name="idplan"  value="">
                                        <%--<input type="hidden" class="form-control" name="getoldplan"  value="">--%>
                                        <ul id="treePlan" class="ztree select-tree hid-top"></ul>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>统计周期：</label>
                                    <div class="col-sm-5">
                                        <select class="form-control" name="sort" id="sort">
                                            <option value="">请选择</option>
                                            <option value="m">月度</option>
                                            <option value="q">季度</option>
                                            <option value="y">年度</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">确定</button>
                                <button type="reset" class="btn btn-default resetplan" data-dismiss="modal">取消</button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
    <!-- 复制到 -->
    <div class="modal" id="mymodal-data2" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form class="form-horizontal J_add_cope" action="${ctx}/zbdata/indexlist.htm?m=copy">
                            <input type="hidden" class="form-control" name="copycode">
                            <input type="hidden" class="form-control" name="cifdata">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指标名称：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="zname"  >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指标代码：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="plcode">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属指数：</label>
                                    <div class="col-sm-5">
                                        <input type="text" class="form-control" name="indexname" value="" disabled>
                                        <input type="hidden" class="form-control" name="newprocode" value="" >
                                        <ul id="treeZs" class="ztree select-tree hid-top"></ul>
                                    </div>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" >确定</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <%--<input type='button'  name="plancode" value='复制到' onclick="show()"/>--%>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
    <!-- 收到的指数 复制到 -->
    <div class="modal" id="mymodal-data4" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form class="form-horizontal J_share_addcope" action="${ctx}/zbdata/indexplan.htm?m=sharecopy">
                    <input type="hidden" class="form-control" name="cosharecode">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指数编码：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="putcode">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指数名称：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="putname"  >
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属指数：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="sharename" value="" disabled>
                                <input type="hidden" class="form-control" name="shareprocode" value="" >
                                <ul id="treeShareZs" class="ztree select-tree hid-top"></ul>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary" >确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- 目录的编辑 -->
    <div class="modal" id="mymodal-data3" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form  class="form-horizontal J_add_edit" action="${ctx}/zbdata/indexlist.htm?m=editCate">
                  <%--  <input type="hidden" class="form-control" name="oldcode" value="">--%>

                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title">编辑目录</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editcode" value="" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editcname" value="">
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editname" value="" disabled>
                                <input type="hidden" class="form-control" name="editprocode"  value="">
                               <%-- <input type="hidden" class="form-control" name="oldeditpro"  value="">
                                <input type="hidden" class="form-control" name="oldproname" value="">--%>
                                <ul id="treeEditc" class="ztree select-tree hid-top"></ul>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary" name="plancode" >确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <%--<input type='button'  name="plancode" value='复制到' onclick="show()"/>--%>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--权限管理-->
    <div class="modal" id="mymodal-right" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">权限管理</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal J_rightlist_form" action="${ctx}/zbdata/indexlist.htm?m=rightListSave">
                        <input type="hidden" class="currentUser" value="<%=user.getUserid()%>">
                        <input type="hidden" id="currentIcode" value="">
                        <div class="form-group">
                            <div class="col-sm-5  panel panel-default">
                                <div class="panel-body">
                                    <div class="form-group">
                                        <input type="text" id="select-input" class="col-xs-8">
                                        <div class="btn right-select" type="button" style="margin-left: 3px;background-color: #F39801;height: 29px"><span style="font-size: 10px;color: white">搜索</span></div>
                                    </div>
                                        <div id="selectList" class="list-group" ></div>
                                    <ul id="treeRight" class="ztree"></ul>
                                </div>
                            </div>
                            <div class="col-sm-2" style="text-align: center">
                                <div class="btn-group-vertical" role="group">
                                    <div class="clearfix" style="height: 30px;margin-top: 30px"></div>
                                    <div class="right-add" style="border-style:solid;border-width:1px;border-color:#F39801;height: 40px;width: 70px">
                                        <span style="color: #F39801;text-align: center;font-size: 20px;margin-left: 5px">></span>
                                    </div>
                                    <div class="clearfix" style="height: 30px"></div>
                                    <div class="remove-alllist" id="delsiggle" style="border-style:solid;border-width:1px;border-color:#F39801;background-color: #F39801;height: 40px;width: 70px">
                                        <span style="color: white;text-align: center;font-size: 20px;margin-left: 5px"><<</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-5 panel panel-default">
                                <div class="toolbar">
                                    <div class="toolbar-left col-md-offset-1 col-md-4">
                                        <span style="text-align: center">组织/用户</span>
                                    </div>
                                    <div class="toolbar-right col-md-4">
                                        <span style="text-align: center">权限</span>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <ul class="list-group right-list ">
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">确定</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
    seajs.use('${ctx}/js/func/zhzs/indexlist/planEdit');
    seajs.use('${ctx}/js/func/zhzs/indexlist/right');
    /**
     * 权限管理删除单个按钮
     */
    function delRemove(obj){
        obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
    }
</script>
</html>

