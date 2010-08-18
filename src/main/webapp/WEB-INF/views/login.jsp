<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" /> 
<form:form commandName="user">
<div class="top_level">
  <div class="content">
    <div class="page_header">Вход</div><c:if test="${activationResult != null}">
<c:if test="${activationResult == true}">Активация прошла успешно и теперь вы можете зайти на сайт.</c:if>
<c:if test="${activationResult == false}">Во время активации произошла ошибка. Вы можете повторно запросить ссылку для активации.</c:if>
    <br />
    <br /></c:if>
    Для входа введите аутентификационную информацию в следующие поля:
    <br><br>
	<table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td>Логин:&#160;</td>
        <td><form:input path="login"/></td>
      </tr>
      <tr>
        <td></td>
        <td><form:errors path="login" cssStyle="color:#f22"></form:errors></td>
      </tr>
      <tr>
        <td>Пароль:&#160;</td>
        <td><form:password path="password"/></td>
      </tr>
      <tr>
        <td></td>
        <td><form:errors path="password" cssStyle="color:#f22"></form:errors></td>
      </tr>
      <tr>
        <td></td>
        <td style="padding-top:10px;"><input style="width: 80px;" type="submit" value="Войти"></td>
      </tr>
    </table>
  </div>
  <div>
    <table class="left_panel">
      <tr>
        <td style="vertical-align:top;"></td>
      </tr>
    </table>
  </div>
</div>
</form:form>
<jsp:include page="footer.jsp" />