<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
    <title>-查看往期</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style>
        table tr th, table tr td { border:1px solid #cecfdf; }
    </style>
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="col-xs-12">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1>查看往期</h1>
        </div>

        <div class="panel-body">
            <div class="toolbar">
                <c:if test="${show.equals('2')}">
                <div class="toolbar-left">
                    <form class="form-inline J_search_form" action="${ctx}/zbdata/pastviews.htm?m=regDatas">
                        <div class="form-group">
                            <span>地区选择：</span>
                            <select  class="form-control input-sm">
                                <c:forEach items="${regcode}" var="regcode">
                                    <c:forEach items="${regnames}" var="reg">
                                        <option value="${regcode}">${reg}</option>
                                    </c:forEach>
                                </c:forEach>
                                <option value="1">地区展示序列</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="toolbar-right">
                    <div class="toolbar-group" style="position: relative;">
                        <button class="btn btn-default "data-toggle="modal" data-target=".wdturn-modal" >维度转换</button>
                        <button class="btn btn-default pastview_download">数据下载</button>
                        <form class="form-inline J_search_form" action="${ctx}/zbdata/pastviews.htm?m=regDatas">
                            <div class="form-group">
                                <span>时间：</span>
                                <select  class="form-control input-sm">
                                    <option value="">最近五年</option>
                                </select>
                            </div>
                        </form>

                    </div>
                </div>
                </c:if>
                <c:if test="${show.equals('1')}">
                    <div class="toolbar-left">
                        <form class="form-inline J_search_form" action="${ctx}/zbdata/pastviews.htm?m=modDatas">
                            <div class="form-group">
                                <span>指标选择：</span>
                                <select  class="form-control input-sm">
                                    <c:forEach items="${orcodes}" var="orcode">
                                        <c:forEach items="${ornames}" var="modname">
                                            <option value="${orcode}">${modname}</option>
                                        </c:forEach>
                                    </c:forEach>
                                    <option value="2">指标展示序列</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="toolbar-right">
                        <div class="toolbar-group" style="position: relative;">
                            <button class="btn btn-default "data-toggle="modal" data-target=".wdturn-modal" >维度转换</button>
                            <button class="btn btn-default pastview_download">数据下载</button>
                            <form class="form-inline J_search_form" action="${ctx}/zbdata/pastviews.htm?m=modDatas">
                                <div class="form-group">
                                    <span>时间：</span>
                                    <select  class="form-control input-sm">
                                        <option value="">最近五年</option>
                                    </select>
                                </div>
                            </form>

                        </div>
                    </div>
                </c:if>
            </div>
        </div>





        <div class="J_pastviews_data_table">
            <jsp:include page="/WEB-INF/jsp/zhzs/zstask/pasttable.jsp" flush="true"/>
        </div>
    </div>
    <div class="modal wdturn-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">维度转换</h4>
                </div>
                <div class="col-md-offset-4" role="group" aria-label="..." >
                    <button type="button" style="border-color: #FF7F19" class="btn btn-default top-select" id="top-reg"><span style="color: #FF7F19">地区</span></button>
                    <button type="button" style="border-color: #FF7F19" class="btn btn-default top-select" id="top-zb"><span style="color: #FF7F19">指标</span></button>
                    <button type="button" style="border-color: #FF7F19" class="btn btn-default top-select" id="top-sj"><span style="color: #FF7F19">时间</span></button>
                </div>
                <div style="margin-bottom: 5%;">
                    <div class="btn-group-vertical  col-md-offset-3" role="group" aria-label="...">
                        <button type="button" style="border-color: #FF7F19" class="btn btn-default left-select" id="left-reg"><span style="color: #FF7F19">地区</span></button>
                        <div class="clearfix" style="height: 5px"></div>
                        <button type="button" style="border-color: #FF7F19" class="btn btn-default left-select" id="left-zb"><span style="color: #FF7F19">指标</span></button>
                        <div class="clearfix"  style="height: 5px"></div>
                        <button type="button" style="border-color: #FF7F19" class="btn btn-default left-select" id="left-sj"><span style="color: #FF7F19">时间</span></button>
                    </div>
                    <table class="table table-hover wdturn-table" style="display: inline-block;position: absolute;margin-left: 16px;margin-top: 5px">

                    </table>
                </div>
                <input type="hidden" id="table-Row" value="">
                <input type="hidden" id="table-Col" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="wd-change">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>

            </div>
        </div>
    </div>
</div>
<div class="common-tips"></div>
<script type="text/javascript">
    seajs.use('${ctx}/js/func/zhzs/zstask/pastviews')
    seajs.use('${ctx}/js/func/zhzs/zstask/wdturn')
</script>
</body>
</html>
