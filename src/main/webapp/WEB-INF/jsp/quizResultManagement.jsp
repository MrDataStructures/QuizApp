<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<html>
<head>
    <title>Quiz Result Management</title>
</head>
<body>
<h1>Quiz Result Management Page</h1>

<!-- Filter Form -->
<form action="${pageContext.request.contextPath}/admin/quiz-result-management" method="get">
    <label for="categoryId">Filter by Category:</label>
    <select name="categoryId" id="categoryId">
        <option value="">All</option>
        <c:forEach var="category" items="${categories}">
            <option value="${category.category_id}" ${category.category_id == selectedCategoryId ? 'selected' : ''}>${category.name}</option>
        </c:forEach>
    </select>

    <label for="userId">Filter by User:</label>
    <select name="userId" id="userId">
        <option value="">All</option>
        <c:forEach var="user" items="${users}">
            <option value="${user.user_id}" ${user.user_id == selectedUserId ? 'selected' : ''}>${user.firstname} ${user.lastname}</option>
        </c:forEach>
    </select>

    <button type="submit">Apply Filters</button>
</form>

<!-- Quiz Results Table -->
<table border="1">
    <thead>
    <tr>
        <th>Taken Time</th>
        <th>Category</th>
        <th>User Full Name</th>
        <th>Number of Questions</th>
        <th>Score</th>
        <th>Details</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="quizResult" items="${quizResults}">
        <tr>
            <td>${quizResult.taken_time}</td>
            <td>${quizResult.category}</td>
            <!--
            <td>${quizResult.user_fullname}</td>
            -->

            <td>${quizResult.user_firstname} ${quizResult.user_lastname}</td>

            <td>${quizResult.num_questions}</td>
            <td>${quizResult.score}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/quiz-result-detail?quizId=${quizResult.quiz_id}">
                    View Details
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination Controls -->
<div>
    <c:if test="${currentPage > 0}">
        <a href="${pageContext.request.contextPath}/admin/quiz-result-management?page=${currentPage - 1}&categoryId=${selectedCategoryId}&userId=${selectedUserId}">Previous</a>
    </c:if>
    <c:if test="${quizResults.size() == 5}">
        <a href="${pageContext.request.contextPath}/admin/quiz-result-management?page=${currentPage + 1}&categoryId=${selectedCategoryId}&userId=${selectedUserId}">Next</a>
    </c:if>
</div>

<a href="${pageContext.request.contextPath}/admin/home">Back to Admin Home</a>
</body>
</html>
