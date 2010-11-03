<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />

<div>
  <div class="content">
    <c:if test="${isThisUser}">
      <div class="page_header"><spring:message code="page.albumInfo.yourAlbums"/>:</div>
    </c:if>
    <c:if test="${!isThisUser}">
      <div class="page_header"><spring:message code="page.albumInfo.albumsOfUser"/> <c:out value="${albums[0].user.login}"></c:out>:</div>
    </c:if>
    <c:if test="${fn:length(albums) gt 0}">
      <div>
    <c:forEach items="${albums}" var="currentAlbum">
      <%-- Следующий блок - повторяющаяся табличка, содержащая информацию об альбоме --%>
        <div style="float:left;">
            <div class="photo_icons">
              [<a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}/delete.html">&#160;X&#160;</a>]
              [<a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}/info.html">&#160;E&#160;</a>]
            </div>
            <table class="album_minitables">
              <tr>
                <td class="photo">
                  <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">
                    <c:if test="${currentAlbum.preview eq null}">
                      <img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" />
                    </c:if>
                    <c:if test="${currentAlbum.preview != null}">
                      <img style="margin-top:10px;" src="/images/${currentAlbum.preview.photoFilesList[4].filename}" />
                    </c:if>
                  </a>
                </td>
              </tr>
              <tr>
                <td style="height:32px;vertical-align:top;">
                  <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
                </td>
              </tr>
              <tr style="height:100%;">
                <td class="photo_caption" style="text-align:left; padding-left:5px;">
                    <fmt:formatDate value="${currentAlbum.addDate}" pattern="yyyy-MM-dd hh:mm" />
                    <%-- Тут будет дата загрузки последней фотографии --%>
                </td>
              </tr>
            </table>
        </div>
    </c:forEach>
      </div>
    </c:if>
<c:if test="${fn:length(albums) eq 0}">
    <c:if test="${isThisUser}">
      <spring:message code="page.albumInfo.noYourAlbums"/>!<br /><br />
      <a href="createAlbum.html"><spring:message code="page.albumInfo.createAlbum"/></a>
    </c:if>
    <c:if test="${!isThisUser}">
      <spring:message code="page.albumInfo.noUserAlbums"/>!<br /><br />
    </c:if>
</c:if>
  </div>
</div>
<jsp:include page="footer.jsp" />