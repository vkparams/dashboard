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
    <script src="${contextpath}/js/jquery-2.1.3.min.js"></script>
    <script src="${contextpath}/js/bootstrap.min.js"></script>
    <script src="${contextpath}/js/bootstrap.js"></script>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {
            packages: ["corechart"]
        });
        google.setOnLoadCallback(drawChart);
        function drawChart() {

            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Bugs', ${SprintIssueForm.bug_count}],
                ['Tasks', ${SprintIssueForm.task_count}],
                ['Story', ${SprintIssueForm.story_count}]
            ]);

            var options = {
                title: 'Issue Summary',
                fontSize: 18,
                is3D: true,

                slices: {

                    0: {color: '#c23b22'},
                    1: {color: '#148dfa'},
                    2: {color: '#a8a8b0'},
                    3: {color: '#345063'},
                    4: {color: '#3479a5'}

                }
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));

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

            var data = google.visualization.arrayToDataTable(${issueGraph});

            var options = {
                title: 'Component Bug Summary',
                fontSize: 16,
                is3D: true,

                slices: {}
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart_3d_component'));

            chart.draw(data, options);
        }
    </script>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Open', ${SprintIssueForm.open_count}],
                ['In-Progress', ${SprintIssueForm.inProgress_count}],
                ['Resolved', ${SprintIssueForm.resolved_count}],
                ['Verified', ${SprintIssueForm.verified_count}],
                ['Fixed', ${SprintIssueForm.closed_count}]
            ]);

            var options = {
                title: 'Issue Status',
                fontSize: 18,
                is3D: true,

                slices: {

                    0: {color: '#990000'},
                    1: {color: '#FFA500'},
                    2: {color: '#2C9900'},
                    3: {color: '#000000'},
                    4: {color: '#2C9900'}

                }

            };


            var chart = new google.visualization.PieChart(document.getElementById('donutchart_3d'));
            chart.draw(data, options);

            var options_list = {
                valueNames: ['issue', 'status', 'assignee', 'issue_number', 'component', 'customer', 'days_open', 'severity']
            };

            var userList = new List('issues', options_list);
        }
    </script>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = google.visualization.arrayToDataTable(${componentGraph});


            var options = {
                isStacked: true,
                height: 400,
                legend: {position: 'top', maxLines: 3},
                vAxis: {
                    minValue: 0,
                }
            };

            var chart = new google.visualization.ColumnChart(document.getElementById('component_chart'));
            chart.draw(data, options);
        }

    </script>


    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable(${bugGraph});

            var options = {
                title: 'Bug Status: Total = ${SprintIssueForm.bug_count}',
                fontSize: 16,
                legend: {position: 'right', textStyle: {fontSize: 12}},
                is3D: true,

                slices: {

                    0: {color: '#990000'},
                    1: {color: '#2C9900'},
                    2: {color: '#FFA500'},
                    3: {color: '#FFA500'}

                }

            };


            var chart = new google.visualization.PieChart(document.getElementById('bug_graph_3d'));
            chart.draw(data, options);
        }
    </script>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable(${taskGraph});

            var options = {
                title: 'Task Status: Total = ${SprintIssueForm.task_count}',
                fontSize: 16,
                legend: {position: 'right', textStyle: {fontSize: 12}},
                is3D: true,

                slices: {

                    0: {color: '#990000'},
                    1: {color: '#2C9900'},
                    2: {color: '#FFA500'},
                    3: {color: '#FFA500'}

                }

            };


            var chart = new google.visualization.PieChart(document.getElementById('task_graph_3d'));
            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script src="http://listjs.com/no-cdn/list.js"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable(${storyGraph});

            var options = {
                title: 'Story Status: Total = ${SprintIssueForm.story_count}',
                is3D: true,
                fontSize: 16,
                legend: {position: 'right', textStyle: {fontSize: 12}},

                slices: {

                    0: {color: '#990000'},
                    1: {color: '#2C9900'},
                    2: {color: '#FFA500'},
                    3: {color: '#FFA500'}

                }

            };


            var chart = new google.visualization.PieChart(document.getElementById('story_graph_3d'));
            chart.draw(data, options);


        }
    </script>

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

    <script type="text/javascript">


        google.load("visualization", "1", {
            packages: ["corechart"]
        });
        google.setOnLoadCallback(drawChart);
        function drawChart() {

            var data = google.visualization
                    .arrayToDataTable([${automationForm.trendData}]);

            var options = {
                vAxis: {title: "Percentage Pass"},
                seriesType: "bars",
                series: {
                    0: {color: '#FF0000'},
                    1: {color: '#FE9A2E'},
                    2: {color: '#088A4B'}

                }


            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div_automation'));
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
                    .arrayToDataTable(${graphData});


            var options = {
                seriesType: "bars",
                bar: {groupWidth: "55%"},
                isStacked: true,
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

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div_bugTrend'));
            chart.draw(data, options);


        }
    </script>

</head>
<body>

<form method="post" action="save.html"
      modelAttribute="SprintIssueForm">
    <div class="page-header " align="center" style="margin-top: -10px">
        <c:set var="customerCheck" value="customer"/>
        <c:set var="componentCheck" value="component"/>
        <c:set var="sprintAllCheck" value="sprint_all"/>
        <c:choose>
            <c:when test="${customer eq customerCheck}">
                <h3 style="font-size: 30px">Customer Issue Summary: ${customerName}
                    <small style="font-size: 20px"> | <span class="label label-success">${fromDate} - ${toDate}</span>
                    </small>
                </h3>
            </c:when>
            <c:when test="${component eq componentCheck}">
                <h3 style="font-size: 30px">Component Issue Summary: ${componentName}
                    <small style="font-size: 20px">| <span class="label label-success">${fromDate} - ${toDate}</span>
                    </small>
                </h3>
            </c:when>
            <c:when test="${sprint_all eq sprintAllCheck}">
                <h3 style="font-size: 30px">Sprint Summary: ${projectName}
                    <small style="font-size: 20px">| <span class="label label-success">${fromDate} - ${toDate}</span>
                    </small>
                </h3>
            </c:when>
            <c:otherwise>
                <h3 style="font-size: 30px">Sprint Issue Summary: ${projectName} |
                    <small style="font-size: 20px; color: #000000;"><span
                            class="label label-primary">${fromDate} - ${toDate}</span></small>
                </h3>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="container ng-scope"
         style="width: 1300px">

        <c:set var="componentValue" value="component"/>

        <table width="100%">
            <c:choose>
                <c:when test="${sprint_all eq sprintAllCheck}">
                    <tr>
                        <td style="align-self: flex-end; float: left; padding-left: 150px">
                            <h4>Automation Trend</h4>
                        </td>
                        <td style="align-self: flex-end; float: right; padding-right: 280px">
                            <h4>Sprint Trend</h4>
                        </td>
                    </tr>
                    <tr style="align-self: flex-end; float: left; ; margin-left: -40px">
                        <c:choose>
                            <c:when test="${automationForm.trendData eq null}">
                                                            <td style="align-self: flex-end; float: left">
                                    <div id="chart_div_automation"
                                         style="width: 600px; height: 400px;"></div>
                                </td>
<!--                                 <td><p><strong><font size="3">No Data to Display!</font></strong></p></td>
 -->                            </c:when>
                            <c:otherwise>
                                <td style="align-self: flex-end; float: left">
                                    <div id="chart_div_automation"
                                         style="width: 600px; height: 400px;"></div>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td style="align-self: flex-end; float: right">
                            <div id="chart_div_bugTrend"
                                 style="width: 600px; height: 400px;"></div>
                        </td>
                    </tr>
                </c:when>
            </c:choose>
            <tr>

                <td>

                    <c:choose>
                        <c:when test="${component eq componentValue}">
                            <div id="piechart_3d_component"
                                 style="width: 600px; height: 400px; align-self: flex-end; float: left;"></div>
                        </c:when>
                        <c:otherwise>
                            <div id="piechart_3d"
                                 style="width: 600px; height: 400px; align-self: flex-end; float: left;"></div>
                        </c:otherwise>
                    </c:choose>

                    <div id="donutchart_3d" style="width: 600px; float: right; height: 400px;"></div>
                </td>
            </tr>
            <tr>
                <c:choose>
                    <c:when test="${component eq componentValue}">
                        <td>
                            <div id="component_chart"
                                 style="width: 600px; height: 400px; margin-left: 200px" align="center"></div>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <div id="bug_graph_3d" style="width: 400px; height: 400px; float: left; margin-left: 60px"
                                 align="center"></div>
                            <div id="task_graph_3d" style="width: 400px; float: left; height: 400px"
                                 align="center"></div>
                            <div id="story_graph_3d" style="width: 400px; float: left; height: 400px"
                                 align="center"></div>
                        </td>
                    </c:otherwise>
                </c:choose>

            </tr>
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
                                    style="background-color: #505050; color: #FFFFFF; text-align: center" width="100px">
                                    Status
                                </th>
                                <th class="sort" data-sort="issue"
                                    style="background-color: #505050; color: #FFFFFF; text-align: center"
                                    align="center">Issue
                                    Type
                                </th>
                                <c:choose>
                                    <c:when test="${component eq componentValue}">
                                        <th class="sort" data-sort="component"
                                            style="background-color: #505050; color: #FFFFFF; text-align: center"
                                            align="center">Component
                                        </th>
                                    </c:when>
                                </c:choose>
                                <%--<c:choose>--%>
                                <%--<c:when test="${customer eq customerCheck}">--%>
                                <th class="sort" data-sort="customer"
                                    style="background-color: #505050; color: #FFFFFF; text-align: center"
                                    align="center">Customer
                                </th>
                                <%--</c:when>--%>
                                <%--</c:choose>--%>
                                <th class="sort" data-sort="days_open"
                                    style="background-color: #505050; color: #FFFFFF; text-align: center"
                                    align="center">Days Open
                                </th>
                                <th class="sort" data-sort="severity"
                                    style="background-color: #505050; color: #FFFFFF; text-align: center"
                                    align="center">Severity
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
                                            <c:choose>
                                                <c:when test="${component eq componentCheck}">
                                                    <td class="component">${SprintIssueForm.components[i]}</td>
                                                </c:when>
                                            </c:choose>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${customer eq customerCheck}">--%>
                                            <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                <%--</c:when>--%>
                                                <%--</c:choose>--%>
                                            <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                            <td class="severity">${SprintIssueForm.severityList[i]}</td>
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
                                            <c:choose>
                                                <c:when test="${component eq componentCheck}">
                                                    <td class="component">${SprintIssueForm.components[i]}</td>
                                                </c:when>
                                            </c:choose>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${customer eq customerCheck}">--%>
                                            <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                <%--</c:when>--%>
                                                <%--</c:choose>--%>
                                            <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                            <td class="severity">${SprintIssueForm.severityList[i]}</td>
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
                                            <c:choose>
                                                <c:when test="${component eq componentCheck}">
                                                    <td class="component">${SprintIssueForm.components[i]}</td>
                                                </c:when>
                                            </c:choose>
                                                <%--<c:choose>--%>
                                                <%--<c:when test="${customer eq customerCheck}">--%>
                                            <td class="customer">${SprintIssueForm.customerName[i]}</td>
                                                <%--</c:when>--%>
                                                <%--</c:choose>--%>
                                            <td class="days_open">${SprintIssueForm.daysOpen[i]}</td>
                                            <td class="severity">${SprintIssueForm.severityList[i]}</td>
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
    </div>
    <br/></form>
</body>
</html>
