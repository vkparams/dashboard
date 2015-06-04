<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="elf" uri="elfunctions" %>

<c:set var="contextpath" value="${pageContext.request.contextPath}"/>
<c:set var="graphData" value=""/>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="fragment" content="!">
    <title>Containers Page</title>
    <link rel="stylesheet" type="text/css"
          href="${contextpath}/css/bootstrap.css">

    <style>
        .tableData {
            border-spacing: 1px;
            background-color: white;
            border-width: 1;
            border-color: black;
        }

        #circle {
            background: #f00;
            width: 10px;
            height: 10px;
            border-radius: 75%;
            text-align: center;
        }
    </style>
    <script>
        var value = "";

    </script>
    <!-- JS dependencies -->
    <script src="${contextpath}/js/jquery-1.8.3.min.js"></script>
    <script src="${contextpath}/js/bootstrap.min.js"></script>
    <script src="${contextpath}/js/bootstrap.js"></script>


    <!-- bootbox code -->
    <script src="${contextpath}/js/bootbox.min.js"></script>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>

    <c:forEach items="${commitDetailsObj.graphData}" var="commit">
        <tr>
            <c:set var="graphData" value="${graphData}${commit}"/>

        </tr>

    </c:forEach>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>


    <script type="text/javascript">


        google.load("visualization", "1", {
            packages: ["corechart"]
        });
        google.setOnLoadCallback(drawChart);
        function drawChart() {

            var data = google.visualization
                    .arrayToDataTable([${graphData}]);

            var options = {
                title: 'Commit Trend',
                vAxis: {title: "Number of commits"},
                hAxis: {title: "Date"},
                seriesType: "bars",
//				series: {
//					0: {color: '#FF0000'},
//					1: {color: '#FE9A2E'},
//					2: {color: '#088A4B'}
//
//				}


            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);

        }
    </script>


</head>
<body>

<form>
        <div class="page-header" align="center">
            <h3 style="font-size: 30px">Sprint Commit Trend
                <small style="font-size: 20px">| <span class="label label-primary">${fromDate} - ${toDate}</span></small>
            </h3>
        </div>

    <div class="container ng-scope"
         style="width: 100%; margin-left: 300px; margin-top: 100px" >


        <table border="1" style="border-collapse: separate;">
            <c:forEach items="${commitDetailsObj.tableData}" var="tableData"
                       varStatus="outer">
                <tr>
                    <td width="200px" style="color: #FFFFFF; background-color: #${elf:getColorCode()}">${tableData.key}
                    </td>
                    <c:forEach items="${tableData.value}" var="commitlist">
                        <c:forEach items="${commitlist}" var="commit">
                            <c:choose>
                                <c:when test="${outer.index >0}">
                                    <c:choose>
                                        <c:when test="${commit >0}">
                                            <c:set var="fontColor" value="green"/>

                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="fontColor" value="red"/>

                                        </c:otherwise>
                                    </c:choose>

                                    <td
                                            style="text-align: center; width: 35px; overflow: hidden; display: inline-block; white-space: nowrap;">
                                        <font
                                                color=" ${fontColor}">${commit}</font></span></td>

                                </c:when>
                                <c:otherwise>
                                    <td
                                            style="color: #FFFFFF; background-color: #525555; text-align: center; width: 35px; overflow: hidden; display: inline-block; white-space: nowrap;">
                                        <span>${commit}</span></td>

                                </c:otherwise>
                            </c:choose>


                        </c:forEach>
                    </c:forEach>
                </tr>

            </c:forEach>
        </table>

    </div>


    <div class="container ng-scope"
         style="width: 1200px; margin-left: 200px;">

        <c:if test="${elf:isGraphDisabled() eq 'true'}">
            <div id="chart_div"
                 style="width: 1000px; height: 600px; margin-top: 50px;"></div>

        </c:if>
        <table class="table table-striped"
               style="color: #0099CC; margin-bottom: 10px">
            <thead>
            <tr>
                <th style="width: 70px">Commit #</th>
                <th style="width: 120px">Name</th>
                <th style="width: 400px">Message</th>
                <th style="width: 100px">Date</th>
            </tr>
            </thead>
        </table>
        <c:forEach items="${commitDetailsObj.commitMap}" var="commitMap">
            <table class="table" style="margin-bottom: 0px">
                <tr>
                    <td style="background-color: #505050; color: #FFFFFF">${commitMap.key}
                    </td>
                </tr>
                <c:forEach items="${commitMap.value}" var="commitlist">
                    <table class="table table-striped table-bordered">
                        <c:forEach items="${commitlist}" var="commit">
                            <tr>

                                <td style="width: 50px"><a href=${commit.changetListPath}>${commit.displayId}</a></td>
                                <td style="width: 140px">${commit.name}</td>
                                <td style="width: 500px" title="${commit.message}">${elf:trimName(commit.message)}</td>
                                <td style="width: 110px">${commit.date}</td>
                            </tr>

                        </c:forEach>
                    </table>
                </c:forEach>
            </table>
        </c:forEach>
    </div>


    <br/>


</form>
</body>
</html>