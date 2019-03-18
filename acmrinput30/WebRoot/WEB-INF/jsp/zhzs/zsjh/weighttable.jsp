<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/7
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<style>
    .glyphicon {
        color: #F39801;
    }

    thead {
        text-align: center;
        vertical-align: middle !important;
        background-color: #EBECF1;
    }

    td {
        text-align: center;
        vertical-align: middle !important;
    }
</style>
<body>
<div class="container-fluid" id="mainpanel">
    <div class="panel panel-default">
        <div class="panel-heading">
            权重设置
        </div>
    </div>
    <div style="height: 30px;">
        <span style="color: #fa7811;font-size:15px;line-height: 30px;">注：同级别权重和必须为1且权重不能小于0</span>
        <button type="button" class="btn btn-default btn-sm save_weight" style="float: right;margin-bottom: 15px"><i id="i1" class="glyphicon glyphicon-floppy-saved"></i>&nbsp;&nbsp;保存设置
        </button>
    </div>
    <table class="table table-bordered" id="module_table">

        <tr style="font-size: 15px;background-color: #F5F5F5;font-weight: bold;font-family: 'Microsoft YaHei';" id="row_head1">
            <td rowspan="2">总指数</td>
            <%--<td colspan="2">指标</td>--%>
        </tr>
        <tr style="font-size: 15px;background-color: #F5F5F5;font-weight: bold;font-family: 'Microsoft YaHei';" id="row_head2">
            <%--<td>空白</td>
            <td>方案</td>--%>
        </tr>
    </table>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script>

    <c:forEach items="${mods}" var="module">
    var zbnums=${module.ZBnums()}
        //总指数
        <c:if test="${module.getProcode().equals('')}">
        $("#module_table").append(
            "<tr class='row_body'><td rowspan=${module.ZBnums()} id=${module.getCode()} flag=${module.ZBnums()}>${module.getCname()}</td></tr>"
        )
    for (var i=zbnums;i>1;i--){
        $("#module_table").append(
            "<tr class='row_body'></tr>"
        )
    }
    </c:if>
    //指数
    <c:if test="${!module.getProcode().equals('')&&module.getIfzs().equals('1')}">
   // console.log("${module.getCname()}")
    //console.log($("#"+"${module.getProcode()}"))
    var flag=$("#"+"${module.getProcode()}").attr("flag")
    var rows=$("#"+"${module.getProcode()}").attr("rowspan")
    //和父节点同行
    if (flag==rows){
        $("#"+"${module.getProcode()}").after(
            "<td rowspan=${module.ZBnums()}>${module.getCname()}</td><td style=\"text-align:left;padding-left:25%\" id=${module.getCode()} rowspan=${module.ZBnums()} flag=${module.ZBnums()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}' placeholder='请输入权重' class='input_weight' value='${module.getWeight()}'></td>"
        )

        $("#"+"${module.getProcode()}").attr("flag",flag-${module.ZBnums()})
    }
    //与父节点不同行
    else {
        //$("#"+"${module.getProcode()}").parent().nextAll(":eq(1)").append("<td>test</td>")
        $("#"+"${module.getProcode()}").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append("<td rowspan=${module.ZBnums()}>${module.getCname()}</td><td style=\"text-align:left;padding-left:25%\" id=${module.getCode()} rowspan=${module.ZBnums()} flag=${module.ZBnums()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}'  placeholder='请输入权重'  class='input_weight' value='${module.getWeight()}'></td>")
        $("#"+"${module.getProcode()}").attr("flag",flag-${module.ZBnums()})
    }
    //console.log($("#"+"${module.getProcode()}").parent().nextAll())

    </c:if>
    //指标
    <c:if test="${!module.getProcode().equals('')&&module.getIfzs().equals('0')}">
    var flag=$("#"+"${module.getProcode()}").attr("flag")
    var rows=$("#"+"${module.getProcode()}").attr("rowspan")
    //和父节点同行
    if (flag==rows){
        $("#"+"${module.getProcode()}").after(
            "<td>${module.getCname()}</td><td style=\"text-align:left;padding-left:25%\" id=${module.getCode()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}'  placeholder='请输入权重' class='input_weight' value='${module.getWeight()}'>&nbsp;&nbsp;公式：<input style=\"width:280px\" value='${module.getFormula()}' readonly>  <a href='#'class='edit_formula' modcode='${module.getCode()}'>编辑</a></td>"
        )

        $("#"+"${module.getProcode()}").attr("flag",flag-1)
    }
    //与父节点不同行
    else {
        //$("#"+"${module.getProcode()}").parent().nextAll(":eq(1)").append("<td>test</td>")
        $("#"+"${module.getProcode()}").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append("<td>${module.getCname()}</td><td style=\"text-align:left;padding-left:25%\" id=${module.getCode()} ><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}'   placeholder='请输入权重' class='input_weight' value='${module.getWeight()}'>&nbsp;&nbsp;公式：<input style=\"width:280px\" value='${module.getFormula()}' readonly>  <a href='#'class='edit_formula' modcode='${module.getCode()}'>编辑</a></td>")
        $("#"+"${module.getProcode()}").attr("flag",flag-1)
    }
    </c:if>
    </c:forEach>
    //console.log(colnum)
    var colnum=0;
    $(".row_body").each(function () {
        var tdnum=$(this).children().length;
        if (tdnum>colnum){
            colnum=tdnum;
        }
    })
    console.log(colnum)
    for (var i=(colnum-1)/2;i>0;i--){
        $("#row_head1").append("<td colspan='2'>指标</td>")
        $("#row_head2").append("<td></td><td class='scheme_name'>${schemename}</td>")
    }

</script>


