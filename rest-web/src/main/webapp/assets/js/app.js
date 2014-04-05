'use strict';


// Declare app level module which depends on filters, and services
angular.module('ngRest', [
  'ngRoute',
  'ngCookies',
  'ngRest.filters',
  'ngRest.services',
  'ngRest.directives',
  'ngRest.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeController'});
  $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: 'LoginController'});
  $routeProvider.otherwise({redirectTo: '/login'});
}]);