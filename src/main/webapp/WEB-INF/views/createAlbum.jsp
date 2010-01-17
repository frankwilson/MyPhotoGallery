<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />
<form method="post">
<div class="page_header">Добавить альбом:</div>
<table class="main" cellpadding="1" cellspacing="0" style="width:100%">
  <colgroup>
    <col style="width:120px;"/>
  </colgroup>
  <tr>
    <td>Название:</td>
    <td>
      <input type="text" name="name" id="name" style="height:20px; width:300px;" value="" />
    </td>
  </tr>
  <tr>
    <td>Описание:</td>
    <td>
      <textarea name="desc" id="desc" cols=60 rows=4></textarea>
    </td>
  </tr>
  <tr>
    <td></td>
    <td><input type="submit" name="add" id="add" value="Создать" style="width:150px;" /></td>
  </tr>
</table>
</form>
<jsp:include page="footer.jsp" />