<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="ru.pakaz.common.model.User"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title><%=application.getInitParameter( "serviceName" ) %> :: <c:out value="${pageName}"></c:out> </title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/style.css">
</head>
<body >
<table id="high_level_table" style="height:100%;">
  <tr>
    <td style="width:100%; margin:0px; padding:0px;">
      <table width="100%" style="margin:0px; padding:0px; border-collapse:collapse;">
        <tr>
          <td style="width:100%; height:45px; background-color:#ffffff; margin:0px; padding:0px;">
            <a href="<%=application.getContextPath() %>/index.html">
              <span class="site_header">&nbsp; <%=application.getInitParameter( "serviceName" ) %> &nbsp;</span>
            </a>
          </td>
        </tr>
        <tr>
          <td style="margin:0px; padding:0px; vertical-align:middle;">
            <table class="menu">
              <form method="post" action="<%=application.getContextPath() %>/login.html">
              <tr>
                <td style="margin:0px; padding:0px; vertical-align:middle; height:25px; padding-left:5px;">
<% 
User user = (User)session.getAttribute( "User" );
if( user != null ) { %>
                    <a href="<%=application.getContextPath() %>/changeUsersInfo.html" title="Личная информация"><%=user.getLogin() %></a> |
                    <a href="<%=application.getContextPath() %>/albumsList.html" title="Список альбомов пользователя">Мои альбомы</a> |
                    <a href="<%=application.getContextPath() %>/logout.html" title="Выход">Выйти</a>
<% } else {
    user = new User();
%>
                    <a href="<%=application.getContextPath() %>/registration.html" title="Регистрация">Регистрация</a> |
                    Логин:&#160;<input style="height:20px; width:80px;" type="text" name="login" value="" id="login">
                    Пароль:&#160;<input style="height:20px; width:80px;" type="password" name="password" value="" id="pass">
                    <input style="width:80px;" type="submit" name="Войти" id="enter" value="enter">
<% } %>
                </td>
              </tr>
              </form>
              <tr>
                <td style="height:100%;"></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="full_size" style="height:100%;margin:0px; padding:0px;">
      <table class="full_size" style="height:100%;margin:0px; padding:0px; border-collapse:collapse;">
        <tr>
          <td bgcolor="#fafafa" style="width:100%; margin:0px; padding:0px; vertical-align:top;">
<!-- Body starts here -->