<%@include file="includes/header.jsp"%>
<html>
    <head>
        <%@include file="includes/styles.jsp"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><spring:message code="manageAccount.title" htmlEscape="true"/></title>
    </head>
    <body>
    <%@include file="includes/navigation.jsp"%>
        <div id="content">
            <h1><spring:message code="manageAccount.title" htmlEscape="true"/></h1>
            <div id="options">
                <ul>
                    <li><a href="/account/changePassword.html">
                            <spring:message code="changePassword.linkTitle" htmlEscape="true"/>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <%@include file="includes/footer.jsp"%>
    </body>
</html>