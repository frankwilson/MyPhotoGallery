<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
<c:if test="${isThisUser}">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/albumMoving.js"></script>
<script>
var allowMultiselect = false;
var selectedCount = 0;

$(function() {
    $(".albumDelLink").click(function(){
        return confirm("<spring:message code="confirm.album.deleteQuestion"/> '${album.title}'?");
    });
});

var deleteConfirm = "<spring:message code="confirm.photo.deleteQuestion"/>";
var currentAlbumId = ${album.albumId};
</script>
</c:if>
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}
      <c:if test="${!isThisUser}">
        <span class="additional">
          &#160;<spring:message code="page.albumSimple.ofUser"/>
          <a href="${pageContext.request.contextPath}/user_${album.user.userId}/albumsList.html">${album.user.login}</a>
        </span>
      </c:if>
    </div>
    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
<c:forEach items="${album.photos}" var="photo">
    <div style="float:left;" id="photo_${photo.photoId}">
<c:if test="${isThisUser}">
      <input type="hidden" class="photo_id" value="${photo.photoId}" />
      <div class="photo_icons">
        [<a class="photoDelLink" href="javascript:void(0);">&#160;X&#160;</a>]
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/info.html">&#160;E&#160;</a>]
      </div>
</c:if>
      <table class="album_microtables">
        <tr>
          <td class="photo">
            <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
              <c:if test="${photo.photoFilesList[4].filename == ''}">
                <img src="${pageContext.request.contextPath}/img/album_no_preview.png" />
              </c:if>
              <c:if test="${photo.photoFilesList[4].filename != ''}">
                <img id="image_${photo.photoId}" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" />
              </c:if>
            </a>
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
  </div>
  <div class="left_panel">
<c:if test="${isThisUser}">
    <div class="left_block">
      <div class="header"><spring:message code="page.albumSimple.links"/></div>
      <div class="body">
        <a href="${pageContext.request.contextPath}
          <c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">
          <spring:message code="page.albumSimple.uploadPhotos"/>
        </a>&#160;
  <c:if test="${album.albumId > 0}"><br />
        <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html"><spring:message code="page.albumSimple.editAlbum"/></a>
        <br /><br />
        <a href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html"><spring:message code="page.albumSimple.deleteAlbum"/></a>
  </c:if>
        <br />
        <input type="checkbox" name="allowMultipleSelection" id="allowMultipleSelection" />
        <label for="allowMultipleSelection">Allow multiple selection</label>
      </div>
    </div>
    <div class="left_block" id="moveAlbumsList">
      <div class="header"><spring:message code="page.albumSimple.albums"/></div>
      <div class="body">
        <select id="albumId">
          <option value="0"><spring:message code="page.albumSimple.noAlbum"/></option>
<c:forEach items="${albums}" var="album">
          <option value="${album.albumId}">${album.title}</option>
</c:forEach>
        </select>
      </div>
    </div>
</c:if>
  </div>
</div>

<jsp:include page="footer.jsp" />