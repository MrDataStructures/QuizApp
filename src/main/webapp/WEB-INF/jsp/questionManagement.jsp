<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="navbar.jsp" %>

<html>
<head>
    <title>Question Management</title>
</head>
<body>
<h1>Question Management Page</h1>

<table border="1">
    <thead>
    <tr>
        <th>Category</th>
        <th>Question Description</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="question" items="${questions}">
        <tr>
            <td>
                <c:forEach var="category" items="${categories}">
                    <c:if test="${category.category_id == question.category_id}">
                        ${category.name}
                    </c:if>
                </c:forEach>
            </td>
            <td>${question.description}</td>
            <td>${question.is_active ? "Active" : "Suspended"}</td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/question-management/toggleStatus" method="post" style="display:inline;">
                    <input type="hidden" name="questionId" value="${question.question_id}"/>
                    <button type="submit">${question.is_active ? "Suspend" : "Activate"}</button>
                </form>
                <form action="${pageContext.request.contextPath}/admin/question-management/edit" method="get" style="display:inline;">
                    <input type="hidden" name="questionId" value="${question.question_id}"/>
                    <button type="submit">Edit</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Button to add a new question -->
<form action="${pageContext.request.contextPath}/admin/question-management/add" method="get">
    <button type="submit">Add New Question</button>
</form>

<a href="${pageContext.request.contextPath}/admin/home">Back to Admin Home</a>
</body>
</html>
