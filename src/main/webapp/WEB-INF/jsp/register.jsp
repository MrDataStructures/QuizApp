<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Register</title>
</head>
<body>
<h2>Register</h2>

<!-- Display error message if registration fails -->
<c:if test="${not empty errorMessage}">
    <p style="color:red">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/user/register" method="post">
    <label>Username:</label>
    <input type="text" name="username" required>
    <br />

    <label>Email:</label>
    <input type="email" name="email" required>
    <br />

    <label>Password:</label>
    <input type="password" name="password" required>
    <br />

    <label>First Name:</label>
    <input type="text" name="firstname" required>
    <br />

    <label>Last Name:</label>
    <input type="text" name="lastname" required>
    <br />

    <button type="submit">Register</button>
</form>

<a href="${pageContext.request.contextPath}/user/logout">Cancel</a>
</body>
</html>
