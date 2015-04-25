'use strict';

angular.module('dbflute-intro')
  .controller('MainCtrl', function ($scope, $http, $window, ApiFactory) {

      $scope.manifest = {};
      $scope.publicProperties = [];
      $scope.versions = [];
      $scope.classificationMap = {};
      $scope.clientBeanList = [];
      $scope.clientBean = null;
      $scope.editFlg = false;

      $scope.manifest = function() {
          ApiFactory.manifest().then(function(response) {
              $scope.manifest = response.data;
          });
      };

      $scope.publicProperties = function(version) {
          ApiFactory.publicProperties().then(function(response) {
              $scope.publicProperties = response.data;
          });
      };

      $scope.engineVersions = function(version) {
          ApiFactory.engineVersions().then(function(response) {
              $scope.versions = response.data;
          });
      };

      $scope.classification = function() {
          ApiFactory.classification().then(function(response) {
              $scope.classificationMap = response.data;
          });
      };

      $scope.clientBeanList = function() {
          ApiFactory.clientBeanList().then(function(response) {
              $scope.clientBeanList = response.data;
          });
      };

      $scope.add = function() {
          $scope.editFlg = true;
          $scope.clientBean = {};
      };

      $scope.edit = function() {
          $scope.editFlg = true;
      };

      $scope.cancelEdit = function() {
          $scope.editFlg = false;
      };

      $scope.create = function(clientBean) {
          ApiFactory.clientCreate(clientBean).then(function(response) {
              $scope.clientBeanList();
          });
      };

      $scope.update = function(clientBean) {
          ApiFactory.clientUpdate(clientBean).then(function(response) {
              $scope.clientBeanList();
          });
      };

      $scope.remove = function(clientBean) {
          ApiFactory.clientRemove(clientBean).then(function(response) {
              $scope.clientBeanList();
          });
      };

      $scope.openSchemaHTML = function(clientBean) {
          $window.open('api/client/schemahtml/' + clientBean.project);
      };

      $scope.openHistoryHTML = function(clientBean) {
          $window.open('api/client/historyhtml/' + clientBean.project);
      };

      $scope.task = function(clientBean, task) {
          $window.open('api/client/task/' + clientBean.project + '/' + task);
      };

      $scope.dbfluteEngine = {
          version: null
      };

      $scope.downloadEngine = function() {
          ApiFactory.downloadEngine($scope.dbfluteEngine).then(function(response) {
              $scope.engineVersions();
          });
      };

      $scope.selectVersion = function(version) {
          $scope.dbfluteEngine.version = version;
      };

      $scope.setCurrentProject = function(clientBean) {
          $scope.clientBean = clientBean.clientBean;
      };

      $scope.manifest();
      $scope.publicProperties();
      $scope.engineVersions();
      $scope.classification();
      $scope.clientBeanList();
});
