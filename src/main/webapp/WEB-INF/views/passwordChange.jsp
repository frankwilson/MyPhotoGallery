<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.changePassword.title"/></div><c:if test="${resetCodeResult != null}">
<c:if test="${resetCodeResult == true}"><spring:message code="page.changePassword.success"/>.</c:if>
<c:if test="${resetCodeResult == false}"><spring:message code="page.changePassword.error"/>.</c:if>
    <br />
    <br /></c:if>
    <spring:message code="page.changePassword.description"/>
    <br><br>
    <table class="main">
      <colgroup>
        <col style="width:110px;">
      </colgroup>
      <tr>
        <td><spring:message code="page.changePassword.newPassword"/>:</td>
        <td>
          <form:input path="password" maxlength="32" autocomplete="off" cssStyle="width:250px;" />
          <form:errors path="password"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td style="padding-top:10px;">
          <input style="width: 80px;" type="submit" value="<spring:message code="page.changePassword.changeBut"/>">
        </td>
      </tr>
    </table>
  </div>
</div>
</form:form>

<jsp:include page="footer.jsp" />