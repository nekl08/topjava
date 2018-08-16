<%--
  Created by IntelliJ IDEA.
  User: Zver
  Date: 16.08.2018
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<hr/>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>

    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
            <td><%= meal.getDateTime().toString().replace("T", " ") %></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>

    </c:forEach>
</table>
</body>
</html>
