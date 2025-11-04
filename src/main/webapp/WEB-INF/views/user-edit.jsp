<%-- /src/main/webapp/WEB-INF/views/user-edit.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Edit User</title>
</head>
<body>
<h1>Edit User Information</h1>
<hr>
<%--
  表单提交到 "update" (相对路径), 对应 Controller 里的 @PostMapping("/user/update")
--%>
<form action="../update" method="post">

    <%-- 关键：必须包含 ID，这样后台才知道要更新哪条记录 --%>
    <input type="hidden" name="id" value="${user.id}">

    <p>
        Username: <input type="text" name="username" value="${user.username}" required>
    </p>
    <p>
        Password: <input type="password" name="password" value="" placeholder="Enter new password if changing">
    </p>
    <p>
        Email: <input type="email" name="email" value="${user.email}">
    </p>
    <p>
        <input type="submit" value="Update User">
    </p>
</form>
</body>
</html>