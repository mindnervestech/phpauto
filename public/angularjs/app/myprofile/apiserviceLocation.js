angular.module('app.apiserviceLocation', [])
.service('apiserviceLocation', function($http,$q,$upload){

	var autodealerTestUrl = 'http://www.glider-autos.com:7071/';

	this.getLocationForGM=function(){
		var defer = $q.defer();
		$http.get('/getLocationForGM').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getLocationValueForGM = function(){
		var defer = $q.defer();
		$http.get('/getLocationValueForGM').success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	
	
	this.deactiveLocationById = function(id){
		var defer = $q.defer();
		$http.get('/deactiveLocationById/'+id).success(function(data){
			 	defer.resolve(data);
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Location deactive succesfully!",
				});
    	});
		return defer.promise;
	};
	
	
	this.uploadLocationImageFile = function(logofile, locationObj){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile)) {
			$http.post('/uploadLocationImageFile',locationObj).success(function(data) {
			 	defer.resolve(data);
			 	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
    	   });
		}else{
			$upload.upload({
	            url : '/uploadLocationImageFile',
	            method: 'post',
	            file:logofile,
	            data:locationObj
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
	
	this.uploadManagerImageFile = function(logofile, managerObj){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile)) {
			$http.post('/uploadManagerImageFile',managerObj).success(function(data) {
			 	defer.resolve(data);
			 	  $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
    	   });
		}else{
			$upload.upload({
	            url : '/uploadManagerImageFile',
	            method: 'post',
	            file:logofile,
	            data:managerObj
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
	
	this.updateUploadLocationImageFile = function(logofile1, locationObj){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile1)) {
			$http.post('/updateUploadLocationImageFile',locationObj).success(function(data) {
			 	defer.resolve(data);
			 	  $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "User saved successfully",
					});
    	   });
		}else{
			$upload.upload({
	            url : '/updateUploadLocationImageFile',
	            method: 'post',
	            file:logofile1,
	            data:locationObj
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
	
	
	this.UpdateuploadManagerImageFile = function(logofile, managerObj){
		var defer = $q.defer();
		
		if(angular.isUndefined(logofile)) {
			$http.post('/UpdateuploadManagerImageFile',managerObj).success(function(data) {
				 if(data != "1"){
		            	$.pnotify({
						    title: "Error",
						    type:'Success',
						    text: "Please Complete all leads Before Deactivet Location...!!",
						});
		            }else{
		            	$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Manager saved successfully",
						});
		            }
			 	defer.resolve(data);
			 	  
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
	
	
	this.checkEmailOfUser = function(email){
		var defer = $q.defer();
		$http.get('/checkEmailOfUser/'+email).success(function(data){
			 	defer.resolve(data);
    	});
		return defer.promise;
	};
	
	
	this.getDeactiveLocationForGM=function(){
		var defer = $q.defer();
		$http.get('/getDeactiveLocationForGM').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
})
