<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<html>
<head>
    <title>Admin Home</title>
</head>
<body>
<h1>Welcome to Admin Dashboard, ${username}!</h1>


<div>
    <form action="${pageContext.request.contextPath}/admin/user-management" method="get">
        <button type="submit">User Management Page</button>
    </form>
    <form action="${pageContext.request.contextPath}/admin/quiz-result-management" method="get">
        <button type="submit">Quiz Result Management Page</button>
    </form>
    <form action="${pageContext.request.contextPath}/admin/question-management" method="get">
        <button type="submit">Question Management Page</button>
    </form>
</div>



<a href="${pageContext.request.contextPath}/user/logout">Logout</a>
</body>
</html>
