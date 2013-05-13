<!DOCTYPE html> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html ng-app="seedboxerui">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
	<div class="row-fluid">
    	
		<div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="active"><a href="#"><i class="icon-home icon-white"></i>Home</a></li>
              <li><a href="#">TVShows</a></li>
              <li><a href="#"></a></li>
            </ul>
          </div><!--/.well -->
        </div>
    	
    	
    	<div class="span9" ng-controller="StatusCtrl">
    	
    		<div>
	    		<h2>Downloading</h2>
	    		
	    		<h5>{{current.download.fileName}}</h5>
	    		<div class="progress progress-striped active">
				  <div class="bar" style="width: {{current.download.transferred * 100 / current.download.size}}%;"></div>
				</div>
				<div>
					{{current.download.transferred}} of {{current.download.size}} Mb 
				</div>
				<div>
					Status: {{current.downloadStatus}} 
				</div>
			</div>
			
			<h3>Queue</h3>
			
			<table class="table table-hover">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Options</th>
                </tr>
              </thead>
              <tbody>
                <tr ng-repeat="download in queue">
                  <td>{{download.order}}</td>
                  <td>{{download.title}}</td>
                  <td><a class="btn btn-small" href="#"><i class="icon-trash"></i></a></td>
                </tr>
              </tbody>
            </table>
    	</div>

	</div>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>

</html>
