<%@ include file="header.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>"/>
        <title><spring:message code="changePassword.title" htmlEscape="true"/></title>
    </head>
    <body>
        <div id="content">
            <h1><spring:message code="changePassword.title" htmlEscape="true"/></h1>
            <forms:form method="POST" modelAttribute="userFormBean">
                <forms:errors cssClass="error" path="password" htmlEscape="true" element="div"/>
                <div id="login">
                    <p>
                        <input type="password" id="password" name="password" placeholder="<spring:message code="Password"/>"/>
                    </p>
                    <p>
                        <input type="password" id="passwordVerification" name="passwordVerification"
                                placeholder="<spring:message code="Password.Verification"/>"/>
                    </p>
                    <input type="submit" value="<spring:message code="changePassword.message" htmlEscape="true"/>" />
                </div>
            </forms:form>
        </div>
        <%@ include file="footer.jsp" %>
    </body>
</html>
