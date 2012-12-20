<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title><spring:message code="registrationSuccess.title" htmlEscape="true"/></title>
    <link rel="stylesheet" type="text/css" href="/css/styles.css" />
</head>
<body>
<h1><spring:message code="registrationSuccess.title" htmlEscape="true"/></h1>
<p>
<spring:message code="registrationSuccess.message" htmlEscape="true"/>
</p>
<p>
<a href="${loginUrl}"><spring:message code="registrationSuccess.gotoLogin" htmlEscape="true" /></a>
</p>
</body>
</html>