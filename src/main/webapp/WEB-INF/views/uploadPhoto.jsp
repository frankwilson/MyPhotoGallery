<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <form:form enctype="multipart/form-data">
      <div class="page_header">Загрузка фотографии
  <c:if test="${currentAlbum != null}">
         в альбом: <a href="<%=application.getContextPath() %>/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
  </c:if>
      </div>
      <div class="main">
  <c:if test="${currentAlbum == null && fn:length(albums) > 0}">
        Альбом для размещения фотографии:
        <br /><br />
        <select name="album">
          <option value=0>Отсутствует</option>
    <c:forEach items="${albums}" var="album">
          <option value="<c:out value="${album.albumId}"></c:out>"><c:out value="${album.title}"></c:out></option>
    </c:forEach>
        </select>
        <br /><br />
  </c:if>
        Укажите файл, который вы хотите загрузить:
        <br /><br />
        <input size="80" type="file" name="file1" id="file" /><br />
        <input size="80" type="file" name="file2" id="file" /><br />
        <input size="80" type="file" name="file3" id="file" /><br />
        <input size="80" type="file" name="file4" id="file" /><br />
        <br /><br />
        <input style="width:120px;" type="submit" name="get" id="get" value="Загрузить" />
      </div>
    </form:form>
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