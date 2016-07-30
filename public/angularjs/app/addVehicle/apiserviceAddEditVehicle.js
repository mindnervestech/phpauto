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
	
	this.getAllCollectionData=function(){
		var defer = $q.defer();
		$http.get('/getAllCollectionData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getCustomizationform=function(typeLead){
		var defer = $q.defer();
		$http.get('/getCustomizationform/'+typeLead).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getMakeList=function(){
		var defer = $q.defer();
		$http.get('/getMakeList').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getModelList=function(title){
		var defer = $q.defer();
		$http.get('/getModelList/'+title).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrimList=function(title){
		var defer = $q.defer();
		$http.get('/getTrimList/'+title).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.checkStockNumber=function(checkStockNumber){
		var defer = $q.defer();
		$http.get('/checkStockNumber/'+checkStockNumber).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVehicleInfo=function(vinNumber){
		var defer = $q.defer();
		$http.get('/getVehicleInfo/'+vinNumber).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.saveVehicle = function(data){
		var defer = $q.defer();
		$http.post('/saveVehicle',data).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle saved successfully",
				});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.saveVehiclePdf = function(data,pdffile){
		var defer = $q.defer();
		$upload.upload({
 	         url : '/saveVehiclePdf/'+data,
 	         method: 'POST',
 	         file:pdffile,
 	      }).success(function(data) {
 	    	 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle saved successfully",
				});
			 	defer.resolve(data);
 	      });
		
		return defer.promise;
	};
	
	this.addPublicCar=function(id){
		var defer = $q.defer();
		$http.get('/addPublicCar/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle published SuccessFully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.findLocation=function(){
		var defer = $q.defer();
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVehicleById=function(id){
		var defer = $q.defer();
		$http.get('/getVehicleById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getModelList=function(make){
		var defer = $q.defer();
		$http.get('/getModelList/'+make).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getTrimList=function(model){
		var defer = $q.defer();
		$http.get('/getTrimList/'+model).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getPriceHistory=function(vin){
		var defer = $q.defer();
		$http.get('/getPriceHistory/'+vin).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getImagesByVin=function(vin, userRole){
		var defer = $q.defer();
		if(userRole == "Photographer"){
			$http.get('http://www.glider-autos.com/getImagesByVin/'+vin).success(function(data) {
				defer.resolve(data);
			});
		}else{
			$http.get('/getImagesByVin/'+vin).success(function(data) {
				defer.resolve(data);
			});
		}
		
		return defer.promise;
	};
	
	this.removeVehiclePdf=function(id){
		var defer = $q.defer();
		$http.get('/removeVehiclePdf/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Pdf remove successfuly",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.updateVehicleById = function(data){
		var defer = $q.defer();
		$http.post('/updateVehicleById',data).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle updated successfuly",
				});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.updateVehicleByIdPdf = function(data,pdffile){
		var defer = $q.defer();
		$upload.upload({
	         url : '/updateVehicleByIdPdf/'+$scope.vinData.specification.id,
	         method: 'POST',
	         file:pdfFile,
	      }).success(function(data) {
	    	  defer.resolve(data);
	      });
		
		return defer.promise;
	};
	
	
	this.isCarPublic=function(id){
		var defer = $q.defer();
		$http.get('/isCarPublic/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.removeDefault=function(listId,id){
		var defer = $q.defer();
		$http.get('/removeDefault/'+listId+"/"+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setDefaultImage=function(id){
		var defer = $q.defer();
		$http.get('/setDefaultImage/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteImage=function(id, userRole){
		var defer = $q.defer();
		if(userRole == "Photographer"){
			$http.get('http://www.glider-autos.com/deleteImage/'+id).success(function(data) {
				defer.resolve(data);
			});
		}else{
			$http.get('/deleteImage/'+id).success(function(data) {
				defer.resolve(data);
			});
		}
		return defer.promise;
	};
	
	this.updateVehicleStatus=function(id,status){
		var defer = $q.defer();
		$http.get('/updateVehicleStatus/'+id+"/"+status).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Status saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteVehicleById=function(id){
		var defer = $q.defer();
		$http.get('/deleteVehicleById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.uploadSoundFile = function(file,vins){
		var defer = $q.defer();
		$upload.upload({
            url : '/uploadSoundFile',
            method: 'post',
            file:file,
            data:vins
        }).success(function(data, status, headers, config) {
            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
        });
		
		return defer.promise;
	};
	
	
	this.deleteAudioFile=function(id){
		var defer = $q.defer();
		$http.get('/deleteAudioFile/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllAudio=function(vin){
		var defer = $q.defer();
		$http.get('/getAllAudio/'+vin).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVirtualTour=function(id){
		var defer = $q.defer();
		$http.get('/getVirtualTour/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.saveVData = function(data){
		var defer = $q.defer();
		$http.post('/saveVData',data).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.saveVideoData = function(data){
		var defer = $q.defer();
		$http.post('/saveVideoData',data).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
})
