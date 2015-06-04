<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="elf" uri="elfunctions" %>

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


    <script type="text/javascript" src="https://www.google.com/jsapi"></script>

    <script type="text/javascript">


        google.load("visualization", "1", {
            packages: ["corechart"]
        });
        google.setOnLoadCallback(drawChart);
        function drawChart() {

            var data = google.visualization
                    .arrayToDataTable(${graphData});


            var options = {
                seriesType: "bars",
                bar: {groupWidth: "35%"},


//        series: {
//
//          0: {color: '#FF0000'},
//          1: {color: '#126712'},
//          2: {color: '#088A4B'}
//
//        }


            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);

        }
    </script>
</head>
<body>

<form:form method="post" action="save.html"
           modelAttribute="AutomationDateForm">

        <div class="page-header" align="center">
            <h3 style="font-size: 30px">Automation Trend Comparison | <small style="font-size: 20px"><span class="label label-primary">${fromDate} to ${toDate}</span></small>
            </h3>
        </div>

    <div class="container ng-scope">

        <table>
            <tr>

                <td>
                    <div id="chart_div" style="width: 800px; height: 400px; margin-left: 100px; margin-top: 100px"></div>
                </td>
            </tr>
        </table>
    </div>
    <br/> </form:form>
</body>
</html>