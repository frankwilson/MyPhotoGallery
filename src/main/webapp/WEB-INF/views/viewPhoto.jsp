<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" /> 
<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header">Фотография</div>
    <br><br>
    <table style="width:100%;">
      <tr>
        <td style="text-align:center;" colspan=3>
          <img {PARAMETERS} style="margin:10px;" alt="{PHOTO_NAME}" />
        </td>
      </tr>
      <tr>
        <td style="text-align:left; width:150px;">
          {PREV_IMAGE_LINK}
        </td>
        <td style="text-align:center;">
          <a href="{SYSTEM_PATH}/photo/full/{PHOTO_ID}" target="_blank" title="Просмотреть полноразмерное изображение">
            <span style="font-size:14px;">{PHOTO_NAME}</span>
          </a>&#160;&#160;({ALBUM_PHOTO_POSITION} из {ALBUM_PHOTO_COUNT})
        </td>
        <td style="text-align:right; width:150px;">
          {NEXT_IMAGE_LINK}
        </td>
      </tr>
      <tr>
        <td style="text-align:center;" colspan=3>
          {DESCRIPTION}
        </td>
      </tr><!-- change_photo_name_form /-->
    <form method="POST" enctype="multipart/form-data">
      <tr>
        <td style="text-align:center;" colspan=3>
          <input name="photo_name" id="photo_name" style="width:450px;" value="{PHOTO_NAME}" /><br />
          <textarea name="photo_description" id="photo_description" style="width:450px;" ></textarea><br />
          <input type="submit" name="change_photo_name" id="change_photo_name" value="сохранить" />
        </td>
      </tr>
      </form><!--/ change_photo_name_form -->{CHANGE_PHOTO_NAME_FORM}
    </table>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />