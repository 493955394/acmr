<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-数据来源-新增</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">增加数据来源</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/metadata/datasrc.htm?m=add">
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
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>数据来源代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code">
								<input type="hidden" id="codeLen" value="${codeLen}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>中文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname_en">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>中文全称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="ccname">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">英文全称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="ccname_en">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata" autocomplete="off">
									<option value="1">指标</option>
									<option value="0">分类</option>
								</select>
							</div>
						</div>
						
						<div class="hidden-group">
							<div class="form-group">
								<label class="col-sm-2 control-label">中文解释</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="cexp"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">英文解释</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="cexp_en"></textarea>
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
						</div>
						
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
		seajs.use('${ctx}/js/func/metadata/datasrc/add');
	</script>
</body>
</html>