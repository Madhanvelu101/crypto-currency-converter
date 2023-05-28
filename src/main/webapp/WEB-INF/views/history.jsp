<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Crypto Conversion History</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css">
    <%@ include file="navbar.jsp" %>
</head>
<body>
<div class="container">
    <h2 class="text-center">Crypto Conversion History</h2>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Crypto Coin</th>
            <th>IP Address</th>
            <th>Country</th>
            <th>Crypto Price</th>
            <th>Timestamp</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${historyList}" var="history">
            <tr>
                <td>${history.id}</td>
                <td>${history.cryptoCoin}</td>
                <td>${history.ipAddress}</td>
                <td>${history.country}</td>
                <td>${history.cryptoPrice}</td>
                <td>${history.timeStamp}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</body>
</html>
