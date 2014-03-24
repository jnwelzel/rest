'use strict';

/* Directives */


angular.module('ngIdentity.directives', [])
  .directive('appVersion', ['version', function(version) {
    return function(scope, element, attrs) {
      element.text(version);
    };
  }])
  .directive('sessionInfo', ['Token', function(Token) {
    return {
      replace: 'true',
      templateUrl: Token.length == 0 ? 'partials/_logged_out.html' : 'partials/_logged_in.html'
    };
  }]);
