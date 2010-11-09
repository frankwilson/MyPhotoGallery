<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />

<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.userInfoForm.title"/>:</div>
    <table class="main" style="width:600px;">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td style="width:150px; height:22px;"><spring:message code="page.userInfoForm.login"/>:</td>
        <td>${user.login}</td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfoForm.nickname"/>:</td>
        <td>
          <form:input path="nickName" cssStyle="width:200px;"/>&#160;
          <form:errors path="nickName"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfoForm.firstName"/>:</td>
        <td>
          <form:input path="firstName" cssStyle="width:200px;"/>&#160;
          <form:errors path="firstName"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfoForm.lastName"/>:</td>
        <td>
          <form:input path="lastName" cssStyle="width:200px;"/>&#160;
          <form:errors path="lastName"/>
        </td>
      </tr>
      <tr>
        <td>e-mail:</td>
        <td>
          <form:input path="email" cssStyle="width:200px;"/>&#160;
          <form:errors path="email"/>
        </td>
      </tr>
      <tr><td colspan=3 style="height:10px;"></td></tr>
      <tr>
        <td colspan=2>
          <spring:message code="page.userInfoForm.notChangePass"/>:
          <input type="hidden" name="password" value="******">
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfoForm.oldPassword"/>:</td>
        <td>
          <input type="password" autocomplete="off" name="old_pass">
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.userInfoForm.newPassword"/>:</td>
        <td>
          <input type="password" autocomplete="off" name="new_pass">
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td><input type="submit" value="<spring:message code="page.userInfoForm.save"/>" /></td>
      </tr>
    </table>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />