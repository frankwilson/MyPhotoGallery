<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" /> 
<div class="top_level">
  <div class="content">
    <div class="page_header">Фотография</div>
    <br><br>
    <table style="width:100%;">
      <tr>
        <td style="text-align:center;" colspan=3>
          <img src="<%=application.getContextPath() %>/photo_<c:out value="${photo.photoId}"></c:out>/size_640/show.html" style="margin:10px;" alt="<c:out value="${photo.title}"></c:out>" />
        </td>
      </tr>
      <tr>
        <td style="text-align:left; width:150px;">
          <%-- {PREV_IMAGE_LINK} --%>
        </td>
        <td style="text-align:center;">
          <span style="font-size:14px;"><c:out value="${photo.title}"></c:out></span>
        </td>
        <td style="text-align:right; width:150px;">
          <%-- {NEXT_IMAGE_LINK} --%>
        </td>
      </tr>
      <tr>
        <td style="text-align:center;" colspan=3>
          <%-- {DESCRIPTION} --%>
        </td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />