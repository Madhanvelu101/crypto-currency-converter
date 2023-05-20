<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Top CryptoConverter</title>
            <style>
                .form-container {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 50vh;
                    width: 50%;
                    margin: auto;
                }

                .form-wrapper {
                    border: 2px solid black;
                    padding: 20px;
                }

                h1 {
                    text-align: center;
                }

                .form-row {
                    margin-bottom: 10px;
                }

                .form-row label {
                    display: inline-block;
                    width: 120px;
                }
            </style>
        </head>

        <body>
            <h1>Crypto Converter</h1>
            <br></br>

            <div class="form-container form-wrapper">

                <form action="/getCryptoValue" method="POST">
                    <div class="form-row">
                        <label for="cryptoSymbol">Crypto Symbol:</label>
                        <select name="cryptoSymbol" id="cryptoSymbol">
                            <c:forEach items="${cryptos}" var="crypto">
                                <option value="${crypto.id}">${crypto.name} (${crypto.id})</option>
                            </c:forEach>
                        </select>
                        <div>


                            <div class="form-row">
                                <label for="ipAddress">IP Address:</label>
                                <input type="text" name="ipAddress" id="ipAddress">
                            </div>

                            <div class="form-row">

                                <input type="submit" value="Submit">
                            </div>
                </form>

                <div class="form-row">

                    <h3>Crypto Value: ${currencySymbol} ${cryptoValue}</h3>
                </div>
            </div>
        </body>

        </html>