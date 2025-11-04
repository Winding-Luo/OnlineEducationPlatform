<%-- /src/main/webapp/WEB-INF/views/login.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>User Login</h1>
<hr>

<%-- 如果登录失败，显示错误信息 --%>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<%-- 登录表单 --%>
<form action="login" method="post">
    <p>
        Username: <input type="text" name="username" required>
    </p>
    <p>
        Password: <input type="password" name="password" required>
    </p>
    <p>
        <input type="submit" value="Login">
    </p>
</form>

<hr>
<p><a href="register">Don't have an account? Register</a></p>
</body>
</html>