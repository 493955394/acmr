<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${projectTitle}- 反馈详细</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
    <style type="text/css">
        .feedTableList{
            clear: both;
            margin:30px 0;
        }
        .feedTableList .title{
            float: left;
            margin-left: 20px;
        }
        .feedTableList .content{
            margin-left: 100px;
        }
        .feed-title{
            color:#ff7f19; padding-bottom:18px;display:block;
            font-weight: 900;
        }
        .feed-supply-container{
            margin: 18px 0;
            overflow: hidden;
            padding-top: 8px;
            border-top:1px solid #ddd;
        }
        .feed-supply-info{
            float: left;
            width: 35%;
            text-align: right;
            margin-right: 30px;
        }
        .feed-suggest-content{
            padding: 10px 20px;
        }
    </style>
    <input type ="hidden" value="${df.id}" id="feedId">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
    <div class="text-center">
        <h4 class="feed-title">${df.title}</h4>
    </div>
	<c:if test="${ df.state != '1'}">
		<div class="feed-supply-container">
			<div class="feed-supply-info">
				<label for="">审批人:</label> <span>${df.userId}</span>
			</div>
			<div class="feed-supply-info">
				<label for="">审批时间:</label> <span>${df.updateTime}</span>
			</div>
		</div>
		<div class="feed-suggest-content">
		<label for="">审批意见:</label>
		   ${df.approvalContent }</div>
	</c:if>
	<div class="feed-supply-container">
        <div class="feed-supply-info">
            <label for="">提交人:</label>
            <span>${df.userId}</span>
        </div>
        <div class="feed-supply-info">
            <label for="">提交时间:</label>
            <span>${df.updateTime}</span>
        </div>
    </div>
    <c:if test="${existExcel}"></c:if>
        <div class="panel-body">
            <div style="background: #f7f7f9;border: 1px solid #e1e1e8;padding: 9px 14px;">
                <span style="color:#ff7f19;">附件：</span>
                <span id="${id}" class="J_excel_download" style="background: #ccc;padding: 5px 15px;">
                <span class="glyphicon glyphicon-file" style="color:#999;"></span>
                <span> 
					<label for=""><a href="${ctx }/personal/feedback.htm?m=download&id=${df.id}"
					>${df.fileName }</a></label>
				</span>
                </span>
            </div>
        </div>
    
    <div class="feedTableList">
        <p class="title"><label>维度详细</label></p>
        <div class="mr-content content">
	        <table class="table table-bordered">
	            <thead>
	                <tr>
	                    <th>指标</th>
	                    <th>分组</th>
	                    <th>地区</th>
	                    <th>数据来源</th>
	                    <th>时间类型</th>
	                    <th>时间</th>
	                    <th>主体</th>
	                </tr>
	            </thead>
	            <tbody>
	                    <tr>
	                        <td>${dataDetail.dimension.zb }</td>
	                        <td>${dataDetail.dimension.fz }</td>
	                        <td>${dataDetail.dimension.dq }</td>
	                        <td>${dataDetail.dimension.sjly }</td>
	                        <td>${dataDetail.dimension.sjlx }</td>
	                        <td>${dataDetail.dimension.sj }</td>
	                        <td>${dataDetail.dimension.zt }</td>
	                    </tr>
	            </tbody>
	        </table>
    	</div>
  	</div>
  	<div class="feedTableList">
        <p class="title"><label>版本详情</label></p>
        <div class="mr-content content">
	        <table class="table table-bordered">
	            <thead>
	                <tr>
	                    <th>版本号</th>
	                    <th>版本内容</th>
	                    <th>单位</th>
	                    <th>是否预测值</th>
	                     <th>更新时间</th>
	                    <th>更新人员</th>
	                    <th>备注</th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach items="${dataDetail.logs }" var="item" varStatus="status">
	                    <tr>
	                        <td>${item.version }</td>
	                        <td>${item.content }</td>
	                        <td>${item.unit }</td>
	                        <td>
		                        <%-- <c:if test="${item.forecast == '0'}">否</c:if>
		                        <c:if test="${item.forecast == '1'}">是</c:if> --%>
		                        ${item.forecast}
	                        </td>
	                        <td>${item.date }</td>
	                        <td>${item.user }</td>
	                        <td>${item.memo }</td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
    	</div>
  	</div>
  	 <div class="feedTableList">
        <p class="title"><label>数据修订</label></p>
        <div class="mr-content content">
	        <table class="table table-bordered">
	            <thead>
	                <tr>
	                    <th>建议值</th>
	                    <th>单位</th>
	                    <th>是否预测值</th>
	                    <th>备注</th>
	                </tr>
	            </thead>
	            <tbody>
	                    <tr>
	                        <td>${df.proposeValue}</td>
	                        <td>${df.unit}</td>
	                        <td>
	                         	<c:if test="${df.pForecast == '0'}">否</c:if>
		                        <c:if test="${df.pForecast == '1'}">是</c:if>
	                        </td>
	                        <td>${df.memo }</td>
	                    </tr>
	            </tbody>
	        </table>
    	</div>
  	</div>
  	<div class="feedback-btn-container">
			<div class="feedback-btn-group">
			
				<button type="button" class="btn feedback-btn Jsuggest2" onclick="history.go(-1)">返回</button>
			</div>
		</div>
  	<script>
  		seajs.use('${ctx}/js/func/personal/main.js');
  	</script>
</body>
</html>