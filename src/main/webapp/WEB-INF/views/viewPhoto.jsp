<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="header.jsp" /> 
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${photo.title}<c:if test="${photo.album != null}"> (${currentPhotoNumber} из ${fn:length(photo.album.photos)})</c:if>
      <br />
      <span class="additional">
        из <c:choose>
          <c:when test="${photo.album == null}">нераспределенных фотографий</c:when>
          <c:otherwise>альбома <a href="<%=application.getContextPath() %>/album_${photo.album.albumId}.html">${photo.album.title}</a></c:otherwise>
        </c:choose>
        <c:if test="${photo.user.userId != sessionScope.User.userId}">
        пользователя <a href="<%=application.getContextPath() %>/user_${photo.user.userId}.html">${photo.user.login}</a>
        </c:if>
      </span>
    </div>
    <div class="photo" style="text-align:center;">
      <img style="margin:10px;" src="<%=application.getContextPath() %>/photo_<c:out value="${photo.photoId}"></c:out>/size_640/show.html" alt="<c:out value="${photo.title}"></c:out>" />
    </div>
    <table style="width:100%;">
      <tr>
        <td style="text-align:left; width:200px; font-size:10px;">
<c:if test="${prevPhoto != null}">
          <a href="<%=application.getContextPath() %>/photo_${prevPhoto.photoId}.html" style="">Предыдущая&#160;<span style="font-size:18px;">&#8592;</span></a><br />
          ${prevPhoto.title}
</c:if>
        </td>
        <td style="text-align:center;">

        </td>
        <td style="text-align:right; width:200px; font-size:10px;">
<c:if test="${nextPhoto != null}">
          <a href="<%=application.getContextPath() %>/photo_${nextPhoto.photoId}.html" style=""><span style="font-size:18px;">&#8594;</span>&#160;Следующая</a><br />
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
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;">
          <div class="main">
            <a href="<%=application.getContextPath() %>/photo_${photo.photoId}/delete.html">Удалить фотографию</a>&#160;
            <br />
            <a href="<%=application.getContextPath() %>/photo_${photo.photoId}/info.html">Изменить фотографию</a>
          </div>
        </td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />