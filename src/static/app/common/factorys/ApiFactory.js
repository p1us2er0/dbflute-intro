angular.module('dbflute-intro').factory('ApiFactory',
        function ($rootScope, $http) {
    'use strict';

    var errorHandler = function(data, status) {

    };

    return {
        manifest: function() {
            return $http({
                method : 'POST',
                url : 'api/intro/manifest'
          }).success(function(data) {
          }).error(errorHandler);
        },
        publicProperties: function() {
            return $http({
                method : 'POST',
                url : 'api/engine/publicProperties'
          }).success(function(data) {
          }).error(errorHandler);
        },
        engineVersions: function() {
            return $http({
                method : 'POST',
                url : 'api/engine/versions'
          }).success(function(data) {
          }).error(errorHandler);
        },
        classification: function() {
            return $http({
                method : 'POST',
                url : 'api/client/classification'
          }).success(function(data) {
          }).error(errorHandler);
        },
        clientBeanList: function() {
            return $http({
                method : 'POST',
                url : 'api/client/list'
          }).success(function(data) {
          }).error(errorHandler);
        },
        clientCreate: function(clientBean, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/create',
                data : {clientBean: clientBean, testConnection: testConnection}
          }).success(function(data) {
          }).error(errorHandler);
        },
        clientUpdate: function(clientBean, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/update',
                data : {clientBean: clientBean, testConnection: testConnection}
          }).success(function(data) {
          }).error(errorHandler);
        },
        clientRemove: function(params) {
            return $http({
                method : 'POST',
                url : 'api/client/remove' + params.project
          }).success(function(data) {
          }).error(errorHandler);
        },
        downloadEngine: function(params) {
            return $http({
                method : 'POST',
                url : 'api/engine/download/' + params.version
          }).success(function(data) {
          }).error(errorHandler);
        }
    };
});
