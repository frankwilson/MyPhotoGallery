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
       // we console.log infos, remove that in production!!
       verbose: true,

       // url is read from the form, so you just have to change one place
       url: $('upload_form').action,

       // path to the SWF file
       path: '${pageContext.request.contextPath}/scripts/Swiff.Uploader.swf',

       // remove that line to select all files, or edit it, add more items
       typeFilter: {
           'Images (*.jpg, *.jpeg, *.png)': '*.jpg; *.jpeg; *.png'
       },

       // this is our browse button, *target* is overlayed with the Flash movie
       target: 'addFiles',

       appendCookieData: true,

       // graceful degradation, onLoad is only called if all went well with Flash
       onLoad: function() {
           $('uploadStatus').removeClass('hide'); // we show the actual UI
           $('fallbackBlock').destroy(); // ... and hide the plain form

           // We relay the interactions with the overlayed flash to the link
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

           // Interactions for the 2 other buttons
           $('clearList').addEvent('click', function() {
               up.remove();
               return false;
           });

           $('uploadFiles').addEvent('click', function() {
               up.start();
               return false;
           });
       },

       /**
        * Is called when files were not added, "files" is an array of invalid File classes.
        * 
        * This example creates a list of error elements directly in the file list, which
        * hide on click.
        */ 
       onSelectFail: function(files) {
           files.each(function(file) {
               new Element('li', {
                   'class': 'validation-error',
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
           } else {
               file.info.set('html', '<strong><spring:message code="page.uploadPhoto.uploadError"/>:</strong> ' + (json.get('error') ? (json.get('error') + ' #' + json.get('code')) : response));
           }
       },

       /**
        * onFail is called when the Flash movie got bashed by some browser plugin
        * like Adblock or Flashblock.
        */
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
    background: url(${pageContext.request.contextPath}/img/progress-bar/progress.gif) no-repeat;
    #background: url(${pageContext.request.contextPath}/img/progress-bar-sample.png) repeat-x;
    margin-right: 0.5em;
    vertical-align: middle;
}
 
#uploadStatus .progress-text {
    font-size: 0.9em;
}
 
#filesList {
    list-style: none;
    padding-left: 0px;
    width: 750px;
    margin: 0;
}
 
#filesList li.validation-error {
    /*display: block;*/
    clear: left;
    line-height: 40px;
    color: #8a1f11;
    cursor: pointer;
    border-bottom: 1px solid #fbc2c4;
}
 
#filesList li.file {
    border-bottom: 1px solid #eee;
    overflow: auto;
}
#filesList li.file.file-uploading {
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
}
td.file-info {
    #font-size: 0.9em;
}

.hide {
    display: none;
}
</style>

<div class="top_level">
  <div class="content">
    <form:form enctype="multipart/form-data" id="upload_form" action="${pageContext.request.contextPath}/${albumUrl}upload.html;jsessionid=${pageContext.session.id}">
      <div class="page_header"><spring:message code="page.uploadPhoto.title"/>
  <c:if test="${currentAlbum != null}">
         <spring:message code="page.uploadPhoto.toAlbum"/>: <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
  </c:if>
      </div>

      <fieldset id="fallbackBlock">
        <legend><spring:message code="page.uploadPhoto.singleTitle"/></legend>
        <p id="errorMessage"><spring:message code="page.uploadPhoto.flashError"/>.</p>
        <label for="photoupload">
          <spring:message code="page.uploadPhoto.uploadPhoto"/>:
          <input type="file" name="single_file" />
        </label>
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
      </fieldset>

      <div id="uploadStatus" class="hide">
        <p>
          <a href="#" id="addFiles"><spring:message code="page.uploadPhoto.addPhoto"/></a> |
          <a href="#" id="clearList"><spring:message code="page.uploadPhoto.clearList"/></a> |
          <a href="#" id="uploadFiles"><spring:message code="page.uploadPhoto.upload"/></a>
        </p>
        <div>
          <b class="overall-title"></b><br />
          <img src="${pageContext.request.contextPath}/img/progress-bar/bar.gif" class="progress overall-progress" />
        </div>
        <div>
          <b class="current-title"></b><br />
          <img src="${pageContext.request.contextPath}/img/progress-bar/bar.gif" class="progress current-progress" />
        </div>
        <div class="current-text"></div>
      </div>
 
      <ul id="filesList"></ul>

    </form:form>
  </div>
</div>
<jsp:include page="footer.jsp" />
