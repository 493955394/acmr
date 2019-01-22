<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<div class="rangetable_container" >
    <table class="table table-bordered table-hover table_head">
        <tr class="zb_row">
            <c:forEach var="zb" items="${zbrow}">
                <td>${zb}</td>
            </c:forEach>
        </tr>
        <tr class="sj_row">

        </tr>
    </table>
</div>



