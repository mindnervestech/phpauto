angular.module('app.apiserviceCustomizationForm', [])
.service('apiserviceCustomizationForm', function($http,$q){

	var accessUrl = '';

	this.getCustomizationform=function(formType){
		var defer = $q.defer();
		
		$http.get('/getCustomizationform/'+formType).success(function(data) {
			defer.resolve(data);
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
	
	this.getLeadCrateForm=function(editform){
		var defer = $q.defer();
		
		$http.post('/getLeadCrateForm',editform).success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Form Created successfully",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getLeadCrateFormTitle=function(setjson){
		var defer = $q.defer();
		
		$http.post('/getLeadCrateFormTitle',setjson).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Title Edit successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
})