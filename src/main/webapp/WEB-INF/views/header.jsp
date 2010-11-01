<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="ru.pakaz.common.model.User"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;
    if( principal instanceof UserDetails && principal instanceof User ) {
        user = (User)principal;
    }
    pageContext.setAttribute( "user", user ); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title><%=application.getInitParameter("serviceName") %> :: <c:out value="${pageName}"></c:out> </title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/functions.js"></script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
    	if( $(".left_panel") != null && $(".left_panel").length > 0 ) {
            $(".left_panel_show").show();
    	}
    });
  </script>
</head>
<body>
<div class="top_level" style="height:100%;">
  <div class=" header">
    <div class="site_header" style="float:left;">
      <a href="${pageContext.request.contextPath}/index.html">&nbsp;<%=application.getInitParameter("serviceName") %>&nbsp;</a>
    </div>
    <div style="text-align:right; padding-right:10px; padding-top:5px;"><span style="float: right">
    <a href="?lang=en">en</a> | <a href="?lang=ru">ru</a>
</span>
  <c:if test="${user eq null}">
      <form method="post" action="${pageContext.request.contextPath}/loginCheck.html">
        Логин:&#160;<input style="height:20px; width:80px;" type="text" name="j_username" value="" id="username"><br />
        Пароль:&#160;<input style="height:20px; width:80px;" type="password" name="j_password" value="" id="password"><br />
        <a href="${pageContext.request.contextPath}/registration.html" title="Регистрация">Регистрация</a> | <input style="width:80px;" type="submit" name="Войти" id="enter" value="Войти">
      </form>
  </c:if>
    </div>
    <table class="menu">
      <tr>
        <td>&#160;
  <c:if test="${user ne null && user.userId gt 0}">
            <a href="${pageContext.request.contextPath}/changeUsersInfo.html" title="Личная информация">${user.login}</a> |
            <a href="${pageContext.request.contextPath}/albumsList.html" title="<spring:message code="link.myAlbums.description"/>"><spring:message code="link.myAlbums.title"/></a>
            [<a href="${pageContext.request.contextPath}/createAlbum.html" title="Добавить альбом">&#160;+&#160;</a>] |
            <a href="${pageContext.request.contextPath}/${albumUrl}upload.html" title="Загрузить фотографию">Загрузить</a> |
            <a href="${pageContext.request.contextPath}/unallocatedPhotos.html" title="Нераспределенные фотографии">Нераспределенные фотографии (${user.unallocatedPhotosCount})</a> |
            <a href="${pageContext.request.contextPath}/logout.html" title="Выход">Выйти</a>
  </c:if>
        </td>
        <td style="text-align:right; width:203px;">
          <div class="left_panel_show"><a href="" onClick="toddleLeftPanel(); return false;">&#160;hide left panel&#160;</a></div>
        </td>
      </tr>
    </table>
  </div>
<!-- Body starts here -->

