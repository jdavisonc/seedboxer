/* Services */

angular.module('userServices', ['ngResource']).factory('User', ['$resource', function($resource) {
    
	this.apikey = function(){
		var apikey;
		if (apikey == null) {
			apikey = $resource('/apikey', {}, {
			    query: {method:'GET'}
			  }).query();
		}
		return apikey;
    }; 
    
    return this;
    
}]);