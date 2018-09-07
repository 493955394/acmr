<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="acmr.util.PubInfo" %>
<%@ page import="com.acmr.model.zhzs.IndexMoudle" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/5
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权重设置</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />

<div class="container-fluid" id="mainpanel">
    <h2>权重设置</h2>
    <button type="button" class="btn btn-primary btn-sm save_weight" style="float: right">保存设置</button>
    <table class="table table-striped table-hover">
        <c:forEach items="${mods}" var="module">
            <c:if test="${module.getProcode()==''}">
                <c:if test="${module.ZBnums()!=0}">
                    <tbody>
                    <tr>
                        <td rowspan="${module.ZBnums()}" class="root_zs ${module.getCode()}">${module.getCname()}</td>
                        <input type="hidden" name="module_code" value="${module.getCode()}">
                            <%--<td>test</td>--%>
                    </tr>
                    </tbody>
                </c:if>
            </c:if>
        </c:forEach>
    </table>
</div>

<div class="test"></div>

</body>

<script>
    define("weightset",function (require,exports,module) {
        var $=require('jquery')

        $(".root_zs").each(function () {
            //console.log("test")
            //var pcode=$(this).next().val()
            var rnums=$(this).attr("rowspan")
            //console.log(rnums)
            for(var i=rnums-1;i>0;i--){
                console.log("addtr")
                $(this).parent().after("<tr></tr>")
            }

        })

        var flag=0;//行计数器
        <c:forEach items="${mods}" var="module">
        <c:if test="${module.getProcode()!=''&&module.ZBnums()!=0}">
        console.log("处理："+"${module.getCname()}")
        var thiszbnums= parseInt("${module.ZBnums()}")
        var classname="${module.getProcode()}"
        var rnums=$("."+classname).parent().parent().children(":eq(0)").children(":eq(0)").attr("rowspan")
        //说明是这一级别的第一个节点
        if (flag==0){
            console.log("是第一个节点")
            if (${module.ZBnums()!=0}) {
                $("."+classname).after("<td class='" +
                    "${module.getCode()}"+"' rowspan='" +
                    "${module.ZBnums()}"+"'>" +
                    "${module.getCname()}"+"</td>")
            }
            else {
                $("."+classname).after("<td class='" +
                    "${module.getCode()}"+"'>" +
                    "${module.getCname()}"+"</td>")
            }
            flag=flag+thiszbnums;
            if (flag==rnums){
                flag=0
            }
            console.log(flag)
        }
        //不是此级别的第一个节点
        else {
            console.log("不是第一个节点")
            if(${module.ZBnums()!=0}){
                var index=flag
                $("."+classname).parent().parent().children(":eq(" +
                    index+")").append("<td class='" +
                    "${module.getCode()}"+"' rowspan='" +
                    "${module.ZBnums()}"+"'>" +
                    "${module.getCname()}"+"</td>")
            }
            else {
                var index=flag
                $("."+classname).parent().parent().children(":eq(" +
                    index+")").append("<td class='" +
                    "${module.getCode()}"+"'>" +
                    "${module.getCode()}"+"</td>")
            }
            flag=flag+thiszbnums
            if (flag==rnums){
                console.log("flag="+flag+"flag重置")
                flag=0
            }
            console.log(flag)
        }
        </c:if>
        </c:forEach>
    })
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/weight');
</script>

</html>

