<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<div class="top_level">
  <div class="content">
    <div class="page_header">Редактирование фотографии:
      <a href="${pageContext.request.contextPath}/photo_${photo.photoId}.html">${photo.title}</a>
    </div>
<form:form commandName="photo">
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td>Превьюшка:</td>
        <td class="photo">
          <img src="${pageContext.request.contextPath}/photo_${photo.photoId}/size_150/show.html" alt="${photo.title}" border="0" />
        </td>
      </tr>
      <tr>
        <td>Название:</td>
        <td>
          <form:input path="title" cssStyle="width:500px;"/>&#160;
          <form:errors path="title"/>
        </td>
      </tr>
      <tr>
        <td>Описание:</td>
        <td>
          <form:textarea path="description" cols="60" rows="4"/>&#160;
          <form:errors path="description"/>
        </td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td><input type="submit" value="Сохранить" /></td>
      </tr>
    </table>
</form:form>
<form:form commandName="album" action="${pageContext.request.contextPath}/photo_${photo.photoId}/move.html">
    <div>
      <input type="submit" value="Переместить в:" />
      <form:select path="albumId">
        <form:option value="0">Отсутствует</form:option>
        <form:options items="${albums}" itemLabel="title" itemValue="albumId" />
      </form:select>
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