<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Top Cryptocurrencies</title>
</head>
<body>
    <h1>Top Cryptocurrencies</h1>
    <form method="GET">
         <label for="cryptoDropdown">Select a cryptocurrency:</label>
                <select id="cryptoDropdown">
                    <c:forEach items="${cryptos}" var="cryptocoin">
                        <option value="${cryptocoin.id}">${cryptocoin.name} (${cryptocoin.id})</option>
                    </c:forEach>
                </select>
        </select>
        <br></br>
        <input type="submit" value="Submit">
    </form>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>
