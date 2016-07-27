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
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Make Live Successfully",
				});
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
	
	this.updateRegisterUser = function(register){
		var defer = $q.defer();
		$http.post("/updateRegisterUser",register).success(function(data){
     		$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Update successfully",
				});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllCarsDetails = function(register){
		var defer = $q.defer();
		$http.get('/getAllCarsDetails').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllBoat = function(register){
		var defer = $q.defer();
		$http.get('/getAllBoat').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllMotorcycles = function(register){
		var defer = $q.defer();
		$http.get('/getAllMotorcycles').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllDesignerFurniture = function(register){
		var defer = $q.defer();
		$http.get('/getAllDesignerFurniture').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllRealState = function(register){
		var defer = $q.defer();
		$http.get('/getAllRealState').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllServiceProvider = function(register){
		var defer = $q.defer();
		$http.get('/getAllServiceProvider').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllLuxuryProducts = function(register){
		var defer = $q.defer();
		$http.get('/getAllLuxuryProducts').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	
	
})
