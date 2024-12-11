<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>

<html>
<head>
    <title>Contact Us</title>
</head>
<body>
<h2>Contact Us</h2>
<p>If you have any concerns or questions, please reach out to us at Chris@beaconfiresupport.com.</p>

<!-- Form for submitting a message -->
<form action="${pageContext.request.contextPath}/contact" method="post">
    <label for="message">Your Message:</label><br>
    <textarea id="message" name="message" rows="5" cols="50" required></textarea><br><br>
    <button type="submit">Send</button>
</form>

<!-- Display the confirmation message if "messageSent" attribute is present -->
<c:if test="${not empty messageSent}">
    <p style="color: green;">Message sent. We will reach out to you shortly.</p>
</c:if>

</body>
</html>
