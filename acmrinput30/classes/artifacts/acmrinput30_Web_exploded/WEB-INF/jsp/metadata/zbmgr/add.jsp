<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-指标管理-新增</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">新增指标</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/metadata/zbmgr.htm?m=toSave">
						<div class="form-group">
								<label class="col-sm-2 control-label">上一级代码名称</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" value="${procodeName}" readonly>
								</div>
						</div>
						
						<div class="form-group">
								<label class="col-sm-2 control-label">上一级代码</label>
								<div class="col-sm-5">
									<input type="hidden" name="procode" value="${procode}" />
									<input type="text" class="form-control" value="${procode}" disabled>
								</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>指标代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code">
							</div>
							<div class="col-sm-5">
								<c:if test="${israndom eq 'yes'}">
									<input type="button"  id="getCodeBtn" value="获取随机码">
								</c:if>
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
							<label class="col-sm-2 control-label">中文全称</label>
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
							<label class="col-sm-2 control-label">指标分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata" autocomplete="off">
									<option value="1">指标</option>
									<option value="0">分类</option>
								</select>
							</div>
						</div>
						
						<div class="hidden-group">
							<div class="form-group">
								<label class="col-sm-2 control-label">中文单位</label>
								<div class="col-sm-5">
									<input type="text" autocomplete="off" class="form-control hid-bottom" name="unitName" placeholder="请选择单位" readonly>
									<input type="hidden" autocomplete="off" name="unitcode" id="unitCode">
									<ul id="treeUnit" class="ztree select-tree hid-top"></ul>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">小数点位数</label>
								<div class="col-sm-5">
									<select class="form-control" name="dotcount" autocomplete="off">
										<option value="">请选择</option>
										<option value="0">0</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
									</select>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">优先分组代码</label>
								<div class="col-sm-5">
									<button type="button" class="btn btn-primary J_group_code">添加查看</button>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">NBS代码</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" name="nbscode">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">时间类型</label>
								<div class="col-sm-5">
									<select class="form-control" name="zbsort" autocomplete="off">
										<option value="">请选择</option>
										<option value="0">时点指标</option>
										<option value="1">时期指标</option>
									</select>
								</div>
							</div>
							
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
								<button type="reset" class="btn btn-primary Reset">重置</button>
								<button type="button" class="btn btn-primary" onclick="javascript:history.go(-1);">关闭</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/zbmgr/add');
	</script>
</body>
</html>