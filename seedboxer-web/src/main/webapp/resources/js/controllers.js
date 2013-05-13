'use strict';

 /* Controllers */

function LoginCtrl($scope, $http) {
	var encoded = Base64.encode(username + ':' + password);
	/* $http.get('webservices/user/apikey', { params: { 'apikey' : 'Opu4uTgf'}, headers: {'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='}})
		.success(function(data) {
			$scope.queue = data;
		}
	); */
}

function StatusCtrl($scope, $http) {
	
	/*$http.get('webservices/user/status', { params: { 'apikey' : 'Opu4uTgf' } })
		.success(function(data) {
			$scope.current = data;
		}
	); */

	/** Response from SeedBoxer API **/	
	$scope.current = {
	    "download": {
	        "size": 1152,
	        "fileName": "The.Following.S01E01.720p.HDTV.X264-DIMENSION",
	        "transferred": 654
	    }, "downloadStatus": "STARTED", "message": null, "status": null 
	};

	/* $http.get('webservices/downloads/queue', { params: { 'apikey' : 'Opu4uTgf' } })
		.success(function(data) {
			$scope.queue = data;
		}
	); */

	$scope.queue = [
		{ order: 1, title: 'Person.of.Interest.S02E21.720p.HDTV.X264-DIMENSION'},
		{ order: 2, title: 'Game.of.Thrones.S03E05.720p.HDTV.x264-IMMERSE'},
		{ order: 3, title: 'The.Big.Bang.Theory.S06E22.720p.HDTV.X264-DIMENSION'},
	];

}
