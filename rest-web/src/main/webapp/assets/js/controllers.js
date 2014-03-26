'use strict';

/* Controllers */

angular.module('ngIdentity.controllers', [])
  .controller('HomeController', ['$scope', 'User', function($scope, User) {
    $scope.users = User.query();
  }])
  .controller('LoginController', ['$scope', 'Session', 'TokenService', '$window', '$location', function($scope, Session, TokenService, $window, $location) {
    $scope.user = {};

    $scope.login = function(user) {
      $scope.master = angular.copy(user);
      Session.save(
        {}, $scope.master,
        function(success) {
          TokenService.setToken(success.authToken.id);
          $window.alert('Successfully logged in.');
          $location.path('/home');
        },
        function(error) {
          $window.alert('Error: ' + error.data);
        }
      );
    };
  }])
  .controller('LogoutController', ['$scope', 'TokenService', 'Session', '$window', '$location', function($scope, TokenService, Session, $window, $location) {
    $scope.logout = function() {
      Session.delete(
        {}, 
        function(success) {
          TokenService.clearToken();
          $window.alert('Successfully logged out.');
          $location.path('/home');
        },
        function(error) {
          $window.alert('Error: ' + error.data);
        }
      );
    }
  }])
  .controller('SessionInfoController', ['$scope','TokenService', function($scope, TokenService) {
    $scope.template = null;
    $scope.$watch(
      function() {
        return TokenService.getToken();
      },
      function(token) {
        $scope.template = token == null ? 'partials/_logged_out.html' : 'partials/_logged_in.html';
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