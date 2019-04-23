<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2019/2/26
  Time: 18:07
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
<body >
<div style="min-width: 1600px">
<div class="ict-header">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true"/>
</div>
<div class="ict-page">
    <div class="container-fluid">
        <input type="hidden" name="schemecodes" value="${info.schemecodes}" class="input-small"/>
        <input type="hidden" name="sname" value="${info.snmae}" class="input-small"/>
        <input type="hidden" name="type" value="${info.type}" class="input-small"/>
        <div>

            <div class="panel-body">
                <form class="form-horizontal J_addZS_form" action="${ctx}/zbdata/zsjhedit.htm?m=toSaveFormular">
                    <input type="hidden" name="icode" value="${icode}" class="input-small"/>
                    <input type="hidden" name="scode" value="${scode}" class="input-small"/>
                    <input type="hidden" id="ifzs" value="${data.getIfzs()}" class="input-small"/>
                    <div class="col-lg-12">
                    <div class="col-lg-6">
                        <span class="col-sm-12" style="font-size: 20px;color: #F39801;text-align: center;">-------------基本信息-------------</span><br>
                    <div class="form-group" style="display: none">
                        <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" name="ZS_code" value="${data.getCode()}" readonly>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 20px">
                        <label class="col-sm-2 control-label">节点类别：</label>
                        <div class="col-sm-9">
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
                        <div class="form-group" >
                            <label class="col-sm-2 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="ZS_cname" value="${data.getCname()}" readonly>
                            </div>
                        </div>
                    <div id="secend_zs" style="display: none">
                        <span class="col-sm-12" style="text-align:center;font-size: 20px;color: #F39801">----------次级指数设置-----------</span><br>
                        <br>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">父级节点：</label>
                            <div class="col-sm-9">
                                <input class="form-control cjzs" name="cjzs" readonly="" data-value="1" value="${proname}">
                            </div>
                        </div>
                    </div>
                    <div id="select_zb" style="display: none;margin-top: 20px">
                        <br>
                        <span class="col-sm-12" style="text-align:center;font-size: 20px;color: #F39801">------------指标设置-------------</span>
                        <div class="form-group" style="margin-top: 20px">
                            <br>
                            <label class="col-sm-2 control-label">父级节点：</label>
                            <div class="col-sm-9">
                                <input class="form-control zb_ifzs" name="zb_ifzs" readonly="" data-value="1" value="${proname}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">指标类型：</label>
                            <div class="col-sm-9">
                                <select class="form-control formula" name="formula" autocomplete="off">
                                    <option value="userdefined" <c:if test="${data.getIfzb() =='0'||data.getIfzb() ==''}">selected</c:if> >自定义</option>
                                    <c:forEach  items="${zblist.zbchoose}" var="zbl">
                                        <c:choose>
                                        <c:when test="${data.getIfzb() == '1'}" >
                                            <option value="${zbl.code}"<c:if test="${zbl.code == data.getFormula()}">selected</c:if>>${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${zbl.code}">${zbl.zbname}(${zbl.dsname},${zbl.unitname})</option>
                                        </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    </div>
                    <div class="col-lg-6" >
                        <span class="col-sm-10" style="font-size: 20px;color: #F39801;text-align: center;">-------------函数使用说明-------------</span><br>
                    <textarea style="margin-top: 10px" class="fun-info" rows="12" cols="100" id="for-Introduction" onfocus=this.blur()>注：getvalue()函数需要配合max()、mix()、avg();
max()取最大值；mix()取最小值；avg()取平均值。
例1：取任务时间所选指标对应地区范围的最大值：
max(getvalue(#互联网用户数_个人_当期值(工信部,万户)#,dq))
例2：取任务时间所选指标对应地区2010到最新任务时间的平均值：
avg(getvalue(#互联网用户数_个人_当期值(工信部,万户)#,2010-))
例3：取任务时间所选指标对应地区计划开始时间到最新任务时间的最小值：
min(getvalue(#互联网用户数_个人_当期值(工信部,万户)#,begintime))
例4：取任务时间所选对应地区环比；
(#互联网用户数_家庭_当期值(工信部,万户)#/avg(getvalue(#互联网用户数_家庭_当期值(工信部,万户)#,lastnum1))
</textarea>
                    </div>
                    </div><br>
<%--调整编辑框样式--%>
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
                            <%--<div type="button" id="add_zb" style="border-radius: 0;background-color: #F39801;color: white;padding-left: 0px;height:30px;width:50px;">添加》</div>--%>
                                <div type="button" class="btn btn-primary" id="add_zb"> 添加>></div>
                        </div>
                        <div class="col-sm-2">
                            <textarea rows="8" cols="30" id="formulatext" name="formulatext"><c:if test="${data.getIfzb()==0}">${data.getFormula()}</c:if></textarea>
                            <div class="clearfix"></div>
                            <p></p>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px" class="btn btn-default cal" onclick="addExpressContent('1')"><span style="color: white;">1</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px" class="btn btn-default" onclick="addExpressContent('2')"><span style="color: white">2</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('3')"><span style="color: white">3</span></div>
                            <div type="button" style="height: 40px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('+')"><span style="color: white">+</span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('4')"><span style="color: white">4</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('5')"><span style="color: white">5</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('6')"><span style="color: white">6</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('-')"><span style="color: white"> - </span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('7')"><span style="color: white">7</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('8')"><span style="color: white">8</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('9')"><span style="color: white">9</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('*')"><span style="color: white">*</span></div>
                            <div style="margin-top: 10px" class="clearfix"></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('()')"><span style="color: white">( )</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('0')"><span style="color: white">0</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('.')"><span style="color: white">.</span></div>
                            <div type="button" style="height: 42px;width: 42px;border-radius: 8px;margin-left: 10px"  class="btn btn-default" onclick="addExpressContent('/')"><span style="color: white">/</span></div>
                        </div>
                        <div class="col-sm-1">
                            <div type="button" id="add_hanshu" class="btn btn-primary" ><<添加</div>
                        </div>
                        <div class="col-sm-2">
                            <select style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width:250px" size="20" id="hanshu">

                                <optgroup label="计算公式" style="background-color: #EEEEEE;"></optgroup>
                                <option value="abs()" title="求绝对值">Math.abs</option>
                                <option value="pow()" title="x的y次方">Math.pow(x,y)</option>
                                <option value="exp()" title="求e的任意次方">Math.exp</option>
                                <option value="log10()" title="以10为底的对数">Math.log10</option>
                                <option value="log()" title="自然对数">Math.log</option>
                                <option value="max()" title="求最大值">max()</option>
                                <option value="min()" title="求最小值">min()</option>
                                <option value="avg()" title="求平均值">avg()</option>

                                <optgroup label="数组公式" style="background-color: #EEEEEE;"></optgroup>
                                <option value="getvalue(指标1,dq)" title="取当前时间对应指标所有地区的数值集合">getvalue(指标,dq)</option>
                                <option value="getvalue()" title="取当前地区对应指标最近n期时间数值集合">getvalue(指标,lastN)</option>
                                <option value="getvalue(指标1,lastnumN)" title="取当前地区对应指标最近第n期时间数值集合">getvalue(指标,lastnumN)</option>
                                <option value="getvalue(指标1,begintime)" title="取当前地区对应指标从计划开始时间到最新时间的数值">getvalue(指标,begintime)</option>
                                <option value="getvalue()" title="取当前地区对应指标参数的数值">getvalue(指标,YYYY)</option>
                                <option value="getvalue()" title="取当前地区对应指标从指定年份到最新时间的数值集合">getvalue(指标,YYYY-)</option>
                                <option value="getvalue()" title="取当前地区对应指标从指定时间范围的数值集合">getvalue(指标,YYYY-YYYY)</option>
                                <option value="getvalue()" title="取当前地区和当前时间不同指标的数值集合">getvalue(指标1,指标2,指标3)</option>

                                <optgroup label="提示:" style="background-color: #F39801;"></optgroup>
                                <option value="" >注：getvalue()数值集合需要配合</option>
                                <option value="" >max(),min(),avg()计算公式使用</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-6 col-sm-6">
                            <button type="submit" class="btn btn-primary ZS_Add"  style="margin-left: 14px">确认</button>
                            <button type="button" class="btn btn-primary resetbutton" style="margin-left: 10px">重置</button>
                            <button type="button" class="btn btn-primary close-edit" style="margin-left: 10px">关闭</button>
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
    seajs.use('${ctx}/js/func/zhzs/zsjhEdit/formular');
</script>
</body>
</html>