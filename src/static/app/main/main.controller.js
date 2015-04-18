'use strict';

angular.module('static')
  .controller('MainCtrl', function ($scope, $http, $window) {

      $scope.manifest = {};
      $scope.publicProperties = [];
      $scope.versions = [];
      $scope.clientBeanList = [];
      $scope.clientBean = {};

      $scope.manifest = function() {
          $http({
              method : 'POST',
              url : 'api/intro/manifest'
          }).success(function(data) {
              $scope.manifest = data;
          });
      }

      $scope.list = function() {
          $http({
              method : 'POST',
              url : 'api/client/list'
          }).success(function(data) {
              $scope.clientBeanList = data;
          });
      }

      $scope.add = function(clientBean) {
      }

      $scope.edit = function(clientBean) {

      }

      $scope.update = function(clientBean) {

      }

      $scope.remove = function(clientBean) {
          $http({
              method : 'POST',
              url : 'api/client/remove/' + clientBean.project
          }).success(function(data) {
              $scope.list();
          });
      }

      $scope.openSchemaHTML = function(clientBean) {
          $window.open('api/client/schemahtml/' + clientBean.project);
      }

      $scope.openHistoryHTML = function(clientBean) {
          $window.open('api/client/historyhtml/' + clientBean.project);
      }

      $scope.task = function(clientBean, task) {
          $window.open('api/client/task/' + clientBean.project + '/' + task);
      }

      $scope.downloadEngine = function(version) {
          $http({
              method : 'POST',
              url : 'api/engine/download/' + version
          }).success(function(data) {
          });
      }

      $scope.engineVersions = function(version) {
          $http({
              method : 'POST',
              url : 'api/engine/versions'
          }).success(function(data) {
              $scope.versions = data;
          });
      }

      $scope.publicProperties = function(version) {
          $http({
              method : 'POST',
              url : 'api/engine/publicProperties'
          }).success(function(data) {
              $scope.publicProperties = data;
          });
      }

      $scope.engineVersions = function(version) {
          $http({
              method : 'POST',
              url : 'api/engine/versions'
          }).success(function(data) {
              $scope.versions = data;
          });
      }

      $scope.setCurrentProject = function(clientBean) {
          $scope.clientBean = clientBean;
      }

      $scope.manifest();
      $scope.publicProperties();
      $scope.engineVersions();
      $scope.list();
});
