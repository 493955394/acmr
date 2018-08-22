<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="renderer" content="webkit">
<title>${projectTitle}</title>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
</head>
<body style="background:#eeeff6;">
	<div class="logintop">
		<h1>中国信通院</h1>
		<div class="logintitle">产业数据库</div>
		<div class="loginexp">集数据录入，审核，作图等功能与一体的综合性数据系统。</div>
	</div>
	<div class="loginmain">
		<div class="loginright">
			<div style="position:absolute;right:0;top:100px;background:#cddefd;width:320px;overflow:hidden;border-radius:6px;">
				<form class="form-horizontal J_login_form" action="" method="post">
					<div class="form-group">
						<div class="col-sm-12 panel-heading" style="background:#5f96f8;color:#fff;">
							请登录
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-12 control-label text-danger" id="msg" style="text-align:left;">${msg}</label>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<div class="input-group input-group-lg">
								<span class="input-group-addon glyphicon glyphicon-user reset-glyphicon"></span>
								<input type="text" class="form-control" name="email" placeholder="用户名">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<div class="input-group input-group-lg">
								<span class="input-group-addon glyphicon glyphicon-lock reset-glyphicon"></span>
								<input type="password" class="form-control" name="pwd" placeholder="密码">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=" col-sm-12">
							<div class="checkbox">
								<label>
									<input type="checkbox"> 下次自动登录
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=" col-sm-12">
							<button type="submit" class="btn btn-primary  col-sm-12">登&nbsp;录</button>
						</div>
					</div>
					<div class="form-group">
					</div>
				</form>
			</div>
		</div>
	</div>
<script type="text/javascript">
	seajs.use('${ctx}/js/func/index/login');
</script>
</body>
</html>