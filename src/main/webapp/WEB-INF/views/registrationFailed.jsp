<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />

<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.registrationFailed.title"/></div>
    <div><spring:message code="page.registrationFailed.description"/></div>
  </div>
</div>

<jsp:include page="footer.jsp" />