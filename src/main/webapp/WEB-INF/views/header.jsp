<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
<%--  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>--%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.min_1.4.2.js"></script>
<%--  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js"></script>--%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui.min_1.8.5.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
    	if( jQuery(".left_panel") != null && jQuery(".left_panel").length > 0 ) {
    		jQuery(".left_panel_show").show();
    	}
    	else {
    		jQuery(".content").addClass('full_width');
    	}
    });
    var unallocatedPhotosCount = ${user.unallocatedPhotosCount};
  </script>
</head>
<body>
<div class="top_level" style="height:100%;">
  <div class=" header">
    <div class="site_header" style="float:left;">
      <a href="${pageContext.request.contextPath}/index.html">&nbsp;<%=application.getInitParameter("serviceName") %>&nbsp;</a>
    </div>
    <div style="text-align:right; padding-right:10px; padding-top:5px;"><span style="float: right">
</span>
<c:if test="${user eq null}">
      <form method="post" action="${pageContext.request.contextPath}/loginCheck.html">
        <spring:message code="header.loginTitle"/>:&#160;<input style="height:20px; width:80px;" type="text" name="j_username" value="" id="username"><br />
        <spring:message code="header.passwordTitle"/>:&#160;<input style="height:20px; width:80px;" type="password" name="j_password" value="" id="password"><br />
        <a href="${pageContext.request.contextPath}/passwordRestore.html" title="<spring:message code="header.restorePass.description"/>"><spring:message code="header.restorePass.title"/></a>
        | <a href="${pageContext.request.contextPath}/registration.html" title="<spring:message code="header.registration.description"/>"><spring:message code="header.registration.title"/></a>
        | <input style="width:80px;" type="submit" name="enter" id="enter" value="<spring:message code="header.enterButton"/>">
      </form>
</c:if>
<c:if test="${user != null}">
      <a href="${pageContext.request.contextPath}/changeUsersInfo.html" title="<spring:message code="header.usersInfo.description"/>">${user.login}</a>
</c:if>
    </div>
    <table class="menu">
      <tr>
        <td>&#160;
  <c:if test="${user ne null && user.userId gt 0}">
            <a href="${pageContext.request.contextPath}/albumsList.html" title="<spring:message code="header.myAlbums.description"/>"><spring:message code="header.myAlbums.title"/></a>
              [<a href="${pageContext.request.contextPath}/createAlbum.html" title="<spring:message code="header.addAlbum.description"/>">&#160;+&#160;</a>]
              [<a href="${pageContext.request.contextPath}/${user.login}/albumsList.html" title="<spring:message code="header.albumsLink.description"/>">&#160;&#8599;&#160;</a>]
            | <a href="${pageContext.request.contextPath}/${albumUrl}upload.html" title="<spring:message code="header.uploadPhoto.description"/>"><spring:message code="header.uploadPhoto.title"/></a>
            | <a href="${pageContext.request.contextPath}/unallocatedPhotos.html" title="<spring:message code="header.unsorted.description"/>"><spring:message code="header.unsorted.title"/> (<span id="unallocatedPhotosCount">${user.unallocatedPhotosCount}</span>)</a>
<sec:authorize ifAllGranted="ROLE_ADMIN">
            | <a href="${pageContext.request.contextPath}/admin/main.html"><b>Админка</b></a>
</sec:authorize>
            | <a href="${pageContext.request.contextPath}/logout.html" title="<spring:message code="header.logout.description"/>"><spring:message code="header.logout.title"/></a>
  </c:if>
        </td>
        <td style="text-align:right; width:203px;">
          <div class="left_panel_show"><a href="" onClick="toddleLeftPanel(); return false;">&#160;hide left panel&#160;</a></div>
        </td>
      </tr>
    </table>
  </div>
<!-- Body starts here -->

