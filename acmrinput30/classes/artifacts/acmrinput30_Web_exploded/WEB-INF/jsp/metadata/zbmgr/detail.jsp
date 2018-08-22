<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-指标管理-详情</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">指标详情</div>
				<div class="panel-body">
					<form class="form-horizontal J_detail_form">
						
						<div class="form-group">
								<label class="col-sm-2 control-label">上一级代码名称</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" value="${procodeName}">
								</div>
						</div>
						
						<div class="form-group">
								<label class="col-sm-2 control-label">上一级代码</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" name="procode" value="${procode}">
								</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">指标代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code" value="${code}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">中文名称</label>
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
							<label class="col-sm-2 control-label">中文全称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="ccname" value="${ccname}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">英文全称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="ccname_en" value="${ccname_en}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">指标分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata" value="${ifdata}">
										<option value=""></option>
										<option value="0" <c:if test="${ifdata == 0}">selected</c:if>>分类</option>
										<option value="1" <c:if test="${ifdata == 1}">selected</c:if>>指标</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">中文单位</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="unitcode" value="${unitcode}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">小数点位数</label>
							<div class="col-sm-5">
								<select class="form-control" name="dotcount">
									<option value=""></option>
									<option value="0" <c:if test="${dotcount == '0'}">selected</c:if>>0</option>
									<option value="1" <c:if test="${dotcount == '1'}">selected</c:if>>1</option>
									<option value="2" <c:if test="${dotcount == '2'}">selected</c:if>>2</option>
									<option value="3" <c:if test="${dotcount == '3'}">selected</c:if>>3</option>
									<option value="4" <c:if test="${dotcount == '4'}">selected</c:if>>4</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">优先分组代码</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="classprocodes"><c:forEach var="list" items="${classProcodes}">【${list.code} = ${list.cname}】</c:forEach></textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">NBS代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="nbscode" value="${nbscode}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">时间类型</label>
							<div class="col-sm-5">
								<select class="form-control" name="zbsort">
									<option value=""></option>
									<option value="0" <c:if test="${zbsort == '0' }">selected</c:if>>时点指标</option>
									<option value="1" <c:if test="${zbsort == '1' }">selected</c:if>>时期指标</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">中文解释</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cexp">${cexp}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">英文解释</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cexp_en">${cexp_en}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">中文备注</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cmemo">${cmemo}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">英文备注</label>
							<div class="col-sm-5">
								<textarea rows="" cols="" class="form-control" name="cmemo_en">${cmemo_en}</textarea>
							</div>
						</div>

						<jsp:include page="/WEB-INF/jsp/metadata/synonym/metaListSee.jsp" flush="true"/>
						
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">关闭</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/zbmgr/detail');
	</script>
</body>
</html>