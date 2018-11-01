<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    th{
        text-align: center;
        vertical-align: middle!important;
        background-color: #EBECF1;
    }
    td{
        text-align: center;
        vertical-align: middle!important;
    }
</style>
<div  style="margin-top: 5px">
<span>${info.get("span")}：</span>
<select class="input-sm wd_selector">
    <c:forEach items="${info.get('options')}" var="option">
        <c:if test="${info.get('spancode')==option.get('code')}">
            <option class="wd_option" id="${option.get('code')}" selected="selected">${option.get('name')}</option>
        </c:if>
        <c:if test="${info.get('spancode')!=option.get('code')&&info.get('spancode')!=null}">
            <option class="wd_option" id="${option.get('code')}">${option.get('name')}</option>
        </c:if>
        <c:if test="${info.get('spancode')==null}">
            <option class="wd_option" id="${option.get('code')}">${option.get('name')}</option>
        </c:if>
    </c:forEach>
    <option class="wd_option" id="change">序列</option>
</select>
</div>
<div>
    <input type="hidden" class="tbgs" value="${fn:length(info.get('head'))}">
    <table style="margin-top: 10px" class="table table-hover pastviewtable">
        <thead>
        <tr>
            <th>${info.get('row')}</th>
            <c:forEach items="${info.get('head')}" var="head">
                <th>${head}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody class="list_body my_datas">
        <c:forEach items="${showdata}" var="mo">
            <tr>
                <c:forEach items="${mo}" var="td">
                    <td>${td}</td>
                </c:forEach>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>
<div class="ict-footer footer fixed-footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>