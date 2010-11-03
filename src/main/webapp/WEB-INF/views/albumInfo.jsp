<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<form:form commandName="album">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.albumInfo.albumEdit"/>:
      <a href="${pageContext.request.contextPath}/album_${album.albumId}.html">${album.title}</a>
    </div>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td><spring:message code="page.albumInfo.preview"/>:</td>
        <td>
        <form:select path="preview">
          <form:option value="0"><spring:message code="page.albumInfo.noPreview"/></form:option>
          <form:options items="${album.photos}" itemLabel="title" itemValue="photoId" />
        </form:select>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.albumInfo.title"/>:</td>
        <td>
          <form:input path="title" cssStyle="width:500px;"/>&#160;
          <form:errors path="title"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.albumInfo.description"/>:</td>
        <td>
          <form:textarea path="description" cols="60" rows="4"/>&#160;
          <form:errors path="description"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td><input type="submit" value="<spring:message code="page.albumInfo.save"/>" /></td>
      </tr>
    </table>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />