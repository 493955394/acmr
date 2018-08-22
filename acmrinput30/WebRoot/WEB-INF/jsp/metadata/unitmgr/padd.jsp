<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-单位管理-新增</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading" id="add">
					增加单位
				</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/metadata/unit.htm?m=add">
						<div class="form-group">
							<label class="col-sm-2 control-label">上一级代码名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control"value="${proname}" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">上一级代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="procode" value="${code }" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>单位代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>中文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">基础换算率</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="rate">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname_en">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata">
									<option value="0">分类</option>
									<option value="1">指标</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">中文备注</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cmemo"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文备注</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cmemo_en"></textarea>
							</div>
						</div>

						<jsp:include page="/WEB-INF/jsp/metadata/synonym/metaList.jsp" flush="true"/>

						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary">提交</button>
								<button type="reset" class="btn btn-primary">重置</button>
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">关闭</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/unitmgr/padd');
	</script>
</body>
</html>