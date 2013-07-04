<%@include file="header.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/styles.css" htmlEscape="true"/>" />
        <title><spring:message code="manageAccount.title" htmlEscape="true"/></title>
    </head>
    <body>
        <div id="content">
            <h1><spring:message code="manageAccount.title" htmlEscape="true"/></h1>
            <div id="options">
                <ul>
                    <li><a href="/account/changePassword.html">
                            <spring:message code="changePassword.title" htmlEscape="true"/>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <%@include file="footer.jsp"%>
    </body>
</html>