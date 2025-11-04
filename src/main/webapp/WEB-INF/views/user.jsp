<%-- /src/main/webapp/WEB-INF/views/user.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Details</title>
</head>
<body>

<%-- ↓↓↓↓ 添加欢迎信息和登出链接 ↓↓↓↓ --%>
<c:if test="${not empty sessionScope.loggedInUser}">
    <div style="text-align: right;">
        Welcome, <strong>${sessionScope.loggedInUser.username}</strong>!
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</c:if>
<c:if test="${empty sessionScope.loggedInUser}">
    <div style="text-align: right;">
        <a href="${pageContext.request.contextPath}/login">Login</a>
    </div>
</c:if>
<%-- ↑↑↑↑ 添加欢迎信息和登出链接 ↑↑↑↑ --%>

<h1>User Information</h1>
<hr>
<c:if test="${not empty user}">
    <%-- ... (你已有的显示用户信息的代码) ... --%>
    <p><strong>ID:</strong> ${user.id}</p>
    <p><strong>Username:</strong> ${user.username}</p>
    <p><strong>Email:</strong> ${user.email}</p>

    <%-- 只有当查看的用户是当前登录用户时，才显示管理链接 --%>
    <c:if test="${sessionScope.loggedInUser.id == user.id}">
        <p>
            <a href="${pageContext.request.contextPath}/user/edit/${user.id}">Edit this user</a>
        </p>
        <p>
            <a href="${pageContext.request.contextPath}/user/delete/${user.id}" onclick="return confirm('Are you sure you want to delete this user?');">Delete this user</a>
        </p>
    </c:if>

</c:if>
<%-- ... (你已有的 <c:if test="${empty user}">) ... --%>

<hr>
<p><a href="${pageContext.request.contextPath}/register">Go to Registration</a></p>
</body>
</html>