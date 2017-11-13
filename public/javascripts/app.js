(function () {
'use strict';

angular.module('app', [])
.controller('TaskDeskController', TaskDeskController)
.service('TaskDeskService', TaskDeskService);

TaskDeskController.$inject = ['TaskDeskService'];
function TaskDeskController(TaskDeskService) {
  var ctrl = this;
  ctrl.states =[];
  ctrl.isExistTasks = false;
  ctrl.getTaskDesk = function() {    	
      var promise = TaskDeskService.getTaskDesk();
      promise.then(function(response){
    	ctrl.states = response.states;
        if(ctrl.states.length == 0)
          ctrl.isExistTasks = false;
        else
          ctrl.isSearchFailed = true;
      });
    
  };
 
  ctrl.getTaskDesk();
}

TaskDeskService.$inject = ['$http'];
function TaskDeskService($http) {
  var service = this;

  service.getTaskDesk = function() {
    var result = $http({
      method: "GET",
      url: "taskdesk"
    })
    .then( function(response) {
      var desk = response.data;
      return desk;
    });
    return result;
  };
}


})();
