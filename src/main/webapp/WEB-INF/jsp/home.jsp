<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Welcome to the QuizApp Home Page</h1>

<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<p>You have successfully logged in, <c:out value="${sessionScope.user.username}" />!</p>

<h2>Choose a category to take a quiz from</h2>
<ul>
    <c:forEach var="category" items="${categories}">
        <li>
                ${category.name}
            <form action="${pageContext.request.contextPath}/quiz/start" method="get" style="display:inline;">
                <input type="hidden" name="categoryId" value="${category.category_id}"/>
                <button type="submit">Start</button>
            </form>
        </li>
    </c:forEach>
</ul>

<h2>Recent Quiz Results</h2>
<table border="1">
    <thead>
    <tr>
        <th>Quiz Name</th>
        <th>Date Taken</th>
        <th>Result</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="quiz" items="${recentQuizzes}">
        <tr>
            <td>${quiz.category != null ? quiz.category.name : 'No category'}</td>
            <td>${quiz.end_time != null ? quiz.end_time : 'Not completed'}</td>
            <td>
                <c:if test="${quiz.end_time != null}">
                    <a href="${pageContext.request.contextPath}/quiz/result?quizId=${quiz.quiz_id}">View Result</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<a href="${pageContext.request.contextPath}/user/logout">Logout</a>
</body>
</html>
