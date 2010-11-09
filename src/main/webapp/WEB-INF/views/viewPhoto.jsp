<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
 
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${photo.title}<c:if test="${photo.album != null}"> (${currentPhotoNumber} <spring:message code="page.viewPhoto.of"/> ${fn:length(photo.album.photos)})</c:if>
      <br />
      <span class="additional">
        <spring:message code="page.viewPhoto.from"/> <c:choose>
          <c:when test="${photo.album == null}"><spring:message code="page.viewPhoto.ofUnsorted"/></c:when>
          <c:otherwise><spring:message code="page.viewPhoto.ofAlbum"/> <a href="${pageContext.request.contextPath}/album_${photo.album.albumId}.html">${photo.album.title}</a></c:otherwise>
        </c:choose>
        <c:if test="${!isThisUser}">
        <spring:message code="page.viewPhoto.ofUser"/> <a href="${pageContext.request.contextPath}/user_${photo.user.userId}/albumsList.html">${photo.user.login}</a>
        </c:if>
      </span>
    </div>
    <div class="photo" style="text-align:center;">
      <c:if test="${photo.photoFilesList[1].filename != ''}"><img style="margin-top:10px;" src="/images/${photo.photoFilesList[1].filename}" alt="${photo.title}" border="0" /></c:if>
    </div>
    <table style="width:100%;">
      <tr>
        <td style="text-align:left; width:200px; font-size:10px;">
<c:if test="${prevPhoto != null}">
          <a href="${pageContext.request.contextPath}/photo_${prevPhoto.photoId}.html" style=""><spring:message code="page.viewPhoto.prevPhoto"/>&#160;<span style="font-size:18px;">&#8592;</span></a><br />
          ${prevPhoto.title}
</c:if>
        </td>
        <td style="text-align:center;">

        </td>
        <td style="text-align:right; width:200px; font-size:10px;">
<c:if test="${nextPhoto != null}">
          <a href="${pageContext.request.contextPath}/photo_${nextPhoto.photoId}.html" style=""><span style="font-size:18px;">&#8594;</span>&#160;<spring:message code="page.viewPhoto.nextPhoto"/></a><br />
          ${nextPhoto.title}
</c:if>
        </td>
      </tr>
      <tr>
        <td style="text-align:center;" colspan=3>
          <c:out value="${photo.description}"></c:out>
        </td>
      </tr>
    </table>
  </div>
  <div class="left_panel">
    <div class="left_block">
      <div class="header"><spring:message code="page.viewPhoto.links"/></div>
      <div class="body">
<c:if test="${isThisUser}">
        <a href="${pageContext.request.contextPath}/photo_${photo.photoId}/info.html"><spring:message code="page.viewPhoto.changePhoto"/></a>
        <br />
  <c:if test="${photo.photoFilesList[0].filename != ''}">
        <a href="/images/${photo.photoFilesList[0].filename}"><spring:message code="page.viewPhoto.fullsizePhoto"/> (${photo.photoFilesList[0].photoWidth}x${photo.photoFilesList[0].photoHeight})</a>
  </c:if>
        <br /><br />
        <a href="${pageContext.request.contextPath}/photo_${photo.photoId}/delete.html"><spring:message code="page.viewPhoto.deletePhoto"/></a>&#160;
</c:if>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp" />