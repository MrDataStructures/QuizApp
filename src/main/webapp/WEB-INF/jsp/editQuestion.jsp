<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Edit Question</title>
</head>
<body>

<h1>Edit Question</h1>

<!-- Display error message if question is not found -->
<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<!-- Form for editing the question -->
<form action="${pageContext.request.contextPath}/admin/question-management/update" method="post">

    <!-- Hidden field for the question ID -->
    <input type="hidden" name="questionId" value="${question.question_id}" />

    <!-- Input field for editing the question description -->
    <label for="description">Question Description:</label><br>
    <input type="text" id="description" name="description" value="${question.description}" required /><br><br>

    <h3>Edit Choices</h3>

    <!-- Loop through each choice associated with the question -->
    <c:forEach var="choice" items="${choices}">
        <div style="margin-bottom: 15px;">

            <!-- Hidden field for each choice ID -->
            <input type="hidden" name="choiceIds" value="${choice.choice_id}" />

            <!-- Input field for editing the choice description -->
            <label>Choice Description:</label><br>
            <input type="text" name="choiceDescriptions" value="${choice.description}" required /><br>

            <!-- Radio button to select the correct choice -->
            <label>Correct Choice:</label>
            <input type="radio" name="correctChoiceId" value="${choice.choice_id}"
                   <c:if test="${choice.is_correct}">checked</c:if> /><br>
        </div>
    </c:forEach>

    <!-- Submit button to save changes -->
    <button type="submit">Save Changes</button>

</form>

<!-- Link to go back to the Question Management Page -->
<br>
<a href="${pageContext.request.contextPath}/admin/question-management">Back to Question Management Page</a>

</body>
</html>
