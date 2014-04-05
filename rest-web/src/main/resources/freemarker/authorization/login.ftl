<!doctype html>
<html lang="en" ng-app="ngRest">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Identity Manager</title>
  <link rel="stylesheet" href="assets/css/app.css"/>
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <![endif]-->
</head>
<body>

  <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="#home">Identity Manager v<span app-version></a>
      </div>
      <div class="navbar-collapse collapse" ng-controller="SessionInfoController">
        <ul class="nav navbar-nav navbar-right" ng-include src="template"></ul>
      </div>
    </div>
  </div>

  <!-- Placeholder for views -->
  <div class="container" ng-view></div>

  <div id="footer">
    <div class="container">
      <p>&#169; <a href="http://jonwelzel.com" target="new" title="Developed and maintained by Jon Welzel">jonwelzel.com</a> <strike>All rights reserved</strike> lol</p>
    </div>
  </div>

  <script src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-route.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-resource.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-cookies.min.js"></script>
  <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
  <script src="assets/js/app.js"></script>
  <script src="assets/js/services.js"></script>
  <script src="assets/js/controllers.js"></script>
  <script src="assets/js/filters.js"></script>
  <script src="assets/js/directives.js"></script>
</body>
</html>