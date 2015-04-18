'use strict';

angular.module('static', ['ngAnimate', 'ngCookies', 'ngTouch', 'ngSanitize', 'ui.router', 'ui.bootstrap'])
  .config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/main/main.html',
        controller: 'MainCtrl'
      }).state('client', {
          url: '/client',
          templateUrl: 'app/client/client.html',
          controller: 'ClientCtrl'
        });

    $urlRouterProvider.otherwise('/');
  })
;
