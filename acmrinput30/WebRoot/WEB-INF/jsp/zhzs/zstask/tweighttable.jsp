<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/7
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div style="overflow: auto;height: 55%;">
    <c:if test="${right!='0'}">
        <%--<select class="input-sm weight_select" style="float: right">
            <option>请选择</option>
            <option id="re_weight">恢复默认值</option>
        </select>--%>
        <button type="button" class="btn btn-default btn-sm weight_select" style="float: right;margin-bottom: 15px"><i id="i1" class="glyphicon glyphicon-floppy-saved"></i>&nbsp;&nbsp;恢复默认值</button>
    </c:if>
    <table class="table table-bordered table-hover" >
        <c:forEach items="${mods}" var="module">
            <c:if test="${module.getProcode()==''}">
                    <tbody>
                    <tr>
                        <td rowspan="${module.ZBnums()}" flag="0" class="root_zs p_${module.getCode()}">${module.getCname()}</td>
                            <%--<td>test</td>--%>
                    </tr>
                    </tbody>
            </c:if>
        </c:forEach>
    </table>
</div>
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script>
    $(".root_zs").each(function () {
        var rnums=$(this).attr("rowspan")-1
        for (var i=rnums;i>0;i--){
            $(this).parent().after("<tr></tr>")
        }
    })

    <c:forEach items="${mods}" var="module">
    <c:if test="${module.getProcode()!=''}">
    var classname="${module.getProcode()}"
    var thiszbnums= parseInt("${module.ZBnums()}")
    //同一父节点的第一个
    if (${module.getSortcode().equals("0")}){
        $(".p_"+classname).after("<td code='" +
            "${module.getCode()}"+"' procode='" +
            "${module.getProcode()}"+"' sort='" +
            "${module.getSortcode()}"+"'  rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "${module.getCname()}"+"</td><td  flag='0' class='" +
            "p_${module.getCode()}"+"' rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'>")
        $(".p_"+classname).attr("flag",parseInt($(".p_"+classname).attr("flag"))+thiszbnums)
    }
    else {
        var index=parseInt($(".p_"+classname).attr("flag"))-1
        $(".p_"+classname).parent().nextAll(":eq(" +
            index+")").append("<td code='" +
            "${module.getCode()}"+"' procode='" +
            "${module.getProcode()}"+"' sort='" +
            "${module.getSortcode()}"+"' rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "${module.getCname()}"+"</td><td flag='0' class='" +
            "p_${module.getCode()}"+"' rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'"+"</td>")
        $(".p_"+classname).attr("flag",parseInt($(".p_"+classname).attr("flag"))+thiszbnums)
    }
    </c:if>
    </c:forEach>
</script>



