<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${projectTitle}-用户管理-审批流程</title>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/editTemplate/css/semantic.min.css">
<style type="text/css">
.ui.dropdown label{
    margin-bottom: 0!important;
    font-weight: normal;
}
.option-stream{
    display: none;
}
</style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="container-fluid J_main_container">
    <form class="form-horizontal J_add_form" action="${ctx}/person.htm?m=updateFlow">
		<input type="hidden" name="code" value="${code}">
		<input type="hidden" id="iflow" value='${flowContent}'>
            <div class="panel panel-default">
            <div class="panel-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label">设置审核流程</label>
                    <div class="col-sm-5">
                        <div class="radio-inline">
                            <label>
                                <input type="radio" name="processradio" value="0" <c:if test="${user.flowtype ==0 }"> checked </c:if>>
                                使用默认
                            </label>
                        </div>
                        <div class="radio-inline">
                            <label>
                                <input type="radio" name="processradio" value="1" <c:if test="${user.flowtype ==1 }"> checked </c:if>>
                                自定义
                            </label>
                        </div>
                        <div class="radio-inline">
                            <label>
                                <input type="radio" name="processradio" value="2" <c:if test="${user.flowtype ==2 }"> checked </c:if>>
                                无审核
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group option-stream">
                    <label class="col-sm-2 control-label">审核流设置</label>
                    <div class="col-sm-5">
                        <button type="button" class="btn btn-default J-process-add">新增</button>
                    </div>
                </div>
                <div class="form-group option-stream">
                    <label class="col-sm-2 control-label"></label>
                    <div class="col-sm-5 J-process-stream"></div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-primary">提交</button>
                <button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">返回</button>
            </div>
        </div>
    </form>
</div>
<div class="common-tips"></div>
<script type="text/javascript">
seajs.use('${ctx}/js/func/management/person/review');
</script>
</body>
</html>