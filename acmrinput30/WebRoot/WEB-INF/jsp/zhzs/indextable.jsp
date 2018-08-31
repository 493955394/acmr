<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
<table class="table table-striped table-hover J_regmgr_table">
    <colgroup>
        <col width="3%"/>
        <col width="5%"/>
        <col width="10%"/>
        <col width="27%"/>
        <col width="10%"/>
        <col width="27%"/>
        <col width="18%"/>
    </colgroup>
    <thead>
    <tr>
        <th><input autocomplete="off" type="checkbox" style="display: none;"></th>
        <th>编码</th>
        <th>名称</th>
        <th>类型</th>
        <th>周期</th>
        <th>最新数据期</th>
        <th>操作</th>
    </tr>
    </thead>
    <div class="modal" id="mymodal-data3" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form  action="${ctx}/zbdata/indexlist.htm?m=update">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editcode" value="${index.getCode()}">
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editcname" value="${index.getCname()}">
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="editname" value="" disabled>
                                <input type="hidden" class="form-control" name="editprocode"  value="">
                                <ul id="treeEditc" class="ztree select-tree hid-top"></ul>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="submit" class="btn btn-primary" name="plancode" value="check_val"  onclick="show()">确定</button>
                        <%--<input type='button'  name="plancode" value='复制到' onclick="show()"/>--%>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <c:if test="${indexlist.size()!=0}">
        <tbody class="list_body " id="my_index_all">
        <c:forEach  items="${indexlist}" var="index">
            <tr class="my_index pro-${index.getCode()}">
                <th><input autocomplete="off" type="checkbox" name="search" value="${index.getCode()}"></th>
                <td >${index.getCode()}</td>
                <td >${index.getCname()}</td>
                <td>${index.getIfdata()}</td>
                <td>${index.getSort()}</td>
                <td>${index.getPlanperiod()}</td>
                <td>
                    <c:if test="${index.getIfdata()==1}">
                    <a href="${ctx}/zbdata/zsjhedit.htm?m=editIndex&id=${index.getCode()}">编辑</a>
                    </c:if>
                    <c:if test="${index.getIfdata()==0}">
                        <a  class="category_edit" href="javascript:;" name="${index.getCname()}" id="${index.getCode()}">编辑</a>
                    </c:if>
                    <a href="javascript:;" class="btn-opr J_opr_del" id="${index.getCode()}">删除</a>
                    <c:if test="${index.getIfdata()==1}">
                        <a href="/">启用</a>
                        <a href="/">权限管理</a>
                        <a href="/">查看往期</a>
                        <a href="/">指数任务</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
    </c:if>
    <tbody class="list_body my_shared">
    </tbody>
    <tbody class="list_body my_received">
    </tbody>

</table>
<div class="toolbar-right">
    <ul class="pagination J_regmgr_pagination">${page}</ul>
</div>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
    function show(){
        var oChk = document.getElementById('cp');
        oChk.onclick = function(){
            if(oChk.checked){
                obj = document.getElementsByName("search");
                check_val = [];
                for(k in obj){
                    if(obj[k].checked)
                        check_val.push(obj[k].value);
                }
                alert(check_val);
            } else {
                alert('请选择计划');
            }
        };
        //alert(check_val);

    }

</script>