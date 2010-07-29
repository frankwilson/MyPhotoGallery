<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <c:if test="${user.userId eq sessionScope.user.userId}">
      <div class="page_header">Ваши альбомы:</div>
    </c:if>
    <c:if test="${user != null}">
      <div class="page_header">Альбомы пользователя <c:out value="${user.login}"></c:out>:</div>
    </c:if>
    <c:if test="${albumsCount gt 0}">
      <div>
    <c:forEach items="${albums}" var="currentAlbum">
      <%-- Следующий блок - повторяющаяся табличка, содержащая информацию об альбоме --%>
        <div style="float:left;">
            <div class="photo_icons">
              [<a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}/delete.html">&#160;X&#160;</a>]
              [<a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}/info.html">&#160;E&#160;</a>]
            </div>
            <table class="album_minitables">
              <tr>
                <td class="photo">
                  <a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}.html">
                    <c:if test="${currentAlbum.preview eq null}"><img style="margin-top:10px;" src="<%=application.getContextPath() %>/images/album_no_preview.png" /></c:if>
                    <c:if test="${currentAlbum.preview != null}"><img style="margin-top:10px;" src="<%=application.getContextPath() %>/photo_${currentAlbum.preview.photoId}/size_150/show.html" /></c:if>
                  </a>
                </td>
              </tr>
              <tr>
                <td style="height:32px;vertical-align:top;">
                  <a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
                </td>
              </tr>
              <tr style="height:100%;">
                <td class="photo_caption" style="text-align:left; padding-left:5px;">
                    Добавлен: <fmt:formatDate value="${currentAlbum.addDate}" pattern="yyyy-MM-dd hh:mm" />
                    <%-- Тут будет дата загрузки последней фотографии --%>
                </td>
              </tr>
            </table>
        </div>
    </c:forEach>
      </div>
    </c:if>
    <c:if test="${albumsCount eq 0}">
      У Вас не создано ни одного альбома!<br /><br />
      <a href="createAlbum.html">Создать альбом</a>
    </c:if>
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;"></td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />