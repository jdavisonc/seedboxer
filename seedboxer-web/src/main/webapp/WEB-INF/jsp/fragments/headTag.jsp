<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>SeedBoxer Web</title>
    <link rel="icon" type="image/png" href="img/favicon.png" />

    <spring:url value="/webjars/jquery/2.0.0/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

	<!-- Bootstrap -->

    <spring:url value="/webjars/bootstrap/2.3.1/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>
    
    <spring:url value="/webjars/angular-ui/0.4.0/angular-ui.min.css" var="angularUiCss"/>
    <link href="${angularUiCss}" rel="stylesheet"/>
    
    <spring:url value="/webjars/select2/3.3.1/select2.css" var="select2Css"/>
    <link href="${select2Css}" rel="stylesheet"></script>

    <spring:url value="/resources/css/bootstrap-editable.css" var="xEditableCss"/>
    <link href="${xEditableCss}" rel="stylesheet"></script>

	<!-- AngularJS -->
    <spring:url value="/webjars/angularjs/1.1.4/angular.js" var="angular"/>
    <script src="${angular}"></script>
    
    <spring:url value="/webjars/angularjs/1.1.4/angular-resource.min.js" var="angularResource"/>
    <script src="${angularResource}"></script>

    <spring:url value="/webjars/angular-ui-bootstrap/0.3.0/ui-bootstrap-tpls.js" var="uiBootstrap"/>
    <script src="${uiBootstrap}"></script>
    
    <spring:url value="/webjars/angular-ui/0.4.0/angular-ui.js" var="angularUi"/>
    <script src="${angularUi}"></script>

    <spring:url value="/webjars/select2/3.3.1/select2.js" var="select2"/>
    <script src="${select2}"></script>
    
    <spring:url value="/resources/js/bootstrap-editable.min.js" var="xEditable"/>
    <script src="${xEditable}"></script>



    

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


