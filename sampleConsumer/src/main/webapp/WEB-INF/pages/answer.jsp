<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>OpenID Answer</title>
</head>
<body>
<p>
Received the following answer from the OpenID system: <br />
Identification: <c:out value="${identifier}" />
</p>
<p>
The answer had the following parameters:
<ul>
    <c:forEach items="${param}" var="parameter">
        <li><c:out value="${parameter.key}"/>: <c:out value="${parameter.value}"/>
    </c:forEach>
</ul>
</p>
</body>
</html>