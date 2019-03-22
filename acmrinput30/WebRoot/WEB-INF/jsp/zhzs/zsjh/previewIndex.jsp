<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/pastreview.css" />
<html>
<head>
    <title>${projectTitle}-预览结果</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style>
        table tr th, table tr td { border:1px solid #cecfdf; }
        #preview-table td,th{
            text-align: center;
            vertical-align: middle;
        }
        .red{background-color: #e4edf6}
    </style>
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div id="ict-loading-box search_ing">
    <div class="ict-loading-box search_ing">
        <img src="${ctx}/images/ict_loading.svg" /> 查询中
    </div>
</div>
<div class="col-xs-12">
    <div>
        <input type="hidden" value="${icode}" id="preview-code">
        <div class="panel panel-default">
            <div class="panel-heading">
                预览结果
            </div>
        </div>
        <input type="hidden" id="timecode" value="${times}">
        <input type="hidden" id="scheme_codes" value="${scodes}">
        <div class="col-sm-7">
            <div class="modselect" style="padding-right: 20px;">
                <select id="ms">
                    <c:forEach items="${mods}" var="module">
                        <option value="${module.getCode()}">${module.getCname()}</option>
                    </c:forEach>
                </select>
                <span id="mod_text"></span>
            </div>

            <div class="J_preview_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/previewTable.jsp" flush="true"/>
            </div>
        </div>
        <div class="col-sm-5">
            <div class="zbselect" style="padding-right: 20px;">
                <select  id="zblist">
                    <c:forEach items="${zblist.zbchoose}" var="zbl">
                        <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="J_zb_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/zsjh/previewzbTable.jsp" flush="true"/>
            </div>

        </div>
    </div>
</div>
</body>
</html>
<div class="ict-footer footer fixed-footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>
<script type="text/javascript">
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/preview');
</script>