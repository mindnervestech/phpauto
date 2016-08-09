angular.module('app.apiserviceAnalytics', [])
.service('apiserviceAnalytics', function($http,$q){

	var accessUrl = '';

	this.getVisitorList=function(startDateFilter, endDateFilter){
		var defer = $q.defer();
		
		$http.get('/getVisitorList/'+startDateFilter+'/'+endDateFilter).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getVisitorData=function(visitorInfos){
		var defer = $q.defer();
		$http.get('/getVisitorData/'+visitorInfos).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getSessionData=function(sessionId, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getSessionData/'+sessionId+'/'+startDateFilter+'/'+endDateFilter).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getIPAddress=function(ipAddressInfo){
		var defer = $q.defer();
		$http.get('/getIPAddress/'+ipAddressInfo).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrafficSourceData=function(trafficSourceTitle, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getTrafficSourceData/'+trafficSourceTitle+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngTimeData=function(engTimeTitle, startDate, endDate){
		var defer = $q.defer();
		$http.get('/getEngTimeData/'+engTimeTitle+"/"+startDate+"/"+endDate).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getEngActionData=function(engActionTitle, startDateFilter, endDateFilter){
		var defer = $q.defer();
		$http.get('/getEngActionData/'+engActionTitle+"/"+startDateFilter+"/"+endDateFilter).success(function(data){
			defer.resolve(data);
		});
		return defer.promise;
	};
})