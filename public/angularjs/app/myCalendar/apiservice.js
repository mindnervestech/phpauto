angular.module('starter.apiservice', [])
.service('apiservice', function($http,$q){

	var accessUrl = '';

	this.getTimeTableOfPhotos=function(){
		var defer = $q.defer();
		
		$http.get('/getTimeTableOfPhotos').success(function(data) {
			defer.resolve(data);
		}).error(function(error){
			defer.reject(error);
		});
		

		return defer.promise;
	};
	
	this.saveNewEvent = function(data){
		var defer = $q.defer();
		$http.post("/saveNewEvent",data).success(function(data){
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.deleteEvent = function(data){
		var defer = $q.defer();
		$http.post("/deleteEvent",data).success(function(data){
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	

	
})
