<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>

         <%@ include file="navbar.jsp" %>
            <meta charset="UTF-8">
            <title>Top CryptoConverter</title>
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css">




        </head>

        <body>
            <h1>Crypto Converter</h1>
            <br></br>

            <div class="form-container form-wrapper">

                <form action="/converter" method="POST">
                    <div class="form-row">
                        <label for="cryptoSymbol">Crypto Currency:</label>
                        <select name="cryptoSymbol" id="cryptoSymbol" class = "form-control">
                            <c:forEach items="${cryptos}" var="crypto">
                                <option value="${crypto.id}">${crypto.name} (${crypto.id})</option>
                            </c:forEach>
                        </select>
                        <div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                            <div class="form-row">
                                <label for="ipAddress">IP Address:</label>
                                <input type="text" name="ipAddress" id="ipAddress" class = "form-control">
                            </div>

                            <div class="form-row">

                                <input type="submit" class="btn btn-lg btn-primary btn-block" value="Submit">
                            </div>
                </form>

                <div class="form-row">

                    <h4>Crypto Value:  ${cryptoValue}</h4>
                      <h4 class="form-error"> ${error}</h4>
                </div>
            </div>
        </body>

        </html>