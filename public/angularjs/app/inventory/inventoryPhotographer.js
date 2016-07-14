angular.module('newApp')
.controller('inventoryPhotographer', ['$scope','$http','$location','$filter', function ($scope,$http,$location,$filter) {
	$scope.tempDate = new Date().getTime();
	$scope.type = "All";
	$scope.vType;
	$scope.doPublic = 0;
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    paginationPageSize: 150,
    		    enableFiltering: true,
    		    enableCellEditOnFocus: true,
    		    useExternalFiltering: true,
    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 
     
    			
    		 
    		 $scope.editPhoto = function(row){
    			 console.log(row);
    			 console.log(row.entity.portalName);
    			 if(row.entity.portalName == "MavenFurniture"){
    				 $location.path('/editInventory/'+row.entity.id+"/"+true+"/"+row.entity.productId);
    			 }else  if(row.entity.portalName == "Autodealer"){
    				 $location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    				 
    			 }
    			 
    			 
    		 }
    		 
    		 $scope.gridOptions.onRegisterApi = function(gridApi){
    			 $scope.gridApi = gridApi;
    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    			 $scope.rowData = rowEntity;
    			 $scope.$apply();
    				// var str = $scope.rowData.price.split(" ");
    				// $scope.rowData.price = str[1];
    			/* $http.post('/updateVehicle',$scope.rowData)
    			 .success(function(data) {
    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    				});*/
    			 });
    			 
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		        });
    			 
    			 };
    			 
    		    		     
    		    		    		
    			 
    			/* $scope.updateVehicleBody = function(row){
    				 $scope.rowData = row.entity;
    				 if($scope.rowData.price !=null && $scope.rowData.price != undefined){
    					 var str = $scope.rowData.price.split(" ");
        				 $scope.rowData.price = str[1];
    				 }
    				 $http.post('/updateVehicle',$scope.rowData)
        			 .success(function(data) {
        					$scope.rowData.price = "$ "+$scope.rowData.price;
        				});
    			 };*/
    			 
    			 
    			 /*$scope.historyVehicle = function(row){
    				 $http.get('/getVehicleHistory/'+row.entity.vin)
    					.success(function(data) {
    						$scope.vehicleHistory = data;
    						$('#vehicleHistory').click();
    					});
    			 };*/
    			 
    			/* $scope.hideVehicle = function(row){
    				 $http.get('/getGoTodraft/'+row.entity.id)
 						.success(function(data) {
 							$scope.hideText = "Vehicle has been hidden from the website and moved to Drafts list";
 							$scope.hideTitle = "Vehicle moved to drafts";
 							$scope.newlyArrivedTab();
 							$('#hideVehicle').click();
 							$.pnotify({
 							    title: "Success",
 							    type:'success',
 							    text: "Vehicle Added In Draft",
 							});
 						
 					});
    				 
    			 }*/
    			 
    			 $scope.mouse = function(row) {
    					$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
    					$('#thumb_image').show();
    					$('#imagePopup').modal();
    				};
    				$scope.mouseout = function(row) {
    					$('#imgClose').click();
    				};
    				
    				/*$scope.vehicleData = function(sts){
    					if($scope.vType == 'new'){
    						 $scope.doPublic = 0;
    						 $http.get('/getAllVehiclesByType/'+sts)
		    			 		.success(function(data) {
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "new";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
    					}
    					if($scope.vType == 'sold'){
    						 $scope.doPublic = 2;
   						 $http.get('/getAllSoldVehiclesByType/'+sts)
		    			 		.success(function(data) {
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "sold";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions2.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
   					}
    				};*/
    				$scope.getImages = function(row) {
    					$location.path('/editVehicle/'+row.entity.id+"/"+true);
    				};
    				
    				
    		    			 $scope.newlyArrivedTab = function(portalType) {
    		    				 $scope.gridOptions.data = [];
    		    				 console.log(portalType);
    		    				    		    				
    		    				 $http.get('/findLocation')
		    						.success(function(data) {
		    							console.log(data);
		    							$scope.userLocationId = data;
		    							if(portalType == "AutoDealer"){
    		    				 if($scope.userType == "Photographer"){
    		    					 console.log($scope.userLocationId);
    		    				 $http.get('http://www.glider-autos.com/getAllVehicles/'+$scope.userLocationId)
    		    				 //$http.post('http://45.33.50.143:9889/uploadImageFile',$scope.user)
    		    			 		.success(function(data) {
    		    			 			console.log(data);
    		    			 			/*for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}*/
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole = $scope.userRole;
    		    			 				data[i].portalName = "Autodealer";
    		    			 				
    		    							}
    		    			 			
    		    			 			$scope.vType = "new";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions.data = data;
    		    			 			console.log($scope.gridOptions.data);
    		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
    		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
    		    			 		});
    		    				 
    		    				 
    		    				 
    		    				 $scope.gridOptions.columnDefs = [
    		    		    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
    		    		    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'last4Vin', displayName: 'VIN',enableColumnMenu: false, width:'5%',cellEditableCondition: true,
    		    		    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.last4Vin}}" >{{row.entity.last4Vin}}</label> </div>',
    		    		    		                                 },
    		    		    		                                 { name: 'stock', displayName: 'Stock',enableColumnMenu: false, width:'6%',
    		    		    		                                 },
    		    		    		                                 { name: 'bodyStyle', displayName: 'Body Style',enableColumnMenu: false,enableFiltering: false, width:'9%',cellEditableCondition: false,
    		    		    		                                 },
    		    		    		                                 { name: 'mileage', displayName: 'Mileage',enableColumnMenu: false,enableFiltering: false, width:'8%',cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'city_mileage', displayName: 'City MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, enableColumnMenu: false,width:'8%',cellEditableCondition: true,
    		    		    		                                 },
    		    		    		                                 { name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
    		    		    		                                 },
    		    		    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,type:'number',enableColumnMenu: false, width:'4%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
    		    		    		                                 },
    		    		    		                                 { name: 'pageViewCount', displayName: 'Views',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
    		    		    		                                 },
    		    		    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		    		    		                                	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>  ', 
    		    		    		                                 
    		    		    		                                 },
    		    		        		                                
    		    		        		                                 ];  
    		    				 
    		    				 
    		    				 
    		    				 }
		    							 }else if(portalType == "MavenFurniture"){
		    								 if($scope.userType == "Photographer"){
		    	    		    				 $http.get('http://www.glider-autos.com:9889/getAllInventory/'+16)
		    	    		    			 		.success(function(data) {
		    	    		    			 			console.log(data);
		    	    		    			 			for(var i=0;i<data.length;i++) {
		    	    		    			 				data[i].userRole = $scope.userRole;
		    	    		    			 				data[i].portalName = "MavenFurniture";
		    	    		    						}	    	    	
		    	    		    			 			
		    	    		    			 			$scope.vehiClesList = data;
		    	    		    			 			$scope.gridOptions.data = data;
		    	    		    			 			console.log($scope.gridOptions.data);
		    	    		    			 		});
		    	    		    				 }
		    								 
		    								 
		    								 $scope.gridOptions.columnDefs = [
		    						    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
		    						    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		    						    		                                 },
		    						    		                                  
		    						    		                                 { name: 'description', displayName: 'Description',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
		    						    		                                 },
		    						    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
		    						    		                                	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>  ', 
		    						    		                                 
		    						    		                                 },
		    						        		                                
		    						        		                                 ];  
		    								 
		    							 }
    		    				 });
    		    			 }	    			 
    		    			 
    		    			
    		    			 
    	$scope.editVehicle = function(row) {
    		$location.path('/editVehicleNew/'+row.entity.id+"/"+false+"/"+row.entity.vin);
    	}	 
    	
   $scope.vehiClesList = [];
  
   $scope.viewInit = function() {
	   
	   $http.get('/getTimeTableOfPhotos').success(function(data) {
				$scope.portalNameList = data.postalNameList;
				var portal = 0;
				   angular.forEach($scope.portalNameList, function(obj, index){
					   if(index == 0){
						   portal = obj.title
						   console.log(portal);
					   }
				   });
				   $scope.newlyArrivedTab(portal);
		 });	
	   
	   
	 
	   
   }
   
   /*$scope.deleteVehicle = function(row){
	   $('#deleteModal').click();
	   $scope.rowDataVal = row;
   }*/
   
   $scope.showSessionData = function(row){
	   $location.path('/sessionsAnalytics/'+row.entity.id+"/"+row.entity.vin+"/"+row.entity.status);
   }
   
   /*$scope.deleteVehicleRow = function() {
	   $http.get('/deleteVehicleById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			if($scope.rowDataVal.entity.status == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
			} 
			if($scope.rowDataVal.entity.status == 'Sold') {
				$scope.soldTab();
			}
		});
   }*/
   
   //$scope.soldContact = {};
   
   /*$scope.updateVehicleStatusPublic = function(row){
	   $http.get('/addPublicCar/'+row.entity.id).success(function(data){
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle published",
				});
			   $scope.draftTab();
	   });
   }*/
  /* $scope.updateVehicleStatus = function(row){
	   $scope.statusVal = "";
	   if(row.entity.status == 'Newly Arrived') {
		   $('#btnStatusSchedule').click();
		   $scope.soldContact.statusVal = "Sold";
	   }
	   if(row.entity.status == 'Sold') {
		   
		    $('#AddbtnInventory').modal();
		  
		  }
	   $scope.addtoinventory = function() {
		   $http.get('/addSameNewCar/'+row.entity.id).success(function(data){
			   if(data=='success'){
				   $scope.soldContact.statusVal = "Newly Arrived";
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle has been added to Inventory",
					});
			   }else{
				   $.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Vehicle already in Inventory",
					});
			   }
		   });
		   
		   
	   }
	   
	   $scope.soldContact.make = row.entity.make;
	   $scope.soldContact.mileage = row.entity.mileage;
	   $scope.soldContact.model = row.entity.model;
	   $scope.soldContact.year = row.entity.year;
	   $scope.soldContact.vin = row.entity.vin;
	   $scope.soldContact.id = row.entity.id;
	   var str = row.entity.price.split(" ");
	   $scope.soldContact.price = str[1];
   }*/
   
	/*$scope.saveVehicalStatus = function() {
		$http.post('/setVehicleStatus',$scope.soldContact)
		.success(function(data) {
			$('#vehicalStatusModal').modal('hide');
			if($scope.soldContact.statusVal == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle status marked Newly Arrived",
					});
				 $scope.soldTab();
			} 
			if($scope.soldContact.statusVal == 'Sold') {
				$scope.soldTab();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle status marked sold",
				});
				
				$scope.newlyArrivedTab();
			}
			
	});
}*/
   
  // $scope.editingData = [];
   
 /*  for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }*/
   
/*
   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       $http.post('/updateVehicle',tableData)
		.success(function(data) {
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
   $scope.exportDataAsCSV = function() {
	   $http.get('/exportDataAsCSV')
		.success(function(data) {
		});
   }
   
   $scope.exportCarfaxCSV = function() {
	   $http.get('/exportCarfaxCSV')
		.success(function(data) {
		});
   }
   
   $scope.exportCarGurusCSV = function() {
	   $http.get('/exportCarGurusCSV')
		.success(function(data) {
		});
   }*/
   
}]);
