<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" />
<form action="" method="post" enctype="application/x-www-form-urlencoded">
<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%;">
  <tr>
    <td>Редактирование пользовательских данных:</td>
  </tr>
  <tr>
    <td>
      <table>
        <tr>
          <td style="width:150px;">Логин:</td>
          <td style="width:350px;">
            <spring:bind path="user.login">
                <input style="height:20px; width:100px;" disabled type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Псевдоним:</td>
          <td style="width:350px;">
            <spring:bind path="user.nickName">
                <input style="height:20px; width:100px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Имя:</td>
          <td style="width:350px;">
            <spring:bind path="user.firstName">
                <input style="height:20px; width:100px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Фамилия:</td>
          <td style="width:350px;">
            <spring:bind path="user.lastName">
                <input style="height:20px; width:100px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">e-mail:</td>
          <td style="width:350px;">
            <spring:bind path="user.email">
                <input style="height:20px; width:100px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="full_size"></td>
  </tr>
</table>
</form>
<jsp:include page="footer.jsp" />