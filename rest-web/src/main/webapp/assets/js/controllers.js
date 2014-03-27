'use strict';

/* Controllers */

angular.module('ngIdentity.controllers', [])
  .controller('HomeController', ['$scope', 'User', function($scope, User) {
    $scope.users = User.query();
  }])
  .controller('ProfileController', ['$scope', function($scope) {

  }])
  .controller('LoginController', ['$scope', 'Session', 'SessionService', '$window', '$location', function($scope, Session, SessionService, $window, $location) {
    $scope.login = function(user) {
      $scope.master = angular.copy(user);
      Session.save(
        {}, $scope.master,
        function(success) {
          SessionService.setToken(success.authToken.id);
          SessionService.setUserName(success.name + ' ' + success.lastName);
          $window.alert('Successfully logged in.');
          $location.path('/home');
        },
        function(error) {
          $window.alert('Error: ' + error.data);
        }
      );
    };
  }])
  .controller('LogoutController', ['$scope', 'SessionService', 'Session', '$window', '$location', function($scope, SessionService, Session, $window, $location) {
    $scope.logout = function() {
      if (SessionService.getToken()) {
        Session.delete(
          {}, 
          function(success) {
            SessionService.clearToken();
            SessionService.clearUserName();
            $window.alert('Successfully logged out.');
          },
          function(error) {
            $window.alert('Error: ' + error.data);
          }
        );
      }
      $location.path('/home');
    }
  }])
  .controller('SessionInfoController', ['$scope','SessionService', function($scope, SessionService) {
    $scope.userName = null;
    $scope.template = null;
    $scope.$watch(
      function() {
        return SessionService.getToken();
      },
      function(token) {
        $scope.template = token == null ? 'partials/_logged_out.html' : 'partials/_logged_in.html';
      }
    );
    $scope.$watch(
      function() {
        return SessionService.getUserName();
      },
      function(name) {
        $scope.userName = name;
      }
    );
  }])
  .controller('SignUpController', ['$scope', '$window', '$location', 'User', function($scope, $window, $location, User) {
    $scope.user = {};

    $scope.signUp = function(user) {
      user.roles = ['ADMIN', 'USER'];
      $scope.master = angular.copy(user);
      User.save(
        {}, $scope.master, 
        function() {
          $window.alert('New user successfully created.');
          $location.path('/home');
        },
        function() {
          $window.alert('Error.');
        }
      );
    };
  }]);