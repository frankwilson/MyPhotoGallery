<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="adminHeader.jsp" />

<div class="top_level">
  <div class="content">
    <div class="page_header">${errorPageTitle}:</div>
    <div class="main">
      <h3>${errorMessage}.</h3>
    </div>
  </div>
</div>

<jsp:include page="adminFooter.jsp" />