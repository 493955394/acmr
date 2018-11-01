<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<style>
    .word{
        color:black;
        font-family: '黑体';
    }

    th{
        text-align: center;
        vertical-align: middle!important;
        background-color: #EBECF1;
    }

    td{
        text-align: center;
        vertical-align: middle!important;
    }
    #i1,#i2,.i3{
        color:#F39801;
    }

</style>
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
                    <input id="queryValue" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="${codes.keyword}" autocomplete="off">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
        <div class="toolbar-right">
            <div class="toolbar-group" style="position: relative;">
                <button class="btn btn-default btn-sm J_Add" data-toggle="modal" data-target="#mymodal-data" type="button"><i id="i1" class="glyphicon glyphicon-plus"></i>新增目录</button>&nbsp;
                <button class="btn btn-default btn-sm J_Add_Plan" data-toggle="modal" data-target="#mymodal-data1" type="button"><i id="i2" class="glyphicon glyphicon-plus"></i>新增计划</button>&nbsp;
                <button class="btn btn-default btn-sm J_AddCopy" type="button"><i class="i3"></i>复制到</button>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${state.equals('1')}">
    <div class="panel-body">
        <div class="toolbar-left">
            <form class="form-inline J_search_form1" action="${ctx}/zbdata/indexlist.htm?m=receiveListFind">
                <div class="form-group">
                    <select id="querykey1" class="form-control input-sm">
                        <option value="cname" <c:if test="${codes.cname != '' && codes.cname!= null}">selected</c:if>>计划名称</option>
                        <option value="code" <c:if test="${codes.code != '' && codes.code != null}">selected</c:if>>分享人</option>
                    </select>
                </div>
                <div class="form-group">
                    <input id="queryValue1" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="${codes.keyword}" autocomplete="off">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
        <div class="toolbar-right">
            <div class="toolbar-group" style="position: relative;">
                <button class="btn btn-default btn-sm J_Share_AddCopy"  type="button" ><i class="i3"></i>复制到</button>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${state.equals('2')}">
    <div class="panel-body">
        <div class="toolbar-left">
            <form class="form-inline J_search_form2" action="${ctx}/zbdata/indexlist.htm?m=shareListFind">
                <div class="form-group">
                    <select id="querykey2" class="form-control input-sm">
                        <option value="cname" <c:if test="${codes.cname != '' && codes.cname!= null}">selected</c:if>>计划名称</option>
                        <option value="code" <c:if test="${codes.code != '' && codes.code != null}">selected</c:if>>被分享人</option>
                    </select>
                </div>
                <div class="form-group">
                    <input id="queryValue2" type="text" class="form-control input-sm" placeholder="输入搜索内容" value="${codes.keyword}" autocomplete="off">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>
        </div>
    </div>
</c:if>
<table class="table table-bordered table-hover J_regmgr_table">
    <c:if test="${state.equals('0')}">
        <colgroup>
            <col width="3%"/>
            <col width="14%"/>
            <col width="15%"/>
            <col width="10%"/>
            <col />
            <col />
            <col />
        </colgroup>
        <thead>
        <tr>
            <th><input type="radio" name="radiotype" style="display: none;"></th>
            <th><span class="word">编码</span></th>
            <th><span class="word">名称</span></th>
            <th><span class="word">类型</span></th>
            <th><span class="word">周期</span></th>
            <th><span class="word">最新数据期</span></th>
            <th><span class="word">操作</span></th>
        </tr>
        </thead>
        <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
            <tbody class="list_body ">
            <tr>
                <td colspan="7">没有查询到数据</td>
            </tr>
            </tbody>
        </c:if>
        <c:if test="${page.data.size()!=0}">
            <tbody class="list_body " id="my_index_all">
            <c:forEach  items="${page.data}" var="index">
                <tr class="my_index pro-${index.getCode()}">
                    <th><input type="radio" name="radiotype" if="${index.getIfdata()}" id="${index.getCode()}" getname="${index.getCname()}" getprocode="${index.getProcode()}"></th>
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
                                <a href="javascript:;" class="start btn-margin" name="${index.getCode()}">启用</a><a class="btn-margin" href="${ctx}/zbdata/zsjhedit.htm?id=${index.getCode()}">编辑</a>
                                <a href="javascript:;" class="btn-margin J_opr_del" id="${index.getCode()}">删除</a>
                            </c:if>
                            <c:if test="${index.getState().equals('1')}">
                                <a href="javascript:;" class="stop btn-margin" name="${index.getCode()}">停用</a><span class="btn-disabled btn-margin">编辑</span>
                                <span class="btn-disabled btn-margin">删除</span>
                            </c:if>

                            <a href="#" class="btn-margin" id="rightbutton" name="${index.getCode()}">权限管理</a>

                            <a class="past_task btn-margin" href="${ctx}/zbdata/pastviews.htm?id=${index.getCode()}" target="_blank">查看往期</a>
                            <a class="btn-margin" href="${ctx}/zbdata/zstask.htm?icode=${index.getCode()}" target="_blank">指数任务</a>
                        </c:if>
                        <c:if test="${index.getIfdata().equals('0')}">
                            <a class="category_edit btn-margin" href="javascript:;" name="${index.getCname()}" id="${index.getCode()}">编辑</a>
                            <a href="javascript:;" class="btn-margin J_opr_del" id="${index.getCode()}">删除</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </c:if>
    </c:if>
    <c:if test="${state.equals('1')}">
        <colgroup>
            <col width="5%"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="15%"/>
            <col width="20%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
        <tr>
            <th><input type="radio" name="radiotype" style="display: none;"></th>
            <th>名称</th>
            <th>分享人</th>
            <th>权限</th>
            <th>时间周期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="list_body_my_received">
        <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
            <tr>
                <td colspan="6">没有查询到数据</td>
            </tr>
        </c:if>
        <c:forEach items="${page.data}" var="index">
            <tr class="my_received">
                <th><input type="radio" name="radiotype" getright="${index.get('right')}"  idcode="${index.get('index').getCode()}" coname="${index.get('index').getCname()}" coprocode="${index.get('index').getProcode()}"></th>
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
                            <c:if test="${index.get('right')!='0'}">
                                <a href="${ctx}/zbdata/zsjhedit.htm?id=${index.get('index').getCode()}&right=${index.get('right')}">编辑</a>
                                <a href="javascript:;" class="btn-opr J_opr_del" id="${index.get('index').getCode()}">删除</a>
                            </c:if>
                            <c:if test="${index.get('right')=='0'}">
                                <label class="btn-disabled">编辑</label>
                                <label class="btn-disabled">删除</label>

                            </c:if>
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
                            <a href="#" id="rightbutton" name="${index.get('index').getCode()}">权限管理</a>
                        </c:if>
                        <c:if test="${index.get('right')!='2'}">
                            <label class="btn-disabled">权限管理</label>
                        </c:if>
                        <a class="past_task" href="${ctx}/zbdata/pastviews.htm?id=${index.get('index').getCode()}" target="_blank">查看往期</a>

                        <a href="${ctx}/zbdata/zstask.htm?&icode=${index.get('index').getCode()}&right=${index.get('right')}" target="_blank">指数任务</a>
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
        <c:if test="${page.totalRecorder==0 or page.totalPage<page.pageNum}">
            <tr>
                <td colspan="5">没有查询到数据</td>
            </tr>
        </c:if>
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

<div class="toolbar-right">
    <ul class="pagination J_zsjh_pagination">${page}</ul>
</div>
<script>
    seajs.use('${ctx}/js/func/zhzs/indexlist/main');
    seajs.use('${ctx}/js/func/zhzs/indexlist/planEdit');
</script>