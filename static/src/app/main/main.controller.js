'use strict';

angular.module('static')
  .controller('MainCtrl', function ($scope, $http, $window) {

      $scope.projectList = [];

      $scope.list = function() {
          $http({
              method : 'POST',
              url : 'api/client/list'
          }).success(function(data) {
              $scope.projectList = data;
          });
      }

      $scope.add = function(project) {
      }

      $scope.edit = function(project) {

      }

      $scope.update = function(project) {

      }

      $scope.remove = function(project) {
          $http({
              method : 'POST',
              url : 'api/client/remove/' + project
          }).success(function(data) {
              $scope.list();
          });
      }

      $scope.openSchemaHTML = function(project) {
          $window.open('api/client/schemahtml/' + project);
      }

      $scope.openHistoryHTML = function(project) {
          $window.open('api/client/historyhtml/' + project);
      }

      $scope.task = function(project, task) {
          $window.open('api/client/task/' + project + '/' + task);
      }

      $scope.list();
});
