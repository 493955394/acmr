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
    <title>${projectTitle}-查看往期</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
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
                <div class="toolbar-left">
                    <form class="form-inline J_search_form" action="${ctx}/system/">
                        <div class="form-group">
                            <span>地区选择：</span>
                            <select  class="form-control input-sm">
                                <option value="">地区序列</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="toolbar-right">
                    <div class="toolbar-group" style="position: relative;">
                        <button class="btn btn-default "data-toggle="modal" data-target=".wdturn-modal" >维度转换</button>
                        <button class="btn btn-default ">数据下载</button>
                        <form class="form-inline J_search_form" action="${ctx}/system/">
                        <div class="form-group">
                            <span>时间：</span>
                            <select  class="form-control input-sm">
                                <option value="">最近五年</option>
                            </select>
                        </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>



        <div class="J_pastviews_data_table">
            <jsp:include page="/WEB-INF/jsp/zhzs/zstask/pasttable.jsp" flush="true"/>
        </div>
    </div>
    <div class="modal wdturn-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="btn-group" role="group" aria-label="..." style="padding-left: 20%">
                    <button type="button" class="btn btn-default top-select" id="top-reg">地区</button>
                    <button type="button" class="btn btn-default top-select" id="top-zb">指标</button>
                    <button type="button" class="btn btn-default top-select" id="top-sj">时间</button>
                </div>
                <div>
                    <div class="btn-group-vertical" role="group" aria-label="...">
                        <button type="button" class="btn btn-default left-select" id="left-reg">地区</button>
                        <button type="button" class="btn btn-default left-select" id="left-zb">指标</button>
                        <button type="button" class="btn btn-default left-select" id="left-sj">时间</button>
                    </div>
                    <table class="table table-striped table-hover wdturn-table" style="display: inline">

                    </table>
                </div>
                <input type="hidden" id="table-Row" value="">
                <input type="hidden" id="table-Col" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="wd-change">确认</button>
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
