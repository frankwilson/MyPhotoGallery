<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="header.jsp" /> 
<form:form commandName="user">
<table>
  <tr>
    <td>
      <table width="100%" border="0" align="center" cellpadding="4" cellspacing="0" bordercolor="#CCCCCC">
        <tr>
          <td class="page_header">Вход</td>
        </tr>
        <tr>
          <td colspan="2">
            <p>Пожалуйста, введите аутентификационную информацию в следующие поля:</p>
            <table style="width:400px;">
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
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form:form>
<!--</form>-->
<jsp:include page="footer.jsp" />