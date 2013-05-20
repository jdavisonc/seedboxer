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

	    $http.get(userStatusService.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
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
    var queueService = {
	apiPath : '/webservices/downloads/queue',
	getQueue :  function(){
	    var deferred = $q.defer();

	    $http.get(queueService.apiPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching the queue");
	    });

	    return deferred.promise;
	}

    };
    return queueService;
	
});


seedboxerUiServices.service('userConfigService',function($http,$q, apikeyResource){
    var userConfigService = {
	listPath : '/webservices/user/configs/list',
	savePath: '/webservices/user/configs/save',
	deletePath: '/webservices/user/configs/delete',
	typesPath: '/webservices/user/configs/types',
	getConfigList :  function(){
	    var deferred = $q.defer();

	    $http.get(userConfigService.listPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching the configurations list.");
	    });

	    return deferred.promise;
	},
	saveConfig :  function(key, value){

	    var deferred = $q.defer();
	    $http.get(userConfigService.savePath, 
	    {
		params : 
		{
		    apikey : apikeyResource.getApiKey(),
		    key : key,
		    value : value
		}
	    }).success(function(data){
		if(data.status == "FAILURE")
		    deferred.reject("An error occured while saving the configuration");
		else
		    deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while saving the configuration");
	    });

	    return deferred.promise;
	},
	deleteConfig :  function(key){

	    var deferred = $q.defer();
	    $http.get(userConfigService.deletePath, 
	    {
		params : 
		{
		    apikey : apikeyResource.getApiKey(),
		    key : key
		}
	    }).success(function(data){
		deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while deleting the configuration");
	    });

	    return deferred.promise;
	},
	getConfigTypes :  function(){
	    var deferred = $q.defer();

	    $http.get(userConfigService.typesPath, {params : {apikey : apikeyResource.getApiKey()}}).success(function(data){
		deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while fetching config types");
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
	},
	putToDownload :  function(fileNames){
	    var deferred = $q.defer();

	    $http.get('/webservices/downloads/put', {params : {apikey : apikeyResource.getApiKey(), fileNames: fileNames}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching contents");
	    });

	    return deferred.promise;
	}

    };
    return contentsService;
	
});


seedboxerUiServices.service('alertService',function($rootScope, $timeout){
   var alertService = {
	alert : null,
	counter : 0,
	showWarning : function(alertMsg){
	    alertService._addAlert({ type : "", msg : alertMsg})
	},
	showError : function(alertMsg){
	    alertService._addAlert({ type : "error", msg : alertMsg})
	},
	showSuccess : function(alertMsg){
	    alertService._addAlert({ type : "success", msg : alertMsg})
	},
	_addAlert : function(alert){
	    alertService.alert = alert;
	    alertService.counter++;
	    if(!$rootScope.$$phase)
		$rootScope.$digest();
	    
	    $timeout.cancel(alertService.timeout);
	    
	    alertService.timeout = $timeout(function(){
		alertService.counter++;
		alertService.alert = null;
		
	    },3000);
	},
	getAlert : function(){
		return alertService.alert;
	},
	clearAlert : function(){
	    alertService.pendingAlert = false;
	}

   };
   return alertService;
	
});