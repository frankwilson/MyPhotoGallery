<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="adminHeader.jsp" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/admin/users.js"></script>
<script>
var deleteConfirm = "<spring:message code="confirm.admin.user.deleteQuestion"/>";
</script>

<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.admin.usersList.title"/></div>
    <div class="page_description"><spring:message code="page.admin.usersList.description"/></div>
    <div>
      <a href="${pageContext.request.contextPath}/admin/addUser.html"><spring:message code="page.admin.usersList.addUser"/></a>
      <br />
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
          <th>Login</th>
          <th>Nickname</th>
          <th>e-mail</th>
          <th>Name</th>
          <th>D</th>
          <th>B</th>
          <th>T</th>
        </tr>
<c:forEach items="${usersList}" var="user">
        <tr id="user_${user.userId}">
          <td class="bool">[<a href="javascript:void(0);" class="userDelLink">&#160;X&#160;</a>]</td>
          <td class="bool">[<a href="${pageContext.request.contextPath}/admin/user_${user.userId}/edit.html" class="userEditLink">&#160;E&#160;</a>]</td>
          <td class="userId">${user.userId}</td>
          <td class="userLogin">${user.login}</td>
          <td>${user.nickName}</td>
          <td>${user.email}</td>
          <td>${user.firstName}&#160;${user.lastName}</td>
          <td class="bool">${user.deleted}</td>
          <td class="bool">${user.blocked}</td>
          <td class="bool">${user.temporary}</td>
        </tr>
</c:forEach>
      </table>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />