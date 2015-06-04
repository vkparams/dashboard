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
		
		<c:forEach items="${unitTests.componentMap}" var="component" varStatus="count">
		<c:set var="componentData" value="${component.value}" />
		<c:set var="componentKey" value="${component.key}" />
			
		var data = google.visualization
				.arrayToDataTable([${componentData}]);

		var options = {
			title : '${componentKey}',
		    legend: { position: 'bottom' },
		    vAxis: { 
		        viewWindowMode:'explicit',
		        viewWindow: {
		            max:100,
		            min:0
		        }
		    }
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('chart_div${count.index}'));
		chart.draw(data, options);
		</c:forEach>
	}
</script>



</head>
<body>

	<form:form>


		<div class="container ng-scope"
			style="width: 1000px; margin-left: 50px; margin-top: 100px">
        <a style="margin-left: 40px"  href="${contextpath}/unitTest">Unit Test Results</a>

			<table>
				<tr>

					<c:forEach items="${unitTests.componentMap}" var="component"
						varStatus="count">
						<c:if test="${count.index mod 2 == 0}">
				</tr>

				<tr>
				</c:if>
				<td>
					<div id="chart_div${count.index}"
						style="width: 500px; height: 350px;"></div>
				</td>

				</c:forEach>
			</table>
		</div>
		<br />
	</form:form>
</body>
</html>