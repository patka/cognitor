<%@ include file="includes/header.jsp"%>

<html>
    <head>
        <title><spring:message code="registrationSuccess.title" htmlEscape="true"/></title>
        <%@include file="includes/styles.jsp"%>
    </head>
    <body>
        <h1><spring:message code="registrationSuccess.title" htmlEscape="true"/></h1>
        <p>
            <spring:message code="registrationSuccess.message" htmlEscape="true"/>
        </p>
        <p>
            <a href="${loginUrl}"><spring:message code="registrationSuccess.gotoLogin" htmlEscape="true" /></a>
        </p>
        <%@ include file="includes/footer.jsp" %>
    </body>
</html>