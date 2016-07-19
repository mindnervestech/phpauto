angular.module('newApp')
.controller('addVehicleCtrl', ['$scope','$http','$location','$upload', function ($scope,$http,$location,$upload) {
  
	$scope.vinErr = false;
   $scope.vehicleInit = function() {
 	  $http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
 	 $http.get('/getDealerProfile').success(function(data) {
 		
 		$scope.vinData.specification.location = data.dealer.address;
 	});
   }
   $scope.makeList = [];
   $scope.modelList = [];
   $scope.trimList = [];
   $scope.labelList = [];
   $scope.madeInList = [];
   $scope.stereoList = [];
   $scope.driveTypeList = [];
   $scope.fuelTypeList = [];
   $scope.exteriorColorList = [];
   
   $http.get('/getMakeList').success(function(data) {
		$scope.labelList = data.label;
		$scope.makeList = data.make;
		$scope.madeInList = data.madeIn;
		$scope.stereoList = data.stereo;
		$scope.driveTypeList = data.driveType;
		$scope.fuelTypeList = data.fuelType;
		$scope.exteriorColorList = data.exteriorColor;
	});
   $scope.selectedMake = function (selectObj) {
		if(selectObj != undefined){
			$scope.vinData.specification.make = selectObj.title;
			$http.get('/getModelList/'+selectObj.title)
			.success(function(data) {
				$scope.modelList = data;
			});
		}
	};
	$scope.selectedModel = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.model = selectObj.title;
				$http.get('/getTrimList/'+selectObj.title)
				.success(function(data) {
					$scope.trimList = data;
				});
			}
	};
	$scope.selectedTrim = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.trim_level = selectObj.title;
			}
	};  
	$scope.selectedLabel = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.label = selectObj.title;
			}
	};	
   $scope.siteIds = [];
   $scope.setSiteId = function(id,flag) {
 	  if(flag == true) {
 		  $scope.siteIds.push(id);
 	  } 
 	  if(flag == false) {
 		  $scope.siteIds.splice($scope.siteIds.indexOf(id),1);
 	  }
 	  
   };
   $scope.selectedFuelType = function (selectObj) {
		if(selectObj != undefined){
			$scope.vinData.specification.fuelType = selectObj.title;
		}
   };
   $scope.fuelFocusOut = function(){
	   $scope.vinData.specification.fuelType = $('#fuelTypeSearch_value').val();
   }
   $scope.fuelTypeChange = function(){
	   $('#fuelTypeSearch_value').val($scope.vinData.specification.fuelType);
   }
   var pdffile;
		$scope.onLogoFileSelect = function($files) {
			pdffile = $files;
		}
   
   $scope.checkStock = function(stock){
	   if(stock == undefined){
		   $.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please Enter Unique Stock Number",
			});
	   }else{
		   $http.get('/checkStockNumber/'+stock)
			.success(function(data) {
				if(data != 0){
					$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Please Enter Unique Stock Number",
					});
					$scope.vinData.specification.stock = null;
				}
			});
	   }
   }
		
   $scope.vinData = {};
   $scope.vinData.specification = {};
   $scope.vinData.specification.typeofVehicle = "New";
   
   $scope.getVinData = function() {
	   if(!angular.isUndefined($scope.vinNumber)) {
	 	  $http.get('/getVehicleInfo/'+$scope.vinNumber)
			.success(function(data) {
				if(data.success == true) {
					$scope.vinData = data;
					if($scope.vinData.specification.siteIds != null) {
						for(var i=0;i<$scope.vinData.specification.siteIds.length;i++) {
							for(var j=0;j<$scope.siteList.length;j++) {
								if($scope.vinData.specification.siteIds[i] == $scope.siteList[j].id) {
									$scope.siteList[j].flag = true;
								}
							}
						}
					}
				}
				
				if(data.success == false) {
					$scope.vinErr = true;
				} else {
					$scope.vinErr = false;
				}
				
			});
	   }
   }
   $scope.dataShow1 = function(check){
		$scope.vinData.specification.price = 0;
	}
   
   
   $scope.saveVehicle = function() {
	   $scope.vinData.specification.model = $('#modelSearch_value').val();
	   $scope.vinData.specification.make = $('#makeSearch_value').val();
	   $scope.vinData.specification.trim_level = $('#trimSearch_value').val();
	   $scope.vinData.specification.label = $('#labelSearch_value').val();
	   
	   $scope.vinData.specification.made_in = $('#madeInSearch_value').val();
	   $scope.vinData.specification.extColor = $('#extColorSearch_value').val();
	   $scope.vinData.specification.stereo = $('#stereoSearch_value').val();
	   $scope.vinData.specification.drivetrain = $('#driveTypeSearch_value').val();
	  /* $scope.vinData.specification.fuelType = $('#fuelTypeSearch_value').val();*/
		   
		   $scope.vinData.specification.cost = $scope.vinData.specification.cost.split(',').join('');
		   var comingSoonDate = {};
	  if($scope.vinData.specification.commingSoonVehicle == true){
		  $scope.vinData.specification.comingSoonDate = $('#comingsoonDate').val();
		  
	  }
 	  $scope.vinData.specification.siteIds = $scope.siteIds;
 	  
 	  
 	  var saveFlag = 0;
 	  
 	 if($scope.vinData.specification.commingSoonVehicle == true){
 		  if($scope.vinData.specification.price != null && $scope.vinData.specification.price != ''){
 			  $scope.vinData.specification.price = $scope.vinData.specification.price.split(',').join('');
 		  }else{
 			 $scope.vinData.specification.price = 0;
 		  }
 		 saveFlag = 1;
 	  }else{
 		  if($scope.vinData.specification.price == null || $scope.vinData.specification.price == '' || $scope.vinData.specification.price == undefined){
 			 $.pnotify({
 			    title: "Error",
 			    type:'success',
 			    text: "Price fields required",
 			});
 			saveFlag = 0;
 		  }else{
 			 $scope.vinData.specification.price = $scope.vinData.specification.price.split(',').join('');
 		     saveFlag = 1;
 		  }
 		 
 	  }
 	  
 	 if(saveFlag == 1){
 		if(($scope.vinData.specification.model != null && $scope.vinData.specification.model != "") && ($scope.vinData.specification.make != null && $scope.vinData.specification.make != " ")){
 	 		 var ele = document.getElementById('loadingmanual');	
 	     	$(ele).show();
 	 		  if(pdffile != undefined){
 	 	 		$http.post('/saveVehicle',$scope.vinData.specification)
 	 			.success(function(data) {
 	 				$.pnotify({
 	 				    title: "Success",
 	 				    type:'success',
 	 				    text: "Vehicle saved successfully",
 	 				});
 	 				
 	 				$scope.dataBeforePdf=data;
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
 	 		 	  		$location.path('/editVehicle/'+$scope.dataBeforePdf+"/"+true);
 	 		 	      });
 	 			});
 	 	 	 }else{
 	 	 		$http.post('/saveVehicle',$scope.vinData.specification)
 	 			.success(function(data) {
 	 				$.pnotify({
 	 				    title: "Success",
 	 				    type:'success',
 	 				    text: "Vehicle saved successfully",
 	 				});
 	 				$location.path('/editVehicle/'+data+"/"+true);
 	 			});
 	 	 	 }
 	 	  }else{
 	 		 $.pnotify({
 				    title: "Error",
 				    type:'success',
 				    text: "Please select all fields",
 				});
 	 	  }
 	 }
 	  
 	 /* */
 	  
   }
   
   $scope.setFlagVal = function() {
	   $scope.flagVal = false;
   }
   
   $scope.addPhoto = function() {
	 $scope.flagVal = true;
	   
   }
   
}]);