<%@ include file="includes/header.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="includes/styles.jsp"%>
        <title><spring:message code="changePassword.title" htmlEscape="true"/></title>
    </head>
    <body>
    <%@include file="includes/navigation.jsp"%>
        <div id="content" class="container">
            <h1><spring:message code="changePassword.title" htmlEscape="true"/></h1>
            <forms:form method="POST" modelAttribute="userFormBean">
                <forms:errors cssClass="error" path="password" htmlEscape="true" element="div"/>
                <forms:errors cssClass="error" path="currentPassword" htmlEscape="true" element="div"/>
                <div id="login">
                    <p>
                        <input type="password" id="currentPassword" name="currentPassword" placeholder="<spring:message code="changePassword.currentPassword"/>" />
                    </p>
                    <p>
                        <input type="password" id="password" name="password" placeholder="<spring:message code="Password"/>"/>
                    </p>
                    <p>
                        <input type="password" id="passwordVerification" name="passwordVerification"
                                placeholder="<spring:message code="Password.Verification"/>"/>
                    </p>
                    <input type="submit" class="btn btn-primary" value="<spring:message code="changePassword.message" htmlEscape="true"/>" />
                </div>
            </forms:form>
        </div>
        <%@ include file="includes/footer.jsp" %>
    </body>
</html>
