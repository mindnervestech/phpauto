angular.module('app.apiserviceProfile', [])
.service('apiserviceProfile', function($http,$q,$upload){

	var autodealerTestUrl = 'http://www.glider-autos.com:7071/';

	this.getUserRole=function(){
		var defer = $q.defer();
		$http.get('/getUserRole').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getDealerProfile = function(){
		var defer = $q.defer();
		$http.get('/getDealerProfile').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	this.getMyProfile = function(){
		var defer = $q.defer();
		$http.get('/getMyProfile').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	
	this.getSaleHourData = function(){
		var defer = $q.defer();
		$http.get('/getSaleHourData').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	this.getSaleHourDataForService = function(){
		var defer = $q.defer();
		$http.get('/getSaleHourDataForService').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	this.getSaleHourDataForParts = function(){
		var defer = $q.defer();
		$http.get('/getSaleHourDataForParts').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	this.myprofile = function(logofile1, myprofile){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile1)) {
			$http.post('/myprofile',myprofile).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Location saved successfully",
				});
    	   });
		}else{
			$upload.upload({
	            url : '/myprofile',
	            method: 'post',
	            file:logofile1,
	            data:myprofile
	        }).success(function(data, status, headers, config) {
	        	defer.resolve(data);
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Location saved successfully",
				});
	            
	        });
		}
		
		return defer.promise;
	};
	
	this.saveSalesHoursForParts = function(operation2){
		var defer = $q.defer();
		
			$http.post('/saveSalesHoursForParts',operation2).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: " saved successfully",
				});
    	   });
	
		
		return defer.promise;
	};
	
	this.saveHours = function(operation){
		var defer = $q.defer();
		
			$http.post('/saveHours',operation).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: " saved successfully",
				});
    	   });
	
		
		return defer.promise;
	};
	
	this.checkForService=function(checkForServiceValue){
		var defer = $q.defer();
		$http.get('/checkForService/'+checkForServiceValue).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.checkForServiceForPart=function(checkForServiceValue){
		var defer = $q.defer();
		$http.get('/checkForServiceForPart/'+checkForServiceValue).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.saveSalesHoursForService = function(operation1){
		var defer = $q.defer();
		
			$http.post('/saveSalesHoursForService',operation1).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: " saved successfully",
				});
    	   });
		
		return defer.promise;
	};
	
	this.updateImageFile = function(logofile, user){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile)) {
			$http.post('/updateImageFile',user).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
    	   });
		}else{
			$upload.upload({
	            url : '/updateImageFile',
	            method: 'post',
	            file:logofile,
	            data:user
	        }).success(function(data, status, headers, config) {
	        	defer.resolve(data);
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            
	        });
		}
		
		return defer.promise;
	};
	
	this.getMangerAndLocation = function(){
		var defer = $q.defer();
		
		$http.get('/getMangerAndLocation').success(function(data) {
			 	defer.resolve(data);
    	   });
		
		return defer.promise;
	};
	
	
	this.UpdateuploadManagerImageFile = function(logofile, managerObj){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile)) {
			$http.post('/UpdateuploadManagerImageFile',managerObj).success(function(data) {
			 	defer.resolve(data);
			 	  $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Manager saved successfully",
					});
    	   });
		}else{
			$upload.upload({
	            url : '/UpdateuploadManagerImageFile',
	            method: 'post',
	            file:logofile,
	            data:managerObj
	        }).success(function(data, status, headers, config) {
	        	defer.resolve(data);
	        	  $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Manager saved successfully",
					});
	            
	        });
		}
		
		return defer.promise;
	};
	
})
