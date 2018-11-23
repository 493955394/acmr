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
    .tb-head table{
        width: 100%;
        position: absolute;
    }
    .tb-body{
        width: 100%;
        height: 71%;
        overflow: auto;

    }
    .tb-head1 table{
        width: 100%;
        position: absolute;
    }
    .tb-body1{
        width: 100%;
        overflow: auto;
    }
</style>
<div style="display: none" class="regtable">
    <div style="padding-top: 10px;"></div>
    <div class="tb-head" style="position: absolute; overflow: hidden;">
    <table class="table table-bordered regs-data-check">
        <thead  style="font-size: 15px;background-color: #F5F5F5;">
        <tr>
        <th class="table-th" id="regcolumn1">时间</th>
        <th class="table-th" id="regcolumn2">指标</th>
        <c:forEach items="${regs}" var="reg" varStatus="i">
            <th class="table-th" id="regcolumn${i.count+2}">${reg}</th>
        </c:forEach>
        </tr>
    </thead>
        <input type="hidden" value="${check}" id="checkreturn">
    </table>
    </div>
    <div class="tb-body">
        <table  class="table table-bordered" id="tabledata">
            <thead  style="font-size: 15px;background-color: #F5F5F5;">
            <tr class="regs-data-check1">
                <th class="regcolumn1">时间</th>
                <th class="regcolumn2">指标</th>
                <c:forEach items="${regs}" var="reg" varStatus="i">
                    <th class="regcolumn${i.count+2}">${reg}</th>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${data}" var="list">
                <tr>
                    <c:forEach items="${list}" var="list1" varStatus="i">
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
        </table>
    </div>
</div>

<div style="display: none" class="data_single">
    <div style="font-size: 15px;margin-top: 5px">
        检查结果：${regname}
        <button class="J_excel_singlereg btn btn-default btn-sm" style="float:right;font-size: 10px;margin-bottom: 5px"><i class="glyphicon glyphicon-save"></i>&nbsp;本地区数据下载</button>
    </div>
    <div style="width:100%;">
        <div class="tb-head1" style="position: absolute; overflow: hidden;margin-top:14px">
            <table class="table table-bordered regs-data-check2" >
                <thead style="font-size: 15px;background-color: #F5F5F5;">
                <tr>
                    <th class="table-th1" id="s-regcolumn1">指标</th>
                    <c:forEach items="${times}" var="time" varStatus="i">
                        <th class="table-th1" id="s-regcolumn${i.count+1}">${time}</th>
                    </c:forEach>
                </tr>
                </thead>
            </table>
        </div>
    <div class="tb-body1">
        <table class="table table-bordered" id="tabledata_single" >
            <thead style="font-size: 15px;background-color: #F5F5F5;">
            <tr class="regs-data-check3">
                <th class="s-regcolumn1">指标</th>
                <c:forEach items="${times}" var="time" varStatus="i">
                    <th class="s-regcolumn${i.count+1}">${time}</th>
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
    </div>
</div>