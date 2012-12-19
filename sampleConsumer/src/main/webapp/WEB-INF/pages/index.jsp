<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Consumer Start</title>
</head>
<body>
<div align="center">
    <form action="/" method="POST">
        <p>
            SSO Handle: <input type="text" name="handle" size="30" value="http://localhost:8080/sso"/>
        </p>
        <p>
            Immediate Request? <input type="checkbox" name="immediate" />
        </p>
        <input type="submit" value="Go To Login" />
    </form>
</div>
</body>
</html>
