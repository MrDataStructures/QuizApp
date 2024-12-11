<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<html>
<head>
    <title>Quiz</title>
</head>
<body>
<h1>Quiz for ${quiz.category.name}</h1>

<form action="${pageContext.request.contextPath}/quiz/submit" method="post">
    <input type="hidden" name="quizId" value="${quiz.quiz_id}" />
    <c:forEach var="question" items="${questions}">
        <div>
            <p><strong>${question.description}</strong></p>
            <c:forEach var="choice" items="${question.choices}">
                <label>
                    <input type="radio" name="choice_${question.question_id}" value="${choice.choice_id}" required>
                        ${choice.description}
                </label><br/>
            </c:forEach>
        </div>
    </c:forEach>
    <button type="submit">Submit Quiz</button>
</form>
</body>
</html>
