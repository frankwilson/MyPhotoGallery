<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page import="ru.pakaz.common.model.User"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>{SERVICE_NAME} :: {PAGENAME}</title>
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
              <span class="site_header">&nbsp; {SERVICE_NAME} &nbsp;</span>
            </a>
          </td>
        </tr>
        <tr>
          <td style="margin:0px; padding:0px; vertical-align:middle;">
            <table style="margin:0px; padding:0px; border-collapse:collapse; width:100%; background-color:#deecaa;">
              <form method="post" action="<%=application.getContextPath() %>/checkLogin.html">
              <tr>
                <td style="margin:0px; padding:0px; vertical-align:middle; height:25px; padding-left:5px;">
<% 
User currentUser = (User)session.getAttribute( "User" );
if( currentUser != null ) { %>
                    Ваш логин: <%=currentUser.getLogin() %>; <a href="<%=application.getContextPath() %>/logout.html">Выйти</a>
<% } else { %>
                  <spring:bind path="user.login">
                    Логин:&#160;<input style="height:20px; width:80px;" type="text" name="${status.expression}" value="${status.value}" id="login">
                  </spring:bind>
                  <spring:bind path="user.password">
                    Пароль:&#160;<input style="height:20px; width:80px;" type="password" name="${status.expression}" value="${status.value}" id="pass">
                  </spring:bind>
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