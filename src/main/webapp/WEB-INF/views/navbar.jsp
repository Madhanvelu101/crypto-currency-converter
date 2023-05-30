<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <style>
        .navbar {
            margin-bottom: 0;
            border-radius: 0;
        }
        .navbar-brand {
            padding: 15px;
        }
        .navbar-right {
            margin-right: 0;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/converter">Crypto Converter</a>
            </div>
            <ul class="nav navbar-nav navbar-right">
             <li><a class="navbar-brand" href="/history">History</a></li>
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <li>
                        <form id="logoutForm" method="POST" action="${contextPath}/logout">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <h5>Welcome ${pageContext.request.userPrincipal.name}</h5>
                            <button  type="submit" class="btn btn-link">Logout</button>
                        </form>
                    </li>
                </c:if>

            </ul>
        </div>
    </nav>

    <div class="container">
        <!-- Rest of your content here -->
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</body>
</html>
