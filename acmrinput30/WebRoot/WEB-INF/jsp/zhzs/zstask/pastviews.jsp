<%--
  Created by IntelliJ IDEA.
  User: 涑水科技
  Date: 2018/10/1
  Time: 9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/pastreview.css" />
<script type="text/javascript" src="${ctx}/js/lib/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/dropList.js"></script>
<html>
<head>
    <title>-查看往期</title>
    <jsp:include page="/WEB-INF/jsp/common/libs.jsp" flush="true" />
    <style>
        table tr th, table tr td { border:1px solid #cecfdf; }
        #i1,#i2,#i3,#i4{color:#F39801;}
    </style>
</head>
<body>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true" />
<div class="col-xs-12">
    <div>
        <div>
            <h4>查看往期</h4>
            <input type="hidden" class="indexcode" value="${info.get('indexcode')}">
        </div>

        <c:if test="${info.get('tasknum')=='0'}">
            <span>该计划下没有往期任务！</span>
        </c:if>
        <c:if test="${info.get('tasknum')!='0'}">
            <div>
                <div class="toolbar">
                    <div style="float: right">
                        <input type="hidden" id="timecode" value="">
                        <button  type="button" id="timeinput" data-value="" style="display: none"/>
                        <button class="btn btn-default btn-sm"data-toggle="modal" data-target=".wdturn-modal" ><i id="i1" class="weidu-turn"></i>&nbsp;维度转换</button>&nbsp;
                        <button class="btn btn-default btn-sm pastview_download"><i id="i2" class="glyphicon glyphicon-download-alt"></i>&nbsp;数据下载</button>
                    </div>
                    <div id="mySelect2"></div>
                </div>
            </div>
            <div class="J_pastviews_data_table">
                <jsp:include page="/WEB-INF/jsp/zhzs/zstask/pasttable.jsp" flush="true"/>
            </div>
        </c:if>
    </div>
    <div class="modal wdturn-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">维度转换</h4>
                </div>
                <div style="margin-left: 220px" role="group" aria-label="..." >
                    <div type="button" style="border-color: #F39801;" class="btn btn-default top-select" id="top-reg"><span style="color: #F39801">地区</span></div>
                    <div type="button" style="border-color: #F39801" class="btn btn-default top-select" id="top-zb"><span style="color: #F39801">指标</span></div>
                    <div type="button" style="border-color: #F39801" class="btn btn-default top-select" id="top-sj"><span style="color: #F39801">时间</span></div>
                </div>
                <div style="margin-bottom: 4%;">
                    <div class="btn-group-vertical  col-md-offset-3" role="group" aria-label="...">
                        <div type="button" style="border-color: #F39801" class="btn btn-default left-select" id="left-reg"><span style="color: #F39801">地区</span></div>
                        <div class="clearfix" style="height: 5px"></div>
                        <div type="button" style="border-color: #F39801" class="btn btn-default left-select" id="left-zb"><span style="color: #F39801">指标</span></div>
                        <div class="clearfix"  style="height: 5px"></div>
                        <div type="button" style="border-color: #F39801" class="btn btn-default left-select" id="left-sj"><span style="color: #F39801">时间</span></div>
                    </div>
                    <table class="table table-hover wdturn-table" style="display: inline-block;position: absolute;margin-left: 16px;margin-top: 5px">

                    </table>
                </div>
                <input type="hidden" id="table-Row" value="">
                <input type="hidden" id="table-Col" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="wd-change">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>

            </div>
        </div>
    </div>
</div>
<div class="common-tips"></div>
<script type="text/javascript">
    seajs.use('${ctx}/js/func/zhzs/zstask/pastviews')
    seajs.use('${ctx}/js/func/zhzs/zstask/wdturn')
    //查询的时间
    $(function(){
        var json2 = {
            wdcode:'sj',
            wdname:'时间',
            nodes:[
                {code:"last5",name:'最近五期'}
            ]
        };
        var dt2 = $('#mySelect2');
        //dt2.dropList(json2,{isText:true});	//实例化2(带底部输入框)、默认选中第一个item
        //dt2.dropList(json2,{isText:true,setIndex: 2});	//实例化2(带底部输入框)、选中指定位置item
        dt2.dropList(json2,{isText:true},function(o){		//事件处理
            $("#timecode").val(o.getItem().code)
            $("#timeinput").click();
        });
    });
</script>
<div class="ict-footer">
    Copyright © 2018 中国信息通信研究院 版权所有
</div>
</body>
</html>