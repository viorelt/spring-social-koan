<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<form id="signin" action="${pageContext.servletContext.contextPath}/signin/authenticate" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}"></input>
    <div class="formInfo">

        <c:if test="${param.error eq 'bad_credentials'}">
            <div class="error">
                Your sign in information was incorrect.
                Please try again or <a th:href="${pageContext.servletContext.contextPath}/signup">sign up</a>.
            </div>
        </c:if>
        <c:if test="${param.error eq 'multiple_users'}">
            <div class="error" >
                Multiple local accounts are connected to the provider account.
                Try again with a different provider or with your username and password.
            </div>
        </c:if>

    </div>
    <fieldset>
        <label for="login">Username</label>
        <input id="login" name="username" type="text" size="25"></input>
        <label for="password">Password</label>
        <input id="password" name="password" type="password" size="25"></input>
    </fieldset>
    <button type="submit">Sign In</button>

    <p>Some test user/password pairs you may use are:</p>
    <ul>
        <li>hero/1234%asd</li>
        <li>habuma/freebirds</li>
        <li>kdonald/melbourne</li>
        <li>rclarkson/atlanta</li>
    </ul>

    <p>Or you can <a th:href="${pageContext.servletContext.contextPath}/signup">signup</a> with a new account.</p>
</form>
</body>
</html>