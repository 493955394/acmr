<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%> 
		<div class="panel-default reset-panel homeP20">
			<div class="toolbar">
				<div class="toolbar-left">
				<c:choose>
					<c:when test="${list.readPage.totalRecorder==0}"><label class="control-label"><font class="green h5">待审批任务</font></label></c:when>
					<c:otherwise><label class="control-label"><font class="green h5">待审批任务数量: ${list.readPage.totalRecorder}</font></label></c:otherwise>
				</c:choose>
				</div> 			
				<div class="toolbar-right">
					<a href="${ctx}/dataapprove/dataapprove.htm" class="green h5"><label>更多任务&gt;&gt;</label></a>
				</div>
			</div>
		</div>
		<table class="table table-striped table-hover index-table">
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
					<c:if test="${list.readPage.totalRecorder==0 or list.readPage.totalPage<list.readPage.pageNum}">
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
			<c:forEach items="${list.readPage.data}" var="item" end="5">
				<tr>
					<td class="wrap">${item['reportcode']}</td>
					<td class="wrap"><a href="${ctx}/dataapprove/dataapprove.htm?m=goReportPage&id=${item['code']}&taskId=${item['taskId']}&state=1&pageType=${pageType}">${item['reportname']}</a></td>
					<td>${item['systemname']}</td>
					<td>${item['reportstage']}</td>
				 	<td>${item['deptname']}</td>
				 	<td>
				<c:if test="${''==item['sort']}"></c:if>
				<c:if test="${'0'==item['sort']}">一次性的</c:if>
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
				 	已生成，待填报
				 	</c:if>
				 	<c:if test="${'2'==item['reportstate']}">
				 	已上传
				 	</c:if>
				 	<c:if test="${'3'==item['reportstate']}">
				 	待审批
				 	</c:if>
				 	<c:if test="${'4'==item['reportstate']}">
				 	已入库
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
				 	<a href="${ctx}/index.htm?m=toPassMoreOrOne" id="${item['taskId']}" class="J_batch_submit_checkbox_data"><font class="green">通过</font></a>&nbsp;&nbsp; <a href="" class="J_write_bach_back" id="${item['taskId']}"><font class="green">退回</font></a> 
				 	</td>
				 </tr>		
			</c:forEach>
			</tbody>
		</table>