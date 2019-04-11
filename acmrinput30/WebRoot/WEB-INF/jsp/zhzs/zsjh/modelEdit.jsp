<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/9/4
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${projectTitle}-编辑模型节点</title>
    <%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
    <style>
        .btn{
            border-color: #F39801;
            background-color: #F39801
        }
    </style>
</head>
<body>
<div class="ict-header">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true"/>
</div>
<div class="ict-page">
    <div class="container-fluid">
        <div>
            <span class="col-sm-offset-2 col-sm-3" style="font-size: 20px;color: #F39801;text-align: center;margin-top: 5px">-------------基本信息-------------</span><br>
            <div class="panel-body">
                <form class="form-horizontal J_addZS_form" action="${ctx}/zbdata/zsjhedit.htm?m=toUpdateZS">
                    <input type="hidden" name="icode" value="${icode}" class="input-small"/>
                    <input type="hidden" id="ifzs" value="${data.getIfzs()}" class="input-small"/>
                    <div class="form-group" style="display: none">
                        <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" name="ZS_code" value="${data.getCode()}" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">节点类别：</label>
                        <div class="col-sm-3">
                                <c:choose>
                                    <c:when test="${(data.getProcode() == '' || data.getProcode()== null)&&data.getIfzs()=='1'}">
                                        <input class="form-control" name="ifzs" id="selectifzs" readonly="" data-value="2" value="总指数">
                                    </c:when>
                                    <c:when test="${data.getIfzs()=='0'}">
                                        <input class="form-control" name="ifzs" id="selectifzs" readonly="" data-value="0" value="指标">
                                    </c:when>
                                    <c:otherwise>
                                        <input class="form-control" name="ifzs" id="selectifzs" readonly="" data-value="1" value="次级指数">
                                    </c:otherwise>
                                </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" name="ZS_cname" value="${data.getCname()}">
                        </div>
                    </div>
                    <div id="secend_zs" style="display: none">
                        <span class="col-sm-offset-2 col-sm-3" style="text-align:center;font-size: 20px;color: #F39801">----------次级指数设置-----------</span><br>
                        <br>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">父级节点：</label>
                            <div class="col-sm-3">
                                <select class="form-control cjzs" name="cjzs" autocomplete="off" >
                                    <c:forEach items="${zslist}" var="list">
                                        <option value="${list.getCode()}" <c:if test="${list.getCode() == data.getProcode()}"> selected</c:if>>${list.getCname()}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div id="select_zb" style="display: none">
                        <br>
                        <span class="col-sm-offset-2 col-sm-3" style="text-align:center;font-size: 20px;color: #F39801">------------指标设置-------------</span><br>
                        <div class="form-group">
                            <br>
                            <label class="col-sm-2 control-label">父级节点：</label>
                            <div class="col-sm-3">
                                <select class="form-control zb_ifzs" name="zb_ifzs" autocomplete="off" >
                                    <c:forEach items="${zslist}" var="list">
                                        <option value="${list.getCode()}" <c:if test="${list.getCode() == data.getProcode()}"> selected</c:if>>${list.getCname()}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">小数点位数：</label>
                        <div class="col-sm-3">
                            <input name="dotcount" type="text" class="form-control" value="${data.getDacimal()}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-6 col-sm-6">
                            <button type="submit" class="btn btn-primary ZS_Add">确认</button>
                            <button type="button" class="btn btn-primary resetbutton">重置</button>
                            <button type="button" class="btn btn-primary" onclick="window.close();">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="teset" style="padding-bottom: 10px;padding-top: 20px"></div>
<div class="ict-footer footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>
<script type="text/javascript">
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/modelEdit');
</script>
</body>
</html>
