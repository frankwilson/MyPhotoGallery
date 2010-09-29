<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}<c:if test="${!isThisUser}">
      <span class="additional">пользователя <a href="${pageContext.request.contextPath}/user_${album.user.userId}/albumsList.html">${album.user.login}</a></span>
      </c:if>
    </div>
    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
<c:forEach items="${album.photos}" var="photo">
    <div style="float:left;">
      <table class="album_microtables">
        <tr>
          <td class="photo" <%--style="height:170px; background-color:#cfcfcf;" --%>>
            <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
              <c:if test="${photo.photoFilesList[4].filename == ''}"><img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" /></c:if>
              <c:if test="${photo.photoFilesList[4].filename != ''}"><img style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" /></c:if>
            </a>
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;">
          <div class="main">
<c:if test="${isThisUser}">
            <a href="${pageContext.request.contextPath}<c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">Загрузить фотографии</a>&#160;
      <c:if test="${album.albumId > 0}"><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html">Изменить альбом</a>
            <br /><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html">Удалить альбом</a>
      </c:if>
          </div>
</c:if>
        </td>
      </tr>
    </table>
  </div>
</div>

<jsp:include page="footer.jsp" />