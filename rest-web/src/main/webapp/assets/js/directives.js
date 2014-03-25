'use strict';

/* Directives */


angular.module('ngIdentity.directives', [])
  .directive('appVersion', ['version', function(version) {
    return function(scope, element, attrs) {
      element.text(version);
    };
  }]);
