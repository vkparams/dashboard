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
    <link rel="stylesheet" type="text/css"
          href="${contextpath}/css/jquery-ui.css">
    <style>
        td{
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

        $(window).load(function () {
            $('#automation').addClass("active");
            $('#home').removeClass("active");
            $('#stash').removeClass("active");
            $('#unitTest').removeClass("active");
            $('#manualTest').removeClass("active");
        });

        $(function () {

            $('#projects').on("change", function () {
                location.href = '${contextpath}/home/project/' + $(this).val();
            });
        });

        $(function () {
            $("#fromDate").datepicker();
        });

        $(function () {
            $("#toDate").datepicker();
        });


        $(function () {
            $("#Reset").click(function(){
                location.href = '${contextpath}/home';
            });
        });

        
        $(function () {

            $("#fromDate").val($.datepicker.formatDate('mm/dd/yy', new Date()));
            $("#toDate").val($.datepicker.formatDate('mm/dd/yy', new Date()));
        });


        
        $(function () {
            $("#Submit").click(function(){
            	if($("#plan").val()=='none'){
            		alert("Please select plan !");
            		$("#plan").focus();
            		return false;
            	}
            	var date1=$("#fromDate").val();
            	var date2=$("#toDate").val();
                var today = new Date();
            	if(fn_DateCompare(date1,date2)>0){
            		alert("From date should be less than To date !");
            		$("#fromDate").focus();
            		return false;
            	}
            	else if(fn_DateCompare(date2,today)>0){
            		alert("To date should not be more than Today's date !");
            		$("#toDate").focus();
            		return false;
            	}
            });

        });
        
        
        function fn_DateCompare(DateA, DateB) {     // this function is good for dates > 01/01/1970

            var a = new Date(DateA);
            var b = new Date(DateB);

            var msDateA = Date.UTC(a.getFullYear(), a.getMonth()+1, a.getDate());
            var msDateB = Date.UTC(b.getFullYear(), b.getMonth()+1, b.getDate());

            if (parseFloat(msDateA) < parseFloat(msDateB))
              return -1;  // lt
            else if (parseFloat(msDateA) == parseFloat(msDateB))
              return 0;  // eq
            else if (parseFloat(msDateA) > parseFloat(msDateB))
              return 1;  // gt
            else
              return null;  // error
        }
        
    </script>


</head>
<body>

<form method="post" action="${contextpath}/automationTestTrend"
      modelAttribute="projectForm">
    <div class="container ng-scope" style="padding-top:100px">

        <table style="margin-top: 50px">
            <tr>
                <td> Project
                    <select id="plan" name="plan">
                        <option selected value=none>- Select Plan -</option>

                        <c:forEach items="${automationForm.planList}" var="plan">
                            <option value="${plan}">${plan}</option>
                        </c:forEach>
                    </select>
                </td>


                <td>From Date: <input type="text" id="fromDate" name="fromDate"></td>
                <td>To Date: <input type="text" id="toDate" name="toDate"></td>
            </tr>

            <tr>

                <td style="padding-top:10px">
                    <button type="submit" id="Submit"
                            class="btn btn-primary btn-sm" data-toggle="button">Submit
                    </button>
                    <button type="button" id="Reset"
                            class="btn btn-primary btn-sm" data-toggle="button">Reset
                    </button>
                </td>


            </tr>
        </table>
    </div>
</form>
</body>
</html>