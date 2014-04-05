'use strict';

/* Services */

var userResource = '/users/:id';
var sessionResource = '/session';
var consumerResource = '/consumers';

angular.module('ngRest.services', ['ngResource'])
  .value('version', '0.1')
  .factory('User', ['$resource',
    function($resource) {
      return $resource(userResource, {}, {
        find: {method: 'GET', params: {id: 'id'}},
        update: {method: 'PUT', params: {id: 'id'}}
      });
    }]
  )
  .factory('Consumer', ['$resource',
    function($resource) {
      return $resource(consumerResource, {}, {
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
