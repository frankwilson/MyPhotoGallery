<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="adminHeader.jsp" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/admin/users.js"></script>
<script>
var deleteConfirm = "<spring:message code="confirm.admin.role.deleteQuestion"/>";
</script>

<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.admin.roles.title"/></div>
    <div class="page_description"><spring:message code="page.admin.roles.description"/></div>
    <div>
      <a class="button_link" href="${pageContext.request.contextPath}/admin/addRole.html"><spring:message code="page.admin.roles.addRole"/></a>

      <br /><br />
      <table class="admin_table">
        <colgroup>
          <col style="width:25px;" />
          <col style="width:25px;" />
          <col style="width:40px;" />
        </colgroup>
        <tr>
          <th></th>
          <th></th>
          <th>ID</th>
          <th>Name</th>
          <th>Description</th>
          <th>On</th>
          <th>Created</th>
          <th>Updated</th>
        </tr>
<c:forEach items="${rolesList}" var="role">
        <tr id="role_${role.roleId}">
          <td class="bool">[<a href="javascript:void(0);" class="roleDelLink" title="<spring:message code="page.admin.roles.markAsDisabled"/>">&#160;X&#160;</a>]</td>
          <td class="bool">[<a href="${pageContext.request.contextPath}/admin/role_${role.roleId}/edit.html" class="roleEditLink">&#160;E&#160;</a>]</td>
          <td class="id">${role.roleId}</td>
          <td class="name">${role.name}</td>
          <td>${role.description}</td>
          <td class="bool">${role.enabled}</td>
          <td><fmt:formatDate value="${role.created}" pattern="yyyy-MM-dd hh:mm" /></td>
          <td><fmt:formatDate value="${role.updated}" pattern="yyyy-MM-dd hh:mm" /></td>
        </tr>
</c:forEach>
      </table>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />