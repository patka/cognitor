<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>OpenID Answer</title>
</head>
<body>
<p>
Received the following answer from the OpenID system:
Identification: ${identifier}
</p>
<p>
The answer had the following parameters:
<ul>
    <% for (String key : request.getParameterMap().keySet())  {%>
    <li><%= key %>: <%= request.getParameter(key) %></li>
    <%} %>
</ul>
</p>
</body>
</html>