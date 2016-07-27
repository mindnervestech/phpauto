angular.module('app.apiserviceRegistration', [])
.service('apiserviceRegistration', function($http,$q){

	var accessUrl = '';

	this.getRegistrList=function(){
		var defer = $q.defer();
		
		$http.get('/getRegistrList').success(function(data) {
			defer.resolve(data);
		}).error(function(error){
			defer.reject(error);
		});

		return defer.promise;
	};
	
	this.getStatus = function(id){
		var defer = $q.defer();
		$http.get('/getStatus/'+id).success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.getRemoveUser = function(id){
		var defer = $q.defer();
		$http.get('/getRemoveUser/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Remove User",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.getSendDemoLink = function(id){
		var defer = $q.defer();
		$http.get('/getSendDemoLink/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Demo Link send",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	

	
})
