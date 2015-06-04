<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextpath" value="${pageContext.request.contextPath}"/>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="fragment" content="!">
    <title>Containers Page</title>
    <link rel="stylesheet" type="text/css"
          href="${contextpath}/css/bootstrap.css">
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <%--<link rel="stylesheet" type="text/css"--%>
    <%--href="${contextpath}/css/bootstrap.min.css">--%>
    <%--<link rel="stylesheet" type="text/css"--%>
    <%--href="${contextpath}/css/bootstrap-theme.css">--%>
    <%--<link rel="stylesheet" type="text/css"--%>
    <%--href="${contextpath}/css/bootstrap-theme.min.css">--%>
    <style>
        td {
            padding-left: 20px;
        }
    </style>
    <!-- JS dependencies -->
    <script src="${contextpath}/js/jquery-1.8.3.min.js"></script>
    <script src="${contextpath}/js/bootstrap.min.js"></script>
    <script src="${contextpath}/js/bootstrap.js"></script>
    <script src="${contextpath}/js/jquery-ui.js"></script>

    <!-- bootbox code -->
    <script src="${contextpath}/js/bootbox.min.js"></script>
    <script>
        var checkbox = null;
        $(window).load(function () {

            $('#sprint').get(0).selectedIndex = 1;
            $('#customer').get(0).selectedIndex = 0;
            $('#component').get(0).selectedIndex = 0;
            $('#projects').get(0).selectedIndex = 4;
            $("#fromDate").val($.datepicker.formatDate('mm/dd/yy', new Date()));
            $("#toDate").val($.datepicker.formatDate('mm/dd/yy', new Date()));
            $("#from_Date").val($.datepicker.formatDate('dd-M-y', new Date()));
            $("#to_Date").val($.datepicker.formatDate('dd-M-y', new Date()));
            $("#from").val($.datepicker.formatDate('dd-M-y', new Date()));
            $("#to").val($.datepicker.formatDate('dd-M-y', new Date()));

            $('#automation').removeClass("active");
            $('#home').addClass("active");
            $('#stash').removeClass("active");
            $('#unitTest').removeClass("active");
            $('#manualTest').removeClass("active");
            $('#Alert').hide();
            $('#alert_project').hide();
            $('#alert_sprint').hide();
            $('#alert_date_1').hide();
            $('#alert_date_2').hide();
            $('#alert_date_3').hide();
            $('#alert_date_4').hide();
            $('#alert_date_comp_1').hide();
            $('#alert_date_comp_2').hide();

            checkbox = "sprint";
            $('#sprint_checkbox').prop('checked', true);

        });

        $(function () {
            $("#Submit").click(function () {

                $('#sprint_all_selected').val("null");
                var all_value = $('#sprint_all_selected').val();

                if ($("#project").val() == 'none') {
                    validate();
                    return false;
                }
                else if ($("#sprint").val() == 'none') {
                    validate_sprint();
                    return false;
                }
                var project = $('#project').val();
                var sprint = $('#sprint').val();
                var projectName = $("#project option:selected").text();

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);


                location.href = '${contextpath}/issueReport/' + project + '/' + sprint + '/' + projectName + '/' + all_value;


            });

        });

        $(function () {

            $('#project').on(
                    "change",
                    function () {
                        if ($(this).val() != 'none')
                            location.href = '${contextpath}/jiraDashboard/project/'
                            + $(this).val();
                    });
        });

        $(function () {

            $('#sprint').on("change", function () {
                if ($(this).val() != 'none')
                    enableSubmit();
            });
        });


        $(function () {
            $("#fromDate").datepicker();
        });

        $(function () {
            $("#toDate").datepicker();
        });

        $(function () {
            $("#from_Date").datepicker({dateFormat: 'dd-M-y'});
        });

        $(function () {
            $("#to_Date").datepicker({dateFormat: 'dd-M-y'});
        });

        $(function () {
            $("#from").datepicker({dateFormat: 'dd-M-y'});
        });

        $(function () {
            $("#to").datepicker({dateFormat: 'dd-M-y'});
        });

        $(function () {
            $("#Reset").click(function () {
                location.href = '${contextpath}/jiraDashboard';

            });
        });

        $(function () {
            $("#sprint_checkbox").click(function () {
                var $this = $(this);

                if ($this.is(':checked')) {
                    // the checkbox was checked
                    $('#from_Date').prop("disabled", true);
                    $('#to_Date').prop("disabled", true);
                    checkbox = "sprint";
                    $this.val('true');
                    checkbox = null;
                } else {
                    // the checkbox was unchecked
                    $('#from_Date').removeAttr("disabled");
                    $('#to_Date').removeAttr("disabled");
                    $this.val('false');
                    checkbox = null;
                }

            });
        });

        $(function () {
            $("#Trends").click(function () {

                $('#sprint_all_selected').val("null");
                var all_value = $('#sprint_all_selected').val();

                var project = $("#project").val();
                var sprint = $("#sprint").val();
                var projectName = $("#project option:selected").text();

                if ($('#project').val() == 'none') {
                    validate();
                    return false;
                }
                if ($('#sprint').val() == 'none') {
                    if(checkbox != null) {
                        validate_sprint();
                    }
                    return false;
                }

                if(checkbox != null){
                    location.href = '${contextpath}/jiraTrends/' + project + '/' + sprint + '/' + projectName + '/' + null + '/' + null + '/' + null;
                }   else    {
                    var from_date = $('#from_Date').val();
                    var to_date = $('#to_Date').val();
                    var today = new Date();

                    if(fn_DateCompare(to_date, today) > 0){
                        $('#alert_date_2').show();
                        $('#alert_date_1').hide();
                        return;
                    }
                    else if(fn_DateCompare(from_date, to_date) > 0){
                        $('#alert_date_1').show();
                        $('#alert_date_2').hide();
                        return;
                    }

                    location.href = '${contextpath}/jiraTrends/' + project + '/' + sprint + '/' + projectName + '/' + from_date + '/' + to_date + '/' + all_value;
                }


                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));

                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);


            });
        });

        $(function () {
            $("#commits").click(function () {
                var project = $("#project").val();
                var sprint = $("#sprint").val();
                var projectName = $("#project option:selected").text();


                if ($('#project').val() == 'none') {
                    validate();
                    return false;
                }
                if ($('#sprint').val() == 'none') {
                    if(checkbox != null) {
                        validate_sprint();
                    }
                    return false;
                }

                var img = new Image();
                var src = '${contextpath}/img/loader.gif';
                img.src = src;

                if(checkbox != null){
                    location.href = '${contextpath}/commits/' + project + '/' + sprint + "/" + projectName + "/" + null + '/' + null;
                }   else    {
                    var from_date = $('#from_Date').val();
                    var to_date = $('#to_Date').val();
                    var today = new Date();

                    if(fn_DateCompare(to_date, today) > 0){
                        $('#alert_date_2').show();
                        $('#alert_date_1').hide();
                        return;
                    }
                    else if(fn_DateCompare(from_date, to_date) > 0){
                        $('#alert_date_1').show();
                        $('#alert_date_2').hide();
                        return;
                    }
                    location.href = '${contextpath}/commits/' + project + '/' + sprint + "/" + projectName + '/' + from_date + '/' + to_date;
                }


                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);





            });
        });

        $(function () {
            $("#SprintComparison").click(function () {
                var multiple_sprints = $("#multi_sprint").val();
                var project = $("#project").val();
                var err1 = {};
                var projectName = $("#project option:selected").text();
                $('#both_multi').val("null");
                var both_value = $('#both_multi').val();

                if ($('#project').val() == 'none') {
                    validate();
                    return false;
                }

                else if ($('#multi_sprint').val() == null) {
                    //alert("Please select Sprints to compare!");
                    //$("#multi_sprint").focus();
                    err1.message = "Select Sprints to compare!";
                    err1.field = $("#multi-sprint");
                }

                if (err1.message) {
                    $('#Alert').show();
                    return false;
                } else {
                    $('#Alert').hide();
                }

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

                location.href = '${contextpath}/sprintTrends/' + project + '/' + multiple_sprints + '/' + projectName + '/' + both_value;
            });
        });

        $(function () {
            $("#CompareAutomationTrend").click(function () {
                var multiple_sprints = $("#multi_sprint").val();
                var project = $("#project").val();
                var err1 = {};
                $('#both_multi').val("null");
                var both_value = $('#both_multi').val();

                if ($('#project').val() == 'none') {
                    validate();
                    return false;
                }

                else if ($('#multi_sprint').val() == null) {
                    err1.message = "Select Sprints to compare!";
                    err1.field = $("#multi_sprint");
                }

                if (err1.message) {
                    $('#Alert').show();
                    return false;
                }

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

                location.href = '${contextpath}/automationTrendComparison/' + project + '/' + multiple_sprints + '/' + both_value;
            });
        });

        $(function () {
            $("#AutomationTrend").click(function () {

                $('#sprint_all_selected').val("null");
                var all_value = $('#sprint_all_selected').val();

                var project = $("#project").val();
                var sprint = $("#sprint").val();
                var projectName = $("#project option:selected").text();

                if (project == 'none') {
                    validate();
                    return true;
                }
                else if (sprint == 'none') {
                    if(checkbox != null) {
                        validate_sprint();
                    }
                    return true;
                }

                if(checkbox != null){
                    location.href = '${contextpath}/automationTrend/' + project + '/' + sprint + '/' + projectName + '/' + null + '/' + null + '/' + null;
                }   else    {
                    var from_date = $('#from_Date').val();
                    var to_date = $('#to_Date').val();
                    var today = new Date();

                    if(fn_DateCompare(to_date, today) > 0){
                        $('#alert_date_2').show();
                        $('#alert_date_1').hide();
                        return;
                    }
                    else if(fn_DateCompare(from_date, to_date) > 0){
                        $('#alert_date_1').show();
                        $('#alert_date_2').hide();
                        return;
                    }

                    location.href = '${contextpath}/automationTrend/' + project + '/' + sprint + '/' + projectName + '/' + from_date + '/' + to_date + '/' + all_value;
                }

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);


            });
        });

        $(function () {
            $("#customer_bugTrend").click(function () {

                var date1 = $("#fromDate").val();
                var date2 = $("#toDate").val();
                var today = new Date();
                var err = {};

                $('#summary').val('false');
                var projectName = $("#projects option:selected").val();
                $('#projectName').val(projectName);

                if (fn_DateCompare(date1, date2) > 0) {
                    err.message("From date should be less than To date !");
                    //$("#fromDate").focus();
                    alert("ERROR 1");
                    err.field('#fromDate');
                    return false;
                }
                else if (fn_DateCompare(date2, today) > 0) {
                    err.message("To date should not be more than Today's date !");
                    alert("ERROR 2");
                    err.field("#toDate");
                    return false;
                }

                if (err.message) {
                    document.getElementById('error_date').style.color = "#ffffff";
                    document.getElementById('error_date').style.width = "1000px";
                    document.getElementById('error_date').innerHTML = err.message;
                    return false;
                }

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);
            });
        });

        $(function () {
            $("#customer_status").click(function () {

                var date1 = $("#fromDate").val();
                var date2 = $("#toDate").val();
                var today = new Date();

                var projectName = $("#projects option:selected").val();
                $('#projectName').val(projectName);

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

                $("#summary").val('true');


            });
        });

        $(function () {
            $("#component_bugTrend").click(function () {

                var date1 = $("#from").val();
                var date2 = $("#to").val();
                var today = new Date();
                var componentName = $('#component').val();
                var err = {};

                $('#compo_summary').val('false');
                var summary = $('#compo_summary').val();

                var projectName = $("#projects option:selected").val();
                $('#projectName').val(projectName);

                if (fn_DateCompare(date1, date2) > 0) {
                    err.message("From date should be less than To date !");
                    //$("#fromDate").focus();
                    alert("ERROR 1");
                    err.field('#fromDate');
                    return false;
                }
                else if (fn_DateCompare(date2, today) > 0) {
                    err.message("To date should not be more than Today's date !");
                    alert("ERROR 2");
                    err.field("#toDate");
                    return false;
                }

                if (err.message) {
                    document.getElementById('error_date').style.color = "#ffffff";
                    document.getElementById('error_date').style.width = "1000px";
                    document.getElementById('error_date').innerHTML = err.message;
                    return false;
                }

                location.href = '${contextpath}/componentReport/' + date1 + '/' + date2 + '/' + summary + '/' + componentName + '/';

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);
            });
        });

        $(function () {
            $("#component_status").click(function () {

                var date1 = $("#from").val();
                var date2 = $("#to").val();
                var today = new Date();
                var componentName = $('#component').val();

                $('#compo_summary').val('true');
                var summary = $('#compo_summary').val();

                location.href = '${contextpath}/componentReport/' + date1 + '/' + date2 + '/' + summary + '/' + componentName + '/';

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

                $("#summary").val('true');


            });
        });

        $(function () {
            $("#multi_both").click(function () {

                $('#both_multi').val("true");
                var both_value = $('#both_multi').val();
                var multiple_sprints = $("#multi_sprint").val();
                var project = $("#project").val();
                var err1 = {};
                var projectName = $("#project option:selected").text();

                if ($('#project').val() == 'none') {
                    validate();
                    return false;
                }

                else if ($('#multi_sprint').val() == null) {
                    err1.message = "Select Sprints to compare!";
                    err1.field = $("#multi_sprint");
                }

                if (err1.message) {
                    $('#Alert').show();
                    return false;
                }

                location.href = '${contextpath}/sprintTrends/' + project + '/' + multiple_sprints + '/' + projectName + '/' + both_value + '/';

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

            });
        });

        $(function () {
            $("#sprint_all").click(function () {

                $('#sprint_all_selected').val("true");
                var all_value = $('#sprint_all_selected').val();
                var project = $("#project").val();
                var projectName = $("#project option:selected").text();
                var sprint = $('#sprint').val();

                if ($("#project").val() == 'none') {
                    validate();
                    return false;
                }
                else if ($("#sprint").val() == 'none') {
                    validate_sprint();
                    return false;
                }

                location.href = '${contextpath}/sprintReport/' + project + '/' + sprint + '/' + projectName + '/' + all_value + '/';

                $(this).css('display', 'none');
                $('<img>').attr('src', '${contextpath}/img/loader.gif').insertAfter($(this));
                setInterval(function () {
                    var t = new Date().getTime();
                    $(this).attr("src", src + '?' + t);
                }, 5000);

            });
        });

        function fn_DateCompare(DateA, DateB) { // this function is good for dates > 01/01/1970

            var a = new Date(DateA);
            var b = new Date(DateB);

            var msDateA = Date.UTC(a.getFullYear(), a.getMonth() + 1, a.getDate());
            var msDateB = Date.UTC(b.getFullYear(), b.getMonth() + 1, b.getDate());

            if (parseFloat(msDateA) < parseFloat(msDateB)) {
                return -1; // lt
            }
            else if (parseFloat(msDateA) == parseFloat(msDateB)) {
                return 0; // eq
            }
            else if (parseFloat(msDateA) > parseFloat(msDateB)) {
                return 1; // gt
            }
            else
                return null; // error
        }

        function validate() {

            if ($("#project").val() == 'none') {
                $('#alert_project').show();
                $('#alert_sprint').hide();

            } else {
                $('#alert_project').hide();
            }
        }

        function enableSubmit() {
            if ($('#sprint').val() != 'none')
                $("#Submit").removeAttr('disabled');
        }

        function validate_sprint() {

            if ($("#sprint").val() == 'none') {
                $('#alert_sprint').show();
                return false;

            } else {
                $('#alert_sprint').hide();
            }
        }

        function validate_date_customer(){
            var date1 = $("#fromDate").val();
            var date2 = $("#toDate").val();
            var today = new Date();

            if (fn_DateCompare(date1, date2) > 0) {
                ('#alert_date_4').show();
                ('#alert_date_3').hide();
                return;
            }
            else if (fn_DateCompare(date2, today) > 0) {
                ('#alert_date_3').show();
                ('#alert_date_4').hide();
                return;
            }
        }

        function initList() {
            $("#sprint").get(0).selectedIndex = 1;
        }

    </script>

</head>
<body onload="enableSubmit()">

<form id="form" method="post" action="${contextpath}/customer"
      modelAttribute="sprintForm" onload="initList()"">

    <div class="panel panel-info" style="margin-top: 40px">
        <div class="panel-heading">
            <h3 class="panel-title">Sprint</h3>
        </div>
        <div class="panel-body" align="center">
            <div class="dropdown">
                <table>
                    <tr>
                        <td>Projects <br><select id="project" name="project" style="text-align: center">
                            <option selected value=33>- Select Project -</option>
                            <c:set var="rapidViewId" value="${sprintForm.rapid}"/>
                            <c:set var="total_projects"
                                   value="${fn:length(sprintForm.rapidView)}"/>


                            <c:forEach items="${sprintForm.rapidView}" var="project">
                                <c:choose>
                                    <c:when test="${sprintForm.rapid eq project.key}">
                                        <option selected value="${project.key}">${project.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${project.key}">${project.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <option value="${project.key}">${project.name}</option>
                        </select>
                        <td>Sprint <br><select id="sprint" name="sprint" style="text-align: center">
                            <option selected value=none>- Select Sprint -</option>
                            <c:set var="total_completed"
                                   value="${fn:length(sprintForm.sprintNames)}"/>
                            <c:forEach var="i" begin="0" end="${total_completed}">
                                <option value="${sprintForm.sprintIds[i]}">${sprintForm.sprintNames[i]}</option>
                            </c:forEach>
                        </select>
                        </td>
                        <td>Sprint mode<br>
                            <input type="checkbox" name="sprint" value="sprint" id="sprint_checkbox" style="transform: scale(1.5);" value="true" title="Click to show ONLY sprint info">
                        </td>
                        <td>From Date <br><input type="text" id="from_Date" name="from_Date" style="text-align: center; width: 80px"></td>
                        <td>To Date <br><input type="text" id="to_Date" name="to_Date" style="text-align: center; width: 80px"></td>

                    </tr>
                </table>
                <br>
                <table align="center">

                    <tr>

                        <div id="alert_project" class="alert alert-danger" role="alert" align="center"
                             style="width: 300px;">
                            <strong>Error!</strong> Select Project.
                        </div>

                        <div id="alert_sprint" class="alert alert-danger" role="alert" align="center"
                             style="width: 300px;">
                            <strong>Error!</strong> Select Sprint.
                        </div>

                        <div id="alert_date_1" class="alert alert-danger" role="alert" align="center"
                             style="width: 350px;">
                            <strong>Error!</strong> From Date should be less than To Date!
                        </div>

                        <div id="alert_date_2" class="alert alert-danger" role="alert" align="center"
                             style="width: 350px;">
                            <strong>Error!</strong> To Date should be less than Today's Date!
                        </div>

                    </tr>
                    <tr>
                        <td width="1000px">
                            <button type="button" id="sprint_all" class="btn btn-info"
                                    data-toggle="button">- All -
                            </button>

                            <button style="margin-left: 20px" type="button" id="Submit" class="btn btn-info"
                                    data-toggle="button">Issue Summary
                            </button>

                            <button style="margin-left: 20px" type="button" id="Trends"
                                    class="btn btn-info"
                                    data-toggle="button">Bug Trend
                            </button>

                            <button style="margin-left: 20px" type="button" id="AutomationTrend"
                                    class="btn btn-info"
                                    data-toggle="button">Automation Trend
                            </button>
                            <button style="margin-left: 20px" type="button" id="commits"
                                    class="btn btn-info"
                                    data-toggle="button">Commits
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">Multi Sprints</h3>
        </div>
        <div class="panel-body" align="center">
            <table>
                <td>Sprints<br><select id="multi_sprint" name="multi_sprint" multiple="multiple" size="5"
                                       style="width: 200px; text-align: center">
                    <c:forEach var="i" begin="0" end="${total_completed}">
                        <option value="${sprintForm.sprintIds[i]}">${sprintForm.sprintNames[i]}</option>
                    </c:forEach>
                </select></td>

            </table>
            <br>

            <div id="Alert" class="alert alert-danger" role="alert" align="center" style="width: 300px;">
                <strong>Error!</strong> Select Sprints to compare.
            </div>

            <table>
                <td>
                    <button type="button" id="multi_both"
                            class="btn btn-success"
                            data-toggle="button">- All -
                    </button>
                </td>
                <td>
                    <button type="button" id="SprintComparison"
                            class="btn btn-success"
                            data-toggle="button">Compare Bug Trends
                    </button>
                </td>
                <td>
                    <button type="button" id="CompareAutomationTrend"
                            class="btn btn-success"
                            data-toggle="button">Compare Automation Trend
                    </button>
                </td>


            </table>
        </div>
    </div>
    <div class="panel panel-warning">
        <div class="panel-heading">
            <h3 class="panel-title">Customers</h3>
        </div>
        <div class="panel-body" align="center">
            <table>
                <c:set var="total_customers" value="${fn:length(sprintForm.customerNames)}"/>
                <c:set var="total_severity" value="${fn:length(sprintForm.severity)}"/>
                <c:set var="total_projects" value="${fn:length(sprintForm.projectNames)}"/>

                <td>Projects <br><select id="projects" name="projects" style="text-align: center">
                    <option selected value=none style="text-align: center">- Select Project -</option>
                    <c:forEach var="i" begin="0" end="${total_projects - 1}">
                        <option style="text-align: center" value="${sprintForm.projectKeys[i]}">${sprintForm.projectNames[i]} (${sprintForm.projectKeys[i]})</option>
                    </c:forEach>
                </select>
                </td>

                <td>Customers<br><select id="customer" name="customer" multiple="multiple" size="5"
                                         style="width: 200px; text-align: center">
                    <c:forEach var="i" begin="0" end="${total_customers}">
                    <option value="${sprintForm.customerNames[i]}">${sprintForm.customerNames[i]}</option>
                    </c:forEach>
                </td>

                <td>Severity <br><select id="severity" name="severity" style="text-align: center">
                    <option selected value=none style="text-align: center">- All -</option>
                    <c:forEach var="i" begin="0" end="${total_severity}">
                        <option value="${sprintForm.severity[i]}" style="text-align: center">${sprintForm.severity[i]}</option>
                    </c:forEach>
                </select>
                </td>
                <td style="margin-top: -10px">From Date <br><input type="text" id="fromDate" name="fromDate" style="text-align: center; width: 80px"></td>
                <td>To Date <br><input type="text" id="toDate" name="toDate" style="text-align: center; width: 80px;"></td>


                <table>
                    <td>
                        <div style="background: #bd1212; width: 100px; padding-left: 10px"
                             id="error_date"></div>
                        <br>
                        <div id="alert_date_3" class="alert alert-danger" role="alert" align="center"
                             style="width: 350px;">
                            <strong>Error!</strong> From Date should be less than To Date!
                        </div>

                        <div id="alert_date_4" class="alert alert-danger" role="alert" align="center"
                             style="width: 350px;">
                            <strong>Error!</strong> To Date should be less than Today's Date!
                        </div>

                        <button type="submit" id="customer_status"
                                class="btn btn-warning"
                                data-toggle="button">Customer Issue Summary
                        </button>

                        <button style="margin-left: 20px" type="submit" id="customer_bugTrend"
                                class="btn btn-warning"
                                data-toggle="button">Customer Bug Trend
                        </button>

                        <input type='text' id='summary' name='summary' value='' hidden="hidden"/>
                        <input type='text' id='customerName' name='customerName' value='' hidden="hidden"/>
                        <input type='text' id='total_project' name='total_project' value='${total_projects}' hidden="hidden"/>
                    </td>

                </table>
            </table>
        </div>
    </div>

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Components</h3>
    </div>
    <div class="panel-body" align="center">
        <table>
            <c:set var="total_components" value="${fn:length(sprintForm.componentNames)}"/>

            <td>Components<br><select id="component" name="component" multiple="multiple" size="5"
                                     style="width: 150px">
                <c:forEach var="i" begin="0" end="${total_components}">
                <option value="${sprintForm.componentNames[i]}" style="text-align: center">${sprintForm.componentNames[i]}</option>
                </c:forEach>
            </td>

            <td style="margin-top: -10px">From Date <br><input type="text" id="from" name="from" style="text-align: center; width: 80px"></td>
            <td>To Date <br><input type="text" id="to" name="to" style="text-align: center; width: 80px;"></td>


            <table>
                <td>
                    <div style="background: #bd1212; width: 100px; padding-left: 10px"
                         id="error_date_comp"></div>
                    <br>
                    <div id="alert_date_comp_1" class="alert alert-danger" role="alert" align="center"
                         style="width: 350px;">
                        <strong>Error!</strong> From Date should be less than To Date!
                    </div>

                    <div id="alert_date_comp_2" class="alert alert-danger" role="alert" align="center"
                         style="width: 350px;">
                        <strong>Error!</strong> To Date should be less than Today's Date!
                    </div>

                    <button type="button" id="component_status"
                            class="btn btn-primary"
                            data-toggle="button">Component Issue Summary
                    </button>

                    <button style="margin-left: 20px" type="button" id="component_bugTrend"
                            class="btn btn-primary"
                            data-toggle="button">Component Bug Trend
                    </button>

                    <input type='text' id='compo_summary' name='compo_summary' value='' hidden="hidden"/>
                    <input type='text' id='componentName' name='componentName' value='' hidden="hidden"/>
                    <input type='text' id='projectName' name='projectName' value='' hidden="hidden"/>
                    <input type='text' id='both_multi' name='both_multi' value='' hidden="hidden"/>
                    <input type='text' id='sprint_all_selected' name='sprint_all_selected' value='' hidden="hidden"/>
                </td>

            </table>
        </table>
    </div>
</div>


</form>

</body>
</html>