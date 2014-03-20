'use strict';


// Declare app level module which depends on filters, and services
angular.module('ngIdentity', [
  'ngRoute',
  'ngIdentity.filters',
  'ngIdentity.services',
  'ngIdentity.directives',
  'ngIdentity.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {templateUrl: 'index.html', controller: 'IndexController'});
  $routeProvider.otherwise({redirectTo: '/'});
}]);