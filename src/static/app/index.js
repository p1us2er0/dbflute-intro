'use strict';

var app = angular.module('dbflute-intro',
        ['ngAnimate', 'ngCookies', 'ngTouch', 'ngSanitize', 'ui.router', 'ui.bootstrap', 'ngStorage', 'pascalprecht.translate']);

app.config(function ($stateProvider, $urlRouterProvider) {
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
});

app.config(function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'assets/i18n/locale-',
        suffix: '.json'
    });

    $translateProvider.preferredLanguage('ja');
    $translateProvider.fallbackLanguage('en');
    $translateProvider.useMissingTranslationHandlerLog();
//    $translateProvider.useLocalStorage();
});
