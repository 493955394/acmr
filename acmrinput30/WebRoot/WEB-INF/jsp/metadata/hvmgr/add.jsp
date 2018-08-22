<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-汇率管理-添加</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">添加汇率</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/metadata/hvmgr.htm?m=toSave">

						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>时间</label>
							<div class="col-sm-5">
								<input type="text" class="form-control hid-bottom" name="sjName" value="${procodeName}" placeholder="请选择时间" readonly/>
								<input type="hidden" name="sj" id="sjCode" value="${procode}" />
								<c:if test="${procode != 'all'}">
									<ul id="treeSj" class="ztree select-tree hid-top"></ul>
								</c:if>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>单位</label>
							<div class="col-sm-5">
								<input type="text" class="form-control hid-bottom" name="unitName" placeholder="请选择单位" readonly />
								<input type="hidden" name="code" id="unitCode" />
								<ul id="treeUnit" class="ztree select-tree hid-top"></ul>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>美元汇率</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="rate" />
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
		seajs.use('${ctx}/js/func/metadata/hvmgr/add');
	</script>
</body>
</html>