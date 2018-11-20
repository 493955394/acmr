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
        <span class="col-sm-offset-2 col-sm-3" style="font-size: 20px;color: #F39801;text-align: center">-------------基本信息-------------</span><br>
        <div class="panel-body">
            <form class="form-horizontal J_addZS_form" action="${ctx}/zbdata/zsjhedit.htm?m=toSaveZS">
                <input type="hidden" name="procodeId" value="${datas.procodeId}" class="input-small"/>
                <input type="hidden" name="icode" value="${datas.indexCode}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="ZS_code">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="ZS_cname">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">节点类别：</label>
                    <div class="col-sm-3">
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
                    <br>
                    <span class="col-sm-offset-2 col-sm-3" style="text-align:center;font-size: 20px;color: #F39801">----------次级指数设置----------</span><br><br>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属节点：</label>
                        <div class="col-sm-3">
                            <select class="form-control cjzs" name="cjzs" autocomplete="off" >
                                <c:forEach items="${zslist}" var="list">
                                    <option value="${list.getCode()}" <c:if test="${list.getCode() == datas.procodeId}"> selected</c:if>>${list.getCname()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div id="select_zb" style="display: none">
                    <br>
                    <span class="col-sm-offset-2 col-sm-3" style="text-align:center;font-size: 20px;color: #F39801">------------指标设置------------</span><br><br>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属节点：</label>
                        <div class="col-sm-3">
                            <select class="form-control zb_ifzs" name="zb_ifzs" autocomplete="off" >
                                <c:forEach items="${zslist}" var="list">
                                    <option value="${list.getCode()}" <c:if test="${list.getCode() == datas.procodeId}"> selected</c:if>>${list.getCname()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">指标类型：</label>
                        <div class="col-sm-3">
                            <select class="form-control formula" name="formula" autocomplete="off">
                                <c:forEach items="${zblist.zbchoose}" var="zbl">
                                    <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                </c:forEach>
                                <option value="userdefined">自定义</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">小数点位数：</label>
                    <div class="col-sm-3">
                        <input name="dotcount" type="text" class="form-control" value="1"/>
                    </div>
                </div>
                <div class="hidden_group form-group" style="display: none">
                        <label class="col-sm-2 control-label">公式编辑器：</label>
                        <div class="col-sm-3">
                            <select size="15" class="zb_index" style="width: 100%">
                                <c:forEach items="${zblist.zbchoose}" var="zbl">
                                    <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-1">
                            <div type="button" class="btn btn-default" id="add_zb" style="height: 30px;width: 50px;border-radius: 3px;margin-left: 15px"><span style="color: white;font-size: 10px">添加></span></div>
                        </div>
                        <div class="col-sm-2">
                            <textarea rows="8" cols="23" id="formulatext" name="formulatext"></textarea>
                            <div class="clearfix"></div>
                            <p></p>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px" class="btn btn-default cal" onclick="addExpressContent('1')"><span style="color: white;">1</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px" class="btn btn-default" onclick="addExpressContent('2')"><span style="color: white">2</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('3')"><span style="color: white">3</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('+')"><span style="color: white">+</span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px"  class="btn btn-default" onclick="addExpressContent('4')"><span style="color: white">4</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('5')"><span style="color: white">5</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('6')"><span style="color: white">6</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('-')"><span style="color: white"> - </span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px"  class="btn btn-default" onclick="addExpressContent('7')"><span style="color: white">7</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('8')"><span style="color: white">8</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('9')"><span style="color: white">9</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('*')"><span style="color: white">*</span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px"  class="btn btn-default" onclick="addExpressContent('()')"><span style="color: white">()</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('0')"><span style="color: white">0</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('.')"><span style="color: white">.</span></div>
                            <div type="button" style="height: 35px;width: 35px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('/')"><span style="color: white">/</span></div>
                        </div>
                        <div class="col-sm-1">
                            <%--<div type="button" id="add_hanshu" style="border-color: #F39801;background-color: #F39801;height: 30px;width: 50px;border-radius: 3px"><span style="color: white"><添加</span></div>--%>
                            <div type="button" class="btn btn-default" id="add_hanshu" style="height: 30px;width: 50px;border-radius: 3px;margin-left: 15px"><span style="color: white;font-size: 10px;"><添加</span></div>
                        </div>
                        <div class="col-sm-2">
                            <select size="15" id="hanshu">
                                <option value="abs()" title="求绝对值">Math.abs</option>
                                <option value="max()" title="求两数中最大">Math.max</option>
                                <option value="min()" title="求两数中最小">Math.min</option>
                                <option value="pow()" title="x的y次方">Math.pow(x,y)</option>
                                <option value="exp()" title="求e的任意次方">Math.exp</option>
                                <option value="log10()" title="以10为底的对数">Math.log10</option>
                                <option value="log()" title="自然对数">Math.log</option>
                                <option value="random()" title="返回0，1之间的一个随机数">Math.random</option>
                               <%-- <option>add(BigInteger val)</option>
                                <option>subtract(BigInteger val)</option>
                                <option>multiply(BigInteger val)</option>
                                <option>divide(BigInteger val)</option>
                                <option>compareTo(BigInteger val)</option>
                                <option>pow(int exponent)</option>--%>
                            </select>
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
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/toAdd');
    seajs.use('${ctx}/js/func/zhzs/zstask/caculate');
</script>
<div class="teset" style="padding-bottom: 10px;padding-top: 20px"></div>
<div class="ict-footer footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>
</body>
</html>