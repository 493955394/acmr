<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-主体管理-修改</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
		<div class="container-fluid">
			<div class="panel panel-default">
				<div class="panel-heading">修改主体</div>
				<div class="panel-body">
					<form class="form-horizontal J_edit_form" action="${ctx}/metadata/company.htm?m=toSave">
						
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
							<label class="col-sm-2 control-label">主体代码</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="code" value="${code}" readonly>
							</div>
						</div>
								
						<div class="form-group">
							<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>中文名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="id" value="${id}" style="display:none">
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
							<label class="col-sm-2 control-label">分类</label>
							<div class="col-sm-5">
								<select class="form-control" name="ifdata">
									<option value="0" <c:if test="${ifdata == 0}">selected</c:if>>分类</option>
									<option value="1" <c:if test="${ifdata == 1}">selected</c:if>>主体</option>
									<c:if test="${ifdata!=0 && ifdata!=1}">
										<option selected="selected"></option>
									</c:if>
								</select>
							</div>
						</div>
						
						<div class="hidden-group" <c:if test='${ifdata == 0}'>style="display: none;"</c:if>>
							<div class="form-group">
								<label class="col-sm-2 control-label">中文单位</label>
								<div class="col-sm-5">
									<input type="text" class="form-control hid-bottom" value="${unicode}" name="unitName" placeholder="请选择单位" readonly>
									<input type="hidden" name="unitcode" id="unitCode" value="${unitcode}">
									<ul id="treeUnit" class="ztree select-tree hid-top"></ul>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">小数点位数</label>
								<div class="col-sm-5">
									<select class="form-control" name="dotcount">
										<option value="">请选择</option>
										<option value="0" <c:if test="${dotcount == '0'}">selected</c:if>>0</option>
										<option value="1" <c:if test="${dotcount == '1'}">selected</c:if>>1</option>
										<option value="2" <c:if test="${dotcount == '2'}">selected</c:if>>2</option>
										<option value="3" <c:if test="${dotcount == '3'}">selected</c:if>>3</option>
										<option value="4" <c:if test="${dotcount == '4'}">selected</c:if>>4</option>
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
							
							<jsp:include page="/WEB-INF/jsp/metadata/synonym/metaListEdit.jsp" flush="true"/>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">企业分类1</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="qyfl1">${qyfl1}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">企业分类2</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="qyfl2">${qyfl2}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">股票简称</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gpjc">${gpjc}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">股票代码</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gpdm">${gpdm}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">所属行业</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="hy">${hy}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">上市板块</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gsxx">${gsxx}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司属性</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gssx">${gssx}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司地址</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gsdz">${gsdz}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">公司网址</label>
								<div class="col-sm-5">
									<textarea rows="" cols="" class="form-control" name="gswz">${gswz}</textarea>
								</div>
							</div>
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
		seajs.use('${ctx}/js/func/metadata/company/edit');
	</script>
</body>
</html>