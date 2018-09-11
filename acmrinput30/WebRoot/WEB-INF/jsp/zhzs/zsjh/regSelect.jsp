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
<div style="height: 500px;width: 710px;overflow:auto;display: none " id="regtable">
    <table class="table table-bordered" id="tabledata" >
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
                    <c:choose>
                        <c:when test="${list1=='0.0'||list1== null || list1==''}">
                            <td class="red"></td>
                        </c:when>
                        <c:otherwise>
                            <td>${list1}</td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
        <input type="hidden" value="${check}" id="checkreturn">
    </table>
</div>
<div style="height: 500px;width: 710px;overflow:auto; display: none" id="data_single">
    <span>
        检查结果：${regname}
    </span>
    <button class="J_excel_singlereg">本地区数据下载</button>
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