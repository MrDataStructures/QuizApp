<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="navbar.jsp" %>

<html>
<head>
    <title>User Management</title>
</head>
<body>
<h1>User Management Page</h1>

<table border="1">
    <thead>
    <tr>
        <th>Full Name</th>
        <th>Email</th>
        <th>Username</th>
        <th>Password</th>
        <th>User ID</th>
        <th>Active Status</th>
        <th>Admin Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.firstname} ${user.lastname}</td>
            <td>${user.email}</td>
            <td>${user.username}</td>
            <td>${user.password}</td>
            <td>${user.user_id}</td>
            <td>${user.is_active ? 'Active' : 'Suspended'}</td>
            <td>${user.is_admin ? 'Admin' : 'User'}</td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/user-management/toggle-status" method="post">
                    <input type="hidden" name="userId" value="${user.user_id}"/>
                    <input type="hidden" name="isActive" value="${user.is_active}"/>
                    <button type="submit">
                            ${user.is_active ? 'Suspend' : 'Activate'}
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination Controls -->
<div>
    <c:if test="${currentPage > 0}">
        <a href="${pageContext.request.contextPath}/admin/user-management?page=${currentPage - 1}">Previous</a>
    </c:if>
    <c:if test="${users.size() == 5}">
        <a href="${pageContext.request.contextPath}/admin/user-management?page=${currentPage + 1}">Next</a>
    </c:if>
</div>

<a href="${pageContext.request.contextPath}/admin/home">Back to Admin Home</a>
</body>
</html>
