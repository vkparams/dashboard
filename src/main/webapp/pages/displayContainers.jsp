<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextpath" value="${pageContext.request.contextPath}" />
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


</head>
<body>

	<form:form method="post" action="save.html"
		modelAttribute="containerForm">

		<div class="container ng-scope">

			<!-- Modal -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="modal-title">Upload Object</h4>
						</div>
						<div class="modal-body">
							<label for="ObjectName">Object Name</label> <input
								class="form-control input-sm" type="text"
								placeholder="Object Name"> <br> <label
								for="containerName">Container Name</label> <input
								class="form-control input-sm" type="text"
								placeholder="Container Name"> <br> <label
								for="folderPath">Folder Path</label> <input
								class="form-control input-sm" type="text"
								placeholder="Ex: /folder-1/folder-3"> <br> <label
								for="containerName">Upload Object</label><input type="file" />

						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
							<button type="button" class="btn btn-primary">Save
								changes</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->




			<table>
				<tr>
					<td>
						<h4>Containers</h4>
					</td>
					<td style="padding-left: 10px">
						<button type="button" id="createContainer"
							class="btn btn-primary btn-sm" data-toggle="button">Create
							Container</button>
					</td>

					<td style="padding-left: 10px">
						<button type="button" id="uploadObject"
							class="btn btn-primary btn-sm" data-toggle="button">Upload
							Object</button>
					</td>


				</tr>
			</table>

			<table class="table table-striped">
				<thead>
					<tr>
						<th>Container Name</th>
						<th>Size</th>


					</tr>
				</thead>
				<tbody>
					<c:forEach items="${containerForm.containers}" var="container"
						varStatus="status">
						<tr>
							<td>${container.name}<a class="deleteContainer"
								rel="tooltip" title="Delete" data-placement="top" href="#">*</a></td>
							<td>${container.size}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<br />

	</form:form>
</body>
</html>