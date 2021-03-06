<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />

<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.userInfo.title"/>:</div>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td style="width:150px; height:22px;"><spring:message code="page.userInfo.login"/>:</td>
        <td>${viewedUser.login}</td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfo.nickname"/>:</td>
        <td>${viewedUser.nickName}</td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfo.firstName"/>:</td>
        <td>${viewedUser.firstName}</td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfo.lastName"/>:</td>
        <td>${viewedUser.lastName}</td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
    </table>
    <div><a href="${pageContext.request.contextPath}/${viewedUser.login}/albumsList.html">Список альбомов пользователя</a></div>
  </div>
</div>
<jsp:include page="footer.jsp" />