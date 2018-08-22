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
			<p>
				<span>发件人:${dimensionInfo.userName} </span>
				<span>发送时间: <fmt:formatDate pattern="yyyy-MM-dd" value="${dimensionInfo.createTime}" type="both"/></span>
			</p>
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
					<p><label>审批意见:</label></p>
					<p>${dimensionInfo.replyMemo}</p>
				</c:if>
				<c:if test="${dimensionInfo.state =='1'}">
					<div class="row">
						<div class="col-sm-10">
							<p><label>审批意见：</label></p>
						</div>
						<div class="form-horizontal form-group">
							<div class="col-sm-10">
								<textarea id="replymemo" name="replymemo" class="form-control input-sm" rows="4"></textarea>
							</div>
						</div>
					</div>
				</c:if>
				<br>
				<p><label>详细说明：<span style="color:#990000">（代码/名称不能超过50字符）<span></label></p>
				<c:if test="${dimensionInfo.state =='2'}">
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
				</c:if>
				<c:if test="${dimensionInfo.state =='1'}">
				<div class="text-right">
					<button class="btn btn-primary btn-sm add_list_item">添加</button>
				</div>
					<table id="J_edit_table" class="table table-bordered" >
						<thead>
						<tr>
							<th>序号</th>
							<th>所属维度</th>							
							<th style="width:350px">代码</th>
							<th style="width:350px">名称</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
							<tr>
							<td>1</td>
								<td>
									<select>
									  <option value ="指标">指标</option>
									  <option value ="分组">分组</option>
									  <option value="计量单位">计量单位</option>
									  <option value="时间类型">时间类型</option>
									  <option value="时间">时间</option>
									  <option value="数据来源">数据来源</option>
									  <option value="地区管理">地区管理</option>
									</select>
								</td>
								<td></td>
								<td></td>
								<td><a href="#" class="J_dimension_delete">删除</a></td>				
							</tr>
						</tbody>
					</table>
				</c:if>
			</div>
			<br>
			<br>
			<br>
		<div id="apply">
			<p><label>维度说明</label></p>
				<p>${dimensionInfo.memo}</p>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>序号</th>
							<th>所属维度</th>
							<th>名称</th>
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
		<button class="btn btn-primary btn-sm J_back" onclick="history.go(-1)">返回</button>
	</div>
	<br>
	<br>
		<script type="text/javascript">
			seajs.use('${ctx}/js/func/matedata/handle/main.js');
		</script>
</body>
</html>