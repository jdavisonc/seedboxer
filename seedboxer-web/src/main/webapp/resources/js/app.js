'use strict';

/* App Module */

var seedboxerui = angular
	.module('seedboxerui', ['httpBasicService'])
	.config(['$routeProvider', function($routeProvider) {
		
		//Set up our mappings between URLs, templates, and controllers
		$routeProvider.
			when('/account-settings', {
				templateUrl: '/ui/account-settings.html'
			}).
			when('/', {
				templateUrl: '/ui/home.html'
			}).
			when('/my-profile', {
				templateUrl: '/ui/my-profile.html'
			}).
			when('/server-settings', {
				templateUrl: '/ui/server-settings.html'
			}).
			when('/tv-shows', {
				templateUrl: '/ui/tv-shows.html'
			}).
			otherwise({ redirectTo: '/'});
	}]);