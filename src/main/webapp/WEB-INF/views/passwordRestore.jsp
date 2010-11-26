<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.restorePassword.title"/></div><c:if test="${sendMessageResult != null}">
<c:if test="${sendMessageResult == true}"><spring:message code="page.restorePassword.sendSuccess"/>.</c:if>
<c:if test="${sendMessageResult == false}"><spring:message code="page.restorePassword.sendError"/>.</c:if>
    <br />
    <br /></c:if>
    <spring:message code="page.restorePassword.description"/>
    <br><br>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td><spring:message code="page.restorePassword.login"/>:</td>
        <td>
          <form:input path="login" maxlength="32" autocomplete="off" cssStyle="width:250px;" />
          <form:errors path="login"/>
        </td>
      </tr>
      <tr>
        <td>e-mail:</td>
        <td>
          <form:input path="email"  maxlength="64" autocomplete="off" cssStyle="width:250px;" />
          <form:errors path="email"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td style="padding-top:10px;">
          <input style="width: 120px;" type="submit" value="<spring:message code="page.restorePassword.restoreBut"/>">
        </td>
      </tr>
    </table>
  </div>
</div>
</form:form>

<jsp:include page="footer.jsp" />