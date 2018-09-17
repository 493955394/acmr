<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/7
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<body>
<div class="container-fluid" id="mainpanel">
    <select style="float: right">
        <option>请选择</option>
        <option>恢复默认值</option>
    </select>
    <table class="table table-striped table-hover">
        <c:forEach items="${mods}" var="module">
            <c:if test="${module.getProcode()==''}">
                    <tbody>
                    <tr>
                        <td  flag="0" class="root_zs p_${module.getCode()}">${module.getCname()}</td>
                            <%--<td>test</td>--%>
                    </tr>
                    </tbody>
            </c:if>
        </c:forEach>
    </table>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>



