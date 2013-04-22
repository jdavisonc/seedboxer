<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>SeedBoxer Web</title>

    <spring:url value="/webjars/bootstrap/2.3.1/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/webjars/jquery/2.0.0/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>
    
    <spring:url value="/resources/css/seedboxer.css" var="seedboxerCss"/>
    <link href="${seedboxerCss}" rel="stylesheet"/>

	<!-- Title Font -->
	<link href='http://fonts.googleapis.com/css?family=Denk+One' rel='stylesheet' type='text/css'>

</head>


