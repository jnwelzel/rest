'use strict';

/* Directives */


angular.module('ngRest.directives', [])
  .directive('appVersion', ['version', function(version) {
    return function(scope, element, attrs) {
      element.text(version);
    };
  }]);
