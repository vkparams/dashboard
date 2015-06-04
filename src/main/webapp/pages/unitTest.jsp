<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table tr td {
	padding: 10px;
}

iframe {
	width: 700px;
	height: 500px
}
</style>
</head>
<body>
	<table>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/acctstats/coverage_report/"></iframe>
			</td>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/auditlogger/coverage_report/"></iframe>
			</td>
		</tr>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/activitylog_consumer/coverage_report/"></iframe>
			</td>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/bizapi/coverage_report/"></iframe>
			</td>
		</tr>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/evauth/coverage_report/"></iframe>
			</td>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/mailapi/coverage_report/"></iframe>
			</td>
		</tr>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/pwapi/coverage_report/"></iframe>
			</td>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/s3multi/coverage_report/"></iframe>
			</td>
		</tr>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/skyline/coverage_report/"></iframe>
			</td>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/swift3/coverage_report/"></iframe>
			</td>
		</tr>
		<tr>
			<td><iframe
					src="http://jenkins-master.fb.lab:8080/jenkins/job/mqbroker/coverage_report/"></iframe>
			</td>
			<td>
			</td>
		</tr>
	</table>

</body>
</html>