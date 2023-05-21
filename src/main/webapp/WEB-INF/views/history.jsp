<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crypto Conversion History</title>

    <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }

            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            tr:nth-child(even) {
                background-color: #f2f2f2;
            }

            th {
                background-color: #4CAF50;
                color: white;
            }
        </style>
</head>
<body>
    <h2>Crypto Conversion History</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Crypto Coin</th>
            <th>IP Address</th>
            <th>Crypto Price</th>
            <th>Country</th>
            <th>Timestamp</th>
        </tr>
        <c:forEach items="${historyList}" var="history">
            <tr>
                <td>${history.id}</td>
                <td>${history.userId}</td>
                <td>${history.cryptoCoin}</td>
                <td>${history.ipAddress}</td>
                <td>${history.cryptoPrice}</td>
                <td>${history.country}</td>
                <td>${history.timeStamp}</td>
            </tr>
        </c:forEach>
    </table>
     <!-- Hyperlink to topCryptoList.jsp -->
        <p><a href="/topCryptoList">Back to Crypto Conversions</a></p>
</body>
</html>
