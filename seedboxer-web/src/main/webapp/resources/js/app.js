'use strict';

/* App Module */

var seedboxerui = angular
    .module('seedboxerui', ['seedboxerui.services','ui.bootstrap','ui'])
    .config(function($provide, $routeProvider) {
	$provide.factory('$routeProvider', function () {
	    return $routeProvider;
	});
    })
    .run(function($routeProvider,userDataResource,$route, userStatusService, userConfigService,
		userContentService, downloadsService, adminRssService, adminUsersService) {
	//get the apikey from the server before setting the route,
	//this way we ensure nothing is done before having the apikey.

	userDataResource.getUserDataFromServer().then(function(){

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
			    queue : downloadsService.getQueue
			}
		}).
		when('/account-settings', {
			templateUrl: '/ui/account-settings.html'
		}).
		when('/users', {
			controller : function($scope, usersData){
			    $scope.users = usersData.users;
			},
			templateUrl: '/ui/users.html',
			resolve : {
			    usersData : adminUsersService.getUsersList,
			}
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
		when('/rss-feeds', {
			controller  : function($scope, serverSettings){
			    $scope.rssFeeds = serverSettings.rssFeeds
			},
			templateUrl: '/ui/server-settings.html',
			resolve : {
			    serverSettings : adminRssService.getRssList
			}
		}).
		when('/downloads', {
		
			controller : function($scope, downloads){
			    $scope.downloads = downloads;
			},
			templateUrl: '/ui/downloads.html',
			resolve : {
			    downloads : downloadsService.getDownloads
			}
		}).
		when('/contents', {
			controller : function($scope, contents){
			    $scope.contents = contents;
			},
			templateUrl: '/ui/contents.html',
			resolve : {
			    contents : userContentService.getContentList
			}
		}).
		otherwise({redirectTo: '/'});
	    $route.reload();
	});
    })    

