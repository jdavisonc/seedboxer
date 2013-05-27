<!DOCTYPE html> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html ng-app="seedboxerui">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container" >
	<div class="row-fluid">
    	
		<div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list" ng-controller="NavController">
              <li ng-class="navClass('home')"><a href="#home"><i class="icon-home"></i> Home</a></li>
              <li ng-class="navClass('contents')"><a href="#contents"><i class="icon-fire"></i> Contents</a></li>
	      <li ng-class="navClass('downloads')"><a href="#downloads"><i class="icon-circle-arrow-down"></i> Downloads</a></li>
            </ul>
          </div>
        </div>
    	
    	<div class="span9">
	    	<div ng-view></div>
		</div>
	</div>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>

</html>
