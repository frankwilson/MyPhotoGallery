<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.photoInfo.title"/>:
      <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html">${photo.title}</a>
      <br />
      <span class="additional">
        <spring:message code="page.photoInfo.from"/>&#160;<c:choose>
          <c:when test="${photo.album == null}"><spring:message code="page.photoInfo.ofUnsorted"/></c:when>
          <c:otherwise><spring:message code="page.photoInfo.ofAlbum"/>&#160;<a href="${pageContext.request.contextPath}/album_${photo.album.albumId}.html">${photo.album.title}</a></c:otherwise>
        </c:choose>
        <c:if test="${isThisUser eq false}">
        <spring:message code="page.photoInfo.ofUser"/> <a href="${pageContext.request.contextPath}/user_${photo.user.userId}.html">${photo.user.login}</a>
        </c:if>
      </span>
    </div>
<form:form commandName="photo">
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td><spring:message code="page.photoInfo.preview"/>:</td>
        <td class="photo">
          <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
            <c:if test="${photo.photoFilesList[4].filename == ''}"><img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" /></c:if>
            <c:if test="${photo.photoFilesList[4].filename != ''}"><img style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" /></c:if>
          </a>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.photoInfo.title"/>:</td>
        <td>
          <form:input path="title" cssStyle="width:500px;"/>&#160;
          <form:errors path="title"/>
        </td>
      </tr>
      <tr>
        <td><spring:message code="page.photoInfo.description"/>:</td>
        <td>
          <form:textarea path="description" cols="60" rows="4"/>&#160;
          <form:errors path="description"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td><input type="submit" value="<spring:message code="page.photoInfo.save"/>" /></td>
      </tr>
    </table>
</form:form>
  </div>
</div>
<jsp:include page="footer.jsp" />