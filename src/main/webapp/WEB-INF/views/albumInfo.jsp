<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<form:form commandName="album">
<div class="top_level">
  <div class="content">
    <div class="page_header">Редактирование альбома:
      <a href="${pageContext.request.contextPath}/album_${album.albumId}.html">${album.title}</a>
    </div>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td>Превьюшка:</td>
        <td>
        <form:select path="preview">
          <form:option value="0">Отсутствует</form:option>
          <form:options items="${album.photos}" itemLabel="title" itemValue="photoId" />
        </form:select>
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
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;"></td>
      </tr>
    </table>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />