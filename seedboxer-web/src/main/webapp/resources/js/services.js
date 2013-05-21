/* Services */

var seedboxerUiServices = angular.module('seedboxerui.services', []);
seedboxerUiServices.service('userDataResource',function($http,$q) {
	
    var userDataResource = {};

    userDataResource.apiPath = '/userData';
    userDataResource.getUserDataFromServer = function(){

	var deferred = $q.defer();

	$http.get(userDataResource.apiPath).success(function(data){
	    userDataResource.apikey = data.apiKey;
	    userDataResource.username = data.name;
	    deferred.resolve(data);
	}).error(function(){

	    deferred.reject("An error occured while fetching status");
	});

	return deferred.promise;
    }
    userDataResource.getApiKey = function(){
	return userDataResource.apikey;
    }
    userDataResource.getUserName= function(){
	return userDataResource.username;
    }
    return userDataResource;
	
});


seedboxerUiServices.service('userStatusService',function($http,$q, userDataResource){
    var userStatusService = {
	apiPath : '/webservices/user/status',
	startApiPath : '/webservices/user/start',
	stopApiPath : '/webservices/user/stop',
	getUserStatusData :  function(){
	    var deferred = $q.defer();

	    $http.get(userStatusService.apiPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
		deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while fetching status");
	    });

	    return deferred.promise;
	},
	start :  function(){
	    var deferred = $q.defer();

	    $http.get(userStatusService.startApiPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while starting");
	    });

	    return deferred.promise;
	},
	stop :  function(){
	    var deferred = $q.defer();

	    $http.get(userStatusService.stopApiPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while stopping");
	    });

	    return deferred.promise;
	}

    };
    return userStatusService;
	
});


seedboxerUiServices.service('queueService',function($http,$q, userDataResource){
    var queueService = {
	apiPath : '/webservices/downloads/queue',
	getQueue :  function(){
	    var deferred = $q.defer();

	    $http.get(queueService.apiPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching the queue");
	    });

	    return deferred.promise;
	}

    };
    return queueService;
	
});


seedboxerUiServices.service('userConfigService',function($http,$q, userDataResource){
    var userConfigService = {
	listPath : '/webservices/user/configs/list',
	savePath: '/webservices/user/configs/save',
	deletePath: '/webservices/user/configs/delete',
	typesPath: '/webservices/user/configs/types',
	getConfigList :  function(){
	    var deferred = $q.defer();

	    $http.get(userConfigService.listPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
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
		    apikey : userDataResource.getApiKey(),
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
		    apikey : userDataResource.getApiKey(),
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

	    $http.get(userConfigService.typesPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
		deferred.resolve(data);
	    }).error(function(){
		deferred.reject("An error occured while fetching config types");
	    });

	    return deferred.promise;
	}
    };
    return userConfigService;
	
});

seedboxerUiServices.service('contentsService',function($http,$q, userDataResource){
    var contentsService = {
	apiPath : '/webservices/downloads/list',
	getContents :  function(){
	    var deferred = $q.defer();

	    $http.get(this.apiPath, {params : {apikey : userDataResource.getApiKey()}}).success(function(data){
	    	deferred.resolve(data);
	    }).error(function(){
	    	deferred.reject("An error occured while fetching contents");
	    });

	    return deferred.promise;
	},
	putToDownload :  function(fileName){
	    var deferred = $q.defer();

	    $http.get('/webservices/downloads/put', {params : {apikey : userDataResource.getApiKey(), fileNames: fileNames}}).success(function(data){
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