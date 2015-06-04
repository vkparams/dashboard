<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextpath" value="${pageContext.request.contextPath}"/>


        <%--<span>--%>
            <%--<a style="margin-left: 40px"  href="${contextpath}/jiraDashboard/project/33">Sprint Dashboard</a>--%>
        <%--<a style="margin-left: 40px" href="${contextpath}/home/project/SCAL">Stash Report</a>--%>
        <%--<a style="margin-left: 40px"  href="${contextpath}/unitTestTrend">Unit Test Trend</a> --%>
        <%--<a style="margin-left: 40px"  href="${contextpath}/automationTrend">Automation Trend</a>           --%>
        <%--<a style="margin-left: 40px"  href="${contextpath}/manualTest">Manual Tests</a>--%>
        <%--</span>--%>

            <ul class="nav nav-pills" style="margin-top: 10px; margin-left: 10px">
                <li href="${contextpath}/jiraDashboard/project/33"><img alt="Seagate" src="${contextpath}/img/rsz_seagate.png"/>
                <li role="presentation" class="active" id="home" style="margin-left: 30px"><a href="${contextpath}/jiraDashboard/project/33">Sprint Dashboard</a></li>
                <li role="presentation" id="stash"><a href="${contextpath}/home/project/SCAL">Stash Report</a></li>
                <li role="presentation" id="unitTest"><a href="${contextpath}/unitTestTrend">Unit Test Trend</a></li>
                <%--<li role="presentation" id="automation"><a href="${contextpath}/automationTrend">Automation Trend</a></li>--%>
                <li role="presentation" id="manualTest"><a href="${contextpath}/manualTest">Manual Tests</a></li>
            </ul>


