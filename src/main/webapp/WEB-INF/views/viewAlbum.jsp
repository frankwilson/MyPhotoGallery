<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%;">
  <tr>
    <td>
      <span style="font-size:22px;">{ALBUM_NAME}</span><br />
      {TOP_PAGE_LINKS}
      <br />
      {ALBUM_DESCRIPTION}<br />
      <table class="line"><tr><td class="line"></td></tr></table><br />
      Загруженные фотографии:<br />
      
      <table>
        <tr>
          <td>
            Страницы списка: {PAGENUMS_LIST} <br />
            Текущая страница: {CURRENT_PAGENUM}
          </td>
        </tr>
      </table>
      
      <table style="width:100%; margin:0px; padding:0px; border-collapse:collapse;"><!-- photos_start -->      {TR1}
          <td style="text-align:center; vertical-align:top; width:{IMAGE_REL_SIZE}%;">
            <table class="album_minitables">
              <tr>
                <td style="height:160px;">
                  <a href="{SYSTEM_PATH}/album/{ALBUM_ID}/big/{PHOTO_ID}" title="{PHOTO_NAME}">
                    <img alt="{PHOTO_NAME}" style="margin:5px;" {PARAMETERS} border="0" />
                  </a>
                </td>
              </tr>
              <tr>
                <td style="height:20px;vertical-align:top;">
                  <a href="{SYSTEM_PATH}/album/{ALBUM_ID}/big/{PHOTO_ID}">{PHOTO_NAME}</a>
                </td>
              </tr>
              <tr>
                <td class="photo_caption" style="text-align:left; padding-left:5px;">
                    Добавлено: {ADD_DATE}<br />
                    {DELETE_PHOTO_LINK}
                </td>
              </tr>
            </table>
          </td>{TR2}<!-- photos_end --><!-- empty_start -->
          <td style="text-align:center; vertical-align:top; width:{IMAGE_REL_SIZE}%;">
          </td><!-- empty_end --><!-- error /-->
          <td style="text-align:center; vertical-align:top;">
            <table style="border:0px; width:100%'">
              <tr>
                <td style="height:20px;vertical-align:middle;"></td>
              </tr>
            </table>
          </td><!--/ error -->
{PHOTOS}
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td style="padding:5px; padding-bottom:10px; text-align:right;">{NEXT_PAGE_LINK}</td>
  </tr>
  <tr>
    <td class="full_size"></td>
  </tr>
</table>

<jsp:include page="footer.jsp" />