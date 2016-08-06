angular.module('app.apiserviceMoreInfo', [])
.service('apiserviceMoreInfo', function($http,$q){

	var accessUrl = '';

	this.getSelectedLeadType=function(){
		var defer = $q.defer();
		
		$http.get('/getSelectedLeadType').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllContactInfo=function(){
		var defer = $q.defer();
		
		$http.get('/getAllContactInfo').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.requestInfoMarkRead=function(flag, id){
		var defer = $q.defer();
		
		$http.get('/requestInfoMarkRead/'+flag+'/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};

	
})
