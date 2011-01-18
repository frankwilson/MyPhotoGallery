<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="adminHeader.jsp" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/admin/users.js"></script>
<script>
var deleteConfirm = "<spring:message code="confirm.admin.user.deleteQuestion"/>";
</script>

<div class="top_level">
  <div class="content full_width">
    <div class="page_header"><spring:message code="page.admin.usersList.title"/></div>
    <div class="page_description"><spring:message code="page.admin.usersList.description"/></div>
    <div>
      <a class="button_link" href="${pageContext.request.contextPath}/admin/addUser.html"><spring:message code="page.admin.usersList.addUser"/></a>
      <a class="button_link" href="${pageContext.request.contextPath}/admin/roles.html"><spring:message code="page.admin.usersList.roles"/></a>

      <br /><br />
      <table class="admin_table">
        <tr>
          <th style="width:25px;"></th>
          <th style="width:25px;"></th>
          <th style="width:40px;">ID</th>
          <th>Login</th>
          <th>Nickname</th>
          <th>e-mail</th>
          <th>Name</th>
          <th>D</th>
          <th>B</th>
          <th>T</th>
          <th style="width:110px;">Created</th>
          <th style="width:110px;">Updated</th>
        </tr>
<c:forEach items="${usersList}" var="user">
        <tr id="user_${user.userId}">
          <td class="bool">[<a href="javascript:void(0);" class="userDelLink" title="<spring:message code="page.admin.usersList.markAsDeleted"/>">&#160;X&#160;</a>]</td>
          <td class="bool">[<a href="${pageContext.request.contextPath}/admin/user_${user.userId}/edit.html" class="userEditLink">&#160;E&#160;</a>]</td>
          <td class="id">${user.userId}</td>
          <td class="login">${user.login}</td>
          <td>${user.nickName}</td>
          <td>${user.email}</td>
          <td>${user.firstName}&#160;${user.lastName}</td>
          <td class="bool">${user.deleted}</td>
          <td class="bool">${user.blocked}</td>
          <td class="bool">${user.temporary}</td>
          <td><fmt:formatDate value="${user.created}" pattern="yyyy-MM-dd hh:mm" /></td>
          <td><fmt:formatDate value="${user.updated}" pattern="yyyy-MM-dd hh:mm" /></td>
        </tr>
</c:forEach>
      </table>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />