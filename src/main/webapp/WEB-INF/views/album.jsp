<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}<c:if test="${album.user.userId != sessionScope.User.userId}">
      <span class="additional">пользователя <a href="<%=application.getContextPath() %>/user_${album.user.userId}.html">${album.user.login}</a></span>
      </c:if>
    </div>
    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
<c:forEach items="${photos}" var="photo">
    <div style="float:left;">
      <div class="photo_icons">
        [<a href="<%=application.getContextPath() %>/photo_${photo.photoId}/delete.html">&#160;X&#160;</a>]
        [<a href="<%=application.getContextPath() %>/photo_${photo.photoId}/info.html">&#160;E&#160;</a>]
      </div>
      <table class="album_minitables">
        <tr>
          <td class="photo" <%--style="height:170px; background-color:#cfcfcf;" --%>>
            <a href="<%=application.getContextPath() %>/photo_${photo.photoId}.html" title="<c:out value="${photo.title}"></c:out>">
              <img style="margin-top:10px;" src="<%=application.getContextPath() %>/photo_<c:out value="${photo.photoId}"></c:out>/size_150/show.html" alt="<c:out value="${photo.title}"></c:out>" border="0" />
            </a>
          </td>
        </tr>
        <tr>
          <td style="height:20px; vertical-align:top;">
            <div style="overflow:hidden; width:187px;">
              <a href="<%=application.getContextPath() %>/photo_${photo.photoId}.html" style=""><c:out value="${photo.title}"></c:out></a>
            </div>
          </td>
        </tr>
        <tr>
          <td class="photo_caption" style="text-align:left;">
              Добавлено: <fmt:formatDate value="${photo.addDate}" pattern="yyyy-MM-dd hh:mm" />
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
  </div>
  <div>
    <table style="width:220px; height:100%; background-color:#deecaa; margin:0px; padding:0px;vertical-align:top;">
      <tr>
        <td style="vertical-align:top;">
          <div class="main">
            <a href="<%=application.getContextPath() %><c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">Загрузить фотографии</a>&#160;
      <c:if test="${album.albumId > 0}"><br />
            <a href="<%=application.getContextPath() %>/album_${album.albumId}/info.html">Изменить альбом</a>
            <br />
            <a href="<%=application.getContextPath() %>/album_${album.albumId}/delete.html">Удалить альбом</a>
      </c:if>
          </div>
        </td>
      </tr>
    </table>
  </div>
</div>

<jsp:include page="footer.jsp" />