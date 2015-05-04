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

app.config(function($httpProvider) {
    $httpProvider.interceptors.push(
            ['$q', '$rootScope', '$injector',
            function($q, $rootScope, $injector) {

        var dialog = null;

        return {
            responseError: function(response) {

                $rootScope.messages = null;
                var reload = false;

                if(response.status === 0) {
                    $rootScope.messages = ['エラーが発生しました。サーバが停止しています。しばらくたってからアクセスしてください。'];
                    reload = true;
                }

                if (response.status === 400) {
                    $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                }

                if (response.status === 401) {
                    $rootScope.messages = ['セッションが切れました。再度認証してください。'];
                    reload = true;
                }

                if (response.status === 403) {
                    $rootScope.messages = ['権限がありません。'];
                }

                if (response.status === 500) {
                    $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                    $rootScope.messages.unshift('エラーが発生しました。');
                }

                if (!dialog && $rootScope.messages) {
                    dialog = $injector.get('$modal').open({templateUrl: 'resultView.html', scope: $rootScope});
                    dialog.result.then(function () {
                            dialog = null;
                            if (reload) {
                                window.location.reload(reload);
                            }
                        }, function () {
                            dialog = null;
                            if (reload) {
                                window.location.reload(reload);
                            }
                        });
                }

                return $q.reject(response);
            }
        }
    }]);
});
