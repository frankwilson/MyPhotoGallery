<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="ru.pakaz.common.model.User"%>
<%  User user = (User)session.getAttribute( "User" ); 
    pageContext.setAttribute("User", user); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title><%=application.getInitParameter("serviceName") %> :: <c:out value="${pageName}"></c:out> </title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" type="text/css" href="<%=application.getContextPath() %>/style.css">
</head>
<body>
<div class="top_level">
  <div class="site_header" style="float:left;">
    <a href="${pageContext.request.contextPath}/index.html">&nbsp;<%=application.getInitParameter("serviceName") %>&nbsp;</a>
  </div>
  <div style="text-align:right; padding-right:10px; padding-top:5px;">
<% if( user == null ) {
    user = new User(); %>
    <form method="post" action="${pageContext.request.contextPath}/login.html">
      Логин:&#160;<input style="height:20px; width:80px;" type="text" name="login" value="" id="login"><br />
      Пароль:&#160;<input style="height:20px; width:80px;" type="password" name="password" value="" id="pass"><br />
      <a href="${pageContext.request.contextPath}/registration.html" title="Регистрация">Регистрация</a> | <input style="width:80px;" type="submit" name="Войти" id="enter" value="Войти">
    </form>
<% } %>
  </div>
  <table class="menu">
    <tr>
      <td>
<% if( user.getUserId() != 0 ) { %>
	      <a href="${pageContext.request.contextPath}/changeUsersInfo.html" title="Личная информация">${User.login}</a> |
	      <a href="${pageContext.request.contextPath}/albumsList.html" title="Список альбомов пользователя">Мои альбомы</a>
	      [<a href="${pageContext.request.contextPath}/createAlbum.html" title="Добавить альбом">&#160;+&#160;</a>] |
	      <a href="${pageContext.request.contextPath}/upload.html" title="Загрузить фотографию">Загрузить</a> |
	      <a href="${pageContext.request.contextPath}/unallocatedPhotos.html" title="Нераспределенные фотографии">Нераспределенные фотографии (${sessionScope.unallocatedPhotosCount})</a> |
	      <a href="${pageContext.request.contextPath}/logout.html" title="Выход">Выйти</a>
<%}%>
      </td>
    </tr>
  </table>
</div>
<!-- Body starts here -->

