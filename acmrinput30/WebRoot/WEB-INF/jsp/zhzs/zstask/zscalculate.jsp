<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/13
  Time: 13:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="container-fluid" id="mainpanel">
    <input type="hidden" id="t_code" value="${taskcode}">
        <div class="panel panel-default">
            <div class="panel-body">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs nav-justified" role="tablist">
                        <li role="presentation" class="active"><a href="#qzsz" aria-controls="qzsz" role="tab" data-toggle="tab">权重设置</a></li>
                        <li role="presentation"><a href="#yssj" aria-controls="yssj" role="tab" data-toggle="tab">原始数据</a></li>
                        <li role="presentation"><a href="#jsjg" aria-controls="jsjg" role="tab" data-toggle="tab">计算结果</a></li>
                        <li role="presentation">
                            <button type="button" class="btn btn-default" id="recalculate">重新计算</button>
                            <button type="button" class="btn btn-default" id="resetpage">重置</button>
                            <button type="button" class="btn btn-default" id="save_calculate">保存并重新计算</button>
                            <button type="button" class="btn btn-default" id="goback">关闭</button>
                        </li>
                    </ul>
                </div>
                <!-- Tab panes -->
                <div class="tab-content row" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="qzsz">
                        <div class="panel panel-default">
                            <div class="panel-body J_zsjs_weight">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/tweighttable.jsp" flush="true"/>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="yssj">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <button type="button" class="btn btn-default" id="data_download">下载</button>
                                <input type="hidden" class="istmpdata" value="${istmp}">
                                <span class="btn btn-default fileinput-button"><span>上传数据</span><input type="file" name="file" id="data_upload"></span>
                                <span id="import_count" class="form-group" style="margin-right:10px;"></span>
                                <%--<button type="button" class="btn btn-default" id="data_upload">上传数据</button>--%>
                                <button type="button" class="btn btn-default" id="data_reload">重新读取数据</button>
                                <input type="hidden" class="reloaddata" value="${taskcode}">
                            </div>
                            <div class="panel-body J_zsjs_data">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/dataTable.jsp" flush="true"/>
                            </div>
                        </div>

                    </div>
                    <div role="tabpanel" class="tab-pane" id="jsjg">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <button type="button" class="btn btn-default" >下载数据</button>
                            </div>
                        <div class="calculate_result">
                            <jsp:include page="/WEB-INF/jsp/zhzs/zstask/tbdataresult.jsp" flush="true"/>
                        </div>
                    </div>
                    <!--Tab panes end-->
                </div>
            </div>
        </div>
</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/zstask/caculate');
    seajs.use('${ctx}/js/func/zhzs/zstask/datahandle');
</script>
</html>
