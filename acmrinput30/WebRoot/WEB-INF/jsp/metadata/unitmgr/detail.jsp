<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-单位管理-详情</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">单位详情</div>
				<div class="panel-body">
					<form class="form-horizontal J_detail_form">
						<div class="form-group">
							<label class="col-sm-2 control-label">单位代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code" value="${code}"><span></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">中文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname" value="${cname}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">基础换算率</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname" value="${rate}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname_en" value="${cname_en}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata">
									<option value=""></option>
									<option value="0" <c:if test="${ifdata == 0}">selected</c:if>>分类</option>
									<option value="1" <c:if test="${ifdata == 1}">selected</c:if>>指标</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">中文备注</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cmemo" value="${cmemo}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文备注</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cmemo_en" value="${cmemo_en}">
							</div>
						</div>

						<jsp:include page="/WEB-INF/jsp/metadata/synonym/metaListSee.jsp" flush="true"/>

						<div class="form-group">
							<div class="col-sm-offset-1 col-sm-11">
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">关闭</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/unitmgr/detail');
	</script>
</body>
</html>