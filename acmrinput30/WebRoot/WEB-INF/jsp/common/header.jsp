
<%@page import="acmr.util.WebConfig"%>
<%@page import="com.acmr.service.security.MenuService"%>
<%@page import="com.acmr.model.security.Right"%>
<%@page import="acmr.util.ListHashMap"%>
<%@page import="com.acmr.model.security.Menu"%>
<%@page import="com.acmr.model.security.User"%>
<%@page import="com.acmr.service.security.SafeService"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%
	@SuppressWarnings("unchecked")
	ListHashMap<Menu> menus = (ListHashMap<Menu>) request.getSession().getAttribute("usermenu");
	User user = (User) request.getSession().getAttribute("loginuser");
%>

<div id="tops">
	<div class="tops">
		<div class="header">
			<h1>中国信通院</h1>
             <img style="display:none;"
              src="${passport}/online.aspx" />
			<div class="top-search">
				<input type="text" value="如：全球 电话用户数 2016" maxlength="60"
					class="search-text" id="keyword" /> <input type="button"
					class="search-btn" />
			</div>

			<div class="top-right">
				<ul>
					<li>欢迎！<%=user.getUsername()%></li>
					<li>|</li>
					<li><a href="${ctx}/login.htm?m=logout">退出</a></li>
				</ul>
			</div>
		</div>
		<div class="navbox">
			<div class="container-fluid clearfix">
				<ul class="navbar-nav" style="float:right;">
					<!-- 
					 <li><a href="<%=WebConfig.factor.getInstance().getPropertie("login.properties", "puburl")%>/index.htm">首页</a></li>
					 -->
					<%=MenuService.getUserMenu(menus)%> 
				</ul>
			</div>
		</div>
	</div>
</div>