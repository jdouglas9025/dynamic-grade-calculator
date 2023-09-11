<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dynamic Grade Calculator</title>
    <link rel="stylesheet" href="styles/dynamic-grade-calculator-styles.css">
</head>
<body>
<section>
    <h1 class="section2-heading">Dynamic Grade Calculator - Result</h1>
</section>

<section>
    <div class="output">
        <div class="output-items">
            Current Score: <c:out value="${requestScope.results.get(\"currentScore\")}"/> <br>
            Current Letter Grade: <c:out value="${requestScope.results.get(\"currentGrade\")}"/>
        </div>

        <div class="output-items">
            <c:out value="${requestScope.results.get(\"resultMessage\")}"/>
            <c:if test="${requestScope.results.get(\"result\") != null}">
                You will need an average of <c:out value="${requestScope.results.get(\"result\")}"/> on all remaining
                items in order to achieve your target grade.
            </c:if>
        </div>
    </div>
</section>

</body>
</html>