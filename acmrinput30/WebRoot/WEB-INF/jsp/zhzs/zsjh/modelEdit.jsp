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
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="container-fluid">
    <div>
        <span class="col-sm-offset-2 col-sm-3" style="font-size: 20px;color: #F39801;text-align: center;margin-top: 5px">-------------基本信息-------------</span><br>
        <div class="panel-body">
            <form class="form-horizontal J_addZS_form" action="${ctx}/zbdata/zsjhedit.htm?m=toUpdateZS">
                <input type="hidden" name="icode" value="${icode}" class="input-small"/>
                <input type="hidden" id="ifzs" value="${data.getIfzs()}" class="input-small"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="ZS_code" value="${data.getCode()}" readonly>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="ZS_cname" value="${data.getCname()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">节点类别：</label>
                    <div class="col-sm-3">
                        <select class="form-control" name="ifzs" autocomplete="off" id="selectifzs" readonly="">
                            <c:choose>
                                <c:when test="${(data.getProcode() == '' || data.getProcode()== null)&&data.getIfzs()=='1'}">
                                    <option value="2" selected>总指数</option>
                                </c:when>
                                <c:when test="${data.getIfzs()=='0'}">
                                    <option value="0" selected>指标</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="1" selected>次级指数</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
                <div id="secend_zs" style="display: none">
                    <span class="col-sm-offset-2 col-sm-3" style="text-align:center;font-size: 20px;color: #F39801">----------次级指数设置-----------</span><br>
                    <br>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属节点：</label>
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
                        <label class="col-sm-2 control-label">所属节点：</label>
                        <div class="col-sm-3">
                            <select class="form-control zb_ifzs" name="zb_ifzs" autocomplete="off" >
                                <c:forEach items="${zslist}" var="list">
                                    <option value="${list.getCode()}" <c:if test="${list.getCode() == data.getProcode()}"> selected</c:if>>${list.getCname()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">指标类型：</label>
                        <div class="col-sm-3">
                            <select class="form-control formula" name="formula" autocomplete="off">
                                <c:forEach  items="${zblist.zbchoose}" var="zbl">
                                    <c:if test="${data.getIfzb() == '1'}" >
                                        <option value="${zbl.code}"<c:if test="${zbl.code == data.getFormula()}">selected</c:if>>${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                    </c:if>
                                    <c:if test="${data.getIfzb() == '0'}">
                                        <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                    </c:if>
                                </c:forEach>
                                    <option value="userdefined" <c:if test="${data.getIfzb() =='0'}">selected</c:if> >自定义</option>
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
                <div class="hidden_group form-group" style="display: none">
                    <div class="row">
                        <label class="col-sm-2 control-label">公式编辑器：</label>
                        <div class="col-sm-3">
                            <select size="15" class="zb_index" style="width: 90%">
                                <c:forEach items="${zblist.zbchoose}" var="zbl">
                                    <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-1">
                            <button type="button" class="btn btn-default" id="add_zb" style="border-color: #F39801;background-color: #F39801"><span style="color: white">添加></span></button>
                        </div>
                        <div class="col-sm-3">
                            <textarea rows="8" cols="35" id="formulatext" name="formulatext"><c:if test="${data.getIfzb() == '0'}">${data.getFormula()}</c:if></textarea>
                            <div class="clearfix"></div>
                            <p></p>
                            <div type="button" style="height: 35px;width: 35px" class="btn btn-default" onclick="addExpressContent('1')"><span style="color: white">1</span></div>
                            <div type="button" style="height: 35px;width: 35px"  style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('2')"><span style="color: white">2</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('3')"><span style="color: white">3</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('+')"><span style="color: white">+</span></div>
                            <div class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('4')"><span style="color: white">4</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('5')"><span style="color: white">5</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('6')"><span style="color: white">6</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('-')"><span style="color: white"> - </span></div>
                            <div class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('7')"><span style="color: white">7</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('8')"><span style="color: white">8</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('9')"><span style="color: white">9</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('*')"><span style="color: white">*</span></div>
                            <div class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('()')"><span style="color: white">()</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('0')"><span style="color: white">0</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('.')"><span style="color: white">.</span></div>
                            <div type="button" style="height: 35px;width: 35px"  class="btn btn-default" onclick="addExpressContent('/')"><span style="color: white">/</span></div>
                        </div>
                        <div class="col-sm-1">
                            <button type="button" class="btn btn-default" id="add_hanshu" style="border-color: #F39801;background-color: #F39801"><span style="color: white"><添加</span></button>
                        </div>
                        <div class="col-sm-2">
                            <select size="15" id="hanshu">
                                <option value="abs()">Math.abs</option>
                                <option value="max()">Math.max</option>
                                <option value="min()">Math.min</option>
                                <option value="pow()">Math.pow(x,y)</option>
                                <option value="exp()">Math.exp</option>
                                <option value="log10()">Math.log10</option>
                                <option value="log()">Math.log</option>
                                <option value="random()">Math.random</option>
                             <%--   <option>add(BigInteger val)</option>
                                <option>subtract(BigInteger val)</option>
                                <option>multiply(BigInteger val)</option>
                                <option>divide(BigInteger val)</option>
                                <option>compareTo(BigInteger val)</option>
                                <option>pow(int exponent)</option>--%>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-6 col-sm-6">
                        <button type="submit" class="btn btn-primary ZS_Add">确认</button>
                        <button type="reset" class="btn btn-primary resetbutton">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    /**
     * 添加内容
     */
    function addExpressContent(str){
        var tc = document.getElementById("formulatext");
        var tclen = tc.value.length;
        tc.focus();
        if(typeof document.selection != "undefined")
        {
            document.selection.createRange().text = str;
        }
        else
        {
            tc.value = tc.value.substr(0,tc.selectionStart)+str+tc.value.substring(tc.selectionStart,tclen);
        }
    }
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/modelEdit');
</script>
</body>
</html>
