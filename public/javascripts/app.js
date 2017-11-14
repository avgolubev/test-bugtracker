(function () {
'use strict';

angular.module('app', [])
.controller('TaskDeskController', TaskDeskController)
.service('TaskDeskService', TaskDeskService);

TaskDeskController.$inject = ['TaskDeskService'];
function TaskDeskController(TaskDeskService) {
  var ctrl = this;
  ctrl.form = "desk";
  ctrl.success = true;
  ctrl.task_id   = 0;
  ctrl.task_title = "";
  ctrl.task_descr = "";
  ctrl.tasksState =[];
  ctrl.isExistTasks = false;
  
  ctrl.changeForm = function(form) {
	  ctrl.form = form;
	  if(ctrl.form === "desk")
		  ctrl.getTaskDesk();
	  if(ctrl.form === "create") {
		  ctrl.task_title = "";
		  ctrl.task_descr = "";		  
	  }
  };
  
  ctrl.setCurrent = function(task, curState) {
	  ctrl.task_id   = task.id;
	  ctrl.task_title = task.title;
	  ctrl.task_descr = task.descr;	  
	  ctrl.curState = curState;
  };
  
  ctrl.changeState = function(task_id, state_id) {
      var promise = TaskDeskService.changeState(task_id, state_id);
      promise.then(function(response){
    	  	ctrl.success = response;
    	  	if(ctrl.success)
    	  	  ctrl.changeForm('desk');
      });	  
  }
  
  ctrl.getTaskDesk = function() {    	
      var promise = TaskDeskService.getTaskDesk();
      promise.then(function(response){
    	  	ctrl.tasksState = response;
        if(ctrl.tasksState .length == 0)
          ctrl.isExistTasks = false;
        else
          ctrl.isSearchFailed = true;
      });
    
  };
  
  ctrl.addTask = function(title, descr) {    	
      var promise = TaskDeskService.addTask(title, descr);
      promise.then(function(response){
    	  	ctrl.success = response;
    	  	if(ctrl.success)
    	  	  ctrl.changeForm('desk');
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
  
  service.addTask = function(title, descr) {
	    var result = $http({
	      method: "POST",
	      url: "tasks",
	      headers: { 'Content-Type': 'application/json' },
	      data: {title: title, descr: descr, stateID: 1}
	    })
	    .then( function(response) {
	      return response.data;
	    });
	    return result;
  };  
  
  service.changeState = function(task_id, state_id) {
	    var result = $http({
	      method: "PUT",
	      url: "tasks/" + task_id + "/states/" + state_id,	      
	    })
	    .then( function(response) {
	      return response.data;
	    });
	    return result;
};    
	  
}


})();
