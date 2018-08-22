<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${projectTitle}-我的</title>
	<jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />


		<div class="text-center">
			<h4 style="color:#ff7f19;"><strong>${dimensionInfo.name}</strong></h4>
			<c:if test="${dimensionInfo.state =='2'}">
				<p>
					<span>审批人:${dimensionInfo.replyUserName} </span>
					<span>审批时间: <fmt:formatDate pattern="yyyy-MM-dd" value="${dimensionInfo.replyTime}" type="both"/></span>
				</p>
			</c:if>
		</div>
		<c:if test="${existExcel}">
			<div class="panel-body">
				<div style="background: #f7f7f9;border: 1px solid #e1e1e8;padding: 9px 14px;">
					<span style="color:#ff7f19;">附件：</span>
					<span class="J_excel_download" id="${id}" style="background: #ccc;padding: 5px 15px;">
					<span class="glyphicon glyphicon-file" style="color:#999;"></span>
					<span class="glyphicon glyphicon-save" style="color:#ff7f19;">
					</span>
					</span>
				</div>
			</div>
		</c:if>
		<div class="panel-body">
			<div id="reply" style="border-top:1px soild black">
				<c:if test="${dimensionInfo.state =='2'}">
					<p><label>审批意见</label></p>
					<p>${dimensionInfo.replyMemo}</p>
				</c:if>
				<c:if test="${dimensionInfo.state =='1'}">
					<p><label>审批意见:</label></p>
					<div class="row form-group form-horizontal">
						<div class="col-sm-10">
							<textarea id="replymemo" name="replymemo" class="form-control input-sm" rows="4"></textarea>
						</div>
					</div>
				</c:if>
				<br>
				<p>
					<label>详细说明:</label>
					<c:if test="${dimensionInfo.state =='1'}">
							<button class="btn btn-primary btn-sm add_list_item" style="float:right">添加</button>
					</c:if>
				</p>
				<c:if test="${dimensionInfo.state =='2'}">
				<table class="table table-bordered">
					<thead>
					<tr>
						<th>序号</th>
						<th>所属维度</th>
						<th>指标代码</th>
						<th>指标名称</th>
					</tr>
					</thead>			
					<tbody>
						<c:forEach items="${dimensionInfo.replyList.dimensionList}" var="item" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${item['type']}</td>
								<td>${item['name']}</td>
								<td>${item['memo']}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</c:if>
				<c:if test="${dimensionInfo.state =='1'}">
					<table id="J_edit_table" class="table table-bordered" >
						<thead>
						<tr>
							<th>序号</th>
							<th>所属维度</th>
							<th style="width:350px">指标代码</th>
							<th style="width:350px">指标名称</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</c:if>
			</div>
			<br>
			<br>
			<br>
		<div class="text-center">
			<p>
				<span>发件人:${dimensionInfo.userName} </span>
				<span>发件时间: <fmt:formatDate pattern="yyyy-MM-dd" value="${dimensionInfo.createTime}" type="both"/></span>
			</p>
		</div>
		<div id="apply">
				<p><label>维度说明</label></p>
				<p>${dimensionInfo.memo}</p>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>序号</th>
							<th>所属维度</th>
							<th>指标名称</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${dimensionInfo.applyList.dimensionList}" var="item" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${item['type']}</td>
								<td>${item['name']}</td>
								<td>${item['memo']}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
	</div>
		<br>
		<br>
	<div class="text-center">
		<c:if test="${dimensionInfo.state =='1'}">
			<button id="${id}" class="btn btn-primary btn-sm J_submit" >回复</button>
		</c:if>
		<button class="btn btn-primary btn-sm J_back" onclick="window.close()">关闭</button>
	</div>
	<br>
	<br>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/management/handle/main.js');
		</script>
</body>
</html>