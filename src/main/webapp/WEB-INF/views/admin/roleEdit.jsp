<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="adminHeader.jsp" />

<form:form commandName="role" id="roleUpdate">
<div class="top_level">
  <div class="content">
    <div class="page_header"><c:if test="${role.roleId > 0}">
      <spring:message code="page.admin.roleEdit.title"/>
    </c:if>
    <c:if test="${role.roleId == 0}">
      <spring:message code="page.admin.roleAdd.title"/>
    </c:if>:</div>
    <table class="main" style="width:600px;">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td style="width:150px; height:22px;"><spring:message code="page.admin.roleEdit.name"/>:</td>
<c:if test="${role.roleId > 0}">
        <td>${role.name} <form:hidden path="name"/></td>
</c:if>
<c:if test="${role.roleId == 0}">
        <td>
          <form:input path="name" cssStyle="width:200px;"/>&#160;
          <form:errors path="name"/>
        </td>
</c:if>
      </tr>
      <tr>
        <td><spring:message code="page.admin.roleEdit.description"/>:</td>
        <td>
          <form:input path="description" cssStyle="width:200px;"/>&#160;
          <form:errors path="description"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.admin.roleEdit.isEnabled"/>:</td>
        <td>
          <form:checkbox path="enabled"/>&#160;
          <form:errors path="enabled"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td>
          <c:if test="${role.roleId > 0}">
            <input type="submit" value="<spring:message code="page.admin.roleEdit.save"/>" />
          </c:if>
          <c:if test="${role.roleId == 0}">
            <input type="submit" value="<spring:message code="page.admin.roleAdd.add"/>" />
          </c:if>
        </td>
      </tr>
    </table>
  </div>
</div>
</form:form>

<jsp:include page="adminFooter.jsp" />