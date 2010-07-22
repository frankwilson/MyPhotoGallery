<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="header.jsp" /> 
<div class="top_level">
  <div class="content">
    <div class="page_header">Фотография из альбома: <a href="<%=application.getContextPath() %>/album_${photo.album.albumId}.html">${photo.album.title}</a></div>
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
          <span style="font-size:14px;">${photo.title} (${currentPhotoNumber} из ${fn:length(photo.album.photos)})</span>
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
    <table style="width:220px; height:100%; background-color:#deecaa; margin:0px; padding:0px;vertical-align:top;">
      <tr>
        <td style="vertical-align:top;"></td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />