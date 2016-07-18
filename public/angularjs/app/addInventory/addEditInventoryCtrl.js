
angular.module('newApp')
.controller('addInventoryCtrl', ['$scope','$http','$location','$upload','$rootScope','apiserviceAddEditInventory', function ($scope,$http,$location,$upload,$rootScope,apiserviceAddEditInventory) {
  
	apiserviceAddEditInventory.getCustomizationform('Inventory').then(function(success){
		$scope.editInput = success;
		 $scope.userFields = $scope.addFormField(angular.fromJson(success.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
	});
	
	apiserviceAddEditInventory.getColl().then(function(success){
		$scope.collectionList = success[0];
		 console.log($scope.collectionList);
	});
	
	
	
   var pdffile;
	
		
   $scope.specification = {};
   

   
   $scope.saveVehicle = function() {
	   
	   if($rootScope.fileCustom != undefined){
     		
     		pdffile = $rootScope.fileCustom;
     		apiserviceAddEditInventory.saveInventory($scope.specification).then(function(success){
 				$scope.dataBeforePdf=success;
 				apiserviceAddEditInventory.saveInventoryPdf(success,pdffile).then(function(success1){
 					
		 	  		$location.path('/editInventory/'+$scope.dataBeforePdf+"/"+true+"/"+$scope.specification.productId);
 				});
     		});
 	 	 }else{
 	 		apiserviceAddEditInventory.saveInventory($scope.specification).then(function(success){
 				$location.path('/editInventory/'+success+"/"+true+"/"+$scope.specification.productId);
 			});
 	 	 }
	   
   }
   
   $scope.addPhoto = function() {
	    $scope.customList =[];
		
		console.log($("#autocomplete").val());
		   console.log($scope.specification);
		   $scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.address_bar = $("#autocomplete").val();
			if($scope.customData.address_bar == undefined){
				delete $scope.customData.address_bar;
			}
			$scope.customData.time_range = $("#bestTimes").val();
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range;
			}
			
			console.log($scope.customData);
			console.log($scope.userFields);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.userFields, function(value1, key) {
				if(value1.key == attr){
					if(angular.isObject(value) == true){
						console.log(value);
						console.log(angular.toJson(value));
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:angular.toJson(value),
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}else{
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:value,
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}
					
				} 
			});
		   });
		
		 
		
		console.log($scope.customList);
		$scope.specification.customData = $scope.customList;
   }
   
}]);

angular.module('newApp')
.controller('EditInventoryCtrl', ['$filter','$scope','$http','$location','$routeParams','$upload','$route','apiserviceAddEditInventory', function ($filter,$scope,$http,$location,$routeParams,$upload,$route,apiserviceAddEditInventory) {
      
	$scope.userFields = [];
	$scope.customData = {};
	$scope.specification = {};
    /*  var ele = document.getElementById('loadingmanual');	
  	$(ele).hide();
	$scope.publishVehicle = function(id){
		   $http.get('/addPublicCar/'+id).success(function(data){
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle published SuccessFully",
					});
				   $location.path('/viewVehicles');
		   });
	   }*/
	
	
	
	apiserviceAddEditInventory.getCustomizationform('Inventory').then(function(success){
		$scope.editInput = success;
		 $scope.userFields = $scope.addFormField(angular.fromJson(success.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
	});
	
	apiserviceAddEditInventory.getColl().then(function(success){
		$scope.collectionList = success[0];
		$scope.getImages();
		 console.log($scope.collectionList);
	});
	
	
	$scope.photoUrl = {};
	
	$scope.init = function() {
		
		apiserviceAddEditInventory.findLocation().then(function(data){

			$scope.productId=$routeParams.productId;
			$scope.userLocationId = data;
			$scope.photoUrl.locationId=$scope.userLocationId;
			$scope.photoUrl.vin=$scope.vinForUrl;
			console.log($scope.userLocationId);
			//$scope.photoUrl=$scope.userLocationId+"/"+$scope.vinForUrl;
			 if(userRole == "Photographer"){
				 var element1 = angular.element("<form  role='form' id='dropzoneFrm' action='http://www.glider-autos.com:9889/uploadPhotos'  method='POST' class='dropzone'> <div> <input type='text'style='display: none;'name='productId' value='"+$scope.productId+"' /> <input type='text'style='display: none;'name='locationIdNew' value='"+$scope.userLocationId+"' /> </div>  <div class='fallback'><input name='file' type='file' multiple /></div></form>");
				 $("#showDiv").append(element1);
				 $scope.getImages();
				 $scope.setDropZone();
				 		//$scope.uploadPhotoUrl="http://www.glider-autos.com/uploadPhotos/"+$scope.userLocationId;	
			 }
			else{
				 var element1 = angular.element("<form role='form' id='dropzoneFrm' action='/uploadPhotos' method='POST' class='dropzone'> <div> <input type='text'style='display: none;'name='productId' value='"+$scope.productId+"' /> <input type='text'style='display: none;'name='locationIdNew' value='"+$scope.userLocationId+"' /> </div>  <div class='fallback'><input name='file' type='file' multiple /></div></form>");
				 $("#showDiv").append(element1);
				 $scope.getImages();
				 $scope.setDropZone();
			 }
		});
		
		apiserviceAddEditInventory.getInventoryById($routeParams.id).then(function(data){
			console.log(data);
			
			 $scope.specification = data;
			 $scope.customData = data.customMapData;
			 console.log($scope.customData);
			 $scope.specification.collection=data.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			 $.each($scope.customData, function(attr, value) {
				 var res = value.split("[");
					 if(res[1] != undefined){
						 console.log(JSON.parse(value));
						 $scope.customData[attr] = JSON.parse(value);
				   	  			
					 }
							
				 });
			 
			 console.log($scope.customData);
			 
		});
				
	}
	
	   
	  /* $http.get('/getMakeList').success(function(data) {
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
	   }*/
	
	$scope.gridsterOpts = {
			
			    columns: 6, // the width of the grid, in columns
			    pushing: true, // whether to push other items out of the way on move or resize
			    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
			    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
			     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
			     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
			    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
			    margins: [10, 10], // the pixel distance between each widget
			    outerMargin: true, // whether margins apply to outer edges of the grid
			    isMobile: false, // stacks the grid items if true
			    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
			    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
			    minColumns: 6, // the minimum columns the grid must have
			    minRows: 1, // the minimum height of the grid, in rows
			    maxRows: 100,
			    defaultSizeX: 1, // the default width of a gridster item, if not specifed
			    defaultSizeY: 1, // the default height of a gridster item, if not specified
			    resizable: {
				       enabled: false,
				       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
				       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
				       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
				       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
				    },
			    /* minSizeX: 1, // minimum column width of an item
			    maxSizeX: null, // maximum column width of an item
			    minSizeY: 1, // minumum row height of an item
			    maxSizeY: null, // maximum row height of an item
			   */
				    draggable: {
					       enabled: true, // whether dragging items is supported
					       handle: '.my-class', // optional selector for resize handle
					       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
					       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
					       stop: function(event, $element, widget) {
					    	   if($(event.target).html() == 'Set Default' || $(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
					    		   return;
					    	   };
					    	   for(var i=0;i<$scope.imageList.length;i++) {
					    		   delete $scope.imageList[i].description;
					    		   delete $scope.imageList[i].width;
					    		   delete $scope.imageList[i].height;
					    		   delete $scope.imageList[i].link;
					    	   } 
					    	   $http.post('/savePosition',$scope.imageList)
						   		.success(function(data) {
						   			$.pnotify({
									    title: "Success",
									    type:'success',
									    text: "Position saved successfully",
									});
						   		});
					    	   
					       } // optional callback fired when item is finished dragging
					    }
			};   
	   
	
	
	var myDropzone;
	$scope.setDropZone = function() {
		myDropzone = new Dropzone("#dropzoneFrm",{
			   parallelUploads: 30,
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.getImages();
				          $scope.$apply();
				      });
				   
				   this.on("complete", function() {
					   this.removeAllFiles();
				   });
			   }
		   });
	}
	
	
	   $scope.uploadFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone.processQueue();
		   
	   }
	
	$scope.getImages = function() {
		apiserviceAddEditInventory.getImagesByProductId($routeParams.productId,userRole).then(function(data){
			console.log("dddd");
			console.log(data);
			$scope.imageList = data;
		});
		
	}
	
	
	
	 
	
	   var pdfFile;
		/*$scope.onPdfFileSelect = function($files) {
			pdfFile = $files;
		}*/
	   
		/*$scope.removePdf = function(){
			$http.get('/removeVehiclePdf/'+$routeParams.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Pdf remove successfuly",
				});
				$route.reload();
			});
		};*/
		
		
		$scope.vinData = {};
		
		
	   
	$scope.updateInventory = function() {
		$scope.customList = [];
		console.log($("#autocomplete").val());
		   console.log($scope.specification);
		   $scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.address_bar = $("#autocomplete").val();
			if($scope.customData.address_bar == undefined){
				delete $scope.customData.address_bar;
			}
			$scope.customData.time_range = $("#bestTimes").val();
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range;
			}
			
			console.log($scope.customData);
			console.log($scope.userFields);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.userFields, function(value1, key) {
				if(value1.key == attr){
					if(angular.isObject(value) == true){
						console.log(value);
						console.log(angular.toJson(value));
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:angular.toJson(value),
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}else{
						$scope.customList.push({
			   	  			key:attr,
			   	  			value:value,
			   	  			savecrm:value1.savecrm,
			   	  			displayGrid:value1.displayGrid,
			   	  			
						});
					}
				} 
			});
		   });
		
		 
		
		console.log($scope.customList);
		$scope.specification.customData = $scope.customList;
		
		   
				if(pdfFile != undefined){
					
					apiserviceAddEditInventory.updateInventoryById($scope.specification).then(function(data){
							
							apiserviceAddEditInventory.updateVehicleByIdPdf($scope.specification.id,pdffile).then(function(success1){
								
							});
						 			
			 		});
					
			 	 }else{
			 		apiserviceAddEditInventory.updateInventoryById($scope.specification).then(function(data){
						$scope.isUpdated = true;
						
					});
			 	 }
		  
	}
	
	$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$('#imgId'+i).css("border","3px solid");
				$('#imgId'+i).css("color","red");
			}
		}
	});
	
	$scope.setAsDefault = function(image,index) {
		
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				apiserviceAddEditInventory.removeDefault($scope.imageList[i].id,image.id).then(function(data){
				});
				
				$('#imgId'+i).removeAttr("style","");
				$scope.imageList[i].defaultImage = false;
				image.defaultImage = true;
				$('#imgId'+index).css("border","3px solid");
				$('#imgId'+index).css("color","red");
				break;
			}
		}
		
		if(i == $scope.imageList.length) {
			apiserviceAddEditInventory.setDefaultImage(image.id).then(function(data){
				
			});
						
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
	}
	
	$scope.deleteImage = function(img) {
		
		apiserviceAddEditInventory.deleteInventoryImage(img.id,userRole).then(function(data){
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	/*$scope.updateVehicleStatus = function(){
		   $http.get('/updateVehicleStatus/'+$routeParams.id+'/'+"Sold")
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status saved successfully",
				});
			});
	   }*/
	
	/*$scope.deleteVehicle = function(){
		 $('#deleteModal').click();
		   
	   }*/
	
	/*$scope.deleteVehicleRow = function() {
		$http.get('/deleteVehicleById/'+$routeParams.id)
		.success(function(data) {
			$location.path('/addInventory');
		});
	}*/
	
	var file;
	$scope.onFileSelect = function($files) {
		file = $files;
	}
	
	
	
	$scope.confirmFileDelete = function(id) {
		$scope.audioFileId = id;
		$('#deleteModal2').click();
	}
	
	/*$scope.deleteAudioFile = function() {
		$http.get('/deleteAudioFile/'+$scope.audioFileId)
		.success(function(data) {
			$scope.getAllAudio();
		});
	}*/
	
	
	
	
	$scope.editImage = function(image) {
		$location.path('/cropInventoryImages/'+image.id+'/'+$routeParams.id+'/'+$routeParams.productId);
	}
	
}]);	
	