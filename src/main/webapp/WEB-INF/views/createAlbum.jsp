<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<form:form commandName="album">
<div class="top_level">
  <div class="content">
    <div class="page_header">Добавить альбом:</div>
    <div class="main">
      Название:
      <br /><br />
      <form:input path="title" cssStyle="height:20px; width:300px;"/>
      <form:errors path="title" />
      <br /><br />
      Описание:
      <br /><br />
      <form:textarea path="description" cols="60" rows="4" />
      <form:errors path="description" />
      <br /><br />
      <input type="submit" value="Создать" />
    </div>
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