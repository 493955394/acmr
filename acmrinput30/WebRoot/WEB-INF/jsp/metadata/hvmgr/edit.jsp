<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-汇率管理-修改</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">修改汇率</div>
				<div class="panel-body">
					<form class="form-horizontal J_edit_form" action="${ctx}/metadata/hvmgr.htm?m=toSave&id=${id}">
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>时间</label>
							<div class="col-sm-5">
								<input type="text" class="form-control hid-bottom" name="sjName" value="${procodeName}" placeholder="请选择时间" readonly/>
								<input type="hidden" name="sj" id="sjCode" value="${sj}" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>单位</label>
							<div class="col-sm-5">
								<input type="text" class="form-control hid-bottom" name="unitName" value="${unicode}" placeholder="请选择单位" readonly />
								<input type="hidden" name="code" id="unitCode"  value="${code}" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>美元汇率</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="rate" value="${rate}" />
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary">提交</button>
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">关闭</button>
							</div>
						</div>
						
					</form>
				</div>
			</div>
		</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/hvmgr/edit');
	</script>
</body>
</html>