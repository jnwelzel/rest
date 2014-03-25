'use strict';

/* Services */

var userResource = 'resources/users/:id';
var loginResource = 'resources/users/login';

// Demonstrate how to register services
// In this case it is a simple value service.
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
  .factory('LogIn', ['$resource',
    function($resource) {
      return $resource(loginResource);
    }]
  )
  .factory('authInterceptor', ['$rootScope', '$q', '$window', function($rootScope, $q, $window) {
    return {
      request: function (config) {
        config.headers = config.headers || {};
        if ($window.sessionStorage.token) {
          config.headers.Authorization = 'Bearer ' + $window.sessionStorage.token;
        }
        return config;
      },
      response: function (response) {
        if (response.status === 401) {
          // handle the case where the user is not authenticated
          console.log('401 Not Authorized');
        }
        return response || $q.when(response);
      }
    };
  }]
  )
  .service('TokenService', ['$window',
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
        }
      };
      return tokenService;
    }]
  );
