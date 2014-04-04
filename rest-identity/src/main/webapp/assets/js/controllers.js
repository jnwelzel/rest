'use strict';

/* Controllers */

angular.module('ngIdentity.controllers', [])
  .controller('HomeController', ['$scope', 'User', function($scope, User) {
    $scope.users = User.query();
  }])
  .controller('ProfileController', ['$scope', function($scope) {
    
  }])
  .controller('ProfileViewController', ['$scope', function($scope) {

  }])
  .controller('NewDeveloperController', ['$scope', '$location', '$window', 'Consumer', function($scope, $location, $window, Consumer) {
    $scope.consumer = {};
    $scope.signUp = function(consumer) {
      var newConsumer = new Consumer(angular.copy(consumer));
      newConsumer.$save(
        {},
        function(success) {
          $window.alert('Successfully signed up!\nAPI key: ' + success.key + '\nSecret: ' + success.secret);
          $location.path('/home');
        },
        function(error) {
          $window.alert('Error: ' + error.data);
        }
      );
    };
  }])
  .controller('LoginController', ['$scope', 'Session', 'SessionService', '$window', '$location', function($scope, Session, SessionService, $window, $location) {
    $scope.login = function(user) {
      $scope.master = angular.copy(user);
      Session.save(
        {}, $scope.master,
        function(success) {
          SessionService.setToken(success.password); // not the actual passwd, it's just the session hash ;)
          SessionService.setUserName(success.firstName + ' ' + success.lastName);
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
            $location.path('/home');
          },
          function(error) {
            $window.alert('Error: ' + error.data);
            if (error.status == 401) {
              SessionService.clearToken();
              SessionService.clearUserName();
              $location.path('/login');
            }
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
      var newUser = new User(angular.copy(user));
      newUser.$save(
        {},
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