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
	</div>
	<c:if test="${dimensionInfo.replyUserName!=''}">
		<div class="text-center">
			<p>
				<span>审批人:${dimensionInfo.replyUserName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<span>审批时间: <fmt:formatDate pattern="yyyy-MM-dd" value="${dimensionInfo.replyTime}" type="both"/></span>
			</p>
		</div>
		<c:if test="${existExcel}">
			<div class="panel-body">
				<div style="background: #f7f7f9;border: 1px solid #e1e1e8;padding: 9px 14px;">
					<span style="color:#ff7f19;">附件：</span>
					<span id="${id}" class="J_excel_download" style="background: #ccc;padding: 5px 15px;">
					<span class="glyphicon glyphicon-file" style="color:#999;"></span>
					<span class="glyphicon glyphicon-save" style="color:#ff7f19;">
					</span>
					</span>
				</div>
			</div>
		</c:if>

		<div class="panel-body">
			<div id="reply" style="border-top:1px soild black">
				<p><label>审批意见:</label></p>
				<p>${dimensionInfo.replyMemo}</p>
				<p><label>详细说明:</label></p>
				<table class="table table-bordered">
					<thead>
					<tr>
						<th>序号</th>
						<th>所属维度</th>
						<th>代码</th>
						<th>名称</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${dimensionInfo.replyList.dimensionList}" var="item" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${item['type']}</td>
							<td>${item['memo']}</td>
							<td>${item['name']}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<br>
			<br>
			<br>
			<br>
		</c:if>
		<div class="text-center">
			<p>
				<span>发件人:${dimensionInfo.userName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<span>发送时间: <fmt:formatDate pattern="yyyy-MM-dd" value="${dimensionInfo.createTime}" type="both"/></span>
			</p>
		</div>
			<div id="apply">
				<p><label>维度说明</label></p>
				<p>${dimensionInfo.memo}</p>
				<p><label>未匹配维度</label></p>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>序号</th>
							<th>所属维度</th>
							<th>名称</th>
							<th>代码</th>
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
		<button class="btn btn-primary btn-sm J_back" onclick="history.go(-1)">返回</button>
	</div>
	<br>
	<br>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/personal/feedback.js');
	</script>
</body>
</html>