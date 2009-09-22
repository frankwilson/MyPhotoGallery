<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <title>Time Expression - Sign In</title>
  <link href="includes/timex.css" rel="stylesheet" type="text/css">
</head>
<body>
<form method="post">
<table width="85%" border="1" align="center" cellpadding="0"
    cellspacing="0">
    <tr>
        <td>
        <table width="100%" border="0" align="center" cellpadding="4"
            cellspacing="0" bordercolor="#CCCCCC">
            <tr bgcolor="#C2DCEB" valign="middle">
                <td width="90%" valign="middle">
                <h1>Sign In</h1>
                </td>
            </tr>
            <tr>
                <td colspan="2"><!-- errors/messages --> 
<center>
<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
        <c:forEach var="error" items="${status.errorMessages}">
            <font color="red"><c:out value="${error}" escapeXml="false" />
            </font>
            <br />
        </c:forEach>
    </c:if>
</spring:bind>

<!-- status messages -->
 <c:if
    test="${not empty message}">
    <font color="green"><c:out value="${message}" /></font>
    <c:set var="message" value="" scope="session" />
</c:if>
</center>
                <div align="center">
                <p>Please provide your authentication information below.</p>
                <p>Логин:&#160;
                      <spring:bind path="user.login">
                        <input style="height:20px; width:80px;" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" id="login">
                      </spring:bind>
                      Пароль:&#160;
                      <spring:bind path="user.password">
                        <input style="height:20px; width:80px;" type="password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" id="pass">
                      </spring:bind>
                      <input style="width:80px;" type="submit" name="Submit" id="enter" value="enter"></p>
                </div>

                </td>
            </tr>
            <tr>
                <td colspan="2" bgcolor="#C2DCEB">&nbsp;</td>
            </tr>
        </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>
