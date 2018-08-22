<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${projectTitle}-首页</title>
<%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />	
	<div class="container-fluid ">
		<!-- 待填报任务 -->
		<c:if test="${permission1==true}">
			<div class="panel panel-default reset-panel">
				<div class="panel-heading">
					<div class="toolbar">
						<div class="toolbar-left">
							<c:choose>
								<c:when test="${list.writePage.totalRecorder==0}"><label class="control-label">待填报任务</label></c:when>
								<c:otherwise>
									<label class="control-label"><font color="red">待填报任务数量: ${list.writePage.totalRecorder}</font></label>
								</c:otherwise>
							</c:choose>
						</div>			
						<div class="toolbar-right">
							<a href="${ctx}/dataload/datamanageload.htm">更多&gt;&gt;</a>
						</div>
					</div>
				</div>
			</div>
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>报表代码</th>
						<th>报表名称</th>
						<th>调查制度</th>
						<th>报告期</th>
						<th>处室</th>
						<th>报表类型</th>
						<th>上传截止日期</th>
						<th>状态</th>
						<th class="textright">采集量</th>
						<th>综合机关</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${list.writePage.totalRecorder==0 or list.writePage.totalPage<list.writePage.pageNum}">
						<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td>没有查询到数据</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						</tr>
					</c:if>
					<c:forEach items="${list.writePage.data}" end="5" var="item">
						<tr>
							<td>${item['reportcode']}</td>
							<td><a href="${ctx}/index.htm?m=checkTableCss&id=${item['taskId']}&fromstate=1" name="${item['taskId']}">${item['reportname']}</a></td>
							<td>${item['systemname']}</td>
							<td>${item['reportstage']}</td>
						 	<td>${item['deptname']}</td>
						 <td>
						 <c:if test="${''==item['sort']}"></c:if>
						<c:if test="${'D'==item['sort']}">日报</c:if>
						<c:if test="${'W'==item['sort']}">周报</c:if>
						<c:if test="${'X'==item['sort']}">旬报</c:if>
						<c:if test="${'M'==item['sort']}">月报</c:if>
						<c:if test="${'Q'==item['sort']}">季报</c:if>
						<c:if test="${'Y'==item['sort']}">年报</c:if>
						</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd" value="${item['createtime']}" type="both"/></td>
							<td>
						 	<c:if test="${''==item['reportstate']}">
							</c:if>
						 	<c:if test="${'1' == item['reportstate']}">
						 	待填报
						 	</c:if>
						 	<c:if test="${'2'==item['reportstate']}">
						 	草稿
						 	</c:if>
						 	<c:if test="${'5'==item['reportstate']}">
						 	退回重报
						 	</c:if>
						 	<c:if test="${'6'==item['reportstate']}">
						 	已校验，待提交
						 	</c:if>
						 	</td>			 	
						 	<td class="textright">${item['intempcount']}</td>
						 	<td>${item['compoffice']}</td>
						 	<td>
						 		<a href="javascript:;" class="J_data_upLoad_now" id="${item['taskId']}" >上传</a>
						 		<a class="J_single_template_export_data" href="${ctx}/dataload/datamanageload.htm?m=downLoad" id="${item['taskId']}">导出模板</a>
						 	</td>
						 </tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		<!-- 待审批任务 -->
		<c:if test="${permission2==true}">
			<div class="J_template_data_table">
				<jsp:include page="/WEB-INF/jsp/tabList.jsp" flush="true"/>
			</div>
		</c:if>
		<!-- 最新入库数据 -->
		<div class="panel-heading"></div>
		<div class="panel panel-default reset-panel">
			<div class="panel-heading">
				<div class="toolbar">
					<div class="toolbar-left">
						<label class="control-label">最新入库数据  </label>
					</div>
					<c:if test="${permission1==true}">
					<div class="toolbar-right">
						<a href="${ctx}/dataload/datamanageload.htm?state=5">更多&gt;&gt;</a>
					</div>
					</c:if>
				</div>
			</div>
		</div>
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>报表代码</th>
					<th>报表名称</th>
					<th>调查制度</th>
					<th>报告期</th>
					<th>处室</th>
					<th>报表类型</th>
					<th>上传截止日期</th>
					<th>状态</th>
					<th class="textright">采集量</th>
					<th>综合机关</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
					<c:if test="${list.allPage.totalRecorder==0 or list.allPage.totalPage<list.allPage.pageNum}">
						<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td>没有查询到数据</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						</tr>
					</c:if>
			<c:forEach items="${list.allPage.data}" end="5" var="item">
					<tr>
						<td>${item['reportcode']}</td>
						<td><a href="${ctx}/index.htm?m=checkTableCss&id=${item['taskId']}&fromstate=1" name="${item['taskId']}">${item['reportname']}</a></td>
						<td>${item['systemname']}</td>
						<td>${item['reportstage']}</td>
					 	<td>${item['deptname']}</td>
					 <td>
					 <c:if test="${''==item['sort']}"></c:if>
					<c:if test="${'D'==item['sort']}">日报</c:if>
					<c:if test="${'W'==item['sort']}">周报</c:if>
					<c:if test="${'X'==item['sort']}">旬报</c:if>
					<c:if test="${'M'==item['sort']}">月报</c:if>
					<c:if test="${'Q'==item['sort']}">季报</c:if>
					<c:if test="${'Y'==item['sort']}">年报</c:if>
					</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd" value="${item['createtime']}" type="both"/></td>
						<td>已入库</td>			 	
					 	<td class="textright">${item['intempcount']}</td>
					 	<td>${item['compoffice']}</td>
					 	<td>
					 		<c:if test="${!empty item['flowcode'] && '4'==item['reportstate'] }">
					 			<a href="${ctx}/index.htm?m=checkAutitySpeed&id=${item['taskId']}" class="J_single_data_speed">审批进度</a>
					 		</c:if>
						</td>
					 </tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="common-tips"></div>
	<script type="text/javascript">
		seajs.use('${ctx}/js/func/index/index');
	</script>
</body>
</html>