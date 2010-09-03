<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" />

<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header">Регистрация:</div>
    <table class="main" style="width:100%;">
      <tr>
        <td style="width:100px;"><span style="color:#a44">*</span>Логин:</td>
        <td>
          <form:input path="login" maxlength="32" autocomplete="off" cssStyle="height:20px; width:175px;" />
          <form:errors path="login"/>
        </td>
      </tr>
      <tr>
        <td><span style="color:#a44">*</span>e-mail:</td>
        <td>
          <form:input path="email" maxlength="64" autocomplete="off" cssStyle="height:20px; width:250px;" />
          <form:errors path="email"/>
        </td>
      </tr>
      <tr><td colspan=3 style="height:10px;"></td></tr>
      <tr>
        <td>Пароль:</td>
        <td>
          <form:password path="password" maxlength="32" autocomplete="off" cssStyle="height:20px; width:250px;" />
          <form:errors path="password"/>
        </td>
      </tr>
      <tr>
        <td colspan=2>Если вы не введете пароль, он будет автоматически сгенерирован.</td>
      </tr>
      <tr><td colspan=3 style="height:10px;"></td></tr>
      <tr>
        <td></td>
        <td><input style="height:25px; width:180px;" type="submit" value="Зарегистрироваться" id="register"></td>
      </tr>
    </table>
    <br />
    После нажатия кнопки "Зарегистрироваться" Вам будет отправлено электронное письмо с логином, паролем и ссылкой подтверждения.
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />