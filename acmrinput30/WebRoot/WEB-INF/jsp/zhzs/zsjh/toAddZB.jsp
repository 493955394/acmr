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
    <title>${projectTitle}-新增模型节点</title>
    <%@ include file="/WEB-INF/jsp/common/libs.jsp"%>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="container-fluid">
    <div class="panel panel-default">
        <div class="panel-heading">基本信息</div>
        <div class="panel-body">
            <form class="form-horizontal J_addZS_form" action="${ctx}/zbdata/zsjhedit.htm?m=toSaveZS">
                <input type="hidden" name="procodeId" value="${datas.procodeId}"/>
                <input type="hidden" name="icode" value="${datas.indexCode}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="ZS_code" value="${code}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="ZS_cname" value="${name}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">节点类别：</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="ifzs" autocomplete="off" id="selectifzs">
                            <c:choose>
                                <c:when test="${datas.procodeId == '' || datas.procodeId== null}">
                                    <option value="2">总指数</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="1">次级指数</option>
                                    <option value="0">指标</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
                <div id="secend_zs" style="display: none">
                    <span class="col-sm-2">次级指数设置</span>
                    <hr class="col-sm-10"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属节点：</label>
                        <div class="col-sm-5">
                            <select class="form-control cjzs" name="cjzs" autocomplete="off" >
                                <c:forEach items="${zslist}" var="list">
                                    <option value="${list.getCode()}" <c:if test="${list.getCode() == datas.procodeId}"> selected</c:if>>${list.getCname()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div id="select_zb" style="display: none">
                    <span class="col-sm-2">指标设置</span>
                    <hr class="col-sm-10"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属节点：</label>
                        <div class="col-sm-5">
                            <select class="form-control zb_ifzs" name="zb_ifzs" autocomplete="off" >
                                <c:forEach items="${zslist}" var="list">
                                    <option value="${list.getCode()}" <c:if test="${list.getCode() == datas.procodeId}"> selected</c:if>>${list.getCname()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">指标类型：</label>
                        <div class="col-sm-5">
                            <select class="form-control formula" name="formula" autocomplete="off">
                                <c:forEach items="${zblist.zbchoose}" var="zbl">
                                    <option value="${zbl.zbcode}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                </c:forEach>
                                <option value="userdefined">自定义</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">小数点位数：</label>
                    <div class="col-sm-5">
                        <input name="dotcount" type="text" class="form-control" value="1"/>
                    </div>
                </div>
                <div class="hidden_group" style="display: none">
                    <div class="row">
                        <div class="col-sm-2">
                            <span>公式编辑器：</span>
                        </div>
                        <div class="col-sm-3">
                            <select size="15" class="zb_index" style="width: 90%">
                                <c:forEach items="${zblist.zbchoose}" var="zbl">
                                    <option value="${zbl.zbcode}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-1">
                            <button type="button">添加></button>
                        </div>
                        <div class="col-sm-3">
                            <select size="5" style="width: 300px">
                            </select>
                            <div class="clearfix"></div>
                            <p></p>
                            <button class="btn btn-default" type="button">1</button>
                            <button class="btn btn-default" type="button">2</button>
                            <button class="btn btn-default" type="button">3</button>
                            <button class="btn btn-default" type="button">+</button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default" type="button">4</button>
                            <button class="btn btn-default" type="button">5</button>
                            <button class="btn btn-default" type="button">6</button>
                            <button class="btn btn-default" type="button">-</button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default" type="button">7</button>
                            <button class="btn btn-default" type="button">8</button>
                            <button class="btn btn-default" type="button">9</button>
                            <button class="btn btn-default" type="button">*</button>
                            <div class="clearfix"></div>
                            <button class="btn btn-default" type="button">()</button>
                            <button class="btn btn-default" type="button">0</button>
                            <button class="btn btn-default" type="button">.</button>
                            <button class="btn btn-default" type="button">/</button>
                        </div>
                        <div class="col-sm-1">
                            <button type="button"><添加</button>
                        </div>
                        <div class="col-sm-2">
                            <select size="15" >
                                <option>Math.abs</option>
                                <option>Math.max</option>
                                <option>Math.min</option>
                                <option>Math.pow(x,y)</option>
                                <option>Math.exp</option>
                                <option>Math.log10</option>
                                <option>Math.log</option>
                                <option>Math.random</option>
                                <option>add(BigInteger val)</option>
                                <option>subtract(BigInteger val)</option>
                                <option>multiply(BigInteger val)</option>
                                <option>divide(BigInteger val)</option>
                                <option>compareTo(BigInteger val)</option>
                                <option>pow(int exponent)</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary ZS_Add">确认</button>
                        <button type="reset" class="btn btn-primary">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/toAdd');
</script>
</body>
</html>