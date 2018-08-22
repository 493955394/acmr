<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-个人信息修改</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading"></div>
				<div class="panel-body">
				<div class="toolbar-right">
				<button type="button"  class="btn btn-primary J_edit">编辑</button>	
				<button type="button"  class="btn btn-primary J_opr_pwd">重置密码</button>
				<!-- <button type="button" class="btn btn-primary" onclick="javascript:window.location.href=document.referrer;">返回</button> -->
				</div>
				</div >
				<div class="panel-body">
					<form class="form-horizontal J_edit_form" action="${ctx}/person.htm?m=update">
					<input type="hidden" name="depcode" value="${user.depcode}">
						<div class="form-group">
							<label class="col-sm-2 control-label">代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="userId" value="${user.userId}" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>用户姓名</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname" value="${user.cname}"  disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>电子邮件</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="email" value="${user.email}" disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">联系电话</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="tel" value="${user.tel}"  disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">职务</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="duty" value="${user.duty}"  disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" style="display: none;" class="btn btn-primary J_submit">提交</button>
								<button type="reset" style="display: none;" class="btn btn-primary J_reset">重置</button>
								<button class="btn btn-primary J_back" style="display: none;">返回</button>
							</div>
						</div>
					</form>
				</div>
			</div>
	</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/management/person/personEdit');
	</script>
</body>
</html>