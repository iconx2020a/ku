

<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     
<sql:query var="listUsers" dataSource="jdbc/testdb">
    select ID, firstName, lastName from Person;
</sql:query>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users List</title>
</head>
<body>
    <div align="center">
        <table border="1" cellpadding="5">
            <caption><h2>Users</h2></caption>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                 <th>Last Name</th>
            </tr>
            <c:forEach var="user" items="${listUsers.rows}">
                <tr>
                     <td><c:out value="${user.ID}" /></td>
                    <td><c:out value="${user.lastName}" /></td>
                    <td><c:out value="${user.firstName}" /></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>