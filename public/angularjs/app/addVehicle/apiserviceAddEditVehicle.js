angular.module('app.apiserviceAddEditVehicle', [])
.service('apiserviceAddEditVehicle', function($http,$q,$upload){

	this.getCustomizationform=function(formType){
		var defer = $q.defer();
		$http.get('/getCustomizationform/'+formType).success(function(response) {
			defer.resolve(response);
		}).error(function(error){
			defer.reject(error);
		});
		return defer.promise;
	};
	
	this.getAllSites=function(){
		var defer = $q.defer();
		$http.get('/getAllSites').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDealerProfile=function(){
		var defer = $q.defer();
		$http.get('/getDealerProfile').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	
})
