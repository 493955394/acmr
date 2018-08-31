<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/31
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
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
