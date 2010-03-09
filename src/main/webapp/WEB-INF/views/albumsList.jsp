<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <c:if test="${userId eq sessionScope.user.userId}">
      <div class="page_header">Ваши альбомы:</div>
    </c:if>
    <c:if test="${userId != sessionScope.user.userId}">
      <div class="page_header">Альбомы пользователя <c:out value="${sessionScope.user.login}"></c:out>:</div>
    </c:if>
    <c:if test="${albumsCount gt 0}">
      <div>
    <c:forEach items="${albums}" var="currentAlbum">
      <%-- Следующий блок - повторяющаяся табличка, содержащая информацию об альбоме --%>
        <div style="float:left;">
            <table class="album_minitables main">
              <tr>
                <td style="height:120px;">
                  <a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}.html">
                    <c:if test="${currentAlbum.preview eq null}"><img src="images/album_no_preview.png" /></c:if>
                    <c:if test="${currentAlbum.preview != null}"><img src="photo_${currentAlbum.preview.id}/size_200/show.html" /></c:if>
                  </a>
                </td>
              </tr>
              <tr>
                <td style="height:20px;vertical-align:top;">
                  <a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
                </td>
              </tr>
              <tr style="height:100%;">
                <td class="photo_caption" style="text-align:left; padding-left:5px;">
                    Добавлен: ${currentAlbum.addDate}<br />
                    <%-- Тут будет дата загрузки последней фотографии --%>
                    <%-- Тут будет ссылка на удаление альбома --%>
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
    <table style="width:220px; height:100%; background-color:#deecaa; margin:0px; padding:0px;vertical-align:top;">
      <tr>
        <td style="vertical-align:top;"></td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />