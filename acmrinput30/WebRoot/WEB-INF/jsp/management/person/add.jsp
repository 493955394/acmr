<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-人员管理-新增</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />		
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">新增用户</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/system/person.htm?m=insert">
						<input type="hidden" name="depcode" value="${depcode}">
						<div class="form-group">
							<label class="col-sm-2 control-label">所属组织</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="deptName" value="${depName}" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="userid">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>用户姓名</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="username">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" name="pwd">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>确认密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" name="ppwd">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>电子邮件</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="email">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">联系电话</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="tel">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">职务</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="duty">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary">提交</button>
								<button type="reset" class="btn btn-primary">重置</button>
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">返回</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="common-tips"></div>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/management/person/add');
		</script>
	</body>
</html>