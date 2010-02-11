<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header"><c:out value="${album.title}"></c:out></div>
    <div>
      Страницы списка: 1 <br />
      Текущая страница: 1
    </div>
      
<c:forEach items="${photos}" var="photo">
    <div style="float:left;">
      <table class="album_minitables">
        <tr>
          <td style="height:160px;">
            <a href="<%=application.getContextPath() %>/album/{ALBUM_ID}/big/<c:out value="${photo.photoId}"></c:out>" title="<c:out value="${photo.title}"></c:out>">
              <img alt="<c:out value="${photo.title}"></c:out>" style="margin:5px;" border="0" />
            </a>
          </td>
        </tr>
        <tr>
          <td style="height:20px;vertical-align:top;">
            <a href="<%=application.getContextPath() %>/album/{ALBUM_ID}/big/<c:out value="${photo.photoId}"></c:out>"><c:out value="${photo.title}"></c:out></a>
          </td>
        </tr>
        <tr>
          <td class="photo_caption" style="text-align:left; padding-left:5px;">
              Добавлено: <c:out value="${photo.addDate}"></c:out>
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
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