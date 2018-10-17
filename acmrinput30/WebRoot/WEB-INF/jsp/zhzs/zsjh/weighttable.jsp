<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/7
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    .glyphicon{
        color:#FF7F19;
    }
</style>
<body>
<div class="container-fluid" id="mainpanel">
    <h2>权重设置</h2>
    <button type="button" class="btn btn-default btn-sm save_weight" style="float: right"><i id="i1" class="glyphicon glyphicon-floppy-saved"></i>&nbsp;&nbsp;保存设置</button>
    <table class="table table-striped table-hover">
        <c:forEach items="${mods}" var="module">
            <c:if test="${module.getProcode()==''}">
                <c:if test="${module.ZBnums()!=0}">
                    <tbody>
                    <tr>
                        <td rowspan="${module.ZBnums()}" flag="0" class="root_zs p_${module.getCode()}">${module.getCname()}</td>
                            <%--<td>test</td>--%>
                    </tr>
                    </tbody>
                </c:if>
            </c:if>
        </c:forEach>
    </table>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script>
    console.log("weightset")
    $(".root_zs").each(function () {
        var rnums=$(this).attr("rowspan")
        for(var i=rnums-1;i>0;i--){
            $(this).parent().after("<tr></tr>")
        }
    })
    <c:forEach items="${mods}" var="module">
    <c:if test="${module.getProcode()!=''&&module.ZBnums()!=0}">
    var classname="${module.getProcode()}"
    var thiszbnums= parseInt("${module.ZBnums()}")
    //console.log("sortcode"+"${module.getSortcode()}")
    //同一父节点的第一个
    if (${module.getSortcode().equals("0")&&module.isLast()==false}){
        //console.log("first:"+"${module.getCname()}")
        $(".p_"+classname).after("<td code='" +
            "${module.getCode()}"+"' procode='" +
            "${module.getProcode()}"+"' sort='" +
            "${module.getSortcode()}"+"'  rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "${module.getCname()}"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'>"+"</td><td class='" +
            "p_${module.getCode()}"+"' flag='0' rowspan='" +
            "${module.ZBnums()}"+"'><label class='btn-disabled mod_up_noclick'>上移</label><a href='#' class='mod_down'>下移</a></td>")
        $(".p_"+classname).attr("flag",parseInt($(".p_"+classname).attr("flag"))+thiszbnums)
    }
    else if (${module.getSortcode().equals("0")&&module.isLast()==true}){
        $(".p_"+classname).after("<td code='" +
            "${module.getCode()}"+"' procode='" +
            "${module.getProcode()}"+"' sort='" +
            "${module.getSortcode()}"+"'  rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "${module.getCname()}"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'>"+"</td><td class='" +
            "p_${module.getCode()}"+"' flag='0' rowspan='" +
            "${module.ZBnums()}"+"'><label class='btn-disabled mod_up_noclick'>上移</label><label class='btn-disabled mod_down_noclick'>下移</label></td>")
        $(".p_"+classname).attr("flag",parseInt($(".p_"+classname).attr("flag"))+thiszbnums)
    }

    else if (${module.isLast()==true}) {
        //console.log("last"+"${module.getCname()}")
        //console.log( $(".p_"+classname).parent().nextAll())
        var index=parseInt($(".p_"+classname).attr("flag"))-1
        $(".p_"+classname).parent().nextAll(":eq(" +
            index+")").append("<td code='" +
            "${module.getCode()}"+"' procode='" +
            "${module.getProcode()}"+"' sort='" +
            "${module.getSortcode()}"+"' rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "${module.getCname()}"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"' class='" +
            "p_${module.getCode()}"+"' flag='0'><a href='#' class='mod_up'>上移</a><label class='btn-disabled mod_up_noclick'>下移</label></td>")
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
            "${module.getCname()}"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"'>" +
            "<input class='input_weight " +
            "${module.getProcode()}"+"' value='" +
            "${module.getWeight()}"+"'"+"</td><td rowspan='" +
            "${module.ZBnums()}"+"' class='" +
            "p_${module.getCode()}"+"' flag='0'><a href='#' class='mod_up'>上移</a><a href='#' class='mod_down'>下移</a></td>")
        $(".p_"+classname).attr("flag",parseInt($(".p_"+classname).attr("flag"))+thiszbnums)

    }



    </c:if>
    </c:forEach>



</script>


