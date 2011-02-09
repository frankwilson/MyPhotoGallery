<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
<c:if test="${isThisUser}">
<script>
$(function() {
    $(".albumDelLink").click(function(){
    	var albumId = $(this).parent().parent().find(".album_id").val();
        var albumTitle = $("#album_"+ albumId).find("td.photo > a").attr('title');

        if( confirm("<spring:message code="confirm.album.deleteQuestion"/> '"+ albumTitle +"'?") ) {
            $.ajax({
                type: "GET",
                url: './album_'+ albumId +'/delete.html',
                dataType: "json",
                success: function(data) {
                    if( data.deleted == true ) {
                        options = { /*to: '#album_'+ albumId, className: "ui-effects-transfer"*/ };
                        $("#album_"+ albumId).effect('fade', options, 'slow');
                    }
                    else {
                        // Out message to show that album was not moved
                    }
                },
                error: function(xhr, status, trown) {
                    if( status != 'success' && xhr.status == 200 ) {
                        alert('Status is not success!');
                    }
                    else if( xhr.status != 200 ) {
                        alert('Server return status '+ xhr.status +'!');
                    }
                }
            });
        }
    });
});
</script>
</c:if>
<div>
  <div class="content">
    <c:if test="${isThisUser}">
    <div class="page_header"><spring:message code="page.albumsList.yourAlbums"/>:</div>
    </c:if>
    <c:if test="${!isThisUser}">
    <div class="page_header"><spring:message code="page.albumsList.albumsOfUser"/>&#160;<c:out value="${albums[0].user.login}"></c:out>:</div>
    </c:if>
    <c:if test="${fn:length(albums) gt 0}">
    <div>
<c:forEach items="${albums}" var="currentAlbum">
    <%-- Следующий блок - повторяющаяся табличка, содержащая информацию об альбоме --%>
      <div style="float:left;" id="album_${currentAlbum.albumId}">
<c:if test="${isThisUser}">
        <input type="hidden" class="album_id" value="${currentAlbum.albumId}" />
        <div class="photo_icons">
          [<a class="albumDelLink" href="javascript:void(0);">&#160;X&#160;</a>]
          [<a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}/info.html">&#160;E&#160;</a>]
        </div>
</c:if>
        <table class="album_minitables">
          <tr>
            <td class="photo">
              <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html" title="${currentAlbum.title}">
                <c:if test="${currentAlbum.preview eq null}">
                  <img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" />
                </c:if>
                <c:if test="${currentAlbum.preview != null}">
                  <img style="margin-top:10px;" src="/images/${currentAlbum.preview.photoFilesList[4].filename}" />
                </c:if>
              </a>
            </td>
          </tr>
          <tr>
            <td>
              <div class="photo_desc">
                <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">${currentAlbum.title} (${fn:length(currentAlbum.photos)})</a>
              </div>
            </td>
          </tr>
          <tr style="height:100%;">
            <td class="photo_caption">
                <fmt:formatDate value="${currentAlbum.addDate}" pattern="yyyy-MM-dd hh:mm" />
                <%-- Тут будет дата загрузки последней фотографии --%>
            </td>
          </tr>
        </table>
      </div>
</c:forEach>
    </div>
    </c:if>
    <c:if test="${fn:length(albums) eq 0}">
    <c:if test="${isThisUser}">
      <spring:message code="page.albumsList.noYourAlbums"/>!<br /><br />
      <a href="createAlbum.html"><spring:message code="page.albumsList.createAlbum"/></a>
    </c:if>
    <c:if test="${!isThisUser}">
      <spring:message code="page.albumsList.noUserAlbums"/>!<br /><br />
    </c:if>
    </c:if>
  </div>
</div>
<jsp:include page="footer.jsp" />