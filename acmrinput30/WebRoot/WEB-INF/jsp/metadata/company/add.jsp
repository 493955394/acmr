<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-主体管理-新增</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">新增主体</div>
				<div class="panel-body">
					<form class="form-horizontal J_add_form" action="${ctx}/metadata/company.htm?m=toSave">
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
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>主体代码</label>
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
									<input type="text" class="form-control hid-bottom" name="unitName" placeholder="请选择单位" readonly>
									<input type="hidden" name="unitcode" id="unitCode">
									<ul id="treeUnit" class="ztree select-tree hid-top"></ul>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">小数点位数</label>
								<div class="col-sm-5">
									<select class="form-control" name="dotcount">
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
							
							<div class="form-group">
								<label class="col-sm-2 control-label">企业分类1</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="qyfl1"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">企业分类2</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="qyfl2"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">股票简称</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gpjc"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">股票代码</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gpdm"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">所属行业</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="hy"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">上市板块</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gsxx"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司属性</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gssx"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司地址</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gsdz"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司网址</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gswz"></textarea>
								</div>
							</div>
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
		seajs.use('${ctx}/js/func/metadata/company/add');
	</script>
</body>
</html>