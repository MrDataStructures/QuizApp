<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Quiz Results</title>
</head>
<body>
<h1>Quiz Results</h1>

<p>Category: ${quiz.category.name}</p>
<p>Username: ${username}</p>
<p>Start Time: ${quiz.start_time}</p>
<p>End Time: ${quiz.end_time}</p>
<p>Score: ${score}</p>
<p>Result: ${result}</p>

<!-- Displaying the Breakdown -->
<h2>Question Breakdown:</h2>

<c:forEach var="questionDetails" items="${quizBreakdown}">

    <p><strong>Question:</strong> ${questionDetails.questionDescription}</p>
    <p><strong>Options:</strong></p>
    <ul>
        <c:forEach var="choiceDescription" items="${questionDetails.choiceDescriptions}">
            <li>${choiceDescription != null ? choiceDescription : 'Option not available'}</li>
        </c:forEach>
    </ul>

    <p><strong>Your Answer:</strong> ${questionDetails.userChoiceDescription != null ? questionDetails.userChoiceDescription : 'No answer selected'}</p>
    <p><strong>Correct Answer:</strong> ${questionDetails.correctChoiceDescription != null ? questionDetails.correctChoiceDescription : 'Correct answer not available'}</p>
    <hr/>
</c:forEach>

<!-- Button to go back to home page -->
<div style="margin-top: 20px;">
    <a href="${pageContext.request.contextPath}/home" class="btn">Take another quiz</a>
</div>

</body>
</html>
