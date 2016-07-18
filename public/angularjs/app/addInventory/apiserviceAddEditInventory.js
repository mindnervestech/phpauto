angular.module('app.apiserviceAddEditInventory', [])
.service('apiserviceAddEditInventory', function($http,$q,$upload){

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
	
	this.getColl = function(){
		var defer = $q.defer();
		$http.get('/getColl').success(function(response) {
			 	defer.resolve(response);
    	});
		
		return defer.promise;
	};
	
	
	this.saveInventory = function(data){
		var defer = $q.defer();
		$http.post('/saveInventory',data).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Inventory saved successfully",
				});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.saveInventoryPdf = function(data,pdffile){
		var defer = $q.defer();
		$upload.upload({
 	         url : '/saveInventoryPdf/'+data,
 	         method: 'POST',
 	         file:pdffile,
 	      }).success(function(data) {
 	    	 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Inventory saved successfully",
				});
			 	defer.resolve(data);
 	      });
		
		return defer.promise;
	};
	
	
	this.findLocation = function(){
		var defer = $q.defer();
		$http.get('/findLocation').success(function(response) {
			 	defer.resolve(response);
    	});
		
		return defer.promise;
	};
	
	this.getInventoryById = function(id){
		var defer = $q.defer();
		$http.get('/getInventoryById/'+id).success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getImagesByProductId = function(productId,userRole){
		var defer = $q.defer();
		if(userRole == "Photographer"){
			$http.get(mavenUrl+'getImagesByProductId/'+productId).success(function(data) {
	 			defer.resolve(data);
			});
		}else{
			$http.get('getImagesByProductId/'+productId).success(function(data) {
		 		defer.resolve(data);
			});
		}	
		return defer.promise;
	};
	
	
	this.updateInventoryById = function(data){
		var defer = $q.defer();
		$http.post('/updateInventoryById',data).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Inventory updated successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.updateVehicleByIdPdf = function(data,pdffile){
		var defer = $q.defer();
		$upload.upload({
 	         url : '/updateVehicleByIdPdf/'+data,
 	         method: 'POST',
 	         file:pdffile,
 	      }).success(function(data) {
 	    	 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Inventory updated successfully",
				});
			 	defer.resolve(data);
 	      });
		
		return defer.promise;
	};
	
	this.setDefaultImage = function(imgId){
		var defer = $q.defer();
		$http.get('/setDefaultImage/'+imgId).success(function(response) {
			 	defer.resolve(response);
    	});
		
		return defer.promise;
	};
	
	
	
	this.deleteInventoryImage = function(imgId,userRole){
		var defer = $q.defer();
		if(userRole == "Photographer"){	
			$http.get(mavenUrl+'deleteInventoryImage/'+imgId).success(function(response) {
				defer.resolve(response);
			});
		}else{
			$http.get('/deleteInventoryImage/'+imgId).success(function(response) {
			 	defer.resolve(response);
			});
		}
		return defer.promise;
	};
	
	
	this.removeDefault = function(listId, imgId){
		var defer = $q.defer();
	
			$http.get('/removeDefault/'+listId+'/'+imgId).success(function(response) {
			 	defer.resolve(response);
			});
		return defer.promise;
	};
	
})
