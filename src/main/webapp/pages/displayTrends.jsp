<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <script src="http://listjs.com/no-cdn/list.js"></script>
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
                bar: {groupWidth: "55%"},

                hAxis: {
                    title: 'Date'
                },
                vAxis: {
                    title: 'Count',
                    maxValue: 10,
                    format: '0'
                },
                series: {
                    0: {color: '#DF0101'},
                    1: {color: '#FE9A2E'},
                    2: {color: '#088A08'},
                    3: {color: '#00FF40'},
                    4: {color: '#0000FF'},
                    5: {color: '#58D3F7'},
                } 

            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);


        }
    </script>
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

                hAxis: {
                    title: 'Date'
                },
                vAxis: {
                    title: 'Count',
                    maxValue: 10,
                    format: '0'
                },
                series: {
                    2: {type: 'line'}
                }
            };


            var chart = new google.visualization.ComboChart(document.getElementById('line_chart'));
            chart.draw(data, options);

            var options_list = {
                valueNames: ['issue', 'status', 'assignee', 'issue_number', 'component', 'customer', 'days_open']
            };

            var userList = new List('issues', options_list);

        }
    </script>

</head>
<body>

<form method="post" action="save.html"
      modelAttribute="SprintTrendForm">

    <c:set var="customer_value" value="customer"/>
    <c:set var="component_value" value="Component"/>

    <div class="page-header" align="center">
        <c:choose>
        <c:when test="${customer eq customer_value}">
        <h3 style="font-size: 30px">Customer Bug Trend: ${customerName}
            </c:when>
            <c:when test="${customer eq component_value}">
            <h3 style="font-size: 30px">Component Bug Trend: ${componentName}
                </c:when>
                <c:otherwise>
                <h3 style="font-size: 30px">Sprint Bug Trend
                    </c:otherwise>
                    </c:choose>
                    <small style="font-size: 20px"> | <span class="label label-primary"> ${fromDate} to ${toDate}</span>
                    </small>
                </h3>
    </div>

    <div class="container ng-scope"
         style="width: 1200px; margin-left: 200px;">

        <table>
            <tr>

                <td>
                    <c:choose>
                        <c:when test="${customer eq customer_value}">
                            <div id="line_chart" style="width: 1000px; height: 600px;margin-left: 50px"></div>
                        </c:when>
                        <c:when test="${customer eq component_value}">
                            <div id="line_chart" style="width: 1000px; height: 600px;"></div>
                        </c:when>
                        <c:otherwise>
                            <div id="chart_div" style="width: 1000px; height: 600px;" align="right"></div>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
    <br/>

    <div class="container ng-scope"
         style="width: 1200px">

        <c:set var="componentValue" value="component"/>
        <c:set var="customerCheck" value="customer"/>

        <c:choose>
            <c:when test="${customer eq customerCheck}">
                <table width="100%">
                    <tr>
                        <td>
                            <div id="issues">
                                <table class="table table-striped table-bordered" align="center">
                                    <tbody class="list">

                                    <tr>
                                        <th class="sort" data-sort="issue_number"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center">Issue
                                            #
                                        </th>
                                        <th class="sort" data-sort="assignee"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center">Assignee
                                            Name
                                        </th>
                                        <th style="background-color: #505050; color: #FFFFFF; text-align: center">Summary</th>
                                        <th class="sort" data-sort="status"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center">Status
                                        </th>
                                        <th class="sort" data-sort="issue"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center"
                                            align="center">Issue
                                            Type
                                        </th>
                                        <th class="sort" data-sort="customer"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center"
                                            align="center">Customer
                                        </th>
                                        <th class="sort" data-sort="days_open"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center"
                                            align="center">Days Open
                                        </th>

                                    </tr>
                                    <c:set var="total_completed"
                                           value="${fn:length(SprintIssueForm.keys_completedIssues)}"/>
                                    <c:set var="total_punted"
                                           value="${fn:length(SprintIssueForm.keys_puntedIssues)}"/>
                                    <c:set var="total_incomplete"
                                           value="${fn:length(SprintIssueForm.keys_incompleteIssues)}"/>

                                    <c:set var="open" value="Open"/>
                                    <c:set var="resolved" value="Resolved"/>
                                    <c:set var="closed" value="Fixed"/>
                                    <c:set var="inprogress" value="In Progress"/>
                                    <c:set var="reopened" value="Reopened"/>
                                    <c:set var="bug" value="Bug"/>
                                    <c:set var="task" value="Task"/>
                                    <c:set var="story" value="Story"/>
                                    <c:set var="other" value="Other"/>
                                    <c:set var="zero" value="0"/>
                                    <c:choose>
                                        <c:when test="${total_punted gt zero}">
                                            <c:forEach var="i" begin="0" end="${total_punted-1}">

                                                <tr>
                                                    <td class="issue_number" style="width: 100px" align="center"><a
                                                            href="http://jira.fb.lab/browse/${SprintIssueForm.keys_puntedIssues[i]}"
                                                            target="_blank">${SprintIssueForm.keys_puntedIssues[i]}</a></td>
                                                    <td class="assignee"
                                                        style="width: 150px">${elf:trimNameJiraAssignee(SprintIssueForm.assigneeNames_puntedIssues[i])}</td>
                                                    <td style="text-align: left"
                                                        title="${SprintIssueForm.summaries_puntedIssues[i]}">${elf:trimNameJira(SprintIssueForm.summaries_puntedIssues[i])}</td>
                                                    <c:choose>
                                                        <c:when test="${SprintIssueForm.statusNames_puntedIssues[i] eq open or SprintIssueForm.statusNames_puntedIssues[i] eq reopened}">
                                                            <td class="status"
                                                                style="background-color: #990000; color: #FFFFFF">${SprintIssueForm.statusNames_puntedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when test="${SprintIssueForm.statusNames_puntedIssues[i] eq inprogress}">
                                                            <td class="status"
                                                                style="background-color: #FFA500; color: #FFFFFF">${SprintIssueForm.statusNames_puntedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when test="${SprintIssueForm.statusNames_puntedIssues[i] eq closed}">
                                                            <td class="status"
                                                                style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_puntedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when test="${SprintIssueForm.statusNames_puntedIssues[i] eq resolved}">
                                                            <td class="status"
                                                                style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_puntedIssues[i]}</td>
                                                        </c:when>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_puntedIssues[i] eq bug}">
                                                            <td class="issue"
                                                                style="background-color: #4A6084; color: #FFFFFF">${SprintIssueForm.issueType_puntedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_puntedIssues[i] eq task}">
                                                            <td class="issue"
                                                                style="background-color: #8EBBEB; color: #FFFFFF">${SprintIssueForm.issueType_puntedIssues[i]}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="issue"
                                                                style="background-color: #798795; color: #FFFFFF">${SprintIssueForm.issueType_puntedIssues[i]}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                        <%--<c:choose>--%>
                                                        <%--<c:when test="${customer eq customerCheck}">--%>
                                                    <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                        <%--</c:when>--%>
                                                        <%--</c:choose>--%>
                                                    <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                                </tr>

                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${total_completed gt zero}">
                                            <c:forEach var="i" begin="0" end="${total_completed-1}">

                                                <tr>
                                                    <td class="issue_number" style="width: 100px" align="center"><a
                                                            href="http://jira.fb.lab/browse/${SprintIssueForm.keys_completedIssues[i]}"
                                                            target="_blank">${SprintIssueForm.keys_completedIssues[i]}</a></td>
                                                    <td class="assignee"
                                                        style="width: 150px">${elf:trimNameJiraAssignee(SprintIssueForm.assigneeNames_completedIssues[i])}</td>
                                                    <td style="text-align: left"
                                                        title="${SprintIssueForm.summaries_completedIssues[i]}">${elf:trimNameJira(SprintIssueForm.summaries_completedIssues[i])}</td>
                                                    <c:choose>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_completedIssues[i] eq closed}">
                                                            <td class="status"
                                                                style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_completedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_completedIssues[i] eq resolved}">
                                                            <td class="status"
                                                                style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_completedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_completedIssues[i] eq open or SprintIssueForm.statusNames_completedIssues[i] eq reopened}">
                                                            <td class="status"
                                                                style="background-color: #990000; color: #FFFFFF">${SprintIssueForm.statusNames_completedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_completedIssues[i] eq inprogress}">
                                                            <td class="status"
                                                                style="background-color: #FFA500; color: #FFFFFF">${SprintIssueForm.statusNames_completedIssues[i]}</td>
                                                        </c:when>

                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_completedIssues[i] eq bug}">
                                                            <td class="issue"
                                                                style="background-color: #4A6084; color: #FFFFFF">${SprintIssueForm.issueType_completedIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_completedIssues[i] eq task}">
                                                            <td class="issue"
                                                                style="background-color: #8EBBEB; color: #FFFFFF">${SprintIssueForm.issueType_completedIssues[i]}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="issue"
                                                                style="background-color: #798795; color: #FFFFFF">${SprintIssueForm.issueType_completedIssues[i]}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                        <%--<c:choose>--%>
                                                        <%--<c:when test="${customer eq customerCheck}">--%>
                                                    <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                        <%--</c:when>--%>
                                                        <%--</c:choose>--%>
                                                    <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                                </tr>

                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${total_incomplete gt zero}">
                                            <c:forEach var="i" begin="0" end="${total_incomplete-1}">
                                                <tr>
                                                    <td class="issue_number" style="width: 100px" align="center"><a
                                                            href="http://jira.fb.lab/browse/${SprintIssueForm.keys_incompleteIssues[i]}"
                                                            target="_blank">${SprintIssueForm.keys_incompleteIssues[i]}</a></td>
                                                    <td class="assignee"
                                                        style="width: 150px">${elf:trimNameJiraAssignee(SprintIssueForm.assigneeNames_incompleteIssues[i])}</td>
                                                    <td style="text-align: left"
                                                        title="${SprintIssueForm.summaries_incompleteIssues[i]}">${elf:trimNameJira(SprintIssueForm.summaries_incompleteIssues[i])}</td>
                                                    <c:choose>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_incompleteIssues[i] eq closed}">
                                                            <td class="status"
                                                                style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_incompleteIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_incompleteIssues[i] eq resolved}">
                                                            <td style="background-color: #2C9900; color: #FFFFFF">${SprintIssueForm.statusNames_incompleteIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_incompleteIssues[i] eq open or SprintIssueForm.statusNames_incompleteIssues[i] eq reopened}">
                                                            <td class="status"
                                                                style="background-color: #990000; color: #FFFFFF">${SprintIssueForm.statusNames_incompleteIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.statusNames_incompleteIssues[i] eq inprogress}">
                                                            <td class="status"
                                                                style="background-color: #FFA500; color: #FFFFFF">${SprintIssueForm.statusNames_incompleteIssues[i]}</td>
                                                        </c:when>

                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_incompleteIssues[i] eq bug}">
                                                            <td class="issue"
                                                                style="background-color: #4A6084; color: #FFFFFF">${SprintIssueForm.issueType_incompleteIssues[i]}</td>
                                                        </c:when>
                                                        <c:when
                                                                test="${SprintIssueForm.issueType_incompleteIssues[i] eq task}">
                                                            <td class="issue"
                                                                style="background-color: #8EBBEB; color: #FFFFFF">${SprintIssueForm.issueType_incompleteIssues[i]}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="issue"
                                                                style="background-color: #798795; color: #FFFFFF">${SprintIssueForm.issueType_incompleteIssues[i]}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                        <%--<c:choose>--%>
                                                        <%--<c:when test="${customer eq customerCheck}">--%>
                                                    <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                        <%--</c:when>--%>
                                                        <%--</c:choose>--%>
                                                    <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                                </tr>

                                            </c:forEach>
                                        </c:when>
                                    </c:choose>

                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </c:when>
        </c:choose>
    </div>
</form>
</body>
</html>