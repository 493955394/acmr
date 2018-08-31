<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/31
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        </div>
    </div>
</div>
