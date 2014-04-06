<!doctype html>
<html lang="en" ng-app="restOauthApp">
<head>
  <title>Allow Account Access</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Identity Manager</title>
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">

  <script src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-resource.min.js"></script>
  <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

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
  </style>
</head>
<body>

  <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="/">Identity Manager v0.1</a>
      </div>
      <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav navbar-right">
          <li><a href="#profile">${user.firstName user.lastName}</a></li>
          <li ng-controller="LogoutController"><a href="#logout" ng-click="logout()"><i class="fa fa-sign-out"></i> Log Out</a></li>
          <li><a href="https://github.com/jnwelzel/rest" target="new" title="Source code on Github"><i class="fa fa-github"></i></a></li>
        </ul>
      </div>
    </div>
  </div>

	<div class="container">
    <h1>Authorize ${consumerApplicationName} to use your account?</h1>
    <h1><small>${consumerApplicationDescription}</small></h1>
    <hr/>

    <div class="row">
      This application <strong class="text-success">will be able to</strong>:
      <ul>
        <li>Access your basic personal information (name and email).</li>
      </ul>
    </div>

    <div class="row">
      <p>
        <button type="button" class="btn btn-primary btn-lg">Authorize app</button>
        <button type="button" class="btn btn-default btn-lg">No, thanks</button>
      </p>
    </div>
  </div>

</body>
</html>