'use strict';

/* Controllers */

angular.module('ngIdentity.controllers', [])
  .controller('IndexController', ['$scope, User', function($scope, User) {
    $scope.users = User.query();
  }]);