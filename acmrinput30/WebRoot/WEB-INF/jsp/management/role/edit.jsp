<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-角色管理-修改</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="container-fluid J_main_container">
		<div class="col-md-10">
			<div class="panel panel-default">
				<div class="panel-heading">编辑角色</div>
				<div class="panel-body">
					<form class="form-horizontal J_edit_form" action="${ctx}/system/role.htm?m=update">
						<div class="form-group">
							<label class="col-sm-2 control-label">代码</label>
							<div class="col-sm-5">
								<input type="text"  class="form-control" name="code" value="${role.code}" readonly="readonly">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>角色名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname" value="${role.cname}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">备注</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="memo" value="${role.memo}">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary">提交</button>
								<button type="reset" class="btn btn-primary">重置</button>
								<button type="button" class="btn btn-primary" onclick="window.location.href=document.referrer;">返回</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
	seajs.use('${ctx}/js/func/management/role/edit');
	</script>
</body>
</html>