<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="ru.pakaz.photo.model.Album"%>
<jsp:include page="header.jsp" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/mootools.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/Swiff.Uploader.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/Fx.ProgressBar.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/Lang.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/FancyUpload2.js"></script>
 
<script type="text/javascript"> 
/* <![CDATA[ */

window.addEvent('domready', function() {
    var up = new FancyUpload2($('uploadStatus'), $('filesList'), { // options object
        verbose: false,
        url: $('upload_form').action,
        path: '${pageContext.request.contextPath}/scripts/Swiff.Uploader.swf',
        typeFilter: { 'Images (*.jpg, *.jpeg, *.png)': '*.jpg; *.jpeg; *.png' },
        target: 'addFiles',
        appendCookieData: true,
        onLoad: function() {
            $('uploadStatus').removeClass('hide'); // we show the actual UI
            $('fallbackBlock').destroy();          // ... and hide the plain form
 
            this.target.addEvents({
                click: function() {
                    return false;
                },
                mouseenter: function() {
                    this.addClass('hover');
                },
                mouseleave: function() {
                    this.removeClass('hover');
                    this.blur();
                },
                mousedown: function() {
                    this.focus();
                }
            });
 
            $('clearList').addEvent('click', function() {
                up.remove();
                return false;
            });
 
            $('uploadFiles').addEvent('click', function() {
                up.start();
                return false;
            });
        },
  
        onSelectFail: function(files) {
            files.each(function(file) {
                new Element('td', {
                    'class': 'validation-error',
                    'colspan': '4',
                    html: file.validationErrorMessage || file.validationError,
                    title: MooTools.lang.get('FancyUpload', 'removeTitle'),
                    events: {
                        click: function() {
                            this.destroy();
                        }
                    }
                }).inject(this.list, 'top');
            }, this);
        },
 
        onFileSuccess: function(file, response) {
            var json = new Hash(JSON.decode(response, true) || {});
 
            if (json.get('status') == '1') {
                file.info.set('html', '<strong><spring:message code="page.uploadPhoto.uploaded"/>:</strong> ' + json.get('width') + ' x ' + json.get('height') + 'px, <em>' + json.get('mime') + '</em>');
 
                if( <c:if test="${currentAlbum != null}">${currentAlbum.albumId}</c:if><c:if test="${currentAlbum == null}">0</c:if> == 0 ){
                    unallocatedPhotosCount++;
                    $("unallocatedPhotosCount").set('html', unallocatedPhotosCount);
                }
            }
            else {
                file.info.set('html', '<strong><spring:message code="page.uploadPhoto.uploadError"/>:</strong> ' + (json.get('error') ? (json.get('error') + ' #' + json.get('code')) : response));
            }
        },
 
        onFail: function(error) {
            switch (error) {
                case 'hidden': // works after enabling the movie and clicking refresh
                    $('errorMessage').set('html', '<spring:message code="page.uploadPhoto.flashHidden"/>.');
                    break;
                case 'blocked': // This no *full* fail, it works after the user clicks the button
                    $('errorMessage').set('html', '<spring:message code="page.uploadPhoto.flashBlocked"/>.');
                    break;
                case 'empty': // Oh oh, wrong path
                    $('errorMessage').set('html', '<spring:message code="page.uploadPhoto.flashEmpty"/>.');
                    break;
                case 'flash': // no flash 9+ :(
                    $('errorMessage').set('html', '<spring:message code="page.uploadPhoto.flashOther"/>.')
            }
        }
    });
});
/* ]]> */

</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/multipleFilesUpload.js"></script>
<style>
/* CSS vs. Adblock tabs */
.swiff-uploader-box a {
    display: none !important;
}

#uploadStatus {
    padding-left: 15px;
    padding-bottom: 10px;
    width: 750px;
    /*border: 1px solid #eee;*/
}
 
#uploadStatus .progress {
    background: url(${pageContext.request.contextPath}/img/progress-bar.png) repeat-x;
    margin-right: 0.5em;
    vertical-align: middle;
}
 
#uploadStatus .progress-text {
    font-size: 0.9em;
}
 
#filesList {
    list-style: none;
    padding-left: 0px;
    #width: 850px;
    margin: 0;
}
 
#filesList tr.validation-error {
    /*display: block;*/
    clear: left;
    line-height: 40px;
    color: #8a1f11;
    cursor: pointer;
    border-bottom: 1px solid #fbc2c4;
}
 
#filesList tr.file td {
    border-bottom: 1px solid #eee;
    overflow: auto;
}
#filesList tr.file.file-uploading {
    background-color: #D9DDE9;
}

td.file-size {
    width: 70px;
}
td.file-remove {
    width: 28px;
}
td.file-name {
    padding-right: 10px;
    min-width: 120px;
    max-width: 600px;
}
td.file-info {
    #font-size: 0.9em;
}

.hide {
    display: none;
}
</style>

<form:form enctype="multipart/form-data" id="upload_form" action="${pageContext.request.contextPath}/${albumUrl}upload.html;jsessionid=${pageContext.session.id}">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.uploadPhoto.title"/>
<c:if test="${currentAlbum != null}">
       <spring:message code="page.uploadPhoto.toAlbum"/>: <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
</c:if>
    </div>
    <span class="tabs_links">
      <a href="javascript:void(0);" onClick="jQuery('.uploaderForm').hide(); jQuery('#multipleFileUploader').show();"><spring:message code="page.uploadPhoto.MultipleUpload"/></a> |
      <a href="javascript:void(0);" onClick="jQuery('.uploaderForm').hide(); jQuery('#flashUploader').show();"><spring:message code="page.uploadPhoto.flashUpload"/></a> | 
      <a href="javascript:void(0);" onClick="jQuery('.uploaderForm').hide(); jQuery('#singleFileUploader').show();"><spring:message code="page.uploadPhoto.singleUpload"/></a>
    </span>

    <br />

    <div id="flashUploader" class="uploaderForm" style="display:none;">
      <span id="fallbackBlock">
        <span id="errorMessage"><spring:message code="page.uploadPhoto.flashError"/>.</span>
      </span>
      <div id="uploadStatus" class="hide">
        <p>
          <a href="#" id="addFiles"><spring:message code="page.uploadPhoto.addPhoto"/></a> |
          <a href="#" id="clearList"><spring:message code="page.uploadPhoto.clearList"/></a> |
          <a href="#" id="uploadFiles"><spring:message code="page.uploadPhoto.upload"/></a>
        </p>
        <div style="vertical-align:middle;">
          <div style="line-height:22px;"><b class="overall-title"></b></div>
          <img src="${pageContext.request.contextPath}/img/progress-bar-frame.png" class="progress overall-progress" />
        </div>
        <div style="vertical-align:middle;">
          <div style=" line-height:22px;"><b class="current-title"></b></div>
          <img src="${pageContext.request.contextPath}/img/progress-bar-frame.png" class="progress current-progress" />
        </div>
        <div class="current-text"></div>
      </div>
   
      <table class="main" id="filesList"></table>
    </div>

    <div id="multipleFileUploader" class="uploaderForm" style="display:none;">
      <div style="padding-top:1em;">
        <spring:message code="page.uploadPhoto.uploadPhoto"/>:&#160;
        <input type="file" name="file" id="file-field" multiple="true" />
        <br />
        <input type="button" name="uploadFile" id="uploadFile" value="<spring:message code="page.uploadPhoto.uploadButton"/>">
      </div>
      <div id="img-container">
        <ul id="img-list"></ul>
      </div>
    </div>
    
    <div id="singleFileUploader" class="uploaderForm" style="display:none;">
      <div style="padding-top:1em;">
        <spring:message code="page.uploadPhoto.uploadPhoto"/>:&#160;
        <input type="file" name="single_file" />
        <br />
        <input type="submit" name="upload" value="<spring:message code="page.uploadPhoto.uploadButton"/>" />
      <c:if test="${fileName != null}">
        <br /><br />
        <div id="uploadedFileInfo">
          <spring:message code="page.uploadPhoto.fileUploaded"/>: <a href="${pageContext.request.contextPath}/photo_${photoId}.html">${fileName}</a>
          (${width}x${height}<c:if test="${mime != null}">, ${mime}</c:if>)
        </div>
      </c:if>
      <c:if test="${status == 0}"><spring:message code="page.uploadPhoto.fileUploadErr"/>: ${error}</c:if>
      </div>
    </div>
    <br />
  </div>
</div>

</form:form>
<script type="text/javascript">
<!--
// Проверяем поддержку File API 
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Стандарный input для файлов
    var fileInput = jQuery('#file-field');

    // ul-список, содержащий миниатюрки выбранных файлов
    var imgList = jQuery('ul#img-list');

    // Контейнер, куда можно помещать файлы методом drag and drop
    var dropBox = jQuery('#img-container');

    // Обработка события выбора файлов в стандартном поле
    fileInput.bind({
        change: function() {
            displayFiles(this.files);
        }
    });
       
    // Обработка событий drag and drop при перетаскивании файлов на элемент dropBox
    dropBox.bind({
        dragenter: function() {
        	jQuery(this).addClass('highlighted');
            return false;
        },
        dragover: function() {
            return false;
        },
        dragleave: function() {
        	jQuery(this).removeClass('highlighted');
            return false;
        },
        drop: function(e) {
            var dt = e.originalEvent.dataTransfer;
            displayFiles(dt.files);
            return false;
        }
    });
}
else {
    alert('File API не поддерживается данным браузером');
}

jQuery("#uploadFile").click(function(){
	imgList.find('li').each(function() {
		var uploadItem = this;
	    var pBar = jQuery(uploadItem).find('.progress');
    	uploadFile(uploadItem.file, "${pageContext.request.contextPath}/${albumUrl}upload.html;jsessionid=${pageContext.session.id}");
    });
});
//-->
</script>
<jsp:include page="footer.jsp" />
