'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */
angular.module('newApp')
  .controller('dashboardCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http', function ($scope, dashboardService, pluginsService,$http) {
      $scope.$on('$viewContentLoaded', function () {
          dashboardService.init();
          pluginsService.init();
          dashboardService.setHeights()
          if ($('.widget-weather').length) {
              widgetWeather();
          }
          handleTodoList();
      });

      $scope.activeTab = true;

  }]);

angular.module('newApp')
.controller('addVehicleCtrl', ['$scope','$http','$location', function ($scope,$http,$location) {
  
	$scope.vinErr = false;
   $scope.vehicleInit = function() {
 	  $http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
   }
   
   $scope.siteIds = [];
   $scope.setSiteId = function(id,flag) {
 	  if(flag == true) {
 		  $scope.siteIds.push(id);
 	  } 
 	  if(flag == false) {
 		  $scope.siteIds.splice($scope.siteIds.indexOf(id),1);
 	  }
 	  
   };
   
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
   
   $scope.saveVehicle = function() {
 	  console.log($scope.vinData);
 	  $scope.vinData.specification.siteIds = $scope.siteIds;
 	  
 	  $http.post('/saveVehicle',$scope.vinData.specification)
		.success(function(data) {
			console.log('success');
		});
 	  
 	  if($scope.flagVal == true) {
 		 $location.path('/addPhoto/'+$scope.vinData.specification.vin);
 	  }
 	  
   }
   
   $scope.setFlagVal = function() {
	   $scope.flagVal = false;
   }
   
   $scope.addPhoto = function() {
	 $scope.flagVal = true;
	   
   }
   
}]);


angular.module('newApp')
.controller('PhotoUploadCtrl', ['$scope','$routeParams','$location', function ($scope,$routeParams,$location) {
	console.log($routeParams.num);
   var myDropzone = new Dropzone("#dropzoneFrm",{
	   parallelUploads: 30,
	   headers: { "vinNum": $routeParams.num },
	   acceptedFiles:"image/*",
	   addRemoveLinks:true,
	   autoProcessQueue:false,
	   init:function () {
		   this.on("queuecomplete", function (file) {
		          
		          $location.path('/managePhotos/'+$routeParams.num);
		          $scope.$apply();
		      });
	   }
   });
   $scope.uploadFiles = function() {
	   Dropzone.autoDiscover = false;
	   myDropzone.processQueue();
	   
   }
   
}]);

angular.module('newApp')
.controller('ManagePhotoCtrl', ['$scope','$routeParams','$location','$http','$timeout', function ($scope,$routeParams,$location,$http,$timeout) {
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
				    	   console.log(event);
				    	   //console.log($element[0].getAttribute("data-row")+" "+$element[0].getAttribute("data-col")+" "+$element[0].getAttribute("id"));
				    	   $http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			//console.log('success');
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	$scope.imageList = [];
	$scope.init = function() {
		 $timeout(function(){
			$http.get('/getImagesByVin/'+$routeParams.num)
			.success(function(data) {
				console.log(data);
				$scope.imageList = data;
			});
		 }, 3000);
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
				console.log('success');
			});
			
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
		
	}
	
	$scope.deleteImage = function(img) {
		$http.get('/deleteImage/'+img.id)
		.success(function(data) {
			console.log('success');
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	$scope.editImage = function(image) {
		$location.path('/cropImage/'+image.id);
	}
	
}]);

angular.module('newApp')
.controller('ViewVehiclesCtrl', ['$scope','$http','$location','$filter', function ($scope,$http,$location,$filter) {
  
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75],
    		    paginationPageSize: 10,
    		    enableFiltering: true,
    		    useExternalFiltering: true,
    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 $scope.gridOptions.columnDefs = [
    		                                 { name: 'category', displayName: 'Vehicle', width:'15%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'stock', displayName: 'Stock',enableFiltering: false, width:'10%',
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'intColor', displayName: 'Int Color',enableFiltering: false, width:'10%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'extColor', displayName: 'Ext Color',enableFiltering: false, width:'10%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'city_mileage', displayName: 'City Mileage',enableFiltering: false, width:'13%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'highway_mileage', displayName: 'Hghway Mileage',enableFiltering: false, width:'14%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false, width:'8%',cellEditableCondition: false,
    		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                		 if(row.entity.status == "Sold") {
    		                                           return 'green';
    		                                		 }
    		                                       }
    		                                 },
    		                                 { name: 'edit', displayName: '', width:'10%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editVehicle(row)"  title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-check" ng-click="grid.appScope.updateVehicleStatus(row)"  title="Sold"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)"></i>', 
        		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		                                		 if(row.entity.status == "Sold") {
        		                                           return 'green';
        		                                		 }
        		                                       } 
    		                                 
    		                                 },
        		                                
        		                                 ];  
     
    		 $scope.gridOptions.onRegisterApi = function(gridApi){
    			 $scope.gridApi = gridApi;
    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    			 $scope.rowData = rowEntity;
    			 $scope.$apply();
    				 var str = $scope.rowData.price.split(" ");
    				 $scope.rowData.price = str[1];
    			 $http.post('/updateVehicle',$scope.rowData)
    			 .success(function(data) {
    					console.log('success');
    					$scope.rowData.price = "$ "+$scope.rowData.price;
    				});
    			 });
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'category':grid.columns[0].filters[0].term},undefined);
    		        });
    			 
    			 
    			 
    			 };

    	$scope.editVehicle = function(row) {
    		$location.path('/editVehicle/'+row.entity.id);
    	}	 
    		 
   $scope.vehiClesList = [];
   $scope.viewVehiclesInit = function() {
 	  $http.get('/getAllVehicles')
 		.success(function(data) {
 			for(var i=0;i<data.length;i++) {
 				data[i].price = "$ "+data[i].price;
 			}
 			$scope.vehiClesList = data;
 			$scope.gridOptions.data = data;
 			console.log(data);
 		});
   }
   
   $scope.deleteVehicle = function(row){
	   $('#deleteModal').click();
	   $scope.rowDataVal = row;
   }
   
   $scope.deleteVehicleRow = function() {
	   $http.get('/deleteVehicleById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			 $scope.viewVehiclesInit();
		});
   }
   
   
   $scope.updateVehicleStatus = function(row){
	   $http.get('/updateVehicleStatus/'+row.entity.id)
		.success(function(data) {
			 $scope.viewVehiclesInit();
		});
   }
   
   $scope.editingData = [];
   
   for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }
   

   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
     console.log(tableData);
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       $http.post('/updateVehicle',tableData)
		.success(function(data) {
			console.log('success');
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
}]);

angular.module('newApp')
.controller('EditVehicleCtrl', ['$scope','$http','$location','$routeParams','$upload', function ($scope,$http,$location,$routeParams,$upload) {
	
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
				    	   console.log(event);
				    	   //console.log($element[0].getAttribute("data-row")+" "+$element[0].getAttribute("data-col")+" "+$element[0].getAttribute("id"));
				    	   $http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			//console.log('success');
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	$scope.imageList = [];
	$scope.isUpdated = false;
	$scope.vinData;
	$scope.init = function() {
		$scope.isUpdated = false;
		$http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
		
		$http.get('/getVehicleById/'+$routeParams.id)
		.success(function(data) {
			 console.log(data);
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
				$scope.setDropZone();
		});
		
		
	}
	
	var myDropzone;
	$scope.setDropZone = function() {
		myDropzone = new Dropzone("#dropzoneFrm",{
			   parallelUploads: 30,
			   headers: { "vinNum": $scope.vinData.specification.vin },
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.getImages();
				          $scope.$apply();
				      });
			   }
		   });
	}
	
	
	   $scope.uploadFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone.processQueue();
		   
	   }
	
	$scope.getImages = function() {
		$scope.isUpdated = false;
		$http.get('/getImagesByVin/'+$scope.vinData.specification.vin)
		.success(function(data) {
			console.log(data);
			$scope.imageList = data;
		});
	}
	
	   $scope.setSiteId = function(id,flag) {
	 	  if(flag == true) {
	 		 $scope.vinData.specification.siteIds.push(id);
	 	  } 
	 	  if(flag == false) {
	 		 $scope.vinData.specification.siteIds.splice($scope.vinData.specification.siteIds.indexOf(id),1);
	 	  }
	 	  
	   };
	
	$scope.updateVehicle = function() {
		$http.post('/updateVehicleById',$scope.vinData.specification)
		.success(function(data) {
			console.log('success');
			$scope.isUpdated = true;
		});
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
				console.log('success');
			});
			
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
		
	}
	
	$scope.deleteImage = function(img) {
		$http.get('/deleteImage/'+img.id)
		.success(function(data) {
			console.log('success');
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	$scope.updateVehicleStatus = function(){
		   $http.get('/updateVehicleStatus/'+$routeParams.id)
			.success(function(data) {
				
			});
	   }
	
	$scope.deleteVehicle = function(){
		 $('#deleteModal').click();
		   
	   }
	
	$scope.deleteVehicleRow = function() {
		$http.get('/deleteVehicleById/'+$routeParams.id)
		.success(function(data) {
			$location.path('/addVehicle');
		});
	}
	
	var file;
	$scope.onFileSelect = function($files) {
		file = $files;
	}
	
	$scope.uploadAudio = function() {
		$upload.upload({
            url : '/uploadSoundFile',
            method: 'post',
            file:file,
            data:{"vinNum":$scope.vinData.specification.vin}
        }).success(function(data, status, headers, config) {
            console.log('success');
            $scope.getAllAudio();
        });
	}
	
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
	
	$scope.getAllAudio = function() {
		$http.get('/getAllAudio/'+$scope.vinData.specification.vin)
		.success(function(data) {
			$scope.audioList = data;
		});
	}
	$scope.vData = {};
	$scope.saveVData = function() {
		console.log($scope.vData);
		$scope.vData.vin = $scope.vinData.specification.vin;
		$http.post('/saveVData',$scope.vData)
		.success(function(data) {
			console.log('success');
		});
	}
	
	$scope.editImage = function(image) {
		$location.path('/cropImage/'+image.id);
	}
	
}]);	
	
angular.module('newApp')
.controller('ImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = $routeParams.id;
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getImageById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.col;
				imageH = data.row;
				$scope.image = data;
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 200, 200, 100, 100 ],
				        trueSize: [data.col,data.row],
				        aspectRatio: 1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 	console.log(c);
			    var rx = imageW / c.w;
				var ry = imageH / c.h;

				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
		    };
		 
		$scope.saveImage = function() {
			$scope.coords.imageId = $routeParams.id;
			console.log($scope.coords);
			
			$http.post('/editImage',$scope.coords)
			.success(function(data) {
				console.log('success');
			});
		}    
		 
		
}]);	

angular.module('newApp')
.controller('HomePageCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	
	$scope.init = function() {
		
		 $http.get('/getSliderAndFeaturedImages')
			.success(function(data) {
				console.log(data);
				$scope.sliderList = data.sliderList;
				$scope.featuredList = data.featuredList;
			});
	}
	
	var myDropzone2;
	
	$scope.uploadSliderImages = function() {
		myDropzone2 = new Dropzone("#dropzoneFrm2",{
			   parallelUploads: 30,
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
			   }
		   });
	}
	   $scope.uploadFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone2.processQueue();
		   
	   }
	   
	   var myDropzone3;
	   $scope.uploadFeaturedImages = function() {
		   myDropzone3 = new Dropzone("#dropzoneFrm3",{
			   parallelUploads: 30,
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.init();
				          $scope.$apply();
				      });
			   }
		   });
	   }  
	   
	   $scope.uploadFeaturedFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone3.processQueue();
		   
	   }
	   
	   $scope.deleteSliderImage = function(image) {
		   $http.get('/deleteSliderImage/'+image.id)
			.success(function(data) {
				$scope.sliderList.splice($scope.sliderList.indexOf(image),1);
			});
	   }
	   
	   $scope.deleteFeaturedImage = function(image) {
		   $http.get('/deleteFeaturedImage/'+image.id)
			.success(function(data) {
				$scope.featuredList.splice($scope.featuredList.indexOf(image),1);
			});
	   }
	   
	   $scope.showFullSliderImage = function(image) {
		   $scope.sliderImgId = image.id;
		   $scope.sliderImgName = image.imgName;
	   }
	   
	   $scope.showFullFeaturedImage = function(image) {
		   $scope.featuredImgId = image.id;
		   $scope.featuredImgName = image.imgName;
	   }
	   
	   $scope.editSliderImage = function(image) {
		   $location.path('/cropSliderImage/'+image.id);
	   }
	   
	   $scope.editFeaturedImage = function(image) {
		   $location.path('/cropFeaturedImage/'+image.id);
	   }
	   
}]);

angular.module('newApp')
.controller('SliderCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = $routeParams.id;
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getSliderImageDataById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.col;
				imageH = data.row;
				$scope.image = data;
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 200, 200, 100, 100 ],
				        trueSize: [data.col,data.row],
				        aspectRatio: 1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 	console.log(c);
			    var rx = imageW / c.w;
				var ry = imageH / c.h;

				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
		    };
		 
		$scope.saveImage = function() {
			$scope.coords.imageId = $routeParams.id;
			console.log($scope.coords);
			
			$http.post('/editSliderImage',$scope.coords)
			.success(function(data) {
				console.log('success');
				$location.path('/homePage');
			});
		}    
		 
		
}]);	

angular.module('newApp')
.controller('FeaturedCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = $routeParams.id;
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getFeaturedImageDataById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.col;
				imageH = data.row;
				$scope.image = data;
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 200, 200, 100, 100 ],
				        trueSize: [data.col,data.row],
				        aspectRatio: 1
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			 	console.log(c);
			    var rx = imageW / c.w;
				var ry = imageH / c.h;

				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
		    };
		 
		$scope.saveImage = function() {
			$scope.coords.imageId = $routeParams.id;
			console.log($scope.coords);
			
			$http.post('/editFeaturedImage',$scope.coords)
			.success(function(data) {
				console.log('success');
				$location.path('/homePage');
			});
		}    
		 
		
}]);	

