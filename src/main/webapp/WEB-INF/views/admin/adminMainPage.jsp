<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="adminHeader.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.admin.main.title"/></div>
    <div class="page_description"><spring:message code="page.admin.main.description"/></div>
    <div>
      <table>
        <tr>
          <td><spring:message code="page.admin.main.totalUsers"/>:</td><td>${usersCount}</td>
        </tr>
        <tr>
          <td><spring:message code="page.admin.main.totalPhotos"/>:</td><td>${photosCount} (${photosSize})</td>
        </tr>
      </table>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />