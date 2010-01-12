<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<form:form commandName="user">
<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%;">
  <tr>
    <td class="page_header">Редактирование пользовательских данных:</td>
  </tr>
  <tr>
    <td>
      <table>
        <colgroup>
          <col style="width:90px;">
        </colgroup>
        <tr>
          <td style="width:150px;">Логин:</td>
          <td style="width:350px;"><form:input path="login" disabled="disabled"/></td>
          <td><form:errors path="login"/></td>
        </tr>
        <tr>
          <td>Псевдоним:</td>
          <td><form:input path="nickName"/></td>
          <td><form:errors path="nickName"/></td>
        </tr>
        <tr>
          <td>Имя:</td>
          <td><form:input path="firstName" /></td>
          <td><form:errors path="firstName"/></td>
        </tr>
        <tr>
          <td>Фамилия:</td>
          <td><form:input path="lastName" /></td>
          <td><form:errors path="lastName"/></td>
        </tr>
        <tr>
          <td>e-mail:</td>
          <td><form:input path="email" /></td>
          <td><form:errors path="email"/></td>
        </tr>
        <tr>
          <td></td>
          <td style="padding-top:10px;"><input type="submit" value="Сохранить" /></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="full_size"></td>
  </tr>
</table>
</form:form>
<jsp:include page="footer.jsp" />