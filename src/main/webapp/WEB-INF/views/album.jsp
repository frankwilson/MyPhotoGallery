<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
<c:if test="${isThisUser}">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/albumMoving.js"></script>

<script>
$(function() {
    $(".left").click(function(){
        var pdiv = $(this).parent('div').parent('div');
        pdiv.insertBefore(pdiv.prev());
        return false
    });
    $(".right").click(function(){
        var pdiv = $(this).parent('div').parent('div');
        pdiv.insertAfter(pdiv.next());
        return false
    });
    
    $(".albumDelLink").click(function(){
        return confirm("<spring:message code="confirm.album.deleteQuestion"/> '${album.title}'?");
    });
});

var deleteConfirm = "<spring:message code="confirm.photo.deleteQuestion"/>";
</script>
</c:if>
<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}
      <c:if test="${!isThisUser}">
        <span class="additional">
          <spring:message code="page.album.ofUser" />&#160;<a href="${pageContext.request.contextPath}/${album.user.login}/albumsList.html">${album.user.login}</a>
        </span>
      </c:if>
    </div>

    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
    <div id="sortable">
<c:forEach items="${album.photos}" var="photo">
    <div style="float:left;" class="photo_frame ui-widget-content" id="photo_${photo.photoId}">
<c:if test="${isThisUser}">
      <div class="photo_icons">
        [<a class="photoDelLink" href="javascript:void(0);">&#160;X&#160;</a>]
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/info.html">&#160;E&#160;</a>]
        <a href="javascript:void(0);" class="left">&#8592;</a>&#160;
        <a href="javascript:void(0);" class="right">&#8594;</a>&#160;
      </div>
</c:if>
      <table class="album_minitables">
        <tr>
          <td class="photo">
            <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
              <c:if test="${photo.photoFilesList[4].filename == ''}">
                <img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" />
              </c:if>
              <c:if test="${photo.photoFilesList[4].filename != ''}">
                <img id="image_${photo.photoId}" style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" />
              </c:if>
            </a>
          </td>
        </tr>
        <tr>
          <td>
            <div class="photo_desc">
              <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" style="">${photo.title}</a>
            </div>
          </td>
        </tr>
        <tr>
          <td class="photo_caption" style="text-align:left;">
              <fmt:formatDate value="${photo.addDate}" pattern="yyyy-MM-dd hh:mm" />
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
    </div>
  </div>

<c:if test="${isThisUser}">
  <div class="left_panel">
    <div class="left_block">
      <div class="header"><spring:message code="page.album.links"/></div>
      <div class="body">
        <a href="${pageContext.request.contextPath}<c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html"><spring:message code="page.album.uploadPhotos"/></a>&#160;
  <c:if test="${album.albumId > 0}"><br />
        <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html"><spring:message code="page.album.editAlbum"/></a>
        <br /><br />
        <a class="albumDelLink" href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html"><spring:message code="page.album.deleteAlbum"/></a>
  </c:if>
      </div>
    </div>
    <div class="left_block" id="moveAlbumsList">
      <div class="header"><spring:message code="page.album.albums"/></div>
      <div class="body">
	    <select id="albumId">
          <option value="0"><spring:message code="page.album.noAlbum"/></option>
<c:forEach items="${albums}" var="album">
		  <option value="${album.albumId}">${album.title}</option>
</c:forEach>
        </select>
      </div>
    </div>
  </div>
</c:if>
</div>

<jsp:include page="footer.jsp" />