<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>

<!-- Display error message if login fails -->
<c:if test="${not empty errorMessage}">
    <p style="color:red">${errorMessage}</p>
</c:if>

<!-- Login Form -->
<form method="post" action="${pageContext.request.contextPath}/user/login">
    <div>
        <label>Username:</label>
        <input type="text" name="username" required>
    </div>
    <br/>
    <div>
        <label>Password:</label>
        <input type="password" name="password" required>
    </div>
    <br/>
    <button type="submit">Login</button>
</form>
<p>Don't have an account? <a href="${pageContext.request.contextPath}/user/register">Register here</a></p>
</body>
</html>
