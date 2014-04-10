<!doctype html>
<html lang="en" ng-app="restLoginApp">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <![endif]-->
  <style type="text/css">
    html {
      position: relative;
      min-height: 100%;
    }

    body {
      background-color: #ffffff;
      padding-bottom: 50px;
      padding-top: 80px;
    }

    #footer {
      background-color: #2E2E2E;
      position: absolute;
      bottom: 0;
      width: 100%;
      /* Set the fixed height of the footer here */
      height: 60px;
      text-align: center;
      padding-top: 20px;
    }

    #footer p {
      text-shadow: 0 1px 0 #000;
      color: #999;
    }

    .container .credit {
      margin: 20px 0;
    }

    .form-signin {
      max-width: 330px;
      padding: 15px;
      margin: 0 auto;
    }

    .form-signin .form-signin-heading,
    .form-signin .checkbox {
      margin-bottom: 10px;
    }

    .form-signin-heading {
      text-align: center;
    }

    .form-signin .checkbox {
      font-weight: normal;
    }

    .form-signin .form-control {
      position: relative;
      height: auto;
      -webkit-box-sizing: border-box;
         -moz-box-sizing: border-box;
              box-sizing: border-box;
      padding: 10px;
      font-size: 16px;
    }

    .form-signin .form-control:focus {
      z-index: 2;
    }

    .form-signin input[type="email"] {
      margin-bottom: -1px;
      border-bottom-right-radius: 0;
      border-bottom-left-radius: 0;
    }

    .form-signin input[type="password"] {
      margin-bottom: 10px;
      border-top-left-radius: 0;
      border-top-right-radius: 0;
    }
  </style>
</head>
<body>

  <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="/identity">Identity Manager v0.1</a>
      </div>
    </div>
  </div>

  <!-- Placeholder for views -->
  <div class="container">

    <div class="alert alert-info alert-dismissable">
      <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
      <strong>Attention!</strong> The application <strong>${consumerApplicationName}</strong> (<a href="${consumerDomain}">${consumerDomain}</a>) would like to connect to your account.
    </div>

    <form class="form-signin" accept-charset="UTF-8" role="form" ng-controller="LoginController">
      <h2 class="form-signin-heading"><i class="fa fa-lock"></i> Please Log In</h2>
      <input type="email" class="form-control" placeholder="Email address" autofocus required ng-model="user.email">
      <input type="password" class="form-control" placeholder="Password" required ng-model="user.password">
      <label class="checkbox">
        <input type="checkbox" value="remember-me"> Remember me
      </label>
      <button class="btn btn-lg btn-primary btn-block" ng-click="login(user)">Log in</button>
    </form>
  </div>

  <div id="footer">
    <div class="container">
      <p>&#169; <a href="http://jonwelzel.com" target="new" title="Developed and maintained by Jon Welzel">jonwelzel.com</a> <strike>All rights reserved</strike> lol</p>
    </div>
  </div>

  <script src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-resource.min.js"></script>
  <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

  <script>
    'use strict';

    var sessionResource = '/rest/authorization/login';
    var token = '${oauthToken}';

    var app = angular.module('restLoginApp', ['ngResource']);
    app.factory('Session', ['$resource',
      function($resource) {
        return $resource(sessionResource);
      }]
    );

    app.controller('LoginController', ['$scope', '$window', 'Session', function($scope, $window, Session) {
      $scope.user = {};
      $scope.login = function(user) {
        $scope.user = angular.copy(user);
        console.log('Token: ' + token);
        Session.save(
          {oauth_token: token}, 
          $scope.user,
          function(success) {
            $window.location.href = '/rest/authorization?oauth_token=' + token + '&session_token=' + success.sessionToken;
          },
          function(error) {
            $window.alert('Error: ' + error.data);
          }
        );
      };
    }]);

  </script>

</body>
</html>