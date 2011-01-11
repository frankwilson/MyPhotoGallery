<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="adminHeader.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.admin.usersList.title"/></div>
    <div class="page_description"><spring:message code="page.admin.usersList.description"/></div>
    <div>
      <table>
        <colgroup>
          <col style="width:25px;" />
          <col style="width:100px;" />
          <col style="width:100px;" />
          <col style="width:120px;" />
        </colgroup>
        <tr>
          <td>ID</td>
          <td>Login</td>
          <td>e-mail</td>
          <td>Nickname</td>
          <td>Name</td>
        </tr>
<c:forEach items="${usersList}" var="user">
        <tr>
          <td>${user.userId}</td>
          <td>${user.login}</td>
          <td>${user.nickName}</td>
          <td>${user.email}</td>
          <td>${user.firstName} ${user.lastName}</td>
        </tr>
</c:forEach>
      </table>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />