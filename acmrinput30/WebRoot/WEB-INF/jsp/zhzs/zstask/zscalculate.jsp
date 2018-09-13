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
        <div class="panel panel-default">
            <div class="panel-body">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs nav-justified" role="tablist">
                        <li role="presentation" class="active"><a href="#qzsz" aria-controls="qzsz" role="tab" data-toggle="tab">权重设置</a></li>
                        <li role="presentation"><a href="#yssj" aria-controls="yssj" role="tab" data-toggle="tab">原始数据</a></li>
                        <li role="presentation"><a href="#jsjg" aria-controls="jsjg" role="tab" data-toggle="tab">计算结果</a></li>
                        <li role="presentation">
                            <button type="button" class="btn btn-default">重新计算</button>
                            <button type="button" class="btn btn-default">重置</button>
                            <button type="button" class="btn btn-default">保存并重新计算</button>
                            <button type="button" class="btn btn-default">关闭</button>
                        </li>
                    </ul>
                </div>
                <!-- Tab panes -->
                <div class="tab-content row" style="padding-top: 20px;">
                    <div role="tabpanel" class="tab-pane active" id="qzsz">
                        <div>
                            qszs
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="yssj">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <button type="button" class="btn btn-default" id="data_download">下载</button>
                                <button type="button" class="btn btn-default" id="data_upload">上传数据</button>
                                <button type="button" class="btn btn-default" id="data_reload">重新读取数据</button>
                            </div>
                            <div class="panel-body J_zsjs_data">
                                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/dataTable.jsp" flush="true"/>
                            </div>
                        </div>

                    </div>
                    <div role="tabpanel" class="tab-pane" id="jsjg">
                        <div>
                            jsjg
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
</script>
</html>
