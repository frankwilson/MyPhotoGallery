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

.ui-effects-transfer {
    border: 2px dotted gray;
}
</style>
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

   	$(".photo a img").draggable({
        stack: ".photo_frame",
        revert: true
    });
    $(".photo_frame").disableSelection();
    
    $("#albumId").change(function(){
        var albumId = $("#albumId option:selected").val();
        if( albumId != 0 && $("#album_"+ albumId).get() == '' ) {
            $(this).after('<div id="album_'+ albumId +'" class="targetAlbum">'+ $("#albumId option:selected").text() +'</div>');
        }

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
              <c:if test="${photo.photoFilesList[4].filename != ''}"><img id="image_${photo.photoId}" style="margin-top:10px;" src="/images/${photo.photoFilesList[4].filename}" alt="${photo.title}" border="0" /></c:if>
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
  <div class="left_panel">
<c:if test="${isThisUser}">
    <div class="left_block">
      <div class="header">Ссылки</div>
      <div class="body">
        <a href="${pageContext.request.contextPath}<c:if test="${album.albumId > 0}">/album_${album.albumId}</c:if>/upload.html">Загрузить фотографии</a>&#160;
  <c:if test="${album.albumId > 0}"><br />
        <a href="${pageContext.request.contextPath}/album_${album.albumId}/info.html">Изменить альбом</a>
        <br /><br />
        <a href="${pageContext.request.contextPath}/album_${album.albumId}/delete.html">Удалить альбом</a>
  </c:if>
      </div>
    </div>
    <div class="left_block" id="moveAlbumsList">
      <div class="header">Альбомы</div>
      <div class="body">
	    <select id="albumId">
          <option value="0">Отсутствует</option>
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