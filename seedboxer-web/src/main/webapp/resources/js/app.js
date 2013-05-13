'use strict';

/* App Module */

var App = angular.module('seedboxerui', ['httpBasicService']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/status', {templateUrl: 'welcome', controller: StatusCtrl}).
      otherwise({redirectTo: '/status'});
}]);