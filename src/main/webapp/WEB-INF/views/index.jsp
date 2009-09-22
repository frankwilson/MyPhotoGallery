<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%;">
  <tr>
    <td>
      Последние обновленные альбомы:<br />
      <h3>Сегодня:</h3>
      <table><!-- photos_start -->      {TR1}
          <td style="text-align:center; vertical-align:top;">
            <table class="album_minitables">
              <tr>
                <td style="height:120px;">
                  <a href="{SYSTEM_PATH}/album/{ALBUM_ID}"><img {PARAMETERS} border="0" /></a>
                </td>
              </tr>
              <tr>
                <td style="height:20px;vertical-align:top;">
                  <a href="{SYSTEM_PATH}/album/{ALBUM_ID}">{ALBUM_NAME}</a>
                </td>
              </tr>
            </table>
          </td>{TR2}<!-- photos_end --><!-- empty_start -->
          <td style="text-align:center; vertical-align:top;">
            <table class="album_minitables" style="border:0px;">
              <tr>
                <td style="height:120px;"></td>
              </tr>
              <tr>
                <td style="height:20px;vertical-align:top;">{ALBUM_NAME}</td>
              </tr>
            </table>
          </td><!-- empty_end -->
{ALBUMS_LIST1}
      </table>
      <h3>Вчера:</h3>
      <table>
{ALBUMS_LIST2}
      </table>
    </td>
  </tr>
  <tr>
    <td class="full_size"></td>
  </tr>
  </form>
</table>
<jsp:include page="footer.jsp" />