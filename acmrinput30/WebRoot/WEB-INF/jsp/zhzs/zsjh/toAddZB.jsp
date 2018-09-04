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
                <input type="hidden" name="procodeId" value="${procodeId}"/>
                <input type="hidden" name="procodeName" value="${procodeName}"/>
                <input type="hidden" name="indexCode" value="${indexCode}"/>
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
                    <label class="col-sm-2 control-label">节点类别</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="ifzs" autocomplete="off">
                            <option value="1">总指数</option>
                            <option value="1">次级指数</option>
                            <option value="0">指标</option>
                        </select>
                    </div>
                </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">小数点位数</label>
                        <div class="col-sm-5">
                            <input name="dotcount" type="text" class="form-control" value="1"/>
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