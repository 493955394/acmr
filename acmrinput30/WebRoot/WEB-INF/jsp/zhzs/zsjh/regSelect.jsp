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
        background-color:#F39801;
    }
    .glyphicon{
        color:#F39801;
    }
    td{
        white-space: nowrap;
    }
</style>
<div style="height: 45%;width: 100%;overflow:auto;display: none " id="regtable">
    <table style="margin-top: 20px" class="table table-bordered" id="tabledata" >
        <thead style="font-size: 15px;background-color: #F5F5F5;">
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
<div style="height: 45%;width:100%;overflow:auto; display: none" id="data_single">
    <div class="col-md-4" style="font-size: 15px">
        检查结果：${regname}
    </div>
    <button class="J_excel_singlereg btn btn-default btn-sm col-md-offset-1 col-md-3" style="font-size: 10px"><i class="glyphicon glyphicon-save"></i>&nbsp;本地区数据下载</button>
    <table style="margin-top: 40px;height: 500px;width:100%;overflow:auto;" class="table table-bordered" id="tabledata_single" >
        <thead style="font-size: 15px;background-color: #F5F5F5;">
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