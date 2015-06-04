<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="elf" uri="elfunctions"%>

<c:set var="contextpath" value="${pageContext.request.contextPath}" />
<c:set var="graphData" value="" />
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


<script type="text/javascript">


google.load("visualization", "1", {
		packages : [ "corechart" ]
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

				  var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
				  chart.draw(data, options);
		
	}
</script>



</head>
<body>

	<form>
		<div class="page-header" align="center">
			<h3 style="font-size: 30px">Sprint Automation Trend
				<small style="font-size: 20px">| <span class="label label-primary">${fromDate} - ${toDate}</span></small>
			</h3>
		</div>

		<div class="container ng-scope"
			style="width: 1300px; margin-left: 280px; margin-top: 100px">
			<table>
				<tr>
					<c:choose>
						<c:when test="${automationForm.trendData eq null}">
							<td><p><strong><font size="3">No Data to Display!</font></strong></p></td>
						</c:when>
						<c:otherwise>
							<td>
								<div id="chart_div" style="width: 900px; height: 500px;"></div>
							</td>
						</c:otherwise>
					</c:choose>

				</tr>

			</table>
		</div>
		<br />
	</form>
</body>
</html>