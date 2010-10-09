<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
window.addEvent('domready', function() { // wait for the content
   // our uploader instance 
   var up = new FancyUpload2($('demo-status'), $('demo-list'), { // options object
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
           $('demo-status').removeClass('hide'); // we show the actual UI
           $('demo-fallback').destroy(); // ... and hide the plain form

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
           $('demo-clear').addEvent('click', function() {
               up.remove(); // remove all files
               return false;
           });

           $('demo-upload').addEvent('click', function() {
               up.start(); // start upload
               return false;
           });
       },

       // Edit the following lines, it is your custom event handling

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

       /**
        * This one was directly in FancyUpload2 before, the event makes it
        * easier for you, to add your own response handling (you probably want
        * to send something else than JSON or different items).
        */
       onFileSuccess: function(file, response) {
           var json = new Hash(JSON.decode(response, true) || {});

           if (json.get('status') == '1') {
               file.element.addClass('file-success');
               file.info.set('html', '<strong>Image was uploaded:</strong> ' + json.get('width') + ' x ' + json.get('height') + 'px, <em>' + json.get('mime') + '</em>)');
           } else {
               file.element.addClass('file-failed');
               file.info.set('html', '<strong>An error occured:</strong> ' + (json.get('error') ? (json.get('error') + ' #' + json.get('code')) : response));
           }
       },

       /**
        * onFail is called when the Flash movie got bashed by some browser plugin
        * like Adblock or Flashblock.
        */
       onFail: function(error) {
           switch (error) {
               case 'hidden': // works after enabling the movie and clicking refresh
                   alert('To enable the embedded uploader, unblock it in your browser and refresh (see Adblock).');
                   break;
               case 'blocked': // This no *full* fail, it works after the user clicks the button
                   alert('To enable the embedded uploader, enable the blocked Flash movie (see Flashblock).');
                   break;
               case 'empty': // Oh oh, wrong path
                   alert('A required file was not found, please be patient and we fix this.');
                   break;
               case 'flash': // no flash 9+ :(
                   alert('To enable the embedded uploader, install the latest Adobe Flash plugin.')
           }
       }

   });

});
/* ]]> */
</script>
<style>
/**
 * FancyUpload Showcase
 *
 * @license     MIT License
 * @author      Harald Kirschner <mail [at] digitarald [dot] de>
 * @copyright   Authors
 */
 
/* CSS vs. Adblock tabs */
.swiff-uploader-box a {
    display: none !important;
}
 
/* .hover simulates the flash interactions */
a:hover, a.hover {
    color: red;
}
 
#demo-status {
    padding: 10px 15px;
    width: 420px;
    border: 1px solid #eee;
}
 
#demo-status .progress {
    background: url(${pageContext.request.contextPath}/img/progress-bar/progress.gif) no-repeat;
    background-position: +50% 0;
    margin-right: 0.5em;
    vertical-align: middle;
}
 
#demo-status .progress-text {
    font-size: 0.9em;
    font-weight: bold;
}
 
#demo-list {
    list-style: none;
    width: 450px;
    margin: 0;
}
 
#demo-list li.validation-error {
    padding-left: 44px;
    display: block;
    clear: left;
    line-height: 40px;
    color: #8a1f11;
    cursor: pointer;
    border-bottom: 1px solid #fbc2c4;
    background: #fbe3e4 url(${pageContext.request.contextPath}/img/failed.png) no-repeat 4px 4px;
}
 
#demo-list li.file {
    border-bottom: 1px solid #eee;
    background: url(${pageContext.request.contextPath}/img/file.png) no-repeat 4px 4px;
    overflow: auto;
}
#demo-list li.file.file-uploading {
    background-image: url(${pageContext.request.contextPath}/img/uploading.png);
    background-color: #D9DDE9;
}
#demo-list li.file.file-success {
    background-image: url(${pageContext.request.contextPath}/img/success.png);
}
#demo-list li.file.file-failed {
    background-image: url(${pageContext.request.contextPath}/img/failed.png);
}
 
#demo-list li.file .file-name {
    font-size: 1.2em;
    margin-left: 44px;
    display: block;
    clear: left;
    line-height: 40px;
    height: 40px;
    font-weight: bold;
}
#demo-list li.file .file-size {
    font-size: 0.9em;
    line-height: 18px;
    float: right;
    margin-top: 2px;
    margin-right: 6px;
}
#demo-list li.file .file-info {
    display: block;
    margin-left: 44px;
    font-size: 0.9em;
    line-height: 20px;
    clear
}
#demo-list li.file .file-remove {
    clear: right;
    float: right;
    line-height: 18px;
    margin-right: 6px;
}
</style>

<div class="top_level">
  <div class="content">
    <form:form enctype="multipart/form-data" id="upload_form" action="${pageContext.request.contextPath}/${albumUrl}upload.html;jsessionid=${pageContext.session.id}">
      <div class="page_header">Загрузка фотографии
  <c:if test="${currentAlbum != null}">
         в альбом: <a href="${pageContext.request.contextPath}/album_${currentAlbum.albumId}.html">${currentAlbum.title}</a>
  </c:if>
      </div>
      <div class="main">
  <c:if test="${currentAlbum == null && fn:length(albums) > 0}">
        Альбом для размещения фотографии:
        <br /><br />
        <select name="album">
          <option value=0>Отсутствует</option>
    <c:forEach items="${albums}" var="album">
          <option value="${album.albumId}">${album.title}</option>
    </c:forEach>
        </select>
        <br /><br />
  </c:if>
      </div>

      <fieldset id="demo-fallback">
          <legend>File Upload</legend>
          <p>Если вы видите этот блок, значит что-то пошло не так</p>
          <label for="demo-photoupload">
              Upload a Photo:
              <input type="file" name="Filedata" />
          </label>
      </fieldset>

    <div id="demo-status" class="hide">
        <p>
            <a href="#" id="addFiles">Добавить</a> |
            <a href="#" id="demo-clear">Очистить список</a> |
            <a href="#" id="demo-upload">Загрузить</a>
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
 
    <ul id="demo-list"></ul>

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
