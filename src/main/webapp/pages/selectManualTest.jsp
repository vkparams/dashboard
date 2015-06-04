<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextpath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table tr td {
	padding: 10px;
}

iframe {
	width: 650px;
	height: 500px
}
</style>
    <!-- JS dependencies -->
    <script src="${contextpath}/js/jquery-1.8.3.min.js"></script>
    <script src="${contextpath}/js/bootstrap.min.js"></script>
    <script src="${contextpath}/js/bootstrap.js"></script>
    <script src="${contextpath}/js/jquery-ui.js"></script>
<script>

	$(window).load(function () {
		$('#automation').removeClass("active");
		$('#home').removeClass("active");
		$('#stash').removeClass("active");
		$('#unitTest').removeClass("active");
		$('#manualTest').addClass("active");
	});

$(function () {
    $("#Submit").click(function(){
    	if($("#file").val()==''){
    		alert("Please select Manual test case to upload !");
    		$("#file").focus();
    		return false;
    	}
    	if($("#buildNo").val()==''){
    		alert("Please enter Build No !");
    		$("#buildNo").focus();
    		return false;
    	}
/*     	if($("#projects").val()=='none'){
    		alert("Please select Project !");
    		$("#projects").focus();
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
    	}*/
    }); 

});

</script>
</head>
<body>
	<form method="post" action="${contextpath}/uploadManualTests"
		modelAttribute="uploadForm" enctype="multipart/form-data">
		<div class="container ng-scope">

			<table>
				<tr>
					<td><c:out value="${status}" /></td>
				</tr>
				<tr>
					<td>Select File &nbsp;&nbsp;<input id="file" name="files[0]" type="file" /></td>
<%-- 
					<td>Enter Build No &nbsp;&nbsp;<input id="buildNo" type="text" name="buildNo" value="${buildNo}"/>					
					</td>
					<td> --%>
					</tr>
					<tr>
					<td>
					<span style="font-size: 7pt"><br> # Excel should have only one sheet and sheet name should match with existing testLink suite name </span>
					<span style="font-size: 7pt"><br> # Please use the latest Full Regression Build No from TestLink <a target=blank href='http://10.28.208.78/testlink/login.php'>http://10.28.208.78/testlink/login.php</a></span>
					</td>
				</tr>



				<tr>

					<td style="padding-top: 10px">
						<button type="submit" id="Submit" class="btn btn-primary btn-sm"
							data-toggle="button">Submit</button>
						<button type="button" id="Reset" class="btn btn-primary btn-sm"
							data-toggle="button">Reset</button>
					</td>


				</tr>
			</table>
		</div>
	</form>

</body>
</html>