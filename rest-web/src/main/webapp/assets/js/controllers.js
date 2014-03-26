'use strict';

/* Controllers */

angular.module('ngIdentity.controllers', [])
  .controller('HomeController', ['$scope', 'User', function($scope, User) {
    $scope.users = User.query();
  }])
  .controller('LoginController', ['$scope', 'LogIn', '$window', '$location', function($scope, LogIn, $window, $location) {
    $scope.user = {};

    $scope.login = function(user) {
      $scope.master = angular.copy(user);
      LogIn.save(
        {}, $scope.master,
        function(success) {
          $window.sessionStorage.setItem('token', success.authToken.id);
          $window.alert('Successfully logged in.');
          $location.path('/home');
        },
        function(error) {
          $window.alert('Error: ' + error.data);
        }
      );
    };
  }])
  .controller('LogoutController', ['$scope', 'TokenService', '$window', '$location', function($scope, TokenService, $window, $location) {
    $scope.logout = function() {
      TokenService.clearToken();
      $window.alert('Successfully logged out.');
      $location.path('/home');
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