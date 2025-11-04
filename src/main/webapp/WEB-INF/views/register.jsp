<%-- /src/main/webapp/WEB-INF/views/register.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>User Registration</title>
</head>
<body>
<h1>Register New User</h1>
<hr>
<%--
  表单的 action="register" 对应 Controller 里的 @PostMapping("/register")
  method="post" 对应 @PostMapping
--%>
<form action="register" method="post">
    <p>
        Username: <input type="text" name="username" required>
    </p>
    <p>
        Password: <input type="password" name="password" required>
    </p>
    <p>
        Email: <input type="email" name="email">
    </p>
    <p>
        <input type="submit" value="Register">
    </p>
</form>
</body>
</html>