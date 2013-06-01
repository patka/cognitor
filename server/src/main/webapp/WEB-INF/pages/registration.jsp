<%@ include file="header.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>"/>
        <title><spring:message code="registration.title" htmlEscape="true"/></title>
    </head>
    <body>
        <h1 align="center"><spring:message code="registration.title" htmlEscape="true"/></h1>

        <forms:form method="POST" modelAttribute="userFormBean" action="${registrationPageUrl}">
            <forms:errors cssClass="error" path="password" htmlEscape="true" element="div"/>
            <forms:errors cssClass="error" path="email" htmlEscape="true" element="div"/>
            <table align="center">
                <tr>
                    <td><spring:message code="Email"/>:</td>
                    <td><forms:input path="email"/></td>
                </tr>
                <tr>
                    <td><spring:message code="Password"/>:</td>
                    <td><forms:password path="password"/></td>
                </tr>
                <tr>
                    <td><spring:message code="Password.Verification" />:</td>
                    <td><forms:password path="passwordVerification"/></td>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <input type="submit" value="<spring:message code="registration.signup" htmlEscape="true"/>" />
                    </td>
                </tr>
            </table>
        </forms:form>
        <%@ include file="footer.jsp" %>
    </body>
</html>
