angular.module('app.apiserviceInventory', [])
.service('apiserviceInventory', function($http,$q){

	var accessUrl = '';

	this.findLocation=function(){
		var defer = $q.defer();
		
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllVehicles=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('http://www.glider-autos.com/getAllVehicles/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllInventory=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('http://www.glider-autos.com:9889/getAllInventory/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getTimeTableOfPhotos=function(){
		var defer = $q.defer();
		
		$http.get('/getTimeTableOfPhotos').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
})