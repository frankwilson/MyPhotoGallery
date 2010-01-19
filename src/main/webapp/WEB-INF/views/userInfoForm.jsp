<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />
<form:form commandName="user">
<div class="page_header">Редактирование пользовательских данных:</div>
<table class="main">
  <colgroup>
    <col style="width:90px;">
  </colgroup>
  <tr>
    <td style="width:150px; height:22px;">Логин:</td>
    <td>
      <c:out value="${user.login}" />
    </td>
  </tr>
  <tr>
    <td>Псевдоним:</td>
    <td>
      <form:input path="nickName" cssStyle="width:200px;"/>&#160;
      <form:errors path="nickName"/>
    </td>
  </tr>
  <tr>
    <td>Имя:</td>
    <td>
      <form:input path="firstName" cssStyle="width:200px;"/>&#160;
      <form:errors path="firstName"/>
    </td>
  </tr>
  <tr>
    <td>Фамилия:</td>
    <td>
      <form:input path="lastName" cssStyle="width:200px;"/>&#160;
      <form:errors path="lastName"/>
    </td>
  </tr>
  <tr>
    <td>e-mail:</td>
    <td>
      <form:input path="email" cssStyle="width:200px;"/>&#160;
      <form:errors path="email"/>
    </td>
  </tr>
  <tr><td colspan=3 style="height:10px;"></td></tr>
  <tr>
    <td>Новый пароль:</td>
    <td>
      <form:password path="password" cssStyle="width:200px;"/>&#160;
      <form:errors path="password" />
    </td>
  </tr>
  <tr><td colspan=2 style="height:10px;"></td></tr>
  <tr>
    <td></td>
    <td><input type="submit" value="Сохранить" /></td>
  </tr>
</table>
</form:form>
<jsp:include page="footer.jsp" />