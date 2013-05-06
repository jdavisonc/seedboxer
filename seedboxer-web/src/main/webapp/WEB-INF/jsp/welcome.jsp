<!DOCTYPE html> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en" ng-app="seedboxer">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
	<div class="row-fluid">
    	
		<div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="active"><a href="#"><i class="icon-home icon-white"></i>  Home</a></li>
              <li><a href="#">TVShows</a></li>
              <li><a href="#"></a></li>
            </ul>
          </div><!--/.well -->
        </div>
    	
    	
    	<div class="span9">
    	
    		<div ng-controller="StatusCtrl">
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
			
			<table class="table table-hover" ng-controller="QueueCtrl">
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

<script type="text/javascript">

	angular.module('statusServices', ['ngResource']).
		factory('Status', function($resource){
			return $resource('webservices/user/status?apikey=:apikey', {}, {
				query: {method:'GET', params:{apikey:'Opu4uTgf'}, isArray:true}
	  	});
	});

	var App = angular.module('seedboxer', ['statusServices']);
	
	 
	 /* Controllers */

	function StatusCtrl($scope, Status) {
		
		/** Response from SeedBoxer API **/	
			$scope.current = {
			    "download": {
			        "size": 1152,
			        "fileName": "The.Following.S01E01.720p.HDTV.X264-DIMENSION",
			        "transferred": 654
			    }, "downloadStatus": "STARTED", "message": null, "status": null 
			};
		
		
		// Uncomment this to make query 
		//$scope.current = Status.query();
	}
	
	function QueueCtrl($scope) {
		$scope.queue = [
			{ order: 1, title: 'Person.of.Interest.S02E21.720p.HDTV.X264-DIMENSION'},
			{ order: 2, title: 'Game.of.Thrones.S03E05.720p.HDTV.x264-IMMERSE'},
			{ order: 3, title: 'The.Big.Bang.Theory.S06E22.720p.HDTV.X264-DIMENSION'},
		];
	}
	
</script>

</body>

</html>
