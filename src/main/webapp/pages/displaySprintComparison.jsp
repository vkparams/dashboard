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


                hAxis: {
                    title: 'Date'
                },
                vAxis: {
                    title: 'Count',
                    format: '0'
                }


            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);

        }
    </script>

    <script type="text/javascript">


        google.load("visualization", "1", {
            packages: ["corechart"]
        });
        google.setOnLoadCallback(drawChart);
        function drawChart() {

            var data = google.visualization
                    .arrayToDataTable(${trendData});


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

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div_automation'));
            chart.draw(data, options);

        }
    </script>
</head>
<body>

<form:form method="post" action="save.html"
           modelAttribute="SprintTrendForm">

    <c:set var="bothCheck" value="both"/>

    <div class="page-header" align="center">
        <c:choose>
            <c:when test="${both eq bothCheck}">
                <h3 style="font-size: 30px">Multi Sprint Comparison |
                    <small style="font-size: 20px"><span class="label label-success">${fromDate} to ${toDate}</span>
                    </small>
                </h3>
            </c:when>
            <c:otherwise>
                <h3 style="font-size: 30px">Bug Trend Comparison |
                    <small style="font-size: 20px"><span class="label label-success">${fromDate} to ${toDate}</span>
                    </small>
                </h3>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="container ng-scope"
         style="width: 1200px; margin-top: 100px">

        <table>
            <tr>
                <c:choose>
                    <c:when test="${both eq bothCheck}">
                        <td><h4>
                           Bug Trend
                        </h4></td>
                        <td>
                            <h4>Automation Trend</h4>
                        </td>
                    </c:when>
                </c:choose>
            </tr>
            <tr>

                <c:choose>
                    <c:when test="${both eq bothCheck}">
                        <td>
                            <div id="chart_div" style="width: 600px; height: 300px;" align="center"></div>
                        </td>
                        <td>
                            <div id="chart_div_automation" style="width: 600px; height: 300px;" align="center"></div>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <div id="chart_div" style="width: 800px; height: 400px; margin-left: 250px;"
                                 align="center"></div>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </table>
        <table style="margin-left: 350px">
            <tr style="color: #4169e1">
                <td><strong>Total Opened: ${total_opened}</strong></td>
                <br></tr>
            <tr style="color: #d22133">
                <td><strong>Total Closed: ${total_closed}</strong></td>
            </tr>
        </table>
    </div>
    <br/> </form:form>
</body>
</html>