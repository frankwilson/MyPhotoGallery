<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<form:form commandName="album">
<div class="page_header">Добавить альбом:</div>
<table class="main" cellpadding="1" cellspacing="0" style="width:100%">
  <colgroup>
    <col style="width:120px;"/>
  </colgroup>
  <tr>
    <td>Название:</td>
    <td>
      <form:input path="title" cssStyle="height:20px; width:300px;"/>
      <form:errors path="title" />
    </td>
  </tr>
  <tr>
    <td>Описание:</td>
    <td>
      <form:textarea path="description" cols="60" rows="4" />
      <form:errors path="description" />
    </td>
  </tr>
  <tr>
    <td></td>
    <td><input type="submit" value="Создать" /></td>
  </tr>
</table>
</form:form>
<jsp:include page="footer.jsp" />