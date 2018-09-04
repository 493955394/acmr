<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/31
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style type="text/css">
    .red{
        background-color: red;
    }
</style>
<div>
    <table class="table table-bordered" id="tabledata" style="display: none">
        <thead>
        <tr>
        <th class="text-center">时间</th>
        <th>指标</th>
        <c:forEach items="${regs}" var="reg">
            <th>${reg}</th>
        </c:forEach>
        </tr>
    </thead>
        <tbody>
        <c:forEach items="${data}" var="list">
            <tr>
                <c:forEach items="${list}" var="list1">
                    <c:if test="${list1=='0.0'}">
                        <td class="red"></td>
                    </c:if>
                    <c:if test="${list1!='0.0'}">
                        <td>${list1}</td>
                    </c:if>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
        <input type="hidden" value="${check}" id="checkreturn">
    </table>
</div>
<div style="display: none" id="data_single">
    <span>
        检查结果：${regname}
    </span>
    <table class="table table-bordered" id="tabledata_single" >
        <thead>
        <tr>
            <th>指标</th>
            <c:forEach items="${times}" var="time">
                <th>${time}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${singledata}" var="singgle">
            <tr>
                <c:forEach items="${singgle}" var="sing">
                    <c:if test="${sing=='0.0'}">
                        <td class="red"></td>
                    </c:if>
                    <c:if test="${sing!='0.0'}">
                        <td>${sing}</td>
                    </c:if>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>