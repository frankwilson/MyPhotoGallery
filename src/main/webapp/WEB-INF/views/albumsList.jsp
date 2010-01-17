<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />
<div>
<c:if test="${userId eq sessionScope.user.userId}">
  <div class="page_header">Ваши альбомы:</div>
</c:if>
<c:if test="${userId != sessionScope.user.userId}">
  <div class="page_header">Альбомы пользователя <c:out value="${sessionScope.user.login}"></c:out>:</div>
</c:if>
<c:if test="${albumsCount gt 0}">
  <div>
  <%-- Следующий блок - повторяющаяся табличка, содержащая информацию об альбоме --%>
    <div style="float:left;">
        <table class="album_minitables main" style="height:100%;">
          <tr>
            <td style="height:120px;">
              <a href="{SYSTEM_PATH}/album/{ALBUM_ID}"><img {PARAMETERS} border="0" /></a>
            </td>
          </tr>
          <tr>
            <td style="height:20px;vertical-align:top;">
              <a href="{SYSTEM_PATH}/album/{ALBUM_ID}">{ALBUM_NAME}</a>
            </td>
          </tr>
          <tr style="height:100%;">
            <td class="photo_caption" style="text-align:left; padding-left:5px;">
                Добавлен: {ADD_DATE}<br />
                Обновлен: {UPDATE_DATE}
                {DELETE_ALBUM_LINK}
            </td>
          </tr>
        </table>
    </div>
  </div>
</c:if>
<c:if test="${albumsCount eq 0}">
  У Вас не создано ни одного альбома!<br /><br />
  <a href="createAlbum.html">Создать альбом</a>
</c:if>
</div>
<jsp:include page="footer.jsp" />