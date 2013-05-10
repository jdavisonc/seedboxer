<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>SeedBoxer Web</title>

    <spring:url value="/webjars/jquery/2.0.0/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

	<!-- Bootstrap -->

    <spring:url value="/webjars/bootstrap/2.3.1/js/bootstrap.js" var="bootstrapJs"/>
    <script src="${bootstrapJs}"></script>

    <spring:url value="/webjars/bootstrap/2.3.1/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

	<!-- AngularJS -->
    <spring:url value="/webjars/angularjs/1.1.4/angular.min.js" var="angular"/>
    <script src="${angular}"></script>
    
    <spring:url value="/webjars/angularjs/1.1.4/angular-resource.min.js" var="angularResource"/>
    <script src="${angularResource}"></script>

    <!-- Title Font -->
    <link href='http://fonts.googleapis.com/css?family=Arbutus|Telex' rel='stylesheet' type='text/css'>
    
    <!-- SeedBoxer -->
    <spring:url value="/resources/css/seedboxer.css" var="seedboxerCss"/>
    <link href="${seedboxerCss}" rel="stylesheet"/>

    <spring:url value="/resources/js/services.js" var="seedboxerServicesJs"/>
    <script src="${seedboxerServicesJs}"></script>

    <spring:url value="/resources/js/app.js" var="seedboxerAppJs"/>
    <script src="${seedboxerAppJs}"></script>

    <spring:url value="/resources/js/controllers.js" var="seedboxerCtrlJs"/>
    <script src="${seedboxerCtrlJs}"></script>

</head>


