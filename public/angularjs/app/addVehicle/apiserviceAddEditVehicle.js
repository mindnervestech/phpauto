angular.module('app.apiserviceAddEditVehicle', [])
.service('apiserviceAddEditVehicle', function($http,$q,$upload){

	var mavenUrl = 'http://www.glider-autos.com:9889/';

	this.getCustomizationform=function(formType){
		var defer = $q.defer();
		
		$http.get('/getCustomizationform/'+formType).success(function(response) {
			defer.resolve(response);
		}).error(function(error){
			defer.reject(error);
		});
		

		return defer.promise;
	};
	
	
	
})
