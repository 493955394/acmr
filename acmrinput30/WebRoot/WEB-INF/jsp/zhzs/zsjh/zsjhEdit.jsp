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
                    <ul class="nav nav-tabs nav-justified">
                        <li role="presentation" class="active" id="de_A" name="de"><a href="#" ><span id='jbxx'>基本信息</span></a></li>
                        <li role="presentation" id="de_B" name="de"><a href="#"><span id='zbsx'>指数筛选</span></a></li>
                        <li role="presentation" id="de_C" name="de"><a href="#"><span id='jsfw'>计算范围</span></a></li>
                        <li role="presentation" id="de_D" name="de"><a href="#"><span id='mxgh'>模型规划</span></a></li>
                    </ul>
                </div>
            </div>
            <p></p>
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
                        <select class="form-control" name="tsort">
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
                        <select class="form-control" name="tsort" disabled>
                            <option value="M"  <c:if test="${timesort == 'M'}"></c:if>月度</option>
                            <option value="Q"  <c:if test="${timesort == 'Q'}"></c:if>季度</option>
                            <option value="Y"  <c:if test="${timesort == 'Y'}"></c:if>年度</option>
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
                        <input id="delayday" name="delayday" value=""  class="form-control"/>
                    </div>
                    <div>
                        <label class="control-label ">自然日</label>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"> 指数个数位数：</label>
                    <div class="col-sm-5">
                        <input id="" name="cname" value="" class="form-control"/>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </form>
        </div>

    </div>

    <button name="btn_change" onclick="onBack()" class="btn btn-danger btn-xs"><i class="glyphicon glyphicon-remove-circle"></i>退回</button>

    <!-- 模态框（Modal) -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="form-horizontal" id="aform" action="" method="post">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="createModalLabel">退回原因</h4>
                    </div>
                    <div class="form-group form-group-sm">
                        <label class="col-sm-3 control-label talign-right fz13">退回理由</label>
                        <div class="col-sm-7">
                            <textarea id="back_reason" name="back_reason" class="form-control" rows="5" required></textarea>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary" onclick="subFeedback()">确定</button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
</script>
<script type="text/javascript">
    $(function(){
            $("li[name='de']").removeClass("active");
            $("#de_"+de).addClass("active");
        });
    function onBack(id){
        var modal = $("#editModal");
        modal.find("input[name='modid']").val(0);
        modal.modal();
    }
</script>
</html>

