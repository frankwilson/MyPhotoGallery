<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
<form:form commandName="album">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.createAlbum.addAlbum"/>:</div>
    <div class="main">
      <spring:message code="page.createAlbum.title"/>:
      <br /><br />
      <form:input path="title" cssStyle="height:20px; width:300px;"/>
      <form:errors path="title" />
      <br /><br />
      <spring:message code="page.createAlbum.description"/>:
      <br /><br />
      <form:textarea path="description" cols="60" rows="4" />
      <form:errors path="description" />
      <br /><br />
      <input type="submit" value="<spring:message code="page.createAlbum.addAlbumButton"/>" />
    </div>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />