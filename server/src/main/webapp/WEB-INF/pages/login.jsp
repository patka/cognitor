<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>" />
    <title>Login</title>
</head>
<body>
<h1 align="center">Login</h1>

<form method="POST" action="${actionUrl}">
    <div class="login">
        <dl>
            <dt><spring:message code="Username"/>:</dt>
            <dd><input type="text" name="username"/></dd>
            <dt><spring:message code="Password"/>:</dt>
            <dd><input type="password" name="password"/></dd>
            <dd><input type="submit" value="Login"/></dd>
        </dl>
        <div align="center">
            No account yet? Register <a href="${registrationPageUrl}">here</a>
        </div>
    </div>
</form>
</body>
</html>
