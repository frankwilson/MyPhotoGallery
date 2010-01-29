<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<form:form>
<table width="400" cellpadding="0" cellspacing="0">
  <tr>
    <td><span style="font-size:16px;">Загрузка фотографии в альбом</span></td>
  </tr>
  <tr>
    <td>Укажите файл, который вы хотите загрузить:</td>
  </tr>
  <tr>
    <td>
      <input type="hidden" name="MAX_FILE_SIZE" value="6291456" />
      <input size="80" type="file" name="file" id="file" /><br />
      <input style="width:120px;" type="submit" name="get" id="get" value="Загрузить" />
    </td>
  </tr>
</table>
</form:form>
<jsp:include page="footer.jsp" />