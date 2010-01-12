<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp" />
<form action="" method="post" enctype="application/x-www-form-urlencoded">
<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%;">
  <tr>
    <td>Регистрация:</td>
  </tr>
  <tr>
    <td>
      <table>
        <tr>
          <td>
            <!-- errors/messages -->
            <center>
            <spring:bind path="user.*">
              <c:if test="${not empty status.errorMessages}">
                <c:forEach var="error" items="${status.errorMessages}">
                  <font color="red"><c:out value="${error}" escapeXml="false" /></font><br />
                </c:forEach>
              </c:if>
            </spring:bind> <!-- status messages -->
            <c:if test="${not empty message}">
              <font color="green"><c:out value="${message}" /></font>
              <c:set var="message" value="" scope="session" />
            </c:if>
            </center>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Логин:</td>
          <td style="width:350px;">
            <spring:bind path="user.login">
              <input style="height:20px; width:250px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Псевдоним:</td>
          <td style="width:350px;">
            <spring:bind path="user.nickName">
              <input style="height:20px; width:250px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Имя:</td>
          <td style="width:350px;">
            <spring:bind path="user.firstName">
              <input style="height:20px; width:250px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">Фамилия:</td>
          <td style="width:350px;">
            <spring:bind path="user.lastName">
              <input style="height:20px; width:250px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;">e-mail:</td>
          <td style="width:350px;">
            <spring:bind path="user.email">
              <input style="height:20px; width:250px;" type="text" name="${status.expression}" value="${status.value}" id="login">
            </spring:bind>
          </td>
        </tr>
        <tr>
          <td style="width:150px;"></td>
          <td style="width:350px; text-align:center;">
            <input style="height:25px; width:180px;" type="submit" value="Зарегистрироваться" id="login">
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