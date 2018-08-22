<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-时间管理-修改</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/datepicker.css" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">修改时间</div>
				<div class="panel-body">
					<form class="form-horizontal J_edit_form" action="${ctx}/metadata/mdate.htm?m=add">
						<div class="form-group hidden">
							<div class="col-sm-5">
								<input type="hidden" class="form-control" name="id" value="${id}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">上一级代码名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" value="${procodeName}" id="procodeName" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">上一级代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="procode" value="${procode}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">时间类型</label>
							<div class="col-sm-5">
								<select class="form-control" name="tsort">
									<option value="M"  <c:if test="${timesort == 'M'}">selected</c:if>>月度时间</option>
									<option value="Q"  <c:if test="${timesort == 'Q'}">selected</c:if>>季度时间</option>
									<option value="Y"  <c:if test="${timesort == 'Y'}">selected</c:if>>年度时间</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">时间代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code" value="${code}" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>中文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="cname" value="${cname}">
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
									<option value="0" <c:if test="${ifdata == 0}">selected</c:if>>分类</option>
									<option value="1" <c:if test="${ifdata == 1}">selected</c:if>>指标</option>
									<c:if test="${ifdata!=0 && ifdata!=1}">
										<option selected="selected"></option>
									</c:if>
								</select>
							</div>
						</div>
						
						<div class="hidden-group" <c:if test='${ifdata == 0}'>style="display: none;"</c:if>>
							<div class="form-group">
								<label class="col-sm-2 control-label">开始时间</label>
								<div class="col-sm-5">
									<div class="input-group">
												<input type="text" class="form-control J_datepicker" name="btime" value="${btime }" readonly> 
										<span class="input-group-addon glyphicon glyphicon-calendar reset-glyphicon"></span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">结束时间</label>
								<div class="col-sm-5">
									<div class="input-group">
											<input type="text" class="form-control J_datepicker" name="etime" value="${etime }" readonly> 
										<span class="input-group-addon glyphicon glyphicon-calendar reset-glyphicon"></span>
									</div>
								</div>
							</div>
						</div>

						<%-- <jsp:include page="/WEB-INF/jsp/metaListEdit.jsp" flush="true"/> --%>

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
	seajs.use('${ctx}/js/func/metadata/mdatemgr/edit');
	</script>
</body>
</html>