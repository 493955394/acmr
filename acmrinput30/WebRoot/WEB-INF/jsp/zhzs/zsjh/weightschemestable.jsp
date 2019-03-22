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
<div style="height:70%;width: 100%;overflow: auto;margin-top: 10px" class="container-fluid" id="mainpanel">
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
    <table class="table table-bordered" id="module_table" style="width: 100%;overflow:hidden; white-space:nowrap; text-overflow:ellipsis;text-align: center;font-size: 14px;">
        <tr style="background-color: #F5F5F5;font-weight: bold;font-family: 'Microsoft YaHei';" id="row_head1">
            <td rowspan="2">总指数</td>
            <%--<td colspan="2">指标</td>--%>
        </tr>

        <tr style="background-color: #F5F5F5;font-weight: bold;font-family: 'Microsoft YaHei';" id="row_head2">
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
            "<tr class='row_body'><td rowspan=${module.ZBnums()} code=${module.getCode()} flag=${module.ZBnums()}>${module.getCname()}</td></tr>"
        )
        for (var i=zbnums;i>1;i--){
            $("#module_table").append(
                "<tr class='row_body'></tr>"
            )
        }
     </c:if>
    //指数
    <c:if test="${!module.getProcode().equals('')&&module.getIfzs().equals('1')}">
    var flag=$("td[code='${module.getProcode()}']:first").attr("flag")
    var rows=$("td[code='${module.getProcode()}']:first").attr("rowspan")
    //和父节点同行
    if (flag==rows){
        console.log("${module.getCname()}:${module.getProcode()}flag:"+flag+",rows:"+rows)
        $("td[code='${module.getProcode()}']:first").parent().append(
            "<td rowspan=${module.ZBnums()} class='count_mod'>${module.getCname()}</td>")

        <c:forEach items="${scodes}" var="scode">
        $("td[code='${module.getProcode()}']:first").parent().append(
            "<td style=\"text-align:center\" code=${module.getCode()} scode='${sode}' rowspan=${module.ZBnums()} flag=${module.ZBnums()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}' scode='${scode}' code='${module.getCode()}' placeholder='请输入权重' class='input_weight' value='${module.getSweight(scode)}'></td>"
        )
        </c:forEach>
        $("td[code='${module.getProcode()}']:first").attr("flag",flag-${module.ZBnums()})
    }
    //与父节点不同行
    else {
        //console.log($("td[code='${module.getProcode()}']:first").parent().nextAll())
        $("td[code='${module.getProcode()}']:first").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append("<td rowspan=${module.ZBnums()} class='count_mod'>${module.getCname()}</td>")
        <c:forEach items="${scodes}" var="scode">
        $("td[code='${module.getProcode()}']:first").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append(
            "<td style=\"text-align:center\" code=${module.getCode()} scode='${sode}' rowspan=${module.ZBnums()} flag=${module.ZBnums()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}' scode='${scode}' code='${module.getCode()}' placeholder='请输入权重' class='input_weight' value='${module.getSweight(scode)}'></td>"
        )
        </c:forEach>


        $("td[code='${module.getProcode()}']:first").attr("flag",flag-${module.ZBnums()})

    }


    </c:if>
    //指标
    <c:if test="${!module.getProcode().equals('')&&module.getIfzs().equals('0')}">
    var flag=$("td[code='${module.getProcode()}']:first").attr("flag")
    var rows=$("td[code='${module.getProcode()}']:first").attr("rowspan")
    //和父节点同行
    if (flag==rows){
        $("td[code='${module.getProcode()}']:last").parent().append(
            "<td class='count_mod'>${module.getCname()}</td>"
        )

        <c:forEach items="${scodes}" var="scode">
        $("td[code='${module.getProcode()}']:last").parent().append(
            "<td style=\"text-align:center;min-width:480px\" code=${module.getCode()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}' scode='${scode}' code='${module.getCode()}'  placeholder='请输入权重' class='input_weight' value='${module.getSweight(scode)}'>&nbsp;&nbsp;公式：<input style=\"width:280px\" value='${module.getSformula(scode)}' readonly>  <a href='#'class='edit_formula' modcode='${module.getCode()}' scode='${scode}'>编辑</a></td>"
        )
        </c:forEach>


        $("td[code='${module.getProcode()}']:first").attr("flag",flag-1)
    }
    //与父节点不同行
    else {
        $("td[code='${module.getProcode()}']:last").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append("<td class='count_mod'>${module.getCname()}</td>")
        <c:forEach items="${scodes}" var="scode">
        $("td[code='${module.getProcode()}']:last").parent().nextAll(":eq(" +
            (rows-flag-1)+")").append(
            "<td style=\"text-align:center;min-width:480px\" code=${module.getCode()}><input style=\"width:86px;text-align:center\" pcode='${module.getProcode()}' scode='${scode}' code='${module.getCode()}' placeholder='请输入权重' class='input_weight' value='${module.getSweight(scode)}'>&nbsp;&nbsp;公式：<input style=\"width:280px\" value='${module.getSformula(scode)}' readonly>  <a href='#'class='edit_formula' modcode='${module.getCode()}' scode='${scode}'>编辑</a></td>"
        )
        </c:forEach>

        $("td[code='${module.getProcode()}']:first").attr("flag",flag-1)

    }
    </c:if>
    </c:forEach>
    console.log("${count}")
    var colnum=parseInt("${count}")-1;
    /*$(".row_body").each(function () {
        var count=$(this).children("[class~='count_mod']").length;
        if (count>colnum) colnum=count;
    })*/
    //console.log(colnum)
    for (i=0;i<colnum;i++){
        $("#row_head1").append("<td colspan='${scodes.size()+1}'>指标</td>")
        $("#row_head2").append("<td></td>");
        <c:forEach items="${snames}" var="sname">
        $("#row_head2").append("<td style=\"width:80px;overflow:hidden; white-space:nowrap; text-overflow:ellipsis\">${sname}</td>")
        </c:forEach>
    }
    $(document).ready(function(){
        //footer位置设置
        function footerPosition(){
            $(".footer").removeClass("fixed-footer");
            var contentHeight = document.body.scrollHeight,//网页正文全文高度

                winHeight = window.innerHeight;//可视窗口高度，不包括浏览器顶部工具栏
            if(!(contentHeight > winHeight)){
                //当网页正文高度小于可视窗口高度时，为footer添加类fixed-footer
                $(".footer").addClass("fixed-footer");
                $(".content").height(winHeight);
            } else {
                $(".footer").removeClass("fixed-footer");
            }
        }
        footerPosition();
        $(window).resize(footerPosition);
        if($("#result-ifcomplete").val()=="true")
            alert("原始数据缺失");
    })

</script>


