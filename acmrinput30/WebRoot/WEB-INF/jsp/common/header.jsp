
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


<link rel="stylesheet" type="text/css" href="${ctx}/css/header.css" />
<style>
	.header-nav{
		background: #5992F8;
		border: none;
	}
	.header-nav .header-navbar > li > a{
		color: #ffffff;
	}
</style>
<div class="head">
	<div class="container-fluid clearfix">
		<a class="logo">
			<img src="${ctx}/images/logo_new.png" />
		</a>
		<div class="header-func">

			<span>欢迎！</span>

		</div>
		<div class="search-box">
			<div class="inner">
				<form action="${ctx}/search.htm" method="post">
					<span class="search-logo"></span>
					<div class="input-box">
						<input type="text" class="search-input" name="s" placeholder="如：全球 电话用户数 2016" maxlength="60" />
						<button type="submit"></button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<div class="header-nav">
	<div class="container-fluid">
		<ul class="header-navbar clearfix">

			<li><a href="${ctx }/index.htm">首页</a></li>

		</ul>
	</div>
</div>
<div style="height: 10px"></div>
<div class="breadcrumbm">
	<span class="icon_home"></span>
	<a href="${ctx }/index.htm">首页</a><span class="sep"></span>
</div>