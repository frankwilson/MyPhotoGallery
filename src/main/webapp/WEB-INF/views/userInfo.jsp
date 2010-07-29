<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />
<div class="top_level">
  <div class="content">
    <div class="page_header">Данные пользователя:</div>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td style="width:150px; height:22px;">Логин:</td>
        <td>${user.login}</td>
      </tr>
      <tr>
        <td>Псевдоним:</td>
        <td>${user.nickName}</td>
      </tr>
      <tr>
        <td>Имя:</td>
        <td>${user.firstName}</td>
      </tr>
      <tr>
        <td>Фамилия:</td>
        <td>${user.lastName}</td>
      </tr>
      <tr><td colspan=2 style="height:10px;"></td></tr>
    </table>
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;">
          <div class="main">
            <a href="<%=application.getContextPath() %>/user_${user.userId}/albumsList.html">Альбомы пользователя</a>
          </div>
        </td>
      </tr>
    </table>
  </div>
</div>
<jsp:include page="footer.jsp" />