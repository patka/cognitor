<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="<spring:url value='/static/css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<spring:url value='/static/css/styles.css'/>"/>
    <title>Consumer Start</title>
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
        <h1>Consumer Start Page</h1>
        <form action="" method="POST">
            <p>
                <label for="handle">SSO Handle:</label>
                <input type="text" name="handle" id="handle" size="30" value="http://localhost:8080/sso"/>
            </p>
            <p>
                <label class="checkbox">
                    <input type="checkbox" class="chekbox" name="immediate"/>Immediate Request?
                </label>
            </p>
            <input type="submit" value="Go To Login" class="btn" />
        </form>
    </div>
</body>
</html>
