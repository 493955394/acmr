<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${projectTitle}-指标管理-数据详情</title>
		<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
	</head>
<body>
<style type="text/css">
.ciDimmer.ui.dimmer {
	-moz-user-select: none;
	animation-duration: 0.5s;
	animation-fill-mode: both;
	background-color: rgba(0, 0, 0, 0.85);
	height: 100%;
	left: 0 !important;
	display: block;
	line-height: 1;
	opacity: 1;
	position: fixed;
	text-align: center;
	top: 0 !important;
	transition: background-color 0.5s linear 0s;
	vertical-align: middle;
	width: 100%;
	-webkit-animation-fill-mode: both;
	animation-fill-mode: both;
	-webkit-animation-duration: .5s;
	animation-duration: .5s;
	-webkit-transition: background-color .5s linear;
	transition: background-color .5s linear;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	will-change: opacity;
	will-change: opacity;
	z-index: 1000;
}

.ui.loader{position:absolute;top:50%;left:50%;margin:0;text-align:center;z-index:1000;-webkit-transform:translateX(-50%) translateY(-50%);-ms-transform:translateX(-50%) translateY(-50%);transform:translateX(-50%) translateY(-50%)}
.ui.loader:before{position:absolute;content:'';top:0;left:50%;border-radius:500rem;border:.2em solid rgba(0,0,0,.1)}
.ui.loader:after{position:absolute;content:'';top:0;left:50%;-webkit-animation:loader .6s linear;animation:loader .6s linear;-webkit-animation-iteration-count:infinite;animation-iteration-count:infinite;border-radius:500rem;border-color:#767676 transparent transparent;border-style:solid;border-width:.2em;box-shadow:0 0 0 1px transparent}@-webkit-keyframes loader{from{-webkit-transform:rotate(0);transform:rotate(0)}to{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes loader{from{-webkit-transform:rotate(0);transform:rotate(0)}to{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}
.ui.loader:after,
.ui.loader:before{width:2.2585em;height:2.2585em;margin:0 0 0 -1.12925em}
.ui.dimmer .ui.loader{color:rgba(255,255,255,.9)}
.ui.dimmer .ui.loader:before{border-color:rgba(255,255,255,.15)}
.ui.dimmer .ui.loader:after{border-color:#FFF transparent transparent}
</style>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading">数据详情<span style="color:red"> (  指标名称：${cname} &nbsp;&nbsp;&nbsp;&nbsp;指标代码：${code} )</span></div>
			<div class="panel-body">
				<div class="toolbar-left">
					<div class="form-inline">
						<div class="form-group" style="white-space:nowrap;">
							<input type="hidden" id="code" value="${code}"/>
							<input type="hidden" id="cname" value="${cname}"/>
							指标分组
							<select id="zbclass" class="form-control input-sm btn-margin" style="width:15%">
								<option value="">全部</option>
								<c:forEach items="${queryFlData}" var="list">
									<option value="${list.code}">${list.cname}【${list.code}】</option>
								</c:forEach>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;地区
							<select id="region" class="form-control input-sm btn-margin" style="width:15%">
								<option value="">全部</option>
								<c:if test="${page.totalRecorder>0}">
									<c:forEach items="${queryRegData}" var="list">
										<option value="${list.code}">${list.cname}【${list.code}】</option>
									</c:forEach>
								</c:if>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;数据来源
							<select id="datasource" class="form-control input-sm btn-margin" style="width:15%">
								<option value="">全部</option>
								<c:if test="${page.totalRecorder>0}">
									<c:forEach items="${queryDsData}" var="list">
										<option value="${list.code}">${list.cname}【${list.code}】</option>
									</c:forEach>
								</c:if>
							</select>
						</div>
					</div>
				</div>
				<div class="toolbar-right">
					<div class="toolbar-group">
						<a href="javascript:;"  class="btn btn-default btn-sm J_export">导出</a>
						<a href="javascript:;" onclick="javascript:history.go(-1);" class="btn btn-default btn-sm">返回</a>
					</div>
				</div>
			</div>
			<table class="table table-striped table-hover J_zbmgr_template">
				<thead>
					<tr>
						<th>指标分组</th>
						<th>地区</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>更新时间</th>
						<th>数据量</th>
						<th>数据来源</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
						<tr>
							<td></td>
							<td>没有查询到数据</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:if>
					<c:forEach items="${page.data}" var="list" varStatus="status">
						<tr>
							<td>${list.flName}【${list.fl}】</td>
							<td>${list.regName}【${list.reg}】</td>
							<td>${list.beginTime}</td>
							<td>${list.endTime}</td>
							<td>${list.updateTime}</td>
							<td>${list.count}</td>
							<td>${list.datasourceName}【${list.datasource}】</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="toolbar-right">
	            <ul class="pagination J_template_pagination">
	            	<li class="disabled"><span>首页</span></li>
	            	<li class="disabled"><span>上一页</span></li>
	            	<li class="active"><a href="">1</a></li>
	            	<c:forEach begin="2" end="4" varStatus="i">
		            	<c:if test="${page.pageNum+i.index-1 <= page.totalPage}">
		            		<li><a href="${ctx}/metadata/zbmgr.htm?m=toDatadetails&code=${code}&pageNum=${page.pageNum+i.index-1}">${i.index}</a></li>
						</c:if>
	            	</c:forEach>
	            	<c:choose>
	            		<c:when test="${page.totalPage==1 or page.totalRecorder==0}">
			            	<li class="disabled"><a href="">下一页</a></li>
			            	<li class="disabled"><a href="">末页</a></li>
	            		</c:when>
	            		<c:otherwise>
			            	<li><a href="${ctx}/metadata/zbmgr.htm?m=toDatadetails&code=${code}&pageNum=${page.pageNum+1}">下一页</a></li>
			            	<li><a href="${ctx}/metadata/zbmgr.htm?m=toDatadetails&code=${code}&pageNum=${page.totalPage}">末页</a></li>
	            		</c:otherwise>
	            	</c:choose>
	            </ul>
			</div>
		</div>
	</div>
	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/metadata/zbmgr/datadetails');
	</script>
</body>
</html>