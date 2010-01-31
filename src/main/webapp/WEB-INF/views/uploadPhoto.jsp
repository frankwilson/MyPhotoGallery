<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<div class="top_level">
	<div class="content">
	  <form:form  enctype="multipart/form-data">
	    <div class="page_header">Загрузка фотографии в альбом</div>
	    <div class="main">
	      Альбом для размещения фотографии:
	      <br /><br />
	      <select name="album">
	        <option value=0>Отсутствует</option>
<c:forEach items="${albums}" var="album">
            <option value=<c:out value="${album.albumId}"></c:out>><c:out value="${album.title}"></c:out></option>
</c:forEach>
	      </select>
	      <br /><br />
		  Укажите файл, который вы хотите загрузить:
		  <br /><br />
		  <input type="hidden" name="MAX_FILE_SIZE" value="6291456" />
		  <input size="80" type="file" name="file" id="file" />
		  <br /><br />
		  <input style="width:120px;" type="submit" name="get" id="get" value="Загрузить" />
	    </div>
	  </form:form>
	</div>
	<div>
	  <table style="width:220px; height:100%; background-color:#deecaa; margin:0px; padding:0px;vertical-align:top;">
	    <tr>
	      <td style="vertical-align:top;"></td>
	    </tr>
	  </table>
	</div>
</div>
<jsp:include page="footer.jsp" />