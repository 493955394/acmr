<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/8/31
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    #preview-table {
        table-layout: fixed;
    }
    .table{
        width: auto;
    }
</style>
<input type="hidden" id="result-ifcomplete" value="${flag}">
<div style="max-height:55%;max-width: 100%;overflow: auto;margin-top: 10px">
    <table class="table table-bordered J_jgyl_table" id="preview-table">
        <thead style="background-color: #e4edf6">

        </thead>
        <tbody>

        </tbody>

    </table>
</div>
