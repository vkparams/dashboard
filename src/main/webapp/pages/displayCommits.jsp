<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextpath" value="${pageContext.request.contextPath}"/>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="fragment" content="!">
    <title>Containers Page</title>
    <link rel="stylesheet" type="text/css"
          href="${contextpath}/css/bootstrap.css">

    <style>
    </style>
    <!-- JS dependencies -->
    <script src="${contextpath}/js/jquery-1.8.3.min.js"></script>
    <script src="${contextpath}/js/bootstrap.min.js"></script>
    <script src="${contextpath}/js/bootstrap.js"></script>


    <!-- bootbox code -->
    <script src="${contextpath}/js/bootbox.min.js"></script>
    <script>
        $(function () {
            $("[rel='tooltip']").tooltip();
        });
    </script>

    <script type="text/javascript">
        $(document).on("click", ".deleteContainer", function (e) {
            bootbox.confirm("Would you like to Delete the Container !", function () {
                console.log("Alert Callback");
            });
        });

        $(document).on("click", "#createContainer", function (e) {

            bootbox.prompt("Enter the Container Name", function (result) {
                if (result === null) {
                    Example.show("Prompt dismissed");
                } else {
                    Example.show("Hi <b>" + result + "</b>");
                }
            });

        });

        $(document).on("click", "#uploadObject", function (e) {
            $('#myModal').modal('show');

        });
    </script>


</head>
<body>

<form:form method="post" action="save.html"
           modelAttribute="containerForm">

    <div class="container ng-scope">


        <table class="table table-striped">
            <thead>
            <tr>
                <th>Commid #</th>
                <th>Name</th>
                <th>Message</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${commits}" var="commitlist">
                <td>-----------------</td>

                <c:forEach items="${commitlist}" var="commit">

                    <tr>
                        <td>${commit.id}</td>
                        <td>${commit.name}</td>
                        <td>${commit.message}</td>
                        <td>${commit.date}</td>

                    </tr>
                </c:forEach>

            </c:forEach>
            </tbody>
        </table>
    </div>

    <br/>

</form:form>
</body>
</html>