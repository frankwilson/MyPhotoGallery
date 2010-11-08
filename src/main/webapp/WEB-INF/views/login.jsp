<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp" /> 
<form method="post" action="${pageContext.request.contextPath}/loginCheck.html">
<div class="top_level">
  <div class="content">
    <div class="page_header"><spring:message code="page.login.title"/></div><c:if test="${activationResult != null}">
<c:if test="${activationResult == true}"><spring:message code="page.login.activationSuccess"/>.</c:if>
<c:if test="${activationResult == false}"><spring:message code="page.login.activationError"/>.</c:if>
    <br />
    <br /></c:if>
    <spring:message code="page.login.formTitle"/>:
    <br><br>
    <table class="main">
      <colgroup>
        <col style="width:90px;">
      </colgroup>
      <tr>
        <td><spring:message code="page.login.loginTitle"/>:&#160;</td>
        <td><input type="text" name="j_username" id="username"></td>
      </tr>
      <tr>
        <td><spring:message code="page.login.passwordTitle"/>:&#160;</td>
        <td><input type="password" name="j_password" id="password"></td>
      </tr>
      <tr>
        <td></td>
        <td style="padding-top:10px;"><input style="width: 80px;" type="submit" value="<spring:message code="page.login.enterButton"/>"></td>
      </tr>
    </table>
  </div>
</div>
</form>
<jsp:include page="footer.jsp" />