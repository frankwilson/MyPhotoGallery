<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="adminHeader.jsp" />

<script>

</script>

<form:form commandName="user" id="userUpdate">
<div class="top_level">
  <div class="content">
    <div class="page_header"><c:if test="${user.userId > 0}">
      <spring:message code="page.admin.userEdit.title"/>
    </c:if>
    <c:if test="${user.userId == 0}">
      <spring:message code="page.admin.userAdd.title"/>
    </c:if>:</div>
    <table class="main" style="width:600px;">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td style="width:150px; height:22px;"><spring:message code="page.admin.userEdit.login"/>:</td>
<c:if test="${user.userId > 0}">
        <td>${user.login} <form:hidden path="login"/></td>
</c:if>
<c:if test="${user.userId == 0}">
        <td>
          <form:input path="login" cssStyle="width:200px;"/>&#160;
          <form:errors path="login"/>
        </td>
</c:if>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.nickname"/>:</td>
        <td>
          <form:input path="nickName" cssStyle="width:200px;"/>&#160;
          <form:errors path="nickName"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.firstName"/>:</td>
        <td>
          <form:input path="firstName" cssStyle="width:200px;"/>&#160;
          <form:errors path="firstName"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.lastName"/>:</td>
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
      <tr><td colspan=2 style="height:10px;"></td></tr>
<c:if test="${user.userId > 0}">
      <tr>
        <td colspan=2>
          <spring:message code="page.admin.userEdit.notChangePass"/>:
          <input type="hidden" name="password" value="******">
        </td>
      </tr>
</c:if>
      <tr>
        <td><spring:message code="page.admin.userEdit.newPassword"/>:</td>
        <td>
<c:if test="${user.userId > 0}">
          <form:password path="password" autocomplete="off" />
          <%--<input type="password" autocomplete="off" name="new_pass" id="new_pass"> --%>
</c:if>
<c:if test="${user.userId == 0}">
          <form:password path="password" autocomplete="off" showPassword="true" />
</c:if>
          <form:errors path="password"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr><td colspan=2><b><spring:message code="page.admin.userEdit.additional"/>:</b></td></tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.isDeleted"/>:</td>
        <td>
          <form:checkbox path="deleted"/>&#160;
          <form:errors path="deleted"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.isBlocked"/>:</td>
        <td>
          <form:checkbox path="blocked"/>&#160;
          <form:errors path="blocked"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.isTemporary"/>:</td>
        <td>
          <form:checkbox path="temporary"/>&#160;
          <form:errors path="temporary"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.userEdit.roles"/>:</td>
        <td>
          <form:select path="roles" multiple="multiple" items="${roles}" itemLabel="name" itemValue="roleId" />
          <form:errors path="roles"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td>
          <c:if test="${user.userId > 0}">
          <input type="submit" value="<spring:message code="page.admin.userEdit.save"/>" />
          </c:if>
          <c:if test="${user.userId == 0}">
          <input type="submit" value="<spring:message code="page.admin.userAdd.add"/>" />
          </c:if>
        </td>
      </tr>
    </table>
  </div>
</div>
</form:form>
<jsp:include page="adminFooter.jsp" />