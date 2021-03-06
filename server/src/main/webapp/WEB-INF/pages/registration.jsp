<%@ include file="includes/header.jsp"%>
<html>
    <head>
        <%@include file="includes/head.jsp"%>
        <title><spring:message code="registration.title" htmlEscape="true"/></title>
    </head>
    <body>
        <h1><spring:message code="registration.title" htmlEscape="true"/></h1>

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
                        <input type="submit" class="btn btn-primary" value="<spring:message code="registration.signup" htmlEscape="true"/>" />
                    </td>
                </tr>
            </table>
        </forms:form>
        <%@ include file="includes/footer.jsp" %>
    </body>
</html>
