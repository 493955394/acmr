<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="hidden" id="top" value="${top}" />
<input type="hidden" id="bottom" value="${bottom}" />
<c:if test="${state.equals('0')}">
    <div class="panel-body">
        <div class="toolbar-left">
            <form class="form-inline J_search_form" action="${ctx}/zbdata/indexlist.htm?m=find">
                <div class="form-group">
                    <select id="querykey" class="form-control input-sm">
                        <option value="cname" <c:if test="${codes.cname != '' && codes.cname!= null}">selected</c:if>>名称</option>
                        <option value="code" <c:if test="${codes.code != '' && codes.code != null}">selected</c:if>>编码</option>
                    </select>
                </div>
                <div class="form-group">
                    <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${codes.code != '' && codes.code!= null}">${codes.code}</c:if><c:if test="${codes.cname != '' && codes.cname != null}">${codes.cname}</c:if>">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
        <div class="toolbar-right">
            <div class="toolbar-group" style="position: relative;">
                <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data" type="button">新增目录</button>&nbsp
                <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data1" type="button">新增计划</button>&nbsp
                <button class="btn btn-default btn-sm J_AddCopy"  type="button">复制到</button>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${state.equals('1')}">
    <div class="panel-body">
        <div class="toolbar-left">
            <form class="form-inline J_search_form1" action="${ctx}/zbdata/indexlist.htm?m=find">
                <div class="form-group">
                    <select id="querykey1" class="form-control input-sm">
                        <option value="cname" <c:if test="${codes.cname != '' && codes.cname!= null}">selected</c:if>>计划名称</option>
                        <option value="code" <c:if test="${codes.code != '' && codes.code != null}">selected</c:if>>分享人</option>
                    </select>
                </div>
                <div class="form-group">
                    <input id="queryValue1" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${codes.code != '' && codes.code!= null}">${codes.code}</c:if><c:if test="${codes.cname != '' && codes.cname != null}">${codes.cname}</c:if>">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
        <div class="toolbar-right">
            <div class="toolbar-group" style="position: relative;">
                <button class="btn btn-default btn-sm J_Share_AddCopy"  type="button">复制到</button>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${state.equals('2')}">
    <div class="panel-body">
        <div class="toolbar-left">
            <form class="form-inline J_search_form2" action="${ctx}/zbdata/indexlist.htm?m=find">
                <div class="form-group">
                    <select id="querykey2" class="form-control input-sm">
                        <option value="cname" <c:if test="${codes.cname != '' && codes.cname!= null}">selected</c:if>>计划名称</option>
                        <option value="code" <c:if test="${codes.code != '' && codes.code != null}">selected</c:if>>被分享人</option>
                    </select>
                </div>
                <div class="form-group">
                    <input id="queryValue2" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="<c:if test="${codes.code != '' && codes.code!= null}">${codes.code}</c:if><c:if test="${codes.cname != '' && codes.cname != null}">${codes.cname}</c:if>">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
    </div>
</c:if>
<table class="table table-striped table-hover J_regmgr_table">


    <c:if test="${state.equals('0')}">
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
            <th><input type="radio" style="display: none;"></th>
            <th>编码</th>
            <th>名称</th>
            <th>类型</th>
            <th>周期</th>
            <th>最新数据期</th>
            <th>操作</th>
        </tr>
        </thead>
        <c:if test="${page.data.size()!=0}">
            <tbody class="list_body " id="my_index_all">
            <c:forEach  items="${page.data}" var="index">
                <tr class="my_index pro-${index.getCode()}">
                    <th><input type="checkbox" if="${index.getIfdata()}" id="${index.getCode()}" getname="${index.getCname()}"></th>
                    <td >${index.getCode()}</td>
                    <td >${index.getCname()}</td>
                    <td>
                        <c:if test="${index.getIfdata() == 0}">目录</c:if>
                        <c:if test="${index.getIfdata() == 1}">计划</c:if>
                    </td>
                    <td>
                        <c:if test="${index.getSort() == 'y'}">年度</c:if>
                        <c:if test="${index.getSort() == 'q'}">季度</c:if>
                        <c:if test="${index.getSort() == 'm'}">月度</c:if>
                    </td>
                    <td>${index.getPlanperiod()}</td>
                    <td>
                        <c:if test="${index.getIfdata().equals('1')}">
                            <c:if test="${index.getState().equals('0')}">
                                <a href="javascript:;" class="start" name="${index.getCode()}">启用</a>
                                <a href="${ctx}/zbdata/zsjhedit.htm?id=${index.getCode()}">编辑</a>
                                <a href="javascript:;" class="btn-opr J_opr_del" id="${index.getCode()}">删除</a>
                            </c:if>
                            <c:if test="${index.getState().equals('1')}">
                                <a href="javascript:;" class="stop" name="${index.getCode()}">停用</a>
                                <label class="btn-disabled">编辑</label>
                                <label class="btn-disabled">删除</label>
                            </c:if>
                            <a href="/">权限管理</a>
                            <a href="/">查看往期</a>
                            <a href="${ctx}/zbdata/zstask.htm?&id=${index.getCode()}">指数任务</a>
                        </c:if>
                        <c:if test="${index.getIfdata().equals('0')}">
                            <a class="category_edit" href="javascript:;" name="${index.getCname()}" id="${index.getCode()}">编辑</a>
                            <a href="javascript:;" class="btn-opr J_opr_del" id="${index.getCode()}">删除</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </c:if>
    </c:if>
    <c:if test="${state.equals('1')}">
        <colgroup>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
        <tr>
            <th>名称</th>
            <th>分享人</th>
            <th>权限</th>
            <th>时间周期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="list_body_my_received">
        <c:forEach items="${page.data}" var="index">
            <tr class="my_received">
                <td>${index.get("index").getCname()}</td>
                <td>${index.get("createuser")}</td>
                <td>
                    <c:if test="${index.get('right')== '0'}">查看</c:if>
                    <c:if test="${index.get('right') == '1'}">协作</c:if>
                    <c:if test="${index.get('right') == '2'}">管理</c:if>
                </td>
                <td>
                    <c:if test="${index.get('index').getSort() == 'y'}">年度</c:if>
                    <c:if test="${index.get('index').getSort() == 'q'}">季度</c:if>
                    <c:if test="${index.get('index').getSort() == 'm'}">月度</c:if>
                </td>
                <td>
                    <c:if test="${index.get('index').getIfdata().equals('1')}">
                        <c:if test="${index.get('index').getState().equals('0')}">
                            <c:if test="${index.get('right')!='0'}">
                                <a href="javascript:;" class="start" name="${index.get('index').getCode()}">启用</a>
                            </c:if>
                            <c:if test="${index.get('right')=='0'}">
                                <label class="btn-disabled">启用</label>
                            </c:if>
                            <a href="${ctx}/zbdata/zsjhedit.htm?id=${index.get('index').getCode()}&right=${index.get('right')}">编辑</a>
                            <a href="javascript:;" class="btn-opr J_opr_del" id="${index.get('index').getCode()}">删除</a>
                        </c:if>
                        <c:if test="${index.get('index').getState().equals('1')}">
                            <c:if test="${index.get('right')!='0'}">
                                <a href="javascript:;" class="stop" name="${index.get('index').getCode()}">停用</a>
                            </c:if>
                            <c:if test="${index.get('right')=='0'}">
                                <label class="btn-disabled">停用</label>
                            </c:if>
                            <label class="btn-disabled">编辑</label>
                            <label class="btn-disabled">删除</label>
                        </c:if>
                        <c:if test="${index.get('right')=='2'}">
                            <a href="/">权限管理</a>
                        </c:if>
                        <c:if test="${index.get('right')!='2'}">
                            <label class="btn-disabled">权限管理</label>
                        </c:if>
                        <a href="/">查看往期</a>
                        <a href="${ctx}/zbdata/zstask.htm?&id=${index.get('index').getCode()}&right=${index.get('right')}">指数任务</a>
                    </c:if>
                    <c:if test="${index.get('index').getIfdata().equals('0')}">
                        <a class="category_edit" href="javascript:;" name="${index.get('index').getCname()}" id="${index.getCode()}&right=${index.get('right')}">编辑</a>
                        <a href="javascript:;" class="btn-opr J_opr_del" id="${index.get('index').getCode()}">删除</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </c:if>
    <c:if test="${state.equals('2')}">
        <colgroup>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
        <tr>
            <th>名称</th>
            <th>被分享人</th>
            <th>权限</th>
            <th>时间周期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="list_body my_shared">
        <c:forEach  items="${page.data}" var="index">
            <tr class="my_shared">
                <td>${index.get("cname")}</td>
                <td>${index.get("depusername")}</td>
                <td>
                    <c:if test="${index.get('right') == '0'}">查看</c:if>
                    <c:if test="${index.get('right') == '1'}">协作</c:if>
                    <c:if test="${index.get('right') == '2'}">管理</c:if>
                </td>
                <td>
                    <c:if test="${index.get('timesort') == 'y'}">年度</c:if>
                    <c:if test="${index.get('timesort') == 'q'}">季度</c:if>
                    <c:if test="${index.get('timesort') == 'm'}">月度</c:if>
                </td>
                <td>
                    <input type="hidden" value="${index.get("sort")}">
                    <input type="hidden" value="${index.get("code")}">
                    <input type="hidden" value="${index.get("depusercode")}">
                    <a href="#" class="share_withdraw">撤回</a>
                </td>
            </tr>
            <%--<tr class="my_index pro-${index.getCode()}">
                <td >${index.getCname()}</td>
                <td>
                    <c:if test="${index.getSort() == 'y'}">年度</c:if>
                    <c:if test="${index.getSort() == 'q'}">季度</c:if>
                    <c:if test="${index.getSort() == 'm'}">月度</c:if>
                </td>
            </tr>--%>
        </c:forEach>
        </tbody>
    </c:if>
    <tbody class="list_body my_received">
    </tbody>

</table>
<div class="modal" id="mymodal-data3" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form  class="form-horizontal J_add_edit" action="${ctx}/zbdata/indexlist.htm?m=update">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">编辑目录</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>编码：</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="editcode" value="${index.getCode()}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>名称：</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="editcname" value="${index.getCname()}">
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"><span class="glyphicon glyphicon-asterisk required_ico"></span>所属目录：</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="editname" value="" disabled>
                            <input type="hidden" class="form-control" name="editprocode"  value="">
                            <ul id="treeEditc" class="ztree select-tree hid-top"></ul>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary" name="plancode" >确定</button>
                    <%--<input type='button'  name="plancode" value='复制到' onclick="show()"/>--%>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="toolbar-right">
    <ul class="pagination J_zsjh_pagination">${page}</ul>
</div>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');

</script>