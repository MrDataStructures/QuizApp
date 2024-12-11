<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Add New Question</title>
</head>
<body>

<h1>Add New Question</h1>

<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/admin/question-management/save" method="post">
    <!-- Dropdown to select the category for the new question -->
    <label>Category:
        <select name="categoryId" required>
            <c:forEach var="category" items="${categories}">
                <option value="${category.category_id}">${category.name}</option>
            </c:forEach>
        </select>
    </label><br/><br/>

    <!-- Input for the question description -->
    <label>Description:
        <input type="text" name="description" required/>
    </label><br/><br/>

    <h3>Add Choices</h3>

    <!-- Input fields for choices and buttons to mark the correct choice -->
    <c:forEach var="i" begin="1" end="4">
        <div>
            <label>Choice ${i}:
                <input type="text" name="choiceDescriptions" required/>
            </label>
            <input type="radio" name="correctChoiceIndex" value="${i - 1}"
                   <c:if test="${i == 1}">checked</c:if> /> Mark as Correct
        </div><br/>
    </c:forEach>

    <!-- Submit button to save the question and choices -->
    <button type="submit">Add Question</button>
</form>

<a href="${pageContext.request.contextPath}/admin/question-management">Cancel</a>

</body>
</html>
