/* Services */

var seedboxerUiServices = angular.module('seedboxerui.services', []);
seedboxerUiServices.service('apikeyResource',function($http,$q) {
	
    var apikeyResource = {};

    apikeyResource.apiPath = '/apikey';
    apikeyResource.getApiKeyFromServer = function(){

	var deferred = $q.defer();

	$http.get(apikeyResource.apiPath).success(function(data){
	    apikeyResource.apikey = data.apiKey;
	    deferred.resolve(data);
	}).error(function(){

	    deferred.reject("An error occured while fetching status");
	});

	return deferred.promise;
    }
    apikeyResource.getApiKey = function(){
	return apikeyResource.apikey;
    }
    return apikeyResource;
	
});


seedboxerUiServices.service('userStatusService',function($http,$q, apikeyResource){
    var userStatusService = {
	apiPath : '/webservices/user/status',
	getUserStatusData :  function(){
	    var deferred = $q.defer();

	    $http.get(this.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
		deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while fetching status");
	    });

	    return deferred.promise;
	}

    };
    return userStatusService;
	
});


seedboxerUiServices.service('queueService',function($http,$q, apikeyResource){
    var userStatusService = {
	apiPath : '/webservices/downloads/queue',
	getQueue :  function(){
	    var deferred = $q.defer();

	    $http.get(this.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching the queue");
	    });

	    return deferred.promise;
	}

    };
    return userStatusService;
	
});


seedboxerUiServices.service('userConfigService',function($http,$q, apikeyResource){
    var userConfigService = {
	apiPath : '/webservices/user/configs/list',
	getConfigList :  function(){
	    var deferred = $q.defer();

	    $http.get(this.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching the queue");
	    });

	    return deferred.promise;
	}

    };
    return userConfigService;
	
});

seedboxerUiServices.service('contentsService',function($http,$q, apikeyResource){
    var contentsService = {
	apiPath : '/webservices/downloads/list',
	getContents :  function(){
	    var deferred = $q.defer();

	    $http.get(this.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching contents");
	    });

	    return deferred.promise;
	}

    };
    return contentsService;
	
});
