<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<form method="POST" enctype="multipart/form-data">
<table width="400" cellpadding="0" cellspacing="0">
  <tr>
    <td><a href="{SYSTEM_PATH}/album/{ALBUM_ID}"><span style="font-size:22px;">{ALBUM_NAME}</span></a></td>
  </tr>
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
</form>
<jsp:include page="footer.jsp" />