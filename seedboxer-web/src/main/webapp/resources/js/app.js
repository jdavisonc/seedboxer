'use strict';

/* App Module */

var seedboxerui = angular
    .module('seedboxerui', ['seedboxerui.services','ui.bootstrap'])
    .config(function($provide, $routeProvider) {
	$provide.factory('$routeProvider', function () {
	    return $routeProvider;
	});
    })
    .run(function($routeProvider,apikeyResource,$route, userStatusService, queueService, userConfigService) {
	//get the apikey from the server before setting the route,
	//this way we ensure nothing is done before having the apikey.

	apikeyResource.getApiKeyFromServer().then(function(){

	//Set up the routes and reload so the proper view is fetched.

	    $routeProvider.
		when('/', {
			controller : function($scope, status, queue){
			    $scope.current = status;
			    $scope.queue = queue;
			},
			templateUrl: '/ui/home.html',
			resolve    :{
			    status : userStatusService.getUserStatusData,
			    queue : queueService.getQueue
			}
		}).
		when('/account-settings', {
			templateUrl: '/ui/account-settings.html'
		}).
		when('/my-profile', {
			controller : function($scope, configData, configTypes){
			    $scope.configs = configData.configs;
			    $scope.types = configTypes;
			},
			templateUrl: '/ui/my-profile.html',
			resolve : {
			    configData : userConfigService.getConfigList,
			    configTypes : userConfigService.getConfigTypes
			}
		}).
		when('/server-settings', {
			templateUrl: '/ui/server-settings.html'
		}).
		when('/contents', {
			templateUrl: '/ui/contents.html'
		}).
		otherwise({ redirectTo: '/'});
	    $route.reload();	
	});
    })    
