<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/7
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

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


                            <%--<td>test</td>--%>
                    </tr>
                    </tbody>
                </c:if>
            </c:if>
        </c:forEach>
    </table>
</div>
<script>
    define("weightset",function (require,exports,module) {
        var $=require('jquery')
        $(".root_zs").each(function () {
            var rnums=$(this).attr("rowspan")
            for(var i=rnums-1;i>0;i--){
                $(this).parent().after("<tr></tr>")
            }

        })

        var flag=0;//行计数器
        <c:forEach items="${mods}" var="module">
        <c:if test="${module.getProcode()!=''&&module.ZBnums()!=0}">
        //console.log("处理："+"${module.getCname()}")
        var thiszbnums= parseInt("${module.ZBnums()}")
        var classname="${module.getProcode()}"
        var rnums=$("."+classname).parent().parent().children(":eq(0)").children(":eq(0)").attr("rowspan")
        //说明是这一级别的第一个节点
        if (flag==0){
            //console.log("是第一个节点")
            //第一节点肯定不能上移
            $("."+classname).after("<td code='" +
                "${module.getCode()}"+"' procode='" +
                "${module.getProcode()}"+"' sort='" +
                "${module.getSortcode()}"+"'  rowspan='" +
                "${module.ZBnums()}"+"'>" +
                "${module.getCname()}"+"</td><td rowspan='" +
                "${module.ZBnums()}"+"'>" +
                "<input class='input_weight " +
                "${module.getProcode()}"+"' value='" +
                "${module.getWeight()}"+"'>"+"</td><td class='" +
                "${module.getCode()}"+"' rowspan='" +
                "${module.ZBnums()}"+"'><label class='btn-disabled mod_up_noclick'>上移</label><a href='#' class='mod_down'>下移</a></td>")
            flag=flag+thiszbnums;
            if (flag==rnums){
                flag=0
            }
            //console.log(flag)
        }
        //不是此级别的第一个节点
        else {
           // console.log("不是第一个节点")
            var index=flag
            var thissort="${module.getSortcode()}"
            //不能上移
            if (thissort=="0"){
                $("."+classname).parent().parent().children(":eq(" +
                    index+")").append("<td code='" +
                    "${module.getCode()}"+"' procode='" +
                    "${module.getProcode()}"+"' sort='" +
                    "${module.getSortcode()}}"+"' rowspan='" +
                    "${module.ZBnums()}"+"'>" +
                    "${module.getCname()}"+"</td><td rowspan='" +
                    "${module.ZBnums()}"+"'>" +
                    "<input class='input_weight " +
                    "${module.getProcode()}"+"' value='" +
                    "${module.getWeight()}"+"'"+"</td><td rowspan='" +
                    "${module.ZBnums()}"+"' class='" +
                    "${module.getCode()}"+"'><label class='btn-disabled mod_up_noclick'>上移</label><a href='#' class='mod_down'>下移</a></td>")
            }
            //不能下移
            else if (${module.isLast()==true}){
                $("."+classname).parent().parent().children(":eq(" +
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
                    "${module.getCode()}"+"'><a href='#' class='mod_up'>上移</a><label class='btn-disabled mod_up_noclick'>下移</label></td>")
            }
            else {
                $("."+classname).parent().parent().children(":eq(" +
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
                    "${module.getCode()}"+"'><a href='#' class='mod_up'>上移</a><a href='#' class='mod_down'>下移</a></td>")
            }

            flag=flag+thiszbnums
            if (flag==rnums){
                //console.log("flag="+flag+"flag重置")
                flag=0
            }
            //console.log(flag)
        }

        </c:if>
        </c:forEach>

    })
</script>
