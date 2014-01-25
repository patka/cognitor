<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!Doctype HTML>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<spring:url value='/static/css/bootstrap.css' />"/>
        <link rel="stylesheet" type="text/css" href="<spring:url value='/static/css/styles.css' />" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>OpenID Answer</title>
    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <div class="nav">
                        <a class="brand" href="<spring:url value='/'/>">Cognitor Sample Consumer</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <h3>Received the following answer from the OpenID system:</h3>
            <p><span class="text-info">Identification: </span><c:out value="${identifier}" /></p>
            <p><span class="text-info">Unique Id to identify user in local system: </span>
                <c:out value="${uniqueId}"/></p>
            <p>
                The answer had the following parameters:
                <ul>
                    <c:forEach items="${param}" var="parameter">
                        <li><c:out value="${parameter.key}"/>: <c:out value="${parameter.value}"/></li>
                    </c:forEach>
                </ul>
            </p>
        </div>
    </body>
</html>