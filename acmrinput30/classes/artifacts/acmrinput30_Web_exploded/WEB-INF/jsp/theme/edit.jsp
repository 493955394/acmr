<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${projectTitle}-栏目管理-编辑模块</title>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/editTemplate/css/semantic.min.css">
<style type="text/css">
	html,body{
		font-size:1em;
	}
	.ui.dropdown label{
        margin-bottom: 0!important;
        font-weight: normal;
    }
    .option-stream{
        display: none;
    }
	.col-sm-5 {min-width:500px;}
	.vtop {
		vertical-align: top;
	}
	select.vtop {
		display: inline-block;
	}
	.ui.selection.dropdown{
		padding: 0.4em 2.5em 0.4em 1em;
		min-height: 1em;
	}
	.ui.multiple.dropdown > .label{
		font-size: 0.75em;
	}
	.J_delPower{
		cursor: pointer;
		color: #f00;
	}
	
	#table input[type="text"]{
		display: block;
		width: 100%;
		padding: 6px 12px;
		font-size: 14px;
		line-height: 1.42857143;
		color: #555555;
		background-color: #ffffff;
		background-image: none;
		border: 1px solid #cccccc;
	}
	.did{
		width: 240px;
		height: 237px;
		border: 1px solid #ccc;
		position: absolute;
		display: none;
		background: #fff;
	}
	.did ul{
		width: 240px;
		height: 180px;
		overflow-y: auto;
		overflow-x: hidden;
	}
	.didhead {
		height:56px;
		padding: 10px;
		border-bottom: 1px solid #ccc;
	}
	.did li,
	.searchBox li{
		line-height: 28px;
		list-style: none;
		cursor: pointer;
		padding: 0 15px;
	}
	.did li:hover{
		background: #ff7f19;
		color: #fff;
	}
	.searchBox {
		width: 240px;
		height: 120px;
		overflow-y: auto;
		display: none;
		position: absolute;
		top: 56px;
		left: 0;
		overflow-y: auto;
		overflow-x: hidden;
		background: #fff;
		border-bottom: 1px solid #ccc;
		box-shadow: 0 1px 2px rgba(0,0,0,6);
	}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading">编辑模块</div>
			<div class="panel-body">
				<form class="form-horizontal J_add_form" method="post" enctype="multipart/form-data" action="${ctx}/theme/theme.htm?m=toEditSave">
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>模块 ID</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="code" readonly="readonly" value="${themePo.code}"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>模块名称</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="cname" value="${themePo.cname}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">所属分类</label>
						<div class="col-sm-5">
							<select class="form-control" name="fl" autocomplete="off">
								<c:forEach items="${tree}" var="list">
									<option value="${list.code}" <c:if test="${list.code eq themePo.procode}">selected</c:if> >${list.cname}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">图标</label>
						<div class="col-sm-5">
							<label class="col-sm-2 control-label">
								<input type="file" name="tubiao" accept=".JPG,.JPEG,.PNG" />
							</label>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-5">
							<img width="100" border="0" src="${themePo.photo}">
						</div>
					</div>
						
					<div class="form-group">
						<label class="col-sm-2 control-label">备注</label>
						<div class="col-sm-5">
							<textarea rows="" cols="" class="form-control" name="exp">${themePo.exp}</textarea>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">模块权限</label>
						<input type="hidden" name="modePower" value="">
						<div class="col-sm-5">
						<div class="radio-inline">
                           <label>
	                            <input type="radio" name="visible" autocomplete="off" <c:if test="${themePo.visible != 1}">checked="checked"</c:if> value="0">所有用户均可见
                           </label>
						</div>
						</div>
					</div>
					
					<input id="isPower" type="hidden" value='${menuright}' powerType="${themePo.visible}" />
					
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-5">
							<div class="radio-inline col-sm-10">
	                           <label>
	                               <input type="radio" name="visible" autocomplete="off" <c:if test="${themePo.visible == 1 }">checked="checked"</c:if> value="1">仅以下组织/用户/角色可查看
	                           </label>
	                        </div>
	                        
							<div class="col-sm-2">
								<buttom class="btn btn-primary btn-sm J_addPower  <c:if test="${themePo.visible!=1}">hide</c:if>">新增权限</buttom>
	                        </div>
                        </div>
					</div>
						
					<select class="publicSelect hide"><!-- 公用用户下拉 -->
						<c:forEach items="${roles}" var="list">
							<option value="${list.code}">${list.cname}</option>
						</c:forEach>
					</select>
					
					<div class="form-group J_power <c:if test="${themePo.visible!=1}">hide</c:if>">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-8 J_powerList"></div>
					</div>
					
					<hr />
							 
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-5">
							<buttom class="btn btn-primary btn-sm J_addModel">增加子模块</buttom>
							<input type="hidden" name="childMode" value="" list='${lanmu}' >
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-5">
							<table class="table table-striped table-bordered" id="table">
								<thead>
									<tr>
										<td>ID</td>
										<td>名称</td>
										<td style="width:100px;">默认</td>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${themePo.childs}" var="child">
										<tr>
											<td><input type="text" value="${child.code}" style="background-color:#eeeeee;" readonly="readonly"><span style="color:red;font-size:10px;" name="spans"></span></td>
											<td><input type="text" value="${child.cname}"></td>
											<td><input type="radio" name="aa" <c:if test="${'1' eq child.def }">checked</c:if> > 　<img name="delimg" src="${ctx}/images/del.png"></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
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
<div class="did">
	<div class="didhead">
		<div class="input-group">
			<input class="form-control didsearch" type="text" />
			<span class="input-group-addon glyphicon glyphicon-search reset-glyphicon"></span>
		</div>
	</div>
	<div class="searchBox">
		<ul></ul>
	</div>
	
	<ul>
		<c:forEach items="${lanmus}" var="list">
			<li val="${list.code}" title="${list.name}/${list.dbcode}/${list.code}">${list.name}/${list.dbcode}/${list.code}</li>
		</c:forEach>
	</ul>
</div>
<div class="common-tips"></div>
<script type="text/javascript">
	seajs.use('${ctx}/js/func/theme/edit');
</script>
</body>
</html>