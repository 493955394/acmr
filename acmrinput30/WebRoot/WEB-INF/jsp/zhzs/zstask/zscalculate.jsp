<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>指数任务计算</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style type="text/css">
        .glyphicon{
            color:#F39801;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="container-fluid" id="mainpanel">
    <input type="hidden" id="t_code" value="${taskcode}">
        <div>
            <div class="panel-body">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-pills" role="tablist" id="myTabs">
                        <li role="presentation" class="active"><a href="#qzsz" aria-controls="qzsz" role="tab" data-toggle="tab">权重设置</a></li>
                        <li role="presentation"><a href="#yssj" aria-controls="yssj" role="tab" data-toggle="tab">原始数据</a></li>
                        <li role="presentation"><a href="#jsjg" aria-controls="jsjg" role="tab" data-toggle="tab">计算结果</a></li>
                        <li role="presentation" style="float: right">
                            <c:if test="${right!='0'}">
                                <button type="button" class="btn btn-default btn-sm" id="recalculate"><i class="glyphicon glyphicon-retweet"></i>&nbsp;重新计算</button>
                                <button type="button" class="btn btn-default btn-sm" id="resetpage"><i class="glyphicon glyphicon-refresh"></i>&nbsp;重置</button>
                                <button type="button" class="btn btn-default btn-sm" id="save_calculate"><i class="glyphicon glyphicon-floppy-saved"></i>&nbsp;保存并重新计算</button>
                            </c:if>
                            <button type="button" class="btn btn-default btn-sm" id="goback" data-right="${right}"><i class="glyphicon glyphicon-remove"></i>&nbsp;关闭</button>
                        </li>
                    </ul>
                </div>
                <!-- Tab panes -->
                <div class="tab-content row" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="qzsz">
                        <div>
                            <div class="panel-body J_zsjs_weight">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/tweighttable.jsp" flush="true"/>
                            </div>
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="yssj">
                        <div>
                            <div style="float: right;margin-right: 13px";>
                                <button type="button" class="btn btn-default btn-sm" id="data_download"><i class="glyphicon glyphicon-download"></i>下载</button>
                                <input type="hidden" class="istmpdata" value="${istmp}">
                                <c:if test="${right!='0'}">
                                    <span class="btn btn-default btn-sm fileinput-button"><span><i class="glyphicon glyphicon-upload"></i>上传数据</span><input type="file" name="file" id="data_upload"></span>
                                    <span id="import_count" class="form-group"></span>
                                    <%--<button type="button" class="btn btn-default" id="data_upload">上传数据</button>--%>
                                    <button type="button" class="btn btn-default btn-sm" id="data_reload"><i class="glyphicon glyphicon-folder-open"></i>&nbsp;重新读取数据</button>
                                </c:if>
                                <input type="hidden" class="reloaddata" value="${taskcode}">

                            </div>
                            <div class="J_zsjs_data">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/dataTable.jsp" flush="true"/>
                            </div>
                        </div>

                    </div>
                    <div role="tabpanel" class="tab-pane" id="jsjg">
                        <div>
                            <div style="float: right;margin-right: 13px">
                                <button class="btn btn-default btn-sm" id="result_download"><i class="glyphicon glyphicon-download"></i>下载数据</button>
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
</div>
</body>
<script>
    seajs.use('${ctx}/js/func/zhzs/zstask/caculate');
    seajs.use('${ctx}/js/func/zhzs/zstask/datahandle');
</script>
</html>
