'use strict';

/* Services */

var userResource = '/users/:id';
var sessionResource = '/session';

angular.module('ngIdentity.services', ['ngResource'])
  .value('version', '0.1')
  .factory('User', ['$resource', 
    function($resource) {
      return $resource(userResource, {}, {
        find: {method: 'GET', params: {id: 'id'}},
        update: {method: 'PUT', params: {id: 'id'}}
      });
    }]
  )
  .factory('Session', ['$resource',
    function($resource) {
      return $resource(sessionResource);
    }]
  )
  .factory('authInterceptor', ['$rootScope', '$q', '$window', 'SessionService', '$location', function($rootScope, $q, $window, SessionService, $location) {
    return {
      request: function (config) {
        config.headers = config.headers || {};
        if (SessionService.getToken()) {
          config.headers.Authorization = SessionService.getToken();
        }
        return config;
      },
      response: function (response) {
        return response || $q.when(response);
      },
      responseError: function(rejection) {
        console.log('ERROR HTTP STATUS ' + rejection.status);
        if (rejection.status === 401 || rejection.status === 403) {
          // handle the case where the user is not authenticated
          SessionService.clearToken(); // clear session just in case
          SessionService.clearUserName();
          $location.path('/login');
        }
        return $q.reject(rejection);
      }
    };
  }]
  )
  .service('SessionService', ['$window',
    function($window) {
      var tokenService = {
        getToken: function() {
          var token = $window.sessionStorage.getItem('token');
          return token || null;
        },
        setToken: function(token) {
          $window.sessionStorage.setItem('token', token);
        },
        clearToken: function() {
          $window.sessionStorage.removeItem('token');
        },
        setUserName: function(name) {
          $window.sessionStorage.setItem('user', name);
        },
        getUserName: function() {
          var userName = $window.sessionStorage.getItem('user');
          return userName || null;
        },
        clearUserName: function() {
          $window.sessionStorage.removeItem('user');
        },
        clearAll: function() {
          $window.sessionStorage.removeItem('user');
          $window.sessionStorage.removeItem('token');
        }
      };
      return tokenService;
    }]
  );
