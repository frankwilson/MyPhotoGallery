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
<body>
<div class="top_level">
  <div class="site_header">
    <a href="<%=application.getContextPath() %>/index.html">&nbsp;<%=application.getInitParameter( "serviceName" ) %>&nbsp;</a>
  </div>
  <table class="menu">
    <tr>
      <td>
        <form method="post" action="<%=application.getContextPath() %>/login.html">
<% 
User user = (User)session.getAttribute( "User" );
if( user != null ) { %>
	      <a href="<%=application.getContextPath() %>/changeUsersInfo.html" title="Личная информация"><%=user.getLogin() %></a> |
	      <a href="<%=application.getContextPath() %>/albumsList.html" title="Список альбомов пользователя">Мои альбомы</a>
	      [<a href="<%=application.getContextPath() %>/createAlbum.html" title="Добавить альбом">&#160;+&#160;</a>] |
	      <a href="<%=application.getContextPath() %>/upload.html" title="Загрузить фотографию">Загрузить</a> |
	      <a href="<%=application.getContextPath() %>/unallocatedPhotos.html" title="Нераспределенные фотографии">Нераспределенные фотографии (0)</a> |
	      <a href="<%=application.getContextPath() %>/logout.html" title="Выход">Выйти</a>
<% } else {
    user = new User();
%>
	      <a href="<%=application.getContextPath() %>/registration.html" title="Регистрация">Регистрация</a> |
	      Логин:&#160;<input style="height:20px; width:80px;" type="text" name="login" value="" id="login">
	      Пароль:&#160;<input style="height:20px; width:80px;" type="password" name="password" value="" id="pass">
	      <input style="width:80px;" type="submit" name="Войти" id="enter" value="enter">
<% } %>
        </form>
      </td>
    </tr>
  </table>
</div>
<!-- Body starts here -->

