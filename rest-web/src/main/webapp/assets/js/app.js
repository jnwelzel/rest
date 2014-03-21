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
  $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeController'});
  $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: 'LoginController'});
  $routeProvider.when('/sign-up', {templateUrl: 'partials/sign_up.html', controller: 'SignUpController'});
  $routeProvider.otherwise({redirectTo: '/home'});
}]);