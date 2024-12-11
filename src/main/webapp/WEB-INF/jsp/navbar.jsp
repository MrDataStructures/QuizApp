<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>

<%--<div style="background-color: #333; padding: 10px;">--%>


<%--    <c:if test="${not empty sessionScope.user}">--%>
<%--        <!-- Links that only show when a user is logged in -->--%>
<%--        <a href="${pageContext.request.contextPath}/home" style="color: white; margin-right: 20px;">Home</a>--%>
<%--        <a href="${pageContext.request.contextPath}/user/logout" style="color: white; margin-right: 20px;">Logout</a>--%>
<%--    </c:if>--%>

<%--    <!-- Contact Us link available to everyone -->--%>
<%--    <a href="${pageContext.request.contextPath}/contact" style="color: white;">Contact Us</a>--%>
<%--</div>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div style="background-color: #333; padding: 10px;">
    <c:if test="${not empty sessionScope.user}">
        <!-- Check if the user is an admin -->
        <c:choose>
            <c:when test="${sessionScope.user.is_admin}">
                <!-- Admin Home link -->
                <a href="${pageContext.request.contextPath}/admin/home" style="color: white; margin-right: 20px;">Home</a>
            </c:when>
            <c:otherwise>
                <!-- User Home link -->
                <a href="${pageContext.request.contextPath}/home" style="color: white; margin-right: 20px;">Home</a>
            </c:otherwise>
        </c:choose>

        <!-- Logout link, common for both admins and regular users -->
        <a href="${pageContext.request.contextPath}/user/logout" style="color: white; margin-right: 20px;">Logout</a>
    </c:if>

    <!-- Contact Us link available to everyone -->
    <a href="${pageContext.request.contextPath}/contact" style="color: white;">Contact Us</a>
</div>


