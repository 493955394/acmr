<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table class="table table-striped table-hover">
    <thead>
        <tr>            
            <th>标题</th>
            <th>发送时间</th>
            <th>状态</th>
            <th>审批人</th>
            <th>意见</th>
            <th width="280">操作</th>
        </tr>
    </thead>
    <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>没有查询到数据</td>
            <td></td>
        </tr>
    </c:if>
    <c:forEach items="${page.data}" var="item" varStatus="status">
      <tr>
            <td>${item.title}</td>
            <td>${item.updateTime}</td>
            <td>
                <c:if test="${item.state == '0'}">已退回</c:if>
                <c:if test="${item.state == '1'}">待审批</c:if>
                <c:if test="${item.state == '2'}">已通过</c:if>
            </td>
            <td>
                ${item.userId}
            </td>
            <td>
                ${item.approvalContent}
            </td>
            <td>
                <a href="${ctx}/personal/feedback.htm?m=findDataFeedInfo&id=${item.id}"  class="btn btn-default btn-sm J_dimension_info">查看</a>
                <c:if test="${item.state == '1'}">
                    <a href="" id="${item.id}" class="btn btn-default btn-sm J_datafeed_cancel">撤销</a>
                </c:if>
                <c:if test="${item.state == '2'}">
                    <label class="btn-opr btn-disabled">撤销</label>  
                </c:if>
            </td>
      </tr>  
    </c:forEach>
</table>
<div class="toolbar-right">
    <ul class="pagination J_dimension_pagination">${page}</ul>      
</div>

