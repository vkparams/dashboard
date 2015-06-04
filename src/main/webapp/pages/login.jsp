<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="contextpath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="../../assets/ico/favicon.png">

<title>Signin Template for Bootstrap</title>

<!-- Bootstrap core CSS -->

<link href="${contextpath}/css/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${contextpath}/css/signin.css" rel="stylesheet">

</head>


<style>
body {
    background-color: #FFFFFF;
    padding-top: 0px;
}
</style>
<!-- JS dependencies -->
<script src="${contextpath}/js/jquery-1.8.3.min.js"></script>
<script src="${contextpath}/js/bootstrap.min.js"></script>
<script src="${contextpath}/js/bootstrap.js"></script>


<!-- bootbox code -->
<script src="${contextpath}/js/bootbox.min.js"></script>
<script>
	$(function() {
		$("[rel='tooltip']").tooltip();
	});
</script>

<script type="text/javascript">
	$(document).on("click", ".deleteContainer", function(e) {
		bootbox.confirm("Would you like to Delete the Container !", function() {
			console.log("Alert Callback");
		});
	});

	$(document).on("click", "#createContainer", function(e) {

		bootbox.prompt("Enter the Container Name", function(result) {
			if (result === null) {
				Example.show("Prompt dismissed");
			} else {
				Example.show("Hi <b>" + result + "</b>");
			}
		});

	});

	$(document).on("click", "#uploadObject", function(e) {
		$('#myModal').modal('show');

	});
</script>




 	<div class="container">
		<div class="span8">
			<form action="verifylogin" method="post" class="form-horizontal"
				id="billingform" accept-charset="utf-8">
				<div class="control-group">
					<label for="name" class="control-label"> User Name </label>
					<div class="controls">
						<input name="username" type="text" placeholder="user name"
							value="" id="username">
					</div>
				</div>

				<div class="control-group">
					<label for="password" class="control-label"> Password </label>
					<div class="controls">
						<input name="password" placeholder="password" type="password" value=""
							id="password">
					</div>
				</div>




				<div class="form-actions">
					<button type="submit" class="btn btn-primary">Login</button>
				</div>
			</form>
		</div>
		<!-- .span8 -->
	</div>
 </html>

