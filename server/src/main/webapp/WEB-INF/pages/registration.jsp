<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="forms" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<spring:url value="/static/css/styles.css" htmlEscape="true"/>"/>
    <title>Registration Page</title>
</head>
<body>
<h1 align="center">Registration Page</h1>

<forms:form method="POST" modelAttribute="registrationFormBean" action="${registrationPageUrl}">
    <table align="center">
        <tr>
            <td><spring:message code="Email"/>:</td>
            <td><forms:input path="email"/></td>
            <td><forms:errors path="email"/></td>
        </tr>
        <tr>
            <td><spring:message code="Password"/>:</td>
            <td><forms:password path="password"/></td>
            <td><forms:errors path="password"/></td>
        </tr>
        <tr>
            <td><spring:message code="Password.Verification" />:</td>
            <td><forms:password path="passwordVerification"/></td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <input type="submit" value="Register" />
            </td>
        </tr>
    </table>
</forms:form>
</body>
</html>
