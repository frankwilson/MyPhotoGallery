<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp" />
<style>
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

.ui-effects-transfer {
    border: 2px dotted gray;
}
</style>
<script>

$(function() {
    $(".photo a img").draggable({
        stack: ".photo_frame",
        revert: true
    });
    $(".photo_frame").disableSelection();
    
    $("#albumId").change(function(){
        var albumId = $("#albumId option:selected").val();
        if( albumId != 0 && $("#album_"+ albumId).get() == '' ) {
            $(this).after('<div id="album_'+ albumId +'" class="targetAlbum" style="display:none;">[<a href="" class="targetAlbumClose">&#160X&#160;</a>]&#160;'+ $("#albumId option:selected").text() +'</div>');
            $("#album_"+ albumId).show("blind", null, 'fast');
        }
        else if( $("#album_"+ albumId).get() != '' && $("#album_"+ albumId).css("display") == "none" ) {
            $("#album_"+ albumId).show("blind", null, 'fast');
        }
        
        $(".targetAlbumClose").click(function() {
            $(this).parent().hide("blind", null, "fast");
            return false;
        });

        $(".targetAlbum").droppable({
            accept: ".photo a img",
            hoverClass: "ui-state-active",
            drop: function( event, ui ) {
                $(ui.draggable).draggable( "option", "revert", false );
                $(ui.draggable).draggable("disable");
                var re = /^image_(\d+)$/i;
                var str = $(ui.draggable).attr("id");
                if( str != undefined ) {
                    var photoId = str.match( re )[1];

                    $.ajax({
                        type: "POST",
                        data: "albumId="+ albumId,
                        url: './photo_'+ photoId +'/move.html',
                        dataType: "json",
                        success: function(data) {
                            if( data.moved == true ) {
                                var options = {};
                                $(ui.draggable).effect('fade', options, 'slow');
                                
                                options = { to: '#album_'+ albumId, className: "ui-effects-transfer" };
                                $("#photo_"+ photoId).effect('transfer', options, 'slow');
                                $("#photo_"+ photoId).effect('fade', options, 'slow');
                            }
                            else {
                                $(ui.draggable).draggable("enable");
                                $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
                                $(ui.draggable).draggable( "option", "revert", true );
                            }
                        },
                        error: function(xhr, status, trown) {
                            if( status != 'success' && xhr.status == 200 ) {
                                $(ui.draggable).draggable("enable");
                                $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
                                $(ui.draggable).draggable( "option", "revert", true );
                                alert('Status is not success!');
                            }
                            else if( xhr.status != 200 ) {
                                $(ui.draggable).draggable("enable");
                                $(ui.draggable).animate({top: '0px', left: '0px'}, 'slow');
                                $(ui.draggable).draggable( "option", "revert", true );
                                alert('Server return status '+ xhr.status +'!');
                            }
                        }
                    });
                }
            }
        });
    });
});

</script>

<div class="top_level">
  <div class="content">
    <div class="page_header">
      ${album.title}<c:if test="${!isThisUser}">
      <span class="additional">пользователя <a href="${pageContext.request.contextPath}/user_${album.user.userId}/albumsList.html">${album.user.login}</a></span>
      </c:if>
    </div>
    <div class="page_description">${album.description}</div>
    <%-- здесь будет список страниц для постраничного вывода --%>
<c:forEach items="${album.photos}" var="photo">
    <div style="float:left;" id="photo_${photo.photoId}">
      <div class="photo_icons">
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/delete.html">&#160;X&#160;</a>]
        [<a href="${pageContext.request.contextPath}/photo_${photo.photoId}/info.html">&#160;E&#160;</a>]
      </div>
      <table class="album_microtables">
        <tr>
          <td class="photo" <%--style="height:170px; background-color:#cfcfcf;" --%>>
            <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html" title="${photo.title}">
              <c:if test="${photo.photoFilesList[4].filename == ''}"><img style="margin-top:10px;" src="${pageContext.request.contextPath}/img/album_no_preview.png" /></c:if>
              <c:if test="${photo.photoFilesList[4].filename != ''}"><img id="image_${photo.photoId}" style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" /></c:if>
            </a>
          </td>
        </tr>
      </table>
    </div>
</c:forEach>
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;">
<c:if test="${isThisUser}">
          <div class="main">
            <a href="${pageContext.request.contextPath}<c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">Загрузить фотографии</a>&#160;
  <c:if test="${album.albumId > 0}"><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html">Изменить альбом</a>
            <br /><br />
            <a href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html">Удалить альбом</a>
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
</c:if>
        </td>
      </tr>
    </table>
  </div>
</div>

<jsp:include page="footer.jsp" />