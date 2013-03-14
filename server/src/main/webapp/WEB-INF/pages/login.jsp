<%@ include file="header.jsp" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>" />
    <title><spring:message code="login.title" htmlEscape="true"/></title>
</head>
<body>
<h1 align="center"><spring:message code="login.title" htmlEscape="true"/></h1>

<form method="POST" action="${actionUrl}">
    <core:if test="${not empty error}">
        <div class="error">
            <spring:message code="login.badCredentials" htmlEscape="true" />
        </div>
    </core:if>
    <div class="login">
        <dl>
            <dt><label for="username"><spring:message code="Email" htmlEscape="true"/>:</label></dt>
            <dd><input type="text" id="username" name="username"/></dd>
            <dt><label for="password"><spring:message code="Password" htmlEscape="true"/>:</label></dt>
            <dd><input type="password" id="password" name="password"/></dd>
            <dd><input type="submit" value="Login"/></dd>
        </dl>
        <div align="center">
            <spring:message code="login.noAccount" htmlEscape="true"/>
            <a href="${registrationPageUrl}"><spring:message code="login.signUp" htmlEscape="true"/></a>
        </div>
    </div>
</form>
</body>
</html>
