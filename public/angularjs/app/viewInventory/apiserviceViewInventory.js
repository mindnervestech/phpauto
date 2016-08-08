angular.module('app.apiserviceViewInventory', [])
.service('apiserviceViewInventory', function($http,$q){

	var accessUrl = '';

	this.findLocation=function(){
		var defer = $q.defer();
		
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllInventory=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('/getAllInventory/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
})