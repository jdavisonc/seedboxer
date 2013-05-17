'use strict';

 /* Controllers */

function StatusCtrl($scope, $http, User) {
	
	/*$http.get('webservices/user/status', { params: { 'apikey' : 'Opu4uTgf' } })
		.success(function(data) {
			$scope.current = data;
		}
	); */
	var apikey = User.apikey();
	console.log(  User.apikey() );

	/** Response from SeedBoxer API **/	
	$scope.current = {
	    "download": {
	        "size": 1152,
	        "fileName": "The.Following.S01E01.720p.HDTV.X264-DIMENSION",
	        "transferred": 500
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

function NavController($scope, $location){
   $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'home';
        return page === currentRoute ? 'active' : '';
    };   
}

function ProfileCtrl($scope, $http, User) {
	$scope.configs = [
                {
                    "value": "boxerseed",
                    "key": "FtpPassword"
                },
                {
                    "value": "Downloads",
                    "key": "FtpRemoteDir"
                },
                {
                    "value": "ftp.personalserver.org",
                    "key": "FtpUrl"
                },
                {
                    "value": "seedboxer",
                    "key": "FtpUsername"
                }
            ];
}


