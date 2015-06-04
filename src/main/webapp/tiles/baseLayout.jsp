<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextpath" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    <link rel="stylesheet" type="text/css"
	href="${contextpath}/css/bootstrap.css">
        <link rel="shortcut icon" href="${contextpath}/img/seagate_logo.png">

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title><tiles:insertAttribute name="title" ignore="true" /></title>
    </head>
    <body>
 
                    <tiles:insertAttribute name="header" />
  <div class="container-fluid">
                    <tiles:insertAttribute name="body" />
 
                    <tiles:insertAttribute name="footer" />
 </div>
    </body>
</html>
