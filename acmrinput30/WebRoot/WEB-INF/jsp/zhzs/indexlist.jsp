<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <div class="panel-heading">我的指数</div>
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
                        <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data" type="button">新增目录</button>&nbsp
                        <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data1" type="button">新增计划</button>&nbsp
                        <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data2" type="button">复制到</button>
                        <!-- 模态弹出窗内容 -->
                        <!-- 新增目录 -->
                            <%--<form class="J_add_catalogue"  action="${ctx}/zbdata/indexlist.htm">
                                <input name="m" value="insert" type="hidden">--%>
                            <div class="modal" id="mymodal-data" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <form class="J_add_catalogue"  action="${ctx}/zbdata/indexlist.htm?m=insert">
                                            <input type="hidden" class="form-control" name="ifdata" value="0" >
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h4 class="modal-title">新增/编辑目录</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="code" >
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="cname" >
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="cataname" value="" disabled>
                                                    <input type="hidden" class="form-control" name="idcata" value="" >
                                                    <ul id="treeCata" class="ztree select-tree hid-top"></ul>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                            <button type="submit" class="btn btn-primary">确定</button>
                                        </div>
                                        </form>
                                    </div>

                                </div>
                            </div>

                        <!-- 新增计划 -->
                        <div class="modal" id="mymodal-data1" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form class="J_add_plan"  action="${ctx}/zbdata/indexlist.htm?m=insert">
                                        <input type="hidden" class="form-control" name="ifdata" value="1" >
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h4 class="modal-title">新增计划</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="code" >
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="cname" >
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="planname" value="" disabled>
                                                    <input type="hidden" class="form-control" name="idplan"  value="">
                                                    <ul id="treePlan" class="ztree select-tree hid-top"></ul>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>统计周期：</label>
                                                <div class="col-sm-5">
                                                    <select class="form-control" name="sort" >
                                                        <option value="">请选择</option>
                                                        <option value="M">月度</option>
                                                        <option value="Q">季度</option>
                                                        <option value="Y">年度</option>
                                                    </select>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                            <button type="submit" class="btn btn-primary">确定</button>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </div>
                        <!-- 复制到 -->

                            <input type="hidden" class="form-control" name="copename" value="${name}">
                            <div class="modal" id="mymodal-data2" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <form class="J_add_cope" action="${ctx}/zbdata/addmenu.htm?m=insert">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指标名称：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="zname" value="请选择计划"  >
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属指数：</label>
                                                <div class="col-sm-5">
                                                    <input type="text" class="form-control" name="indexname" value="" disabled>
                                                    <input type="hidden" class="form-control" name="proid" value="" >
                                                    <ul id="treeZs" class="ztree select-tree hid-top"></ul>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>

                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                            <button type="submit" class="btn btn-primary">确定</button>
                                        </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

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
    define("listjsp",function (require,exports,module) {
        var indexlist=[];
        <c:forEach items="${indexlist}" var="index">
        if(${index.getProcode()!=null})
        indexlist.push({id:"${index.getCode()}",pId:"${index.getProcode()}",name:"${index.getCname()}",isPrent:!${index.getIfdata()},sou:!${index.getIfdata()},path:""})
        else indexlist.push({id:"${index.getCode()}",pId:"#1",name:"${index.getCname()}",isParent:!${index.getIfdata()},sou:!${index.getIfdata()},path:""})
        </c:forEach>
        module.exports={
            indexlist:indexlist
        }

    })
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
</script>
</html>