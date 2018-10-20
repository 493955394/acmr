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
        background-color:#ff7f19;
    }
    .glyphicon{
        color:#FF7F19;
    }
</style>
<div style="height: 500px;width: 100%;overflow:auto;display: none " id="regtable">
    <table class="table table-bordered" id="tabledata" >
        <thead>
        <tr>
        <th>时间</th>
        <th style="min-width: 200px">指标</th>
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
                        <c:when test="${list1== null || list1==''}">
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
<br>
<div style="height: 500px;width:100%;overflow:auto; display: none" id="data_single">
    <div class="col-md-4" style="font-size: 15px">
        检查结果：${regname}
    </div>
    <button class="J_excel_singlereg col-md-offset-1 col-md-3" style="font-size: 10px"><i class="glyphicon glyphicon-save"></i>&nbsp;本地区数据下载</button>
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
                    <c:choose>
                    <c:when test="${sing==''||sing ==null}">
                        <td class="red"></td>
                    </c:when>
                    <c:otherwise >
                        <td>${sing}</td>
                    </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>