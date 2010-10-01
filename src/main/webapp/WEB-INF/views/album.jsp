<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="header.jsp" />
<style>
.ui-state-active {
    background-color: #eee;
}

.targetAlbum {
    width:160px;
    height:160px;
    margin-top:5px;
    padding-top: 5px;
    border: 1px solid #ccc;
    border-radius: 5px 5px 0 0;
    background-color:#ccc;
    text-align:center;
}

</style>
<script>

function hidePhotoFrame( obj ) {
    obj.find(".photo_icons").hide();
    var table = obj.find("table");
    table.find("td:not(.photo)").each(function() {
    	$(this).hide();
    });
    table.toggleClass('album_minitables', 'fast');
}
function showPhotoFrame( obj ) {
    obj.find(".photo_icons").show();
    var table = obj.find("table");
    table.find("td:not(.photo)").each(function() {
        $(this).show();
    });
    table.toggleClass('album_minitables', 'fast');
}

$(function() {
    $(".photo_frame").draggable({
        stack: ".photo_frame",
        revert: true,
        start: function() {
            hidePhotoFrame( $(this) );
        },
        stop: function() {
            showPhotoFrame( $(this) );
        }
    });
    $(".photo_frame").disableSelection();
    
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
    
    $("#albumId").change(function(){
        var albumId = $("#albumId option:selected").val();
        if( albumId != 0 && $("#album_"+ albumId).get() == '' ) {
            $(this).after('<div id="album_'+ albumId +'" class="targetAlbum">'+ $("#albumId option:selected").text() +'</div>');
            }

        $(".targetAlbum").droppable({
            accept: ".photo_frame",
            hoverClass: "ui-state-active",
            drop: function( event, ui ) {
            	$("#album_"+ albumId).html( "Moved!" );
                $(ui.draggable).draggable("disable");
                //$(ui.draggable).draggable("enable");
                $(ui.draggable).hide('blind', null, 'slow');
            }
        });
    });
});

</script>

<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}<c:if test="${!isThisUser}">
      <span class="additional"> пользователя <a href="${pageContext.request.contextPath}/user_${album.user.userId}/albumsList.html">${album.user.login}</a></span>
      </c:if>
    </div>

    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
    <div id="sortable">
<c:forEach items="${album.photos}" var="photo">
    <div style="float:left;" class="photo_frame ui-widget-content" id="photo_${photo.photoId}">
      <div class="photo_icons">
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/delete.html">&#160;X&#160;</a>]
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/info.html">&#160;E&#160;</a>]
        <a href="" class="left">&#8592;</a>&#160;
        <a href="" class="right">&#8594;</a>&#160;
      </div>
      <table class="album_minitables">
        <tr>
          <td class="photo">
            <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
              <c:if test="${photo.photoFilesList[4].filename == ''}"><img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" /></c:if>
              <c:if test="${photo.photoFilesList[4].filename != ''}"><img style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" /></c:if>
            </a>
          </td>
        </tr>
        <tr>
          <td style="height:32px; vertical-align:top;">
            <div style="overflow:hidden; width:187px;">
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
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;">
          <div class="main">
<c:if test="${isThisUser}">
            <a href="${pageContext.request.contextPath}<c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">Загрузить фотографии</a>&#160;
  <c:if test="${album.albumId > 0}"><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html">Изменить альбом</a>
            <br /><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html">Удалить альбом</a>
  </c:if>
</c:if>
          </div>
          <div class="main" id="moveAlbumsList">
		    <select id="albumId">
              <option value="0">Отсутствует</option>
<c:forEach items="${albums}" var="album">
		      <option value="${album.albumId}">${album.title}</option>
</c:forEach>
		    </select>
          </div>
        </td>
      </tr>
    </table>
  </div>
</div>

<jsp:include page="footer.jsp" />