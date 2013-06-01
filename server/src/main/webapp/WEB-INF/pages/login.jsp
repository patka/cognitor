<%@ include file="header.jsp" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>" />
        <title><spring:message code="login.title" htmlEscape="true"/></title>
    </head>
    <body>
        <h1><spring:message code="login.title" htmlEscape="true"/></h1>

        <div id="login">
            <form method="POST" action="${actionUrl}">
                <core:if test="${not empty error}">
                    <div class="error">
                        <spring:message code="login.badCredentials" htmlEscape="true" />
                    </div>
                </core:if>
                <p><input type="text" id="username" name="username" placeholder="<spring:message code="Email" htmlEscape="true"/>"/></p>
                <p><input type="password" id="password" name="password" placeholder="<spring:message code="Password" htmlEscape="true"/>"/></p>
                <p class="submit"><input type="submit" value="Login"/></p>
                <div id="login-help">
                    <p><spring:message code="login.noAccount" htmlEscape="true"/>
                    <a href="${registrationPageUrl}"><spring:message code="login.signUp" htmlEscape="true"/></a>
                    </p>
                </div>
            </form>
        </div>
        <%@ include file="footer.jsp" %>
    </body>
</html>
