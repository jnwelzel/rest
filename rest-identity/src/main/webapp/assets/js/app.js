'use strict';


// Declare app level module which depends on filters, and services
angular.module('ngIdentity', [
  'ngRoute',
  'ngCookies',
  'ngIdentity.filters',
  'ngIdentity.services',
  'ngIdentity.directives',
  'ngIdentity.controllers'
]).
config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
  $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeController'});
  $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: 'LoginController'});
  $routeProvider.when('/logout', {controller: 'LogoutController'});
  $routeProvider.when('/sign-up', {templateUrl: 'partials/sign_up.html', controller: 'SignUpController'});
  $routeProvider.when('/profile', {templateUrl: 'partials/profile.html', controller: 'ProfileController'});
  $routeProvider.when('/developers', {templateUrl: 'partials/new_consumer.html', controller: 'NewDeveloperController'});
  $routeProvider.when('/profile/:id', {templateUrl: 'partials/profile_view.html', controller: 'ProfileViewController'});
  $routeProvider.otherwise({redirectTo: '/home'});
  $httpProvider.interceptors.push('authInterceptor');
}]);