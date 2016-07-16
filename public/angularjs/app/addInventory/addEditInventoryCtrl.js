
angular.module('newApp')
.controller('addInventoryCtrl', ['$scope','$http','$location','$upload','$rootScope', function ($scope,$http,$location,$upload,$rootScope) {
  
	  
	$http.get('/getCustomizationform/'+'Inventory').success(function(response) {
		console.log(response);
		 $scope.editInput = response;
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
		});
	
	
	$http.get('/getColl').success(function(response) {
		 $scope.collectionList = response[0];
		 console.log($scope.collectionList);
	});
	
	
   var pdffile;
	
		
   $scope.specification = {};
   

   
   $scope.saveVehicle = function() {
	   
	   if($rootScope.fileCustom != undefined){
     		
     		pdffile = $rootScope.fileCustom;
     		
 	 		$http.post('/saveInventory',$scope.specification)
 			.success(function(data) {
 				$.pnotify({
 				    title: "Success",
 				    type:'success',
 				    text: "Inventory saved successfully",
 				});
 				
 				$scope.dataBeforePdf=data;
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
 		 	  		$location.path('/editInventory/'+$scope.dataBeforePdf+"/"+true+"/"+$scope.specification.productId);
 		 	      });
 			});
 	 	 }else{
 	 		$http.post('/saveInventory',$scope.specification)
 			.success(function(data) {
 				$.pnotify({
 				    title: "Success",
 				    type:'success',
 				    text: "Inventory saved successfully",
 				});
 				$location.path('/editInventory/'+data+"/"+true+"/"+$scope.specification.productId);
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
.controller('EditInventoryCtrl', ['$filter','$scope','$http','$location','$routeParams','$upload','$route', function ($filter,$scope,$http,$location,$routeParams,$upload,$route) {
      
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
	
	
	$http.get('/getColl').success(function(response) {
		 $scope.collectionList = response[0];
		
		 $scope.getImages();
		 console.log($scope.collectionList);
	});
	
	$http.get('/getCustomizationform/'+'Inventory').success(function(response) {
		 $scope.editInput = response;
		 //$scope.josnData = angular.fromJson(response.jsonData);
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
		});
	
	$scope.photoUrl = {};
	
	$scope.init = function() {
		
		
		$http.get('/findLocation')
		.success(function(data) {
			console.log($routeParams.productId);
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
		
		
		$http.get('/getVehicleById/'+$routeParams.id)
		.success(function(data) {
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
				    	   /*$http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			$.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Position saved successfully",
								});
					   		});*/
				    	   
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
		
		if(userRole == "Photographer"){
			 $http.get('http://www.glider-autos.com:9889/getImagesByProductId/'+$routeParams.productId)
				.success(function(data) {
					console.log("dddd");
					console.log(data);
					$scope.imageList = data;
				});
			 }
			 else{
				 $http.get('getImagesByProductId/'+$routeParams.productId)
					.success(function(data) {
						console.log("dddd");
						console.log(data);
						$scope.imageList = data;
					});
			 }
		
	}
	
	
	
	  /* $scope.setSiteId = function(id,flag) {
	 	  if(flag == true) {
	 		 $scope.vinData.specification.siteIds.push(id);
	 	  } 
	 	  if(flag == false) {
	 		 $scope.vinData.specification.siteIds.splice($scope.vinData.specification.siteIds.indexOf(id),1);
	 	  }
	 	  
	   };*/
	
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
		
		/*$scope.dataShow = function(check){
			if(check == undefined){
				$('#comingsoonDateEdit').val('');
			}
			
			if(check == true){
				
				$('#comingsoonDateEdit').val('');
			}
		}	*/
	   
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
					$http.post('/updateInventoryById',$scope.specification)
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Inventory updated successfuly",
						});
						
						$upload.upload({
				 	         url : '/updateVehicleByIdPdf/'+$scope.specification.id,
				 	         method: 'POST',
				 	         file:pdfFile,
				 	      }).success(function(data) {
				 	  			
				 	      });
					});
			 	 }else{
			 		$http.post('/updateInventoryById',$scope.specification)
					.success(function(data) {
						$scope.isUpdated = true;
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Inventory updated successfuly",
						});
						
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
				$http.get('/removeDefault/'+$scope.imageList[i].id+'/'+image.id)
				.success(function(data) {
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
			$http.get('/setDefaultImage/'+image.id)
			.success(function(data) {
			});
			
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
		
	}
	
	$scope.deleteImage = function(img) {
		
		
		if(userRole == "Photographer"){	
			  	$http.get('http://www.glider-autos.com:9889/deleteInventoryImage/'+img.id)
			  	.success(function(data) {
			  			$scope.imageList.splice($scope.imageList.indexOf(img),1);
			  	});
		}else{
					
		    	 $http.get('/deleteImage/'+img.id)
					.success(function(data) {
						$scope.imageList.splice($scope.imageList.indexOf(img),1);
					});
					
		}
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	$scope.updateVehicleStatus = function(){
		   $http.get('/updateVehicleStatus/'+$routeParams.id+'/'+"Sold")
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status saved successfully",
				});
			});
	   }
	
	$scope.deleteVehicle = function(){
		 $('#deleteModal').click();
		   
	   }
	
	$scope.deleteVehicleRow = function() {
		$http.get('/deleteVehicleById/'+$routeParams.id)
		.success(function(data) {
			$location.path('/addInventory');
		});
	}
	
	var file;
	$scope.onFileSelect = function($files) {
		file = $files;
	}
	
	/*$scope.uploadAudio = function() {
		$upload.upload({
            url : '/uploadSoundFile',
            method: 'post',
            file:file,
            data:{"vinNum":$scope.vinData.specification.vin}
        }).success(function(data, status, headers, config) {
            $scope.getAllAudio();
            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
        });
	}*/
	
	$scope.confirmFileDelete = function(id) {
		$scope.audioFileId = id;
		$('#deleteModal2').click();
	}
	
	$scope.deleteAudioFile = function() {
		$http.get('/deleteAudioFile/'+$scope.audioFileId)
		.success(function(data) {
			$scope.getAllAudio();
		});
	}
	
	/*$scope.getAllAudio = function() {
		$http.get('/getAllAudio/'+$scope.vinData.specification.vin)
		.success(function(data) {
			$scope.audioList = data;
		});
	}*/
	/*$scope.vData = {};
	$scope.videoData={};
	$scope.getVirtualTourData = function() {
		$http.get('/getVirtualTour/'+$scope.vinData.specification.id)
		.success(function(data) {
			
			$scope.vData = data.virtualTour;
			$scope.videoData = data.video;
		});
	}*/
	
	/*$scope.saveVData = function() {
		
		$scope.vData.vin = $scope.vinData.specification.vin;
		$scope.vData.vehicleId = $scope.vinData.specification.id;
		$http.post('/saveVData',$scope.vData)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
		});
	}*/
	
	
	/*$scope.saveVideoData = function() {
		
		$scope.videoData.vin = $scope.vinData.specification.vin;
		$scope.videoData.vehicleId = $scope.vinData.specification.id;
		$http.post('/saveVideoData',$scope.videoData)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
		});
	}*/
	
	
	$scope.editImage = function(image) {
		$location.path('/cropInventoryImages/'+image.id+'/'+$routeParams.id+"/"+$routeParams.productId);
	}
	
}]);	
	