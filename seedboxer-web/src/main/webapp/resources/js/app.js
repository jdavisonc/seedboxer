'use strict';

/* App Module */

var seedboxerui = angular
    .module('seedboxerui', ['seedboxerui.services','ui.bootstrap'])
    .config(function($provide, $routeProvider) {
	$provide.factory('$routeProvider', function () {
	    return $routeProvider;
	});
    })
    .run(function($routeProvider,apikeyResource,$route) {
	//get the apikey from the server before setting the route,
	//this way we ensure nothing is done before having the apikey.

	apikeyResource.getApiKeyFromServer().then(function(){

	//Set up the routes and reload so the proper view is fetched.

	    $routeProvider.
		when('/', {
			templateUrl: '/ui/home.html'
		}).
		when('/account-settings', {
			templateUrl: '/ui/account-settings.html'
		}).
		when('/my-profile', {
			templateUrl: '/ui/my-profile.html'
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
